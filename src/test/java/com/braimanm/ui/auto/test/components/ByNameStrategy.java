package com.braimanm.ui.auto.test.components;

import com.braimanm.ui.auto.pagecomponent.LocatorStrategy;
import org.openqa.selenium.By;

public class ByNameStrategy implements LocatorStrategy {
    @Override
    public By getStrategy(String value) {
        return By.xpath(String.format("//div//div[@label='%s']//input", value));
    }
}
