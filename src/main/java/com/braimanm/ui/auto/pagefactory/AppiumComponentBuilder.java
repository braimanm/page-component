package com.braimanm.ui.auto.pagefactory;

import io.appium.java_client.internal.CapabilityHelpers;
import io.appium.java_client.pagefactory.DefaultElementByBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.lang.reflect.Field;

import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.unpackObjectFromSearchContext;


public class AppiumComponentBuilder extends DefaultElementByBuilder {
    private final Field field;
    private final By parentBy;

    public AppiumComponentBuilder(SearchContext searchContext, Field field, By parentBy) {
        super(readStringCapability(searchContext,"platformName"),
                readStringCapability(searchContext, "automationName"));
        this.field = field;
        this.parentBy = parentBy;
    }

    private static String readStringCapability(SearchContext searchContext, String capName) {
        if (searchContext == null) {
            return null;
        }
        return unpackObjectFromSearchContext(searchContext, HasCapabilities.class)
                .map(HasCapabilities::getCapabilities)
                .map(caps -> CapabilityHelpers.getCapability(caps, capName, String.class))
                .orElse(null);
    }

    private By chainIfParentExists(By childLocator) {
        return (parentBy != null) ? new ByChained(parentBy, childLocator) : childLocator;
    }

    @Override
    public boolean isLookupCached() {
        return false;
    }

    @Override
    public By buildBy() {
        this.setAnnotated(field);
        By patternBy = FindByPatternBuilder.buildFromPattern(field);
        By by = (patternBy != null) ? patternBy : super.buildBy();
        return chainIfParentExists(by);
    }

}
