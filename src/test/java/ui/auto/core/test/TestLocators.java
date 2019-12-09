package ui.auto.core.test;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ui.auto.core.context.PageComponentContext;

public class TestLocators {
    private PageComponentContext context;

    @BeforeTest
    public void setUp() {
        context = new PageComponentContext(new ChromeDriver());
    }

    @Test
    public void testPageObjectLocator() {
        Page2 page2 = new Page2().fromResource("LocatorsData.xml");
        page2.initPage(context);
        page2.page1.initPage(context);
        page2.page1.page3.initPage(context);
        page2.page1.page4.initPage(context);
        Assert.assertNull(page2.getLocator());
        Assert.assertEquals(page2.comp3.getLocator().toString(), "By.xpath: //comp3");
        Assert.assertEquals(page2.page1.getLocator().toString(), "By.xpath: //page1");
        Assert.assertEquals(page2.page1.comp1.getLocator().toString(), "By.chained({By.xpath: //page1,By.xpath: //comp1})");
        Assert.assertNull(page2.page1.page3.getLocator());
        Assert.assertEquals(page2.page1.page3.web3.getLocator().toString(), "By.cssSelector: web3");
        Assert.assertEquals(page2.page1.page4.getLocator().toString(), "By.cssSelector: page4");
        Assert.assertEquals(page2.page1.page4.web3.getLocator().toString(), "By.chained({By.cssSelector: page4,By.cssSelector: web3})");
    }

    @AfterTest
    public void cleanUp() {
        context.getDriver().quit();
    }
}
