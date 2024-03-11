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

package ui.auto.core.pagecomponent;


import net.sf.cglib.proxy.Enhancer;
import org.openqa.selenium.By;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.pagefactory.*;
import ui.auto.core.data.ComponentData;
import ui.auto.core.data.DataTypes;

import java.lang.reflect.Field;
import java.util.Map;

public class ComponentFieldDecorator extends DefaultFieldDecorator {
    PageObject page;

    public ComponentFieldDecorator(ElementLocatorFactory factory, PageObject page) {
        super(factory);
        this.page = page;
    }

    private By buildFromPattern(Field field) {
        if (field.isAnnotationPresent(FindBy.class)) {
            FindBy fb = field.getAnnotation(FindBy.class);
            if (fb.how().equals(How.UNSET) && !fb.using().isEmpty()) {
                Class type = field.getType();
                do {
                    if (type.isAnnotationPresent(LocatorPattern.class)) {
                        LocatorPattern lp = (LocatorPattern) type.getAnnotation(LocatorPattern.class);
                        if (lp.how().equals(How.XPATH) || lp.how().equals(How.CSS)) {
                            String loc = lp.pattern().replace("${val}", fb.using());
                            return lp.how().equals(How.XPATH) ? By.xpath(loc) : By.cssSelector(loc);
                        }
                    } else {
                        type = type.getSuperclass();
                    }
                } while (!type.equals(PageComponent.class));
            }

        }
        return null;
    }

    private By modifyLocator(ElementLocator locator, By parentLocator, Field field) {
        By bys = null;
        By patternLocator = buildFromPattern(field);
        if (DefaultElementLocator.class.isAssignableFrom(locator.getClass())) {
            try {
                Field by = DefaultElementLocator.class.getDeclaredField("by");
                by.setAccessible(true);
                if (patternLocator != null) {
                    by.set(locator, patternLocator);
                }
                By childLocator = (By) by.get(locator);
                if (parentLocator != null) {
                    bys = new ByChained(parentLocator, childLocator);
                    by.set(locator, bys);
                } else {
                    bys = childLocator;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return bys;
    }


    @Override
    public Object decorate(ClassLoader loader, final Field field) {
        String dataValue = null;
        String initialValue = null;
        String expectedValue = null;
        Map<String, String> customData = null;

        field.setAccessible(true);
        if (PageComponent.class.isAssignableFrom(field.getType())) {
            ElementLocator locator = factory.createLocator(field);
            if (locator == null) return null;

            By by = modifyLocator(locator, page.getLocator(), field);

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
                customData = componentData.getCustomData();
            }

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(field.getType());
            enhancer.setCallback(new ComponentMethodInterceptor(locator));
            Object componentProxy = enhancer.create();
            ((ComponentData) componentProxy).initializeData(dataValue, initialValue, expectedValue);
            ((ComponentData) componentProxy).addCustomData(customData);
            ((PageComponent) componentProxy).fieldName = field.getName();
            ((PageComponent) componentProxy).selector = by;
            return componentProxy;
        }

        if (AliasedData.class.isAssignableFrom(field.getType())) {
            AliasedData aliasedData;
            try {
                aliasedData = (AliasedData) field.get(page);
                if (aliasedData == null) {
                    aliasedData = new AliasedData();
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            aliasedData.fieldName = field.getName();

            return aliasedData;
        }

        //PageObject as Web Component
        if (PageObject.class.isAssignableFrom(field.getType()) &&
                (
                        field.isAnnotationPresent(FindBy.class) ||
                        field.isAnnotationPresent(FindAll.class) ||
                        field.isAnnotationPresent(FindBys.class) ||
                        field.isAnnotationPresent(InitPage.class)
                )
        ) {
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
                po.ajaxIsUsed = page.ajaxIsUsed;
                if (!field.isAnnotationPresent(InitPage.class)) {
                    Annotations annotations = new Annotations(field);
                    po.locator = annotations.buildBy();
                }
                PageFactory.initElements(new ComponentFieldDecorator(factory, po), po);
                po.context = page.getContext();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return super.decorate(loader, field);
    }
}
