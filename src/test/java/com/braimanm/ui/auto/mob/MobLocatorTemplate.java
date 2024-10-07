package com.braimanm.ui.auto.mob;

import com.braimanm.ui.auto.pagefactory.LocatorTemplate;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class MobLocatorTemplate implements LocatorTemplate {

    @Override
    public By getBy(String[] value) {
        return AppiumBy.id(String.format("com.bitbar.testdroid:id/%s", value[0]));
    }
}
