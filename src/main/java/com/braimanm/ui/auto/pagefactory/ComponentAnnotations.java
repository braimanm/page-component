package com.braimanm.ui.auto.pagefactory;

import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.lang.reflect.Field;

public class ComponentAnnotations extends Annotations {
    private final By parentBy;

    public ComponentAnnotations(Field field, By parentBy) {
        super(field);
        this.parentBy = parentBy;
    }


    private By chainIfParentExists(By childLocator) {
        return (parentBy != null) ? new ByChained(parentBy, childLocator) : childLocator;
    }

    @Override
    public By buildBy() {
        By patternBy = FindByPatternBuilder.buildFromPattern(getField());
        By by = (patternBy != null) ? patternBy : super.buildBy();
        return chainIfParentExists(by);
    }

}
