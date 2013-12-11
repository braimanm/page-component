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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.ComponentData;
import ui.auto.core.data.DataAliases;
import ui.auto.core.data.DataTypes;


public abstract class PageComponent implements ComponentData, DefaultAction{
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
	
	
	public String getData(DataTypes type,boolean resolveAliases){
		String dat=null;
		switch (type){
			case Data: 
				dat=data;
				break;
			case Initial:
				dat=initialData;
				break;
			case Expected:
				dat=expectedData;
				break;
		}
		if (dat!=null && resolveAliases){
			Pattern pat=Pattern.compile("\\$\\{[\\w-]+\\}");
			Matcher mat=pat.matcher(dat);
			while (mat.find()){
				String key=mat.group();
				DataAliases aliases= PageComponentContext.getGlobalAliases();
				String value=aliases.get(key);
				if(value!=null){
				 dat=dat.replace(key,value);
				}
			}
		}
		return dat;
	}
	
	@Override
	public String getData() {
		return getData(DataTypes.Data,true);
	}
	
	@Override
	public String getInitialData() {
		return getData(DataTypes.Initial,true);
	}
	
	@Override
	public String getExpectedData() {
		return getData(DataTypes.Expected,true);
	}
	
	@Override
	public void setData(String data) {
		this.data=data;
	}

	@Override
	public void setInitialData(String data) {
		this.initialData=data;
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
