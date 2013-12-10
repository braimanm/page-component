package ui.auto.core.data;

import java.lang.reflect.Method;

public enum DataTypes {
	Data("getData"),
	Initial("getInitialData"),
	Expected("getExpectedData");
	
	private String type;
	
	private DataTypes(String type){
		this.type=type;
	}
	
	public String getData(ComponentData data){
		String valData=null;
		if (data!=null){
			try {
				Method m=ComponentData.class.getMethod(type);
				valData=(String) m.invoke(data);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return valData;
	}
	
}
