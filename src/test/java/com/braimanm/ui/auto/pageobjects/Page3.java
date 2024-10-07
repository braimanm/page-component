package com.braimanm.ui.auto.pageobjects;

import com.braimanm.ui.auto.components.WebComponent;
import com.braimanm.ui.auto.pagecomponent.PageObject;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.openqa.selenium.support.FindBy;

@XStreamAlias("page3")
@SuppressWarnings("NewClassNamingConvention")
public class Page3 extends PageObject {
    @FindBy(css = "web3")
    public WebComponent web3;
    @FindBy(xpath = "//div[.='page4']")
    public Page4 page4;
}
