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

package ui.auto.core.pagecomponent;

import io.appium.java_client.HasSessionDetails;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.DefaultElementByBuilder;
import net.sf.cglib.proxy.Enhancer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ByChained;
import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.ComponentData;
import ui.auto.core.data.DataTypes;

import java.lang.reflect.Field;

import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.unpackWebDriverFromSearchContext;
import static java.util.Optional.ofNullable;

public class WidgetFieldDecorator extends AppiumFieldDecorator {
    private PageObject page;
    private PageComponentContext context;

    public WidgetFieldDecorator(PageComponentContext context, PageObject page) {
        super(context.getDriver());
        this.context = context;
        this.page = page;
    }

    @Override
    public Object decorate(ClassLoader ignored, Field field) {
        String dataValue = null;
        String initialValue = null;
        String expectedValue = null;

        field.setAccessible(true);
        if (PageComponent.class.isAssignableFrom(field.getType())) {
            String platform = null;
            String automation = null;
            WebDriver webDriver = unpackWebDriverFromSearchContext(this.context.getDriver());
            HasSessionDetails hasSessionDetails = ofNullable(webDriver).map(driver -> {
                if (!HasSessionDetails.class.isAssignableFrom(driver.getClass())) {
                    return null;
                }
                return (HasSessionDetails) driver;
            }).orElse(null);

            if (hasSessionDetails != null) {
                platform = hasSessionDetails.getPlatformName();
                automation = hasSessionDetails.getAutomationName();
            }
            DefaultElementByBuilder builder = new DefaultElementByBuilder(platform, automation);
            builder.setAnnotated(field);
            By by = builder.buildBy();
            if (by == null) return null;

            if (page.getLocator() != null) {
                by = new ByChained(page.getLocator(), by);
            }

            ComponentData componentData;
            try {
                componentData = (ComponentData) field.get(page);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (componentData != null) {
                dataValue = componentData.getData(DataTypes.Data, false);
                initialValue = componentData.getData(DataTypes.Initial, false);
                expectedValue = componentData.getData(DataTypes.Expected, false);
            }

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(field.getType());
            enhancer.setCallback(new WidgetMethodInterceptor(by, context));
            Object componentProxy = enhancer.create();
            ((ComponentData) componentProxy).initializeData(dataValue, initialValue, expectedValue);
            ((PageComponent) componentProxy).selector = by;
            return componentProxy;
        }

        //PageObject as Web Component
        if (PageObject.class.isAssignableFrom(field.getType()) &&
                (field.isAnnotationPresent(FindBy.class) ||
                        field.isAnnotationPresent(FindAll.class) || field.isAnnotationPresent(FindBys.class))) {

            PageObject po;
            try {
                po = (PageObject) field.get(page);
                if (po == null) {
                    Object value = field.getType().newInstance();
                    po = (PageObject) value;
                    field.set(page, value);
                    po.dataProvided = false;
                } else {
                    po.dataProvided = true;
                }
                Annotations annotations = new Annotations(field);
                po.locator = annotations.buildBy();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return super.decorate(ignored, field);
    }
}
