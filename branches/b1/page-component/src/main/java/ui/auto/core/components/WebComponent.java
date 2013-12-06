package ui.auto.core.components;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import ui.auto.core.data.DataValidationMethod;
import ui.auto.core.pagecomponent.PageComponent;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("basic-component")
public class WebComponent extends PageComponent {

	public WebComponent(){}
	
	public WebComponent(WebElement element) {
		super(element);
	}

	@Override
	protected void init() {
		// no additional initialization or verification is needed
	}
	
	@Override
	public void setValue(){
		coreElement.clear();
		coreElement.sendKeys(getData());
	}
	
	@Override
	public String getValue(){
		String value=coreElement.getAttribute("value");
		if (value==null){
			value=coreElement.getText();
		}
		return value;
	}

	@Override
	public void validateData(DataValidationMethod validationMethod) {
		String valData=validationMethod.getData(this);
		if (valData!=null)
			Assert.assertEquals(getValue(),valData);
	}
	
	public void validateData(String data) {
		Assert.assertEquals(getValue(),data);
	}

}
