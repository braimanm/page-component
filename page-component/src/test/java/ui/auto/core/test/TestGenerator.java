package ui.auto.core.test;


import org.testng.annotations.Test;

import ui.auto.core.data.DataPersistence;
import ui.auto.core.data.generators.GeneratorType;
import ui.auto.examples.FormPageObject;

public class TestGenerator {
	
	@Test
	public void test(){
		System.out.println(GeneratorType.ADDRESS.generate("<#> <S>, <T>, <O>, <K> (<P>)",null));
		System.out.println(GeneratorType.ALPHANUMERIC.generate("<A><B><C><D>(a)(A)(a)",null));
		System.out.println(GeneratorType.CUSTOM_LIST.generate(null, "aaa,bbb,ccc,ddd"));
		System.out.println(GeneratorType.DATE.generate("2010/01/01|2013/12/31|yyyy/MM/dd","dd MMM yyyy"));
		System.out.println(GeneratorType.HUMAN_NAMES.generate(null,"<M> and <F> <S>"));
		System.out.println(GeneratorType.NUMBER.generate("-100,100", "##.000"));
		System.out.println(GeneratorType.WORD.generate("<a> <b>",null));
		
		FormPageObject formPageObject=DataPersistence.fromResource("DynamicDataSet.xml", FormPageObject.class);
		System.out.println(formPageObject.toXML());
	}
	
}
