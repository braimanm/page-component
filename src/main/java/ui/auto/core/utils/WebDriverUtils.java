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

import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverUtils {
    private WebDriver driver;

    public WebDriverUtils(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriverUtils(WebElement element) {
        this.driver = getDriverFromElement(element);
    }

    public static WebDriver getDriverFromElement(WebElement element) {
        return ((WrapsDriver) element).getWrappedDriver();
    }

    public static boolean isElementOfClass(WebElement coreElement, String className) {
        String[] classes = coreElement.getAttribute("class").split(" ");
        for (String clz : classes) {
            if (clz.equals(className)) {
                return true;
            }
        }
        return false;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebElement waitForElement(final By by, long timeOut) {
        WebDriverWait wwait = new WebDriverWait(driver, timeOut / 1000);
        wwait.ignoring(NoSuchElementException.class, InvalidElementStateException.class);
        WebElement ajaxElement = wwait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver d) {
                WebElement el = d.findElement(by);
                if (el.isEnabled()) return el;
                return null;
            }
        });
        return ajaxElement;
    }

    public WebElement waitForVisibleElement(final By by, long timeOut) {
        WebDriverWait wwait = new WebDriverWait(driver, timeOut / 1000);
        wwait.ignoring(NoSuchElementException.class, ElementNotVisibleException.class).ignoring(StaleElementReferenceException.class);
        wwait.ignoring(InvalidElementStateException.class);
        WebElement ajaxElement = null;
        try {
            ajaxElement = wwait.until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver d) {
                    WebElement el = d.findElement(by);
                    if (el.isDisplayed()) return el;
                    return null;
                }
            });
        } catch (TimeoutException e) {
            throw new RuntimeException("[TIME OUT ERROR ] elemnet '" + by + "' was not found! \n" + e.getLocalizedMessage());
        }
        return ajaxElement;
    }

    public WebElement waitForVisibleEnabledElement(final By by, long timeOut) {
        WebDriverWait wwait = new WebDriverWait(driver, timeOut / 1000);
        wwait.ignoring(NoSuchElementException.class, ElementNotVisibleException.class);
        wwait.ignoring(InvalidElementStateException.class);
        WebElement ajaxElement = wwait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver d) {
                WebElement el = d.findElement(by);
                if (el.isDisplayed() && el.isEnabled()) return el;
                return null;
            }
        });
        return ajaxElement;
    }

}
