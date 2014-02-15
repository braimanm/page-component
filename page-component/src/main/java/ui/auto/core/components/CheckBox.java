package ui.auto.core.components;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

public class CheckBox extends PageComponent {
	
	public CheckBox() {}
	
	public CheckBox(WebElement element){
		super(element);
	}
	
	public void check(){
		if (!coreElement.isSelected()) coreElement.click();
	}
	
	public void uncheck(){
		if (coreElement.isSelected()) coreElement.click();
	}
	
	public void check(boolean value){
		if (value) {
			check();
		} else {
			uncheck();
		}
	}
	
	@Override
	protected void init() {}
	
	@Override
	public void setValue() {
			check(getData().toLowerCase().equals("true"));
	}
	
	@Override
	public String getValue() {
		return String.valueOf(coreElement.isSelected());
	}
	
	@Override
	public void validateData(DataTypes validationMethod) {
		Assert.assertEquals(coreElement.isSelected(),validationMethod.getData(this).toLowerCase().trim().equals("true"));
	}
	
	

}


