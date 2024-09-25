package com.braimanm.ui.auto.pagefactory;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import org.openqa.selenium.support.pagefactory.AjaxElementLocator;
import org.openqa.selenium.support.pagefactory.Annotations;

import java.lang.reflect.Field;
import java.time.Clock;

@SuppressWarnings("unused")
public class AjaxVisibleElementLocator extends AjaxElementLocator {


    public AjaxVisibleElementLocator (
            SearchContext context, int timeOutInSeconds, AbstractAnnotations annotations) {
        super(context, timeOutInSeconds, annotations);
    }

    public AjaxVisibleElementLocator (
            Clock clock, SearchContext context, int timeOutInSeconds, AbstractAnnotations annotations) {
        super(clock, context, timeOutInSeconds, annotations);
    }

    public AjaxVisibleElementLocator(SearchContext searchContext, Field field, int timeOutInSeconds) {
        super(searchContext, field, timeOutInSeconds);
    }

    public AjaxVisibleElementLocator(
            Clock clock, SearchContext searchContext, Field field, int timeOutInSeconds) {
        super(clock, searchContext, timeOutInSeconds, new Annotations(field));
    }

    @Override
    protected boolean isElementUsable(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected long sleepFor() {
        return 100;
    }

}
