/*
Copyright 2010-2019 Michael Braiman braimanm@gmail.com

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

package com.braimanm.ui.auto.utils;

import com.braimanm.ui.auto.context.PageComponentContext;
import com.braimanm.ui.auto.pagecomponent.PageComponent;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@SuppressWarnings("unused")
public class WebDriverUtils {

    public static void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {}
    }

    public static WebDriver getWebDriver() {
        if (PageComponentContext.getContext() != null) {
            return PageComponentContext.getContext().getDriver();
        }
        return null;
    }

    public static JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) getWebDriver();
    }

    public static void waitForXHR(long timeout, long sleep, boolean debug) {
        String script = "function reqCallBack(t){document.getElementsByTagName('body')[0].setAttribute('ajaxcounter',++ajaxCount)}function resCallback(t){document.getElementsByTagName('body')[0].setAttribute('ajaxcounter',--ajaxCount)}function intercept(){XMLHttpRequest.prototype.send=function(){if(reqCallBack(this),this.addEventListener){var t=this;this.addEventListener('readystatechange',function(){4===t.readyState&&resCallback(t)},!1)}else{var e=this.onreadystatechange;e&&(this.onreadystatechange=function(){4===t.readyState&&resCallbck(this),e()})}originalXhrSend.apply(this,arguments)}}var originalXhrSend=XMLHttpRequest.prototype.send,ajaxCount=0;document.getElementsByTagName('body')[0].hasAttribute('ajaxcounter')||intercept();";
        getJSExecutor().executeScript(script);

        long to = System.currentTimeMillis() + timeout;
        boolean flag = true;
        if (debug) System.out.print("XHR: ");
        do {
            List<WebElement> vals = getWebDriver().findElements(By.cssSelector("body"));
            String val = (vals.isEmpty()) ? null :  vals.get(0).getAttribute("ajaxcounter");
            if (val == null) {
                val = "-1";
                if (System.currentTimeMillis() > (to - timeout + 2000)) {
                    if (debug) System.out.println();
                    return;
                }
            }
            if (debug) System.out.print(val + " ");
            if (Integer.parseInt(val) == 0) {
                flag = false;
            }
            if (flag) sleep(sleep);
        } while (flag && System.currentTimeMillis() < to);
        if (debug) System.out.println();
    }

    private static long getElementTimeOut() {
        return PageComponentContext.getContext().getElementLoadTimeout();
    }

    public static void waitForXHR() {
        waitForXHR(getElementTimeOut() * 1000, 500, false);
    }

    public static WebDriverWait getWebDriverWait() {
        return new WebDriverWait(getWebDriver(), Duration.ofSeconds(getElementTimeOut()));
    }

    public static void clickWebElementWithJS(WebElement element) {
        getJSExecutor().executeScript("arguments[0].click();", element);
    }

    public static void clickComponentWithJS(PageComponent component) {
        clickWebElementWithJS(component.getCoreElement());
    }

    public static void scrollIntoView(PageComponent component) {
        scrollIntoView(component.getCoreElement());
    }

    public static void scrollIntoView(WebElement element) {
        scrollIntoView(element, "true");
    }

    public static void scrollIntoView(WebElement element, String options) {
        getJSExecutor().executeScript("arguments[0].scrollIntoView(" + options + ");", element );
    }

    public static void scrollIntoCenter(WebElement element) {
        scrollIntoView(element, "{block: 'center', inline: 'nearest'}");
    }

    public static void scrollIntoCenter(PageComponent component) {
        scrollIntoCenter(component.getCoreElement());
    }

    public static boolean isDisplayed(PageComponent component) {
        if (component.getLocator() == null) {
            return component.getCoreElement().isDisplayed();
        }
        List<WebElement> elList = getWebDriver().findElements(component.getLocator());
        if (elList.isEmpty()) {
            return false;
        }
        if (elList.get(0).isDisplayed()) {
            scrollIntoCenter(elList.get(0));
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDisplayed(PageComponent component, long timeOut) {
        long to = System.currentTimeMillis() + timeOut;
        do {
            if (isDisplayed(component)) {
                return true;
            }
        } while (System.currentTimeMillis() < to);
        return false;
    }

}
