package com.braimanm.ui.auto.test.pageobjects;

import com.braimanm.datainstiller.data.Data;
import com.braimanm.ui.auto.pagecomponent.InitPage;
import com.braimanm.ui.auto.pagecomponent.PageObject;
import com.braimanm.ui.auto.test.components.Component1;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("NewClassNamingConvention")
public class PageObject1 extends PageObject {
    @Data("John")
    @FindBy(using = "First Name")
    public Component1 comp1;
    @Data("Smith")
    @FindBy(className = "ByNameStrategy", using = "Last Name")
    public Component1 comp2;
    @InitPage
    public PageObject3 pageObject3;
}
