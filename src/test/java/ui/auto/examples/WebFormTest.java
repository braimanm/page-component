package ui.auto.examples;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ui.auto.core.context.PageComponentContext;

public class WebFormTest {
    private PageComponentContext context;

    @BeforeMethod
    public void init() {
        WebDriver driver = new ChromeDriver();
        context = new PageComponentContext(driver);
        driver.get("http://goo.gl/gUqDHg");
    }

    @Test
    public void fillForm() {
        FormPageObject formPageObject = new FormPageObject().fromResource("FormDataSet.xml");
        formPageObject.initPage(context);
        formPageObject.fillForm();
    }

    @Test
    public void fillFormDynamicDataSet() {
        FormPageObject formPageObject = new FormPageObject().fromResource("FormDynamicDataSet.xml");
        formPageObject.initPage(context);
        formPageObject.fillForm();
    }

    @AfterMethod
    public void end() {
        context.getDriver().quit();
    }
}
