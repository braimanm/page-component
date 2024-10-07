package com.braimanm.ui.auto.pageobjects;

import com.braimanm.datainstiller.data.Data;
import com.braimanm.ui.auto.components.ByNameLocatorTemplate;
import com.braimanm.ui.auto.components.WebComponent;
import com.braimanm.ui.auto.pagecomponent.PageObject;
import com.braimanm.ui.auto.pagefactory.FindByTemplate;

@SuppressWarnings("NewClassNamingConvention")
public class PageObject3 extends PageObject {
    @Data("Junior")
    @FindByTemplate(template = ByNameLocatorTemplate.class, value = "Middle Name")
    public WebComponent comp1;
}
