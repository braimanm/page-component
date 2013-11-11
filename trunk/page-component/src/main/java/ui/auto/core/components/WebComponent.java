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
		return coreElement.getAttribute("value");
	}
	
	@Override
	public void validateData(){
		if (getData()!=null)
			Assert.assertEquals(coreElement.getText(),getData());
	}

	@Override
	public void validateInitialData() {
		if (getInitialData()!=null)
			Assert.assertEquals(coreElement.getAttribute("value"),getInitialData());
	}

	public void validateData(String data) {
		Assert.assertEquals(coreElement.getAttribute("value"),data);
	}
	

}
