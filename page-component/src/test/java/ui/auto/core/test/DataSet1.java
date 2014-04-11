package ui.auto.core.test;

import java.util.List;

import org.testng.annotations.Test;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import ui.auto.core.components.WebComponent;
import ui.auto.core.data.DataPersistence;
import ui.auto.core.data.generators.Data;

@XStreamAlias("dataset1")
public class DataSet1 extends DataPersistence {
	WebComponent comp1;
	@Data()
	WebComponent comp2;
	@Data(alias="alias3",value="value3")
	WebComponent comp3;
	@Data(alias="alias-l1",value="list of web components")
	List<WebComponent> list1;
	@Data(alias="alias-l1")
	List<WebComponent> list2;
	DataSet2 dataSet2;
	@Data(alias="alias-int", value="33")
	Short integer;
	
	@Test
	public void generate() throws Exception{
		System.out.println(generateXML());
	}

}
