package test.com.PageComponent;

import org.testng.annotations.Test;

import ui.auto.core.components.WebComponent;
import ui.auto.core.data.generators.Data;
import ui.auto.core.data.generators.GeneratorType;
import ui.auto.core.pagecomponent.PageObject;

public class MainPO extends PageObject {
	String kuku;
	@Data(value="basic",alias="bbb")
	WebComponent basicComponent;
	@Data(alias="bb", type=GeneratorType.DATE,init="1920/01/01|2000/12/31|yyyy/MM/dd")
	String kaka;
	public MainPO(){
	}
	
	@Test
	public void doTest(){
		MainPO main=new MainPO();
		String filepath=main.generateData();
		MainPO sec=MainPO.fromFile(filepath, MainPO.class);
		System.out.println(sec.toXML());
	}
	
	
	

}
