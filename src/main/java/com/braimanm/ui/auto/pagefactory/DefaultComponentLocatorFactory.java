package com.braimanm.ui.auto.pagefactory;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;

public class DefaultComponentLocatorFactory implements ChainedLocatorFactory {
    private final SearchContext searchContext;
    private By parentBy = null;

    public DefaultComponentLocatorFactory(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    public void setParentBy(By parentBy) {
        this.parentBy = parentBy;
    }

    @Override
    public ElementLocator createLocator(Field field) {
        ComponentAnnotations componentAnnotations = new ComponentAnnotations(field, parentBy);
        return new DefaultElementLocator(searchContext, componentAnnotations);
    }
}
