package com.braimanm.ui.auto.components;

import com.braimanm.ui.auto.pagecomponent.PageComponent;
import org.openqa.selenium.WebElement;

public abstract class ScreenComponent extends PageComponent {
    public ScreenComponent() {
        super();
    }

    public ScreenComponent(WebElement coreElement) {
        super(coreElement);
    }
}
