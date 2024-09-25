package com.braimanm.ui.auto.pagefactory;

import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

public interface ChainedLocatorFactory extends ElementLocatorFactory {
    void setParentBy(By parentBy);
}
