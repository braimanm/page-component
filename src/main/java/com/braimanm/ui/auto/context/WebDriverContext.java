package com.braimanm.ui.auto.context;

import org.openqa.selenium.WebDriver;

@SuppressWarnings("unused")
public class WebDriverContext {
    private final WebDriver driver;
    private int elementLoadTimeout = 10; //in seconds
    private int pageLoadTimeOut = 10000; //in milliseconds

    WebDriverContext(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public int getElementLoadTimeout() {
        return elementLoadTimeout;
    }

    public void setElementLoadTimeout(int elementLoadTimeout) {
        this.elementLoadTimeout = elementLoadTimeout;
    }

    public int getPageLoadTimeOut() {
        return pageLoadTimeOut;
    }

    public void setPageLoadTimeOut(int pageLoadTimeOut) {
        this.pageLoadTimeOut = pageLoadTimeOut;
    }

}
