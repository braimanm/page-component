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

/**
 * @author Michael Braiman braimanm@gmail.com
 *			<p/>
 *			This is main page component class all the other user defined components must inherit this class.
 *			User defined page component must implement {@link DefaultAction} interface and abstract init() method
 */
public abstract class PageComponent implements ComponentData, DefaultAction{
	protected WebElement coreElement;
	private String data;
	private String initialData;
	private String expectedData;
	By selector;
	
	/**
	 * This method is needed for initialization of page component
	 */
	protected abstract void init();
	
	/**
	 * Default constructor is neded for serialization purposes
	 */
	public PageComponent(){
	}
	
	@Override
	public void initializeData (String data, String initialData, String expectedData){
		this.data=data;
		this.initialData=initialData;
		this.expectedData=expectedData;
	}
	
	/**
	 * Construct page component using existing web element as root of this component
	 * @param coreElement web element which is a core element for this component 
	 */
	public PageComponent(WebElement coreElement) {
		initComponent(coreElement);
	}
	
	/**
	 * 
	 * @return core web element for this component
	 */
	public WebElement getCoreElement(){
		return coreElement;
	}
	
	/**
	 * 
	 * @param coreElement core web element for this component
	 */
	void initComponent(WebElement coreElement){
		this.coreElement=coreElement;
		init();
	}
	
	@Override
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
				String alias=mat.group();
				String key=alias.replace("${","").replace("}","");
				DataAliases aliases= PageComponentContext.getGlobalAliases();
				if (aliases.containsKey(key)){
					String value=aliases.get(key).toString();
					if(value!=null){
						dat=dat.replace(alias,value);
					}
				}
			}
		}
		return dat;
	}
	
	/**
	 * Get populated data for this component
	 * @return data 
	 */
	public String getData() {
		return getData(DataTypes.Data,true);
	}
	
	/**
	 *Get Initial data for this component 
	 * @return initial data
	 */
	public String getInitialData() {
		return getData(DataTypes.Initial,true);
	}
	
	/**
	 * Get Expected data for this component
	 * @return expected data
	 */
	public String getExpectedData() {
		return getData(DataTypes.Expected,true);
	}
	
	/**
	 * 
	 * @param data string which represent data
	 */
	void setData(String data) {
		this.data=data;
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
	
	@Override
	public By getLocator() {
		return selector;
	}
	
}
