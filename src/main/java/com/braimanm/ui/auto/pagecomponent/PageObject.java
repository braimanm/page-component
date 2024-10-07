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

import com.braimanm.datainstiller.data.DataPersistence;
import com.braimanm.ui.auto.context.WebDriverContext;
import com.braimanm.ui.auto.data.DataTypes;
import com.braimanm.ui.auto.data.PageComponentDataConverter;
import com.braimanm.ui.auto.pagefactory.AjaxVisibleElementLocatorFactory;
import com.braimanm.ui.auto.pagefactory.AppiumComponentLocatorFactory;
import com.braimanm.ui.auto.pagefactory.DefaultComponentLocatorFactory;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.time.Duration;

/**
 * @author Michael Braiman braimanm@gmail.com
 *         This is main Page Object class, and it includes all the page component manipulation methods.
 *         All user defined page object classes must inherit this class and override required constructors.
 */
@SuppressWarnings({"unused", "SameParameterValue", "NewClassNamingConvention"})
public class PageObject extends DataPersistence {
    @XStreamOmitField
    private final static Logger logger = LoggerFactory.getLogger(PageObject.class);
    @XStreamOmitField
    protected WebDriverContext context;
    @XStreamOmitField
    private String currentUrl;
    @XStreamOmitField
    protected boolean ajaxIsUsed;
    @XStreamOmitField
    protected By locator;
    @XStreamOmitField
    protected boolean dataProvided;

    protected PageObject(WebDriverContext context) {
        initPage(context);
    }

    protected PageObject(WebDriverContext context, String expectedUrl) {
        initPage(context, expectedUrl);
    }

    protected PageObject(WebDriverContext context, boolean ajaxIsUsed) {
        initPage(context, ajaxIsUsed);
    }

    protected PageObject(WebDriverContext context, String expectedUrl, boolean ajaxIsUsed) {
        initPage(context, expectedUrl, ajaxIsUsed);
    }

    protected PageObject() {
    }


    private static void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    protected XStream getXstream() {
        XStream xStream = super.getXstream();
        xStream.registerConverter(new PageComponentDataConverter());
        return xStream;
    }

    @Override
    protected <T extends DataPersistence> void finalizeData(T data) {
        super.finalizeData(data);
        if (context != null) {
            ((PageObject) data).initPage(context, ajaxIsUsed);
        }
        ((PageObject) data).dataProvided = true;
    }

    @Override
    public void generateData() {
        DataPersistence dataPersistence = getGenerator().generate(this.getClass());
        WebDriverContext cont = context;
        deepCopy(dataPersistence, this);
        if (cont != null) {
            initPage(cont);
        }
    }

    @Override
    public String toXML() {
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
        XStream xstream = getXstream();

        // If this class extends PageObject, and it initialized with context
        // then all the WebComponent fields of this class are CGLIB proxies and by default xml node
        // is marked with attribute class. This will remove class attribute from the xml node
        if (this.getContext() != null) {
            xstream.aliasSystemAttribute(null, "class");
        }
        String xml = xstream.toXML(this);
        return header + xml;
    }

    public <T extends WebDriverContext> void initPage(T context, boolean ajaxIsUsed) {
        this.context = context;
        this.ajaxIsUsed = ajaxIsUsed;

        if (AppiumDriver.class.isAssignableFrom(getDriver().getClass())) {
            Duration dur = Duration.ofSeconds(context.getElementLoadTimeout());
            AppiumComponentLocatorFactory appLocFactory = new AppiumComponentLocatorFactory(context.getDriver(), dur);
            PageFactory.initElements(new ComponentFieldDecorator(appLocFactory, this), this);
        } else {
            currentUrl = context.getDriver().getCurrentUrl();
            if (ajaxIsUsed) {
                AjaxVisibleElementLocatorFactory ajaxElFactory =
                        new AjaxVisibleElementLocatorFactory(context.getDriver(), context.getElementLoadTimeout());
                PageFactory.initElements(new ComponentFieldDecorator(ajaxElFactory, this), this);
            } else {
                DefaultComponentLocatorFactory deElFactory = new DefaultComponentLocatorFactory(context.getDriver());
                PageFactory.initElements(new ComponentFieldDecorator(deElFactory, this), this);
            }
        }
    }

    public void initPage(WebDriverContext context) {
        initPage(context, true);
    }

    public void initPage(WebDriverContext context, String expectedUrl) {
        initPage(context, expectedUrl, true);
    }

    private void initPage(WebDriverContext context, String expectedUrl, boolean ajaxIsUsed) {
        initPage(context, ajaxIsUsed);
        if (expectedUrl != null) waitForUrl(expectedUrl);
    }

    public WebDriverContext getContext() {
        return context;
    }

    public void waitForUrl(String expectedUrl) {
        long endTime = System.currentTimeMillis() + context.getPageLoadTimeOut();
        while (System.currentTimeMillis() < endTime) {
            if (context.getDriver().getCurrentUrl().contains(expectedUrl)) {
                currentUrl = context.getDriver().getCurrentUrl();
                return;
            }
            sleep();
        }
        throw new RuntimeException("Expected url:" + expectedUrl + " is not displayed!");
    }

    public boolean waitForUrlToChange() {
        long endTime = System.currentTimeMillis() + context.getPageLoadTimeOut();
        boolean exitWhile = false;
        while (System.currentTimeMillis() < endTime && !exitWhile) {
            if (!context.getDriver().getCurrentUrl().contains(currentUrl)) {
                currentUrl = context.getDriver().getCurrentUrl();
                exitWhile = true;
            }
            sleep();
        }
        return exitWhile;
    }

    protected void setElementValue(PageComponent component) {
        setElementValue(component, true);
    }

    protected void setElementValue(PageComponent component, String value) {
        setElementValue(component, value, true);
    }

    protected void setElementValue(PageComponent component, String value, boolean validate) {
        String realValue = null;
        if (component.getData() != null && !component.getData().isEmpty()) {
            realValue = component.getData(DataTypes.Data, false);
        }
        component.setData(value);
        setElementValue(component, validate);
        component.setData(realValue);
    }

    protected void setElementValue(PageComponent component, boolean validate) {
        String value = component.getData();
        if (value != null && !value.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                component.setValue();
                if (!validate) return;
                if (isValueAsExpected(component)) return;
            }
            throw new RuntimeException("Can't set page component '" + this.getClass().getName() + "." + component.getFieldName() + "' to value: " + value);
        }
    }

    protected boolean isValueAsExpected(PageComponent component) {
        DataTypes dataType = DataTypes.Data;
        if (component.getData(DataTypes.Expected, true) != null) {
            dataType = DataTypes.Expected;
        }
        boolean isAsExpected;
        try {
            component.validateData(dataType);
            isAsExpected = true;
        } catch (Throwable ignore) {
            isAsExpected = false;
        }
        return isAsExpected;
    }


    protected void enumerateFields(FieldEnumerationAction action) {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(XStreamOmitField.class)) {
                if (PageComponent.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    PageComponent component;
                    try {
                        component = (PageComponent) field.get(this);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        action.doAction(component, field);
                    } catch (Exception e) {
                        throw new RuntimeException("Failure during field enumeration for field: " +
                                this.getClass().getName() + ":" + field.getName(), e);
                    }
                }
            }
        }
    }

    protected void autoFillPage() {
        autoFillPage(true);
    }

    protected void autoFillPage(final boolean validate) {
        if (context == null) throw new RuntimeException("PageObject is not initialized, invoke initPage method!");
        enumerateFields((PageComponent, field) -> {
            if (!field.isAnnotationPresent(SkipAutoFill.class))
                setElementValue(PageComponent, validate);
        });

    }

    protected void autoValidatePage() {
        autoValidatePage(DataTypes.Data);
    }

    protected void autoValidatePage(final DataTypes validationMethod) {
        if (context == null) throw new RuntimeException("PageObject is not initialized, invoke initPage method!");
        enumerateFields((pageComponent, field) -> {
            String data = pageComponent.getData(validationMethod, true);
            if (!field.isAnnotationPresent(SkipAutoValidate.class) && data != null && !data.isEmpty())
                pageComponent.validateData(validationMethod);
        });
    }

    public void initData(PageObject pageObject) {
        WebDriverContext currentContext = context;
        boolean currentAjaxIsUsed = ajaxIsUsed;
        deepCopy(pageObject, this);
        if (currentContext != null) {
            initPage(currentContext, currentAjaxIsUsed);
        }
    }

    public WebDriver getDriver() {
        if (context != null) {
            return context.getDriver();
        }
        return null;
    }

    public By getLocator() {
        return locator;
    }

    public boolean isDataProvided() {
        return dataProvided;
    }

}
