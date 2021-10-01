package ui.auto.core.test;

import org.openqa.selenium.support.FindBy;
import ui.auto.core.components.WebComponent;
import ui.auto.core.pagecomponent.InitPage;
import ui.auto.core.pagecomponent.PageObject;

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
