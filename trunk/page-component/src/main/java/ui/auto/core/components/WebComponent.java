package ui.auto.core.components;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import ui.auto.core.pagecomponent.PageComponent;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("basic-component")
public class WebComponent extends PageComponent {

	public WebComponent(String data,String initialData){
		super(data,initialData);
	}
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
	public void validateData(){
		if (getData()!=null)
			Assert.assertEquals(getValue(),getData());
	}

	@Override
	public void validateInitialData() {
		if (getInitialData()!=null)
			Assert.assertEquals(getValue(),getInitialData());
	}

	public void validateData(String data) {
		Assert.assertEquals(getValue(),data);
	}
	

}
