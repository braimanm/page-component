package ui.auto.core.test;

import java.io.InputStream;
import java.net.URL;

import org.testng.Assert;
import org.testng.annotations.Test;

import datainstiller.data.Data;
import ui.auto.core.components.WebComponent;
import ui.auto.core.pagecomponent.PageObject;

public class TestPageObjectUTF8 extends PageObject{
	@Data("City")
	WebComponent cityEnglish;
	@Data("Ville de Québec")
	WebComponent cityFrench;
	@Data("город")
	WebComponent cityRussian;
	@Data("鎮")
	WebComponent cityChinese;
	@Data("शहर")
	WebComponent cityHindi;
	@Data("都市")
	WebComponent cityJapanese;
	
	private void assertPageObject(TestPageObjectUTF8 po){
		Assert.assertEquals(po.cityEnglish.getData(), "City");
		Assert.assertEquals(po.cityFrench.getData(),"Ville de Québec");
		Assert.assertEquals(po.cityRussian.getData(), "город");
		Assert.assertEquals(po.cityChinese.getData(), "鎮");
		Assert.assertEquals(po.cityHindi.getData(), "शहर");
		Assert.assertEquals(po.cityJapanese.getData(), "都市");
	}
	
	@Test
	public void testFromXML() throws Exception{
		String xml=generateXML();
		TestPageObjectUTF8 po = new TestPageObjectUTF8().fromXml(xml,false);
		assertPageObject(po);
	}
	
	@Test
	public void testFromResource(){
		TestPageObjectUTF8 po = new TestPageObjectUTF8().fromResource("PageObjectUTF8.xml",false);
		assertPageObject(po);
	}
	
	@Test
	public void testFromFile(){
		URL url=TestPageObjectUTF8.class.getResource("/PageObjectUTF8.xml");
		String filePath=url.getFile();	
		TestPageObjectUTF8 po=new TestPageObjectUTF8().fromFile(filePath,false);
		assertPageObject(po);
	}
	
	@Test
	public void testFromInputStream(){	
		InputStream inputStream=TestPageObjectUTF8.class.getResourceAsStream("/PageObjectUTF8.xml");			
		TestPageObjectUTF8 po=new TestPageObjectUTF8().fromInputStream(inputStream,false);
		assertPageObject(po);
	}
	
	@Test
	public void testFromURL(){
		URL url=TestPageObjectUTF8.class.getResource("/PageObjectUTF8.xml");
		TestPageObjectUTF8 po=new TestPageObjectUTF8().fromURL(url,false);
		assertPageObject(po);
	}
}
