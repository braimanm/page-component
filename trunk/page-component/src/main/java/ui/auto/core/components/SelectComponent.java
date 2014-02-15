package ui.auto.core.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

public class SelectComponent extends PageComponent{
	private Select select;
	
	public SelectComponent(){}
	
	public SelectComponent(WebElement element) {
		super(element);
	}
	
	public Select getSelectElement(){
		return select;
	}
	
	public void selectByVisibleText(){
		select.selectByVisibleText(getData());
	}
	
	public void selectByIndex() {
		select.selectByIndex(Integer.parseInt(getData()));
	}
	
	public void selectByValue() {
		select.selectByValue(getData());
	}

	public void deselectByValue() {
		select.deselectByValue(getData());
	}

	public void deselectByIndex() {
		select.deselectByIndex(Integer.parseInt(getData()));
	}
	
	public void deselectByVisibleText() {
		select.deselectByVisibleText(getData());
	}

	@Override
	protected void init() {
		select=new Select(coreElement);
	}
	
	@Override
	public void setValue() {
		selectByVisibleText();
	}

	@Override
	public String getValue() {
		return select.getFirstSelectedOption().getText();
	}

	@Override
	public void validateData(DataTypes validationMethod) {
		Assert.assertEquals(getValue(), validationMethod.getData(this));
	}


	
	
}
