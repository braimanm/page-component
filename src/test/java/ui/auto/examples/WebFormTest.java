package ui.auto.examples;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ui.auto.core.context.PageComponentContext;

public class WebFormTest {
    private PageComponentContext context;

    @BeforeTest
    public void init() {
        WebDriver driver = new FirefoxDriver();
        context = new PageComponentContext(driver);
        driver.get("http://goo.gl/gUqDHg");
    }

    @Test
    public void fillForm() {
        FormPageObject formPageObject = new FormPageObject().fromResource("FormDataSet.xml", false);
        formPageObject.initPage(context);
        formPageObject.fillForm();
    }

    @AfterTest
    public void end() {
        context.getDriver().quit();
    }
}
