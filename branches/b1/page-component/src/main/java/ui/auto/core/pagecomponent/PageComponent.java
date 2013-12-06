/*
Copyright 2010-2012 Michael Braiman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/


package ui.auto.core.pagecomponent;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import ui.auto.core.data.ComponentData;


public  abstract class PageComponent implements ComponentData, DefaultAction{
	protected WebElement coreElement;
	private String data;
	private String initialData;
	private String expectedData;
	
	protected abstract void init();
	
	public PageComponent(){
	}
	
	@Override
	public void initializeData (String data, String initialData, String expectedData){
		this.data=data;
		this.initialData=initialData;
		this.expectedData=expectedData;
	}
	
	public PageComponent(WebElement coreElement) {
		this.coreElement=coreElement;
		init();
	}
	
	public WebElement getCoreElement(){
		return coreElement;
	}
	
	void setCoreElement(WebElement coreElement){
		this.coreElement=coreElement;
	}
	
	@Override
	public String getData() {
		return data;
	}

	@Override
	public void setData(String data) {
		this.data=data;
	}

	@Override
	public String getInitialData() {
		return initialData;
	}

	@Override
	public void setInitialData(String data) {
		this.initialData=data;
	}
	
	@Override
	public String getExpectedData() {
		return expectedData;
	}

	@Override
	public void setExpectedData(String data) {
		expectedData=data;
	}

	public void click() {
		coreElement.click();
	}

	public String getAttribute(String name) {
		return coreElement.getAttribute(name);
	}

	public boolean isSelected() {
		return coreElement.isSelected();
	}

	public boolean isEnabled() {
		return coreElement.isEnabled();
	}

	public boolean isDisplayed(){
		return coreElement.isDisplayed();
	}
	
	public String getValue(){
		return coreElement.getAttribute("value");
	}

	public String getText() {
		return coreElement.getText();
	}

	public List<WebElement> findElements(By by) {
		return coreElement.findElements(by);
	}

	public WebElement findElement(By by) {
		return coreElement.findElement(by);
	}	
	

}
