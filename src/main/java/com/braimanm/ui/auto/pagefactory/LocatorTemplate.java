package com.braimanm.ui.auto.pagefactory;

import org.openqa.selenium.By;

public interface LocatorTemplate {
    By getBy(String[] value);
}
