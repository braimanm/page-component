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

import java.lang.reflect.Field;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;

import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.DataPersistence;
import ui.auto.core.data.DataValidationMethod;

import com.thoughtworks.xstream.annotations.XStreamOmitField;


public class PageObject extends DataPersistence{
	@XStreamOmitField
	private PageComponentContext context;
	@XStreamOmitField
	private String currentUrl;
	@XStreamOmitField
	private boolean ajaxIsUsed;;
	
	protected <T extends PageComponentContext> PageObject(T context){
		initPage(context);
	}
	
	protected <T extends PageComponentContext> PageObject(T context,String expectedUrl){
		initPage(context,expectedUrl);
	}
	
	protected <T extends PageComponentContext> PageObject(T context,boolean ajaxIsUsed){
		initPage(context,ajaxIsUsed);
	}
	
	protected <T extends PageComponentContext> PageObject(T context,String expectedUrl,boolean ajaxIsUsed){
		initPage(context,expectedUrl,ajaxIsUsed);
	}
	
	protected PageObject() {}

	public <T extends PageComponentContext> void  initPage(T context,boolean ajaxIsUsed){
		this.context=context;
		this.ajaxIsUsed=ajaxIsUsed;
		currentUrl=context.getDriver().getCurrentUrl();
		if (ajaxIsUsed){
			AjaxVisibleElementLocatorFactory ajaxVisibleElementLocatorFactory=new AjaxVisibleElementLocatorFactory(context.getDriver(),context.getAjaxTimeOut());
			PageFactory.initElements(new ComponentFieldDecorator(ajaxVisibleElementLocatorFactory,this),this);
		}else {
			DefaultElementLocatorFactory defLocFactory=new DefaultElementLocatorFactory(context.getDriver());
			PageFactory.initElements(new ComponentFieldDecorator(defLocFactory, this),this);
		}
	}
	
	public <T extends PageComponentContext> void  initPage(T context){
		initPage(context,true);
	}
	
	public <T extends PageComponentContext> void initPage(T context,String expectedUrl){
		initPage(context,expectedUrl,true);
	}
	
	private <T extends PageComponentContext> void initPage(T context, String expectedUrl,boolean ajaxIsUsed) {
		initPage(context,ajaxIsUsed);
		if (expectedUrl!=null) waitForUrl(expectedUrl);
	}

	@SuppressWarnings("unchecked")
	public <T extends PageComponentContext> T getContext(){
		return (T) context;
	}
	
	public void waitForUrl(String expectedUrl){
		long endTime=System.currentTimeMillis() + context.getWaitForUrlTimeOut();
		while (System.currentTimeMillis()<endTime) {
			if (context.getDriver().getCurrentUrl().contains(expectedUrl)) {
				currentUrl=context.getDriver().getCurrentUrl();
				return;
			}
			sleep(100);
		} 
		throw new RuntimeException("Expected url:" + expectedUrl + " is not displayed!");	
	}
	
	public boolean waitForUrlToChange(){
		long endTime=System.currentTimeMillis() + context.getWaitForUrlTimeOut();
		boolean exitWhile=false;
		while (System.currentTimeMillis()<endTime && !exitWhile) {
			if (!context.getDriver().getCurrentUrl().contains(currentUrl)) exitWhile=true;
			sleep(100);
		} 
		return exitWhile;
	}
	
	protected void setElementValue(PageComponent component){
		setElementValue(component,true);
	}
	
	protected void setElementValue(PageComponent component,String value){
		setElementValue(component,value,true);
	}
	
	protected void setElementValue(PageComponent component,boolean validate){
		if (component.getData()!=null && !component.getData().isEmpty()) {
			String valData=null;
			for (int i=0;i<3;i++){
				component.setValue();
				if (!validate) return;
				valData=component.getData();
				if (component.getExpectedData()!=null)
					valData=component.getExpectedData();
				if (component.getValue().equals(valData)) return;
			}
			throw new RuntimeException("Can't set element '" + component.getClass().getSimpleName() + "' to value: " + valData);
		}
	}
	
	protected void setElementValue(PageComponent component,String value,boolean validate){
		String realValue=null;
		if (component.getData()!=null && !component.getData().isEmpty()) 
			realValue=component.getData();
		component.setData(value);
		setElementValue(component,validate);
		component.setData(realValue);
	}
	
	protected void enumerateFields(FieldEnumerationAction action) {
		for (Field field:this.getClass().getDeclaredFields()){
			if (!field.isAnnotationPresent(XStreamOmitField.class) ) {
				if (PageComponent.class.isAssignableFrom(field.getType())){
					field.setAccessible(true);
					PageComponent component=null;
					try {
						component = (PageComponent) field.get(this);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
					try {
						action.doAction(component,field);
					} catch (Exception e){
						throw new RuntimeException("Failure during field enumeration for field: " + 
								this.getClass().getName() + ":" + field.getName(),e);
					}
				}
			}
		}
	}
	
	protected void autoFillPage(){
		autoFillPage(true);
	}
	
	protected void autoFillPage(final boolean validate){
		if (context==null) throw new RuntimeException("PageObject is not initialized, invoke initPage method!");
		enumerateFields(new FieldEnumerationAction() {	
			
			@Override
			public void doAction(PageComponent PageComponent,Field field) {
				if (!field.isAnnotationPresent(SkipAutoFill.class))
					setElementValue(PageComponent,validate);
			}
		});
	
	}
	
	protected void autoValidatePage(){
		autoValidatePage(DataValidationMethod.Data);
	}
	
	protected void autoValidatePage(final DataValidationMethod validationMethod){
		if (context==null) throw new RuntimeException("PageObject is not initialized, invoke initPage method!");
		enumerateFields(new FieldEnumerationAction() {
			
			@Override
			public void doAction(PageComponent pageComponent,Field field) {
				if (!field.isAnnotationPresent(SkipAutoValidate.class))
						pageComponent.validateData(validationMethod);
			}
		});
	}
	
	public void initData(PageObject pageObject){
		PageComponentContext currentContext=context;
		deepCopy(pageObject,this);
		if (currentContext!=null)
			initPage(currentContext);
	}
	
	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public WebDriver getDriver(){
		if (context!=null){
			return context.getDriver();
		}
		return null;
	}
	
}
