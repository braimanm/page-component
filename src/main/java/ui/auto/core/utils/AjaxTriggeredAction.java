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
import ui.auto.core.pagecomponent.PageComponent;

/**
 * @author Michael Braiman braimanm@gmail.com
 *         This is utility to handle Ajax loaded components
 */
public abstract class AjaxTriggeredAction {
    /**
     * @param action            action to perform which will trigger Ajax event
     * @param affectedComponent affected by Ajax component
     * @param time_out          maximum time to wait until component is affected by Ajax
     */
    public static void waitForAjax(AjaxTriggeredAction action, PageComponent affectedComponent, long time_out) {
        waitForAjax(action, affectedComponent, time_out, true);
    }

    /**
     * This method will wait for Ajax event to complete redrawing the DOM.
     * The idea behind this method is: first to trigger an action which will cause Ajax event and then to wait until affected
     * by Ajax element become stale. this means that affected element loses his handler as Ajax renders portion of the DOM
     * where affected element resides.
     *
     * @param action            action to perform which will trigger Ajax event
     * @param affectedComponent affected by Ajax component
     * @param time_out          maximum time to wait until component is affected by Ajax
     * @param throwException    if true will throw exception if Ajax event is not detected
     */
    public static void waitForAjax(AjaxTriggeredAction action, PageComponent affectedComponent, long time_out, boolean throwException) {
        long to = System.currentTimeMillis() + time_out;
        WebElement coreElement = affectedComponent.getCoreElement();
        action.doAction();
        do {
            try {
                coreElement.isDisplayed();        //This should trigger exception if element is detached from DOM
            } catch (StaleElementReferenceException e) {
                return;
            }
        } while (System.currentTimeMillis() < to);
        if (throwException) {
            throw new RuntimeException("[TIME-OUT ERROR] Ajax event was not triggered!");
        }
    }

    /**
     * This method is the same as waitForAjax method but it will wait after Ajax event that affected element is usable.
     * Use this method when component is stale or redrawn by the Ajax event and there is no other visual indications on the
     * web page that Ajax event was triggered.
     *
     * @param action            action to perform which will trigger Ajax event
     * @param affectedComponent affected by Ajax component
     * @param time_out          maximum time to wait until component is usable after Ajax event
     * @param throwException    if true will throw exception if Ajax event is not detected
     */
    public static void waitForAjaxAndComponent(AjaxTriggeredAction action, PageComponent affectedComponent, long time_out, boolean throwException) {
        long to = System.currentTimeMillis() + time_out;
        boolean isStaleElement = false;
        WebElement coreElement = affectedComponent.getCoreElement();
        action.doAction();
        do {
            try {
                coreElement.isDisplayed();        //This should trigger exception if element is detached from DOM
                if (isStaleElement) return;
            } catch (StaleElementReferenceException e) {
                isStaleElement = true;
                coreElement = affectedComponent.getCoreElement(); //This will try to locate page component again using ComponentMethodInterceptor
            }
        } while (System.currentTimeMillis() < to);
        if (throwException) {
            throw new RuntimeException("[TIME-OUT ERROR] Ajax event was not triggered!");
        }
    }

    public abstract void doAction();


    public void waitForAjax(PageComponent affectedElement, long time_out) {
        waitForAjax(affectedElement, time_out, true);
    }


    public void waitForAjax(PageComponent affectedElement, long time_out, boolean throwException) {
        waitForAjax(this, affectedElement, time_out, throwException);
    }

}