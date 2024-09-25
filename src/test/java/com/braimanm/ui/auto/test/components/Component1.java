package com.braimanm.ui.auto.test.components;

import com.braimanm.ui.auto.components.WebComponent;
import com.braimanm.ui.auto.pagecomponent.LocatorPattern;
import org.openqa.selenium.support.How;

@LocatorPattern(how = How.XPATH, pattern = "//div//div[@label='${val}']//input")
public class Component1 extends WebComponent {
}
