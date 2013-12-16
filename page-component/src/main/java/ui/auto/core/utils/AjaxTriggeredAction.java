package ui.auto.core.utils;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

public abstract class AjaxTriggeredAction {
	public abstract void doAction();
	
	
	public void waitForAjax(WebElement affectedElement,long time_out){
		waitForAjax(affectedElement, time_out,true);
	}
	
	public void waitForAjax(WebElement affectedElement,long time_out,boolean throwException){
		waitForAjax(this, affectedElement, time_out,throwException);
	}
	
	public static void waitForAjax(AjaxTriggeredAction action, WebElement affectedElement,long time_out){
		waitForAjax(action, affectedElement, time_out, true);
	}
	
	public static void waitForAjax(AjaxTriggeredAction action, WebElement affectedElement,long time_out,boolean throwException){
		long to=System.currentTimeMillis()+time_out;
		action.doAction();
		do {
			try {
				affectedElement.isDisplayed();//This should trigger exception if element is detached from Dom
			} catch (StaleElementReferenceException e){
				return;
			}
		} while (System.currentTimeMillis()<to);
		if (throwException){
			throw new RuntimeException("[TIME-OUT ERROR] Ajax event didn't triggered!");
		}
	}
}