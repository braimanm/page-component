package ui.auto.core.test;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.openqa.selenium.support.FindBy;
import ui.auto.core.components.WebComponent;
import ui.auto.core.pagecomponent.PageObject;

@XStreamAlias("Data-set-3")
public class DataSet3 extends PageObject {
    @FindBy(xpath = "locator")
    public WebComponent component;
}
