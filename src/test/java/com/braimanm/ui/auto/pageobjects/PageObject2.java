package com.braimanm.ui.auto.pageobjects;

import com.braimanm.ui.auto.pagecomponent.PageObject;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("NewClassNamingConvention")
public class PageObject2 extends PageObject {
    @FindBy(xpath = "//pagObject1//div")
    public PageObject1 pageObject1;
}
