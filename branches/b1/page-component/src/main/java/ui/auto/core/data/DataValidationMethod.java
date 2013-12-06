package ui.auto.core.data;

import java.lang.reflect.Method;

public enum DataValidationMethod {
	Data("getData"),
	Initial("getInitialData"),
	Expected("getExpectedData");
	
	private String method;
	
	private DataValidationMethod(String method){
		this.method=method;
	}
	
	public String getData(ComponentData data){
		String valData=null;
		if (data!=null){
			try {
				Method m=ComponentData.class.getMethod(method);
				valData=(String) m.invoke(data);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return valData;
	}
	
}
