package com.braimanm.ui.auto.pageobjects;

import com.braimanm.ui.auto.components.WebComponent;
import com.braimanm.ui.auto.pagecomponent.InitPage;
import com.braimanm.ui.auto.pagecomponent.PageObject;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.openqa.selenium.support.FindBy;

@XStreamAlias("inner-page")
@SuppressWarnings({"NewClassNamingConvention", "unused"})
public class InnerPageObjects extends PageObject {
    @FindBy(css = "#main1")
    private WebComponent main1;
    @InitPage
    private InnerPO innerPO;

    public static class InnerPO extends PageObject {
        @FindBy(xpath = "./../html")
        private WebComponent in1;
    }


}
