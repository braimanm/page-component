package com.braimanm.ui.auto.test.pageobjects;

import com.braimanm.ui.auto.components.WebComponent;
import com.braimanm.ui.auto.pagecomponent.PageObject;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.openqa.selenium.support.FindBy;

@XStreamAlias("google-page")
@SuppressWarnings({"unused", "NewClassNamingConvention"})
public class GooglePage extends PageObject {
    @FindBy(css = "form textarea")
    private WebComponent search;
    @FindBy(css = "form textarea")
    private WebComponent search2;


    public void search() {
        setElementValue(search);
    }

    public void search2() {
        setElementValue(search2);
    }

}
