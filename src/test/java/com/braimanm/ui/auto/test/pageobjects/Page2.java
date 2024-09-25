package com.braimanm.ui.auto.test.pageobjects;

import com.braimanm.ui.auto.components.WebComponent;
import com.braimanm.ui.auto.pagecomponent.PageObject;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.openqa.selenium.support.FindBy;

@XStreamAlias("page2")
@SuppressWarnings("NewClassNamingConvention")
public class Page2 extends PageObject {
    @FindBy(xpath = "//comp3")
    public WebComponent comp3;
    @FindBy(xpath = "//page1")
    public Page1 page1;
}
