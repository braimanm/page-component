package com.braimanm.ui.auto.pagefactory;

import io.appium.java_client.pagefactory.AppiumElementLocatorFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import java.lang.reflect.Field;
import java.time.Duration;

public class AppiumComponentLocatorFactory implements ChainedLocatorFactory {
    private final SearchContext searchContext;
    private final Duration duration;
    private By parentBy = null;

    public AppiumComponentLocatorFactory(SearchContext searchContext, Duration duration ) {
        this.searchContext = searchContext;
        this.duration = duration;
    }

    public void setParentBy(By parentBy) {
        this.parentBy = parentBy;
    }

    @Override
    public ElementLocator createLocator(Field field) {
        AppiumComponentBuilder defBuilder = new AppiumComponentBuilder(searchContext, field, parentBy);
        AppiumElementLocatorFactory appElLocatorFactory = new AppiumElementLocatorFactory(searchContext, duration, defBuilder);
        return appElLocatorFactory.createLocator(field);
    }
}
