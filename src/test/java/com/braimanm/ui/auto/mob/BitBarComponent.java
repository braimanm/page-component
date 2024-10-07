package com.braimanm.ui.auto.mob;

import com.braimanm.ui.auto.components.MobileComponent;
import com.braimanm.ui.auto.pagefactory.LocatorTemplate;
import org.openqa.selenium.By;

public class BitBarComponent extends MobileComponent implements LocatorTemplate {

    @Override
    public By getBy(String[] value) {
        return new MobLocatorTemplate().getBy(value);
    }
}
