package ui.auto.core.test;

import org.openqa.selenium.support.FindBy;
import ui.auto.core.components.WebComponent;
import ui.auto.core.pagecomponent.PageObject;

public class Page2 extends PageObject {
    @FindBy(xpath = "//page1")
    Page1 page1;
    @FindBy(xpath = "//comp3")
    WebComponent comp3;
}
