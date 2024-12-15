package com.braimanm.ui.auto.pagecomponent;

import com.braimanm.ui.auto.data.ComponentData;
import com.braimanm.ui.auto.data.DataTypes;
import com.braimanm.ui.auto.pagefactory.AppiumComponentBuilder;
import com.braimanm.ui.auto.pagefactory.ChainedLocatorFactory;
import com.braimanm.ui.auto.pagefactory.ComponentAnnotations;
import com.braimanm.ui.auto.pagefactory.FindByTemplate;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.*;
import io.appium.java_client.pagefactory.locator.CacheableLocator;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ComponentFieldDecorator extends DefaultFieldDecorator {
    private final ChainedLocatorFactory factory;
    private final PageObject page;

    public ComponentFieldDecorator(ChainedLocatorFactory factory, PageObject page) {
        super(factory);
        this.factory = factory;
        this.page = page;
    }

    private By getByFromElementLocator(ElementLocator locator) {

        try {
            Field byField;
            if (DefaultElementLocator.class.isAssignableFrom(locator.getClass())) {
                byField = DefaultElementLocator.class.getDeclaredField("by");
            } else if (CacheableLocator.class.isAssignableFrom(locator.getClass())) {
                Class<?> clazz = Class.forName("io.appium.java_client.pagefactory.AppiumElementLocator");
                byField = clazz.getDeclaredField("by");
            } else {
                return null;
            }
            byField.setAccessible(true);
            return (By) byField.get(locator);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Could not extract By from ElementLocator", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Object decorate(ClassLoader loader, final Field field) {

        field.setAccessible(true);
        if (PageComponent.class.isAssignableFrom(field.getType())) {
            String dataValue = null;
            String initialValue = null;
            String expectedValue = null;
            Map<String, String> customData = null;

            factory.setParentBy(page.getLocator());
            ElementLocator locator = factory.createLocator(field);
            if (locator == null) return null;
            By by = getByFromElementLocator(locator);

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

            Class<?> type = new ByteBuddy()
                    .with(new NamingStrategy.Suffixing("BB"))
                    .subclass(field.getType())
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(new ComponentMethodInterceptor(locator)))
                    .make()
                    .load(field.getType().getClassLoader())
                    .getLoaded();

            PageComponent comp;
            try {
                comp = (PageComponent) type.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            comp.initializeData(dataValue, initialValue, expectedValue);
            comp.addCustomData(customData);
            comp.fieldName = field.getName();
            comp.selector = by;

            return comp;
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
                        field.isAnnotationPresent(InitPage.class) ||
                        field.isAnnotationPresent(FindByTemplate.class) ||
                        field.isAnnotationPresent(FindBy.class) ||
                        field.isAnnotationPresent(FindAll.class) ||
                        field.isAnnotationPresent(FindBys.class) ||
                        field.isAnnotationPresent(AndroidFindBy.class) ||
                        field.isAnnotationPresent(AndroidFindBySet.class) ||
                        field.isAnnotationPresent(AndroidFindAll.class) ||
                        field.isAnnotationPresent(AndroidFindByAllSet.class) ||
                        field.isAnnotationPresent(AndroidFindByChainSet.class) ||
                        field.isAnnotationPresent(AndroidFindBys.class) ||
                        field.isAnnotationPresent(iOSXCUITFindBy.class) ||
                        field.isAnnotationPresent(iOSXCUITFindBySet.class) ||
                        field.isAnnotationPresent(iOSXCUITFindAll.class) ||
                        field.isAnnotationPresent(iOSXCUITFindByAllSet.class) ||
                        field.isAnnotationPresent(iOSXCUITFindByChainSet.class) ||
                        field.isAnnotationPresent(iOSXCUITFindBys.class)
                )
        ) {
            PageObject po;
            try {
                po = (PageObject) field.get(page);
                if (po == null) {
                    Object value = field.getType().getDeclaredConstructor().newInstance();
                    po = (PageObject) value;
                    field.set(page, value);
                    po.dataProvided = false;
                } else {
                    po.dataProvided = true;
                }
                po.ajaxIsUsed = page.ajaxIsUsed;
                if (!field.isAnnotationPresent(InitPage.class)) {
                    WebDriver driver = page.getDriver();
                    if (AppiumDriver.class.isAssignableFrom(driver.getClass())) {
                        po.locator = new AppiumComponentBuilder(driver, field, page.getLocator()).buildBy();
                    } else {
                        po.locator = new ComponentAnnotations(field, page.getLocator()).buildBy();
                    }
                }
                po.initPage(page.getContext(), page.ajaxIsUsed);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return super.decorate(loader, field);
    }
}
