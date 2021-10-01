package ui.auto.core.test;

import org.openqa.selenium.support.FindBy;
import ui.auto.core.components.WebComponent;
import ui.auto.core.pagecomponent.PageObject;

public class GooglePage extends PageObject {
    @FindBy(css = "input[name=q]")
    private WebComponent search;
    @FindBy(css = "input[name=q]")
    private WebComponent search2;


    public void search() {
        setElementValue(search);
    }

    public void search2() {
        setElementValue(search2);
    }

}
