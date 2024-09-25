/*
Copyright 2010-2024 Michael Braiman braimanm@gmail.com

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

package com.braimanm.ui.auto.pagecomponent;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;

import java.util.List;

/**
 * @author Michael Braiman braimanm@gmail.com
 *         This is main page component class all the other user defined components must inherit this class.
 *         User defined page component must implement {@link DefaultAction} interface and abstract init() method
 */
@SuppressWarnings("unused")
public abstract class PageComponent extends AliasedData implements DefaultAction {
    protected WebElement coreElement;
    By selector;

    /**
     * Default constructor is needed for serialization purposes
     */
    public PageComponent() {
    }

    /**
     * Construct page component using existing web element as root of this component
     *
     * @param coreElement web element which is a core element for this component
     */
    public PageComponent(WebElement coreElement) {
        initComponent(coreElement);
    }

    /**
     * This method is needed for initialization of page component
     */
    protected abstract void init();

    /**
     * @return core web element for this component
     */
    public WebElement getCoreElement() {
        return coreElement;
    }

    /**
     * @return WebDriver wrapped by coreElement
     */
    public WebDriver getDriver() {
        if (WrapsDriver.class.isAssignableFrom(getCoreElement().getClass())) {
            return ((WrapsDriver) getCoreElement()).getWrappedDriver();
        } else {
            return null;
        }
    }

    /**
     * @param coreElement core web element for this component
     */
    void initComponent(WebElement coreElement) {
        this.coreElement = coreElement;
        init();
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

    public boolean isDisplayed() {
        boolean displayed = false;
        try {
            displayed = coreElement.isDisplayed();
        } catch (Exception ignore) {
        }
        return displayed;
    }

    public String getText() {
        return coreElement.getText();
    }

    public void sendKeys(CharSequence... keysToSend) {
        coreElement.sendKeys(keysToSend);
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
