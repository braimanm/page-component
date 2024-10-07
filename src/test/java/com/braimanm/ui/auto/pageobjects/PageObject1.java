package com.braimanm.ui.auto.pageobjects;

import com.braimanm.datainstiller.data.Data;
import com.braimanm.ui.auto.components.ByNameLocatorTemplate;
import com.braimanm.ui.auto.components.Component1;
import com.braimanm.ui.auto.pagecomponent.InitPage;
import com.braimanm.ui.auto.pagecomponent.PageObject;
import com.braimanm.ui.auto.pagefactory.FindByTemplate;

@SuppressWarnings("NewClassNamingConvention")
public class PageObject1 extends PageObject {
    @Data("John")
    @FindByTemplate("First Name")
    public Component1 comp1;
    @Data("Smith")
    @FindByTemplate(template = ByNameLocatorTemplate.class, value = "Last Name")
    public Component1 comp2;
    @InitPage
    public PageObject3 pageObject3;
}
