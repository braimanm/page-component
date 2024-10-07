package com.braimanm.ui.auto.components;

import com.braimanm.ui.auto.pagefactory.LocatorTemplate;
import org.openqa.selenium.By;

public class Component1 extends WebComponent implements LocatorTemplate {

    @Override
    public By getBy(String[] value) {
        return By.xpath(String.format("//div//div[@label='%s']//input", value[0]));
    }
}
