package com.braimanm.ui.auto.pagecomponent;

import org.openqa.selenium.By;

public interface LocatorStrategy {
    By getStrategy(String value);
}
