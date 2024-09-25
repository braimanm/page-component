package com.braimanm.ui.auto.test;

import com.braimanm.ui.auto.context.PageComponentContext;
import com.braimanm.ui.auto.test.pageobjects.*;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestLocators {

    @BeforeTest
    public void setUp() {
        PageComponentContext.initContext(ChromeDriver::new);
    }

    @Test
    public void testPageObjectLocator() {
        PageComponentContext.initContext(ChromeDriver::new);
        Page2 page2 = new Page2().fromResource("LocatorsData.xml");
        page2.initPage(PageComponentContext.getContext());
        page2.page1.page3.initPage(PageComponentContext.getContext());
        Assert.assertNull(page2.getLocator());
        Assert.assertEquals(page2.comp3.getLocator().toString(), "By.xpath: //comp3");
        Assert.assertEquals(page2.page1.getLocator().toString(), "By.xpath: //page1");
        Assert.assertEquals(page2.page1.comp1.getLocator().toString(), "By.chained({By.xpath: //page1,By.xpath: //comp1})");
        Assert.assertNull(page2.page1.page3.getLocator());
        Assert.assertEquals(page2.page1.page3.web3.getLocator().toString(), "By.cssSelector: web3");
        Assert.assertEquals(page2.page1.page4.getLocator().toString(), "By.cssSelector: page4");
        Assert.assertEquals(page2.page1.page4.web3.getLocator().toString(), "By.chained({By.cssSelector: page4,By.cssSelector: web3})");
    }


    @Test
    public void testSetElementValue() {
        GooglePage googlePage = new GooglePage().fromResource("GoogleDataSet.xml");
        googlePage.initPage(PageComponentContext.getContext());
        PageComponentContext.getContext().getDriver().get("http://google.com");
        googlePage.search();
        try {
            googlePage.search2();
            Assertions.fail("You shouldn't reach here!!!!");
        } catch (RuntimeException e) {
            Assertions.assertThat(e.getLocalizedMessage()).containsPattern("Can't set page component 'com.braimanm.ui.auto.test.pageobjects.GooglePage.search2' to value: github page-component");
        }
    }


    @Test
    public void validateInnerPO() {
        InnerPageObjects innerPageObjects = new InnerPageObjects().fromResource("InnerDataSet.xml");
        innerPageObjects.initPage(PageComponentContext.getContext());
        System.out.println();
    }

    @Test
    public void testPatternLocators() {
        PageObject1 po1 = new PageObject1();
        po1.initPage(PageComponentContext.getContext());
        Assertions.assertThat(po1.comp1.getLocator().toString())
                .isEqualTo("By.xpath: //div//div[@label='First Name']//input");
        Assertions.assertThat(po1.comp2.getLocator().toString())
                .isEqualTo("By.xpath: //div//div[@label='Last Name']//input");
    }

    @Test
    public void testPatterLocatorsAndInitPageAnnotation() {
        PageObject2 po2 = new PageObject2();
        po2.initPage(PageComponentContext.getContext());
        Assertions.assertThat(po2.pageObject1.comp1.getLocator().toString())
                .isEqualTo("By.chained({By.xpath: //pagObject1//div,By.xpath: //div//div[@label='First Name']//input})");
        Assertions.assertThat(po2.pageObject1.comp2.getLocator().toString())
                .isEqualTo("By.chained({By.xpath: //pagObject1//div,By.xpath: //div//div[@label='Last Name']//input})");
        Assertions.assertThat(po2.pageObject1.pageObject3.comp1.getLocator().toString())
                .isEqualTo("By.xpath: //div//div[@label='Middle Name']//input");
    }


    @AfterTest
    public void cleanUp() {
        PageComponentContext.removeContext();
    }
}
