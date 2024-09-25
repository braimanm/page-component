package com.braimanm.ui.auto.test.pageobjects;

import com.braimanm.datainstiller.data.Data;
import com.braimanm.ui.auto.pagecomponent.PageObject;
import com.braimanm.ui.auto.test.components.Component1;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("NewClassNamingConvention")
public class PageObject3 extends PageObject {
    @Data("Junior")
    @FindBy(using = "Middle Name")
    public Component1 comp1;
}
