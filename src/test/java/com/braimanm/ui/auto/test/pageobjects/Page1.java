package com.braimanm.ui.auto.test.pageobjects;

import com.braimanm.ui.auto.components.WebComponent;
import com.braimanm.ui.auto.pagecomponent.PageObject;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.openqa.selenium.support.FindBy;

@XStreamAlias("page1")
@SuppressWarnings("NewClassNamingConvention")
public class Page1 extends PageObject {
    @FindBy(xpath = "//comp1")
    public WebComponent comp1;
    @FindBy(css = "page4")
    public Page3 page4;
    public Page3 page3;

}
