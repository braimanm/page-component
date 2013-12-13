package test.com.PageComponent;

import java.util.List;

import ui.auto.core.components.WebComponent;
import ui.auto.core.data.DataPersistence;
import ui.auto.core.data.generators.Data;

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
	
	public static class DataSet2 {
		@Data(alias="alias1")
		WebComponent comp4;
		@Data("component5")
		WebComponent comp5;
		@Data(alias="alias-l1")
		List<WebComponent> list3;
		@Data(alias="array-alias",value="array-value",n=10)
		WebComponent[] array1;
	}

}
