package com.braimanm.ui.auto.pageobjects;

import com.braimanm.ui.auto.components.WebComponent;
import com.braimanm.ui.auto.pagecomponent.PageObject;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.openqa.selenium.support.FindBy;

@XStreamAlias("page3")
@SuppressWarnings("NewClassNamingConvention")
public class Page4 extends PageObject {
    @FindBy( className = "web4")
    public WebComponent web4;
}
