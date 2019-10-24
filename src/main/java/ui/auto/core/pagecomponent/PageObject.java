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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import datainstiller.data.DataAliases;
import datainstiller.data.DataGenerator;
import datainstiller.data.DataPersistence;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.DataTypes;
import ui.auto.core.data.PageComponentDataConverter;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;

/**
 * @author Michael Braiman braimanm@gmail.com
 *         This is main Page Object class and it includes all the page component manipulation methods.
 *         All user defined page object classes must inherit this class and override required constructors.
 */
public class PageObject extends DataPersistence {
    @XStreamOmitField
    protected PageComponentContext context;
    @XStreamOmitField
    private String currentUrl;
    @XStreamOmitField
    private boolean ajaxIsUsed;

    protected <T extends PageComponentContext> PageObject(T context) {
        initPage(context);
    }

    protected <T extends PageComponentContext> PageObject(T context, String expectedUrl) {
        initPage(context, expectedUrl);
    }

    protected <T extends PageComponentContext> PageObject(T context, boolean ajaxIsUsed) {
        initPage(context, ajaxIsUsed);
    }

    protected <T extends PageComponentContext> PageObject(T context, String expectedUrl, boolean ajaxIsUsed) {
        initPage(context, expectedUrl, ajaxIsUsed);
    }

    protected PageObject() {
    }


    @Override
    protected XStream getXstream() {
        XStream xStream = super.getXstream();
        xStream.registerConverter(new PageComponentDataConverter());
        return xStream;
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private DataGenerator getGenerator() {
        return new DataGenerator(getXstream());
    }

    private void addToGlobalAliases(DataPersistence data) {
        DataAliases global = PageComponentContext.getGlobalAliases();
        DataAliases local = data.getDataAliases();
        if (local != null) {
            global.putAll(local);
        }
    }

    @Override
    public <T extends DataPersistence> T fromXml(String xml, boolean resolveAliases) {
        T data = super.fromXml(xml, resolveAliases);
        addToGlobalAliases(data);
        if (context != null) {
            ((PageObject) data).initPage(context, ajaxIsUsed);
        }
        return data;
    }

    @Override
    public <T extends DataPersistence> T fromURL(URL url, boolean resolveAliases) {
        T data = super.fromURL(url, resolveAliases);
        addToGlobalAliases(data);
        if (context != null) {
            ((PageObject) data).initPage(context, ajaxIsUsed);
        }
        return data;
    }

    @Override
    public <T extends DataPersistence> T fromInputStream(InputStream inputStream, boolean resolveAliases) {
        T data = super.fromInputStream(inputStream, resolveAliases);
        addToGlobalAliases(data);
        if (context != null) {
            ((PageObject) data).initPage(context, ajaxIsUsed);
        }
        return data;
    }

    @Override
    public <T extends DataPersistence> T fromFile(String filePath, boolean resolveAliases) {
        T data = super.fromFile(filePath, resolveAliases);
        addToGlobalAliases(data);
        if (context != null) {
            ((PageObject) data).initPage(context, ajaxIsUsed);
        }
        return data;
    }


    public <T extends DataPersistence> T generateData(boolean resolveAliases) {
        DataPersistence data = getGenerator().generate(this.getClass());
        addToGlobalAliases(data);
        if (context != null) {
            ((PageObject) data).initPage(context, ajaxIsUsed);
        }
        return (T) data;
    }

    @Override
    public void generateData() {
        DataPersistence dataPersistence = getGenerator().generate(this.getClass());
        addToGlobalAliases(dataPersistence);
        PageComponentContext cont = context;
        deepCopy(dataPersistence, this);
        if (cont != null) {
            initPage(cont);
        }
    }

    @Override
    public String generateXML() {
        DataPersistence dataPersistence = getGenerator().generate(this.getClass());
        addToGlobalAliases(dataPersistence);
        return dataPersistence.toXML();
    }

    @Override
    public String toXML() {
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
        XStream xstream = getXstream();

        // If this class extends PageObject and it initialized with context
        // then all the WebComponent fields of this class are CGLIB proxies and by default xml node
        // is marked with attribute class. This will remove class attribute from the xml node
        if (PageObject.class.isAssignableFrom(this.getClass()) &&
                this.getContext() != null) {
            xstream.aliasSystemAttribute(null, "class");
        }
        String xml = xstream.toXML(this);
        return header + xml;
    }

    public <T extends PageComponentContext> void initPage(T context, boolean ajaxIsUsed) {
        this.context = context;
        this.ajaxIsUsed = ajaxIsUsed;
        if (AppiumDriver.class.isAssignableFrom(getDriver().getClass())) {
            PageFactory.initElements(new WidgetFieldDecorator(context, this), this);
        } else {
            currentUrl = context.getDriver().getCurrentUrl();
            if (ajaxIsUsed) {
                AjaxVisibleElementLocatorFactory ajaxVisibleElementLocatorFactory = new AjaxVisibleElementLocatorFactory(context.getDriver(), context.getAjaxTimeOut());
                PageFactory.initElements(new ComponentFieldDecorator(ajaxVisibleElementLocatorFactory, this), this);
            } else {
                DefaultElementLocatorFactory defLocFactory = new DefaultElementLocatorFactory(context.getDriver());
                PageFactory.initElements(new ComponentFieldDecorator(defLocFactory, this), this);
            }
        }
    }

    public <T extends PageComponentContext> void initPage(T context) {
        initPage(context, true);
    }

    public <T extends PageComponentContext> void initPage(T context, String expectedUrl) {
        initPage(context, expectedUrl, true);
    }

    private <T extends PageComponentContext> void initPage(T context, String expectedUrl, boolean ajaxIsUsed) {
        initPage(context, ajaxIsUsed);
        if (expectedUrl != null) waitForUrl(expectedUrl);
    }

    @SuppressWarnings("unchecked")
    public <T extends PageComponentContext> T getContext() {
        return (T) context;
    }

    public void waitForUrl(String expectedUrl) {
        long endTime = System.currentTimeMillis() + context.getWaitForUrlTimeOut();
        while (System.currentTimeMillis() < endTime) {
            if (context.getDriver().getCurrentUrl().contains(expectedUrl)) {
                currentUrl = context.getDriver().getCurrentUrl();
                return;
            }
            sleep(100);
        }
        throw new RuntimeException("Expected url:" + expectedUrl + " is not displayed!");
    }

    public boolean waitForUrlToChange() {
        long endTime = System.currentTimeMillis() + context.getWaitForUrlTimeOut();
        boolean exitWhile = false;
        while (System.currentTimeMillis() < endTime && !exitWhile) {
            if (!context.getDriver().getCurrentUrl().contains(currentUrl)) {
                currentUrl = context.getDriver().getCurrentUrl();
                exitWhile = true;
            }
            sleep(100);
        }
        return exitWhile;
    }

    protected void setElementValue(PageComponent component) {
        setElementValue(component, true);
    }

    protected void setElementValue(PageComponent component, String value) {
        setElementValue(component, value, true);
    }

    protected void setElementValue(PageComponent component, boolean validate) {
        if (component.getData() != null && !component.getData().isEmpty()) {
            String valData = null;
            for (int i = 0; i < 3; i++) {
                component.setValue();
                if (!validate) return;
                valData = component.getData();
                if (component.getData(DataTypes.Expected, true) != null)
                    valData = component.getData(DataTypes.Expected, true);
                if (component.getValue().equals(valData)) return;
            }
            throw new RuntimeException("Can't set element '" + component.getClass().getSimpleName() + "' to value: " + valData);
        }
    }

    protected void setElementValue(PageComponent component, String value, boolean validate) {
        String realValue = null;
        if (component.getData() != null && !component.getData().isEmpty())
            realValue = component.getData();
        component.setData(value);
        setElementValue(component, validate);
        component.setData(realValue);
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
        PageComponentContext currentContext = context;
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

}
