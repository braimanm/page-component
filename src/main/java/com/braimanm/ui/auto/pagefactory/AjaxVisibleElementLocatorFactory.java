package com.braimanm.ui.auto.pagefactory;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;

public class AjaxVisibleElementLocatorFactory implements ChainedLocatorFactory {
    private final SearchContext searchContext;
    private final int timeOutInSeconds;
    private By parentBy = null;

    public AjaxVisibleElementLocatorFactory(SearchContext searchContext, int timeOutInSeconds) {
        this.searchContext = searchContext;
        this.timeOutInSeconds = timeOutInSeconds;
    }

    public void setParentBy(By parentBy) {
        this.parentBy = parentBy;
    }

    @Override
    public ElementLocator createLocator(Field field) {
        ComponentAnnotations componentAnnotations = new ComponentAnnotations(field, parentBy);
        return new AjaxVisibleElementLocator(searchContext, timeOutInSeconds, componentAnnotations);
    }

}
