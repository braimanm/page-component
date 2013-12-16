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
			throw new RuntimeException("[TIME-OUT ERROR] Ajax event was not triggered!");
		}
	}
}