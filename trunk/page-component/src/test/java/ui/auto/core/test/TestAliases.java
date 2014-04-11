package ui.auto.core.test;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import org.testng.Assert;
import org.testng.annotations.Test;

import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.DataPersistence;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

public class TestAliases {
	
	
	private void assertCollection(Object col,String expectedAlias,String expectedValue){
		if (col.getClass().isArray()){
			for (Object o:(Object[])col){
				assertElement(o, expectedAlias, expectedValue);
			}
		}
		
		if (col instanceof List){
			for (Object o:(List<?>)col){
				assertElement(o, expectedAlias, expectedValue);
			}
		}
	}
	
	private void assertElement(Object el,String expectedAlias,String expectedValue){
		if (el instanceof PageComponent){
			PageComponent comp=(PageComponent) el;
			Assert.assertEquals(comp.getData(DataTypes.Data,false),expectedAlias);
			Assert.assertEquals(comp.getData(DataTypes.Data,true),expectedValue);
			Assert.assertEquals(comp.getData(),expectedValue);
		} else {
			if (el instanceof String){
				String comp=(String) el;
				String key=comp.replace("${","").replace("}","");
				String value=(String) PageComponentContext.getGlobalAliases().get(key);
				Assert.assertEquals(comp,expectedAlias);
				Assert.assertEquals(value,expectedValue);
			}
		}
	}
	
	private void assertDataSet(DataSet1 ds){
		assertElement(ds.comp1, "comp1","comp1");
		assertElement(ds.comp2, "","");
		assertElement(ds.comp3, "${alias3}","value3");
		assertCollection(ds.list1,"${alias-l1}","list of web components");
		assertCollection(ds.list2,"${alias-l1}","list of web components");
		assertElement(ds.dataSet2.comp4, "${alias1}","");
		assertElement(ds.dataSet2.comp5, "component5","component5");
		assertElement(ds.dataSet2.list3, "${alias-l1}","list of web components");
		assertElement(ds.dataSet2.array1, "${array-alias}","array-value");
		assertElement(ds.dataSet2.string, "${alias3}","value3");
	}
	

	@Test
	public void testAliasesGeneration() throws Exception{
		
		InputStream is=this.getClass().getResourceAsStream("/ExpectedDataSet.xml");
		@SuppressWarnings("resource")
		Scanner s=new Scanner(is).useDelimiter("\\A");
		String expectedXML=s.hasNext() ? s.next() : "";		
	
		DataSet1 ds=new DataSet1();
		String xml=ds.generateXML();
		Assert.assertEquals(xml,expectedXML);
		
		DataSet1 ds2=new DataSet1();
		String filepath=ds2.generateData();
		String xml2=DataPersistence.fromFile(filepath, DataSet1.class).toXML();
		Assert.assertEquals(xml2,expectedXML);
		
		assertDataSet(ds);
		assertDataSet(ds2);
		
	}
	
	
	@Test
	public void testAliasesFromXML(){
		PageComponentContext.getGlobalAliases().clear();
		DataSet1 ds=DataPersistence.fromResource("ExpectedDataSet.xml",DataSet1.class);
		String xml=ds.toXML();
		ds=DataPersistence.fromXml(xml, DataSet1.class);
		assertDataSet(ds);
	}
	
	@Test
	public void testAliasesFromFile(){
		PageComponentContext.getGlobalAliases().clear();
		String filePath=TestAliases.class.getResource("/ExpectedDataSet.xml").getPath();
		DataSet1 ds=DataPersistence.fromFile(filePath,DataSet1.class);
		assertDataSet(ds);
	}
	
	@Test
	public void testAliasesFromResource(){
		PageComponentContext.getGlobalAliases().clear();
		DataSet1 ds=DataPersistence.fromResource("ExpectedDataSet.xml",DataSet1.class);
		assertDataSet(ds);
	}
	
	@Test
	public void testAliasesFromInputStream(){
		PageComponentContext.getGlobalAliases().clear();
		InputStream inputStream=TestAliases.class.getResourceAsStream("/ExpectedDataSet.xml");
		DataSet1 ds=DataPersistence.fromInputStream(inputStream,DataSet1.class);
		assertDataSet(ds);
	}
	
}
