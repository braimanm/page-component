package ui.auto.core.test;

import org.openqa.selenium.support.FindBy;
import ui.auto.core.components.WebComponent;
import ui.auto.core.pagecomponent.PageObject;

public class Page1 extends PageObject {
    @FindBy(xpath = "//comp1")
    WebComponent comp1;
    Page3 page3;
    @FindBy(css = "page4")
    Page3 page4;
}
