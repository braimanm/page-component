package ui.auto.core.pagecomponent;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverUtils {
	private WebDriver driver;
	
	public WebDriverUtils(WebDriver driver) {
		this.driver=driver;
	}
	
	public WebDriverUtils(WebElement element){
		this.driver=getDriverFromElement(element);
	}
	
	public static WebDriver getDriverFromElement(WebElement element){
		return ((WrapsDriver) element).getWrappedDriver();
	}
	
	public WebDriver getDriver(){
		return driver;
	}
	
	public WebElement waitForElement(final By by,long timeOut){
		WebDriverWait wwait=new WebDriverWait(driver,timeOut/1000);
		wwait.ignoring(NoSuchElementException.class,InvalidElementStateException.class);
		WebElement ajaxElement = wwait.until(new ExpectedCondition<WebElement>() {
			@Override
			public WebElement apply(WebDriver d) {
				WebElement el=d.findElement(by);
				if (el.isEnabled()) return el;
				return null;
			}
		});
		return ajaxElement;
	}
	
	public WebElement waitForVisibleElement(final By by,long timeOut){
		WebDriverWait wwait=new WebDriverWait(driver,timeOut/1000);
		wwait.ignoring(NoSuchElementException.class,ElementNotVisibleException.class).ignoring(StaleElementReferenceException.class);
		wwait.ignoring(InvalidElementStateException.class);
		WebElement ajaxElement=null;
		try {
			ajaxElement =wwait.until(new ExpectedCondition<WebElement>() {
				@Override
				public WebElement apply(WebDriver d) {
					WebElement el=d.findElement(by);
					if (el.isDisplayed()) return el;
					return null;
				}
			});
		} catch (TimeoutException e){
			throw new RuntimeException("[TIME OUT ERROR ] elemnet '" + by + "' was not found! \n" + e.getLocalizedMessage() );
		}
		return ajaxElement;
	}
	
	public WebElement waitForVisibleEnabledElement(final By by,long timeOut){
		WebDriverWait wwait=new WebDriverWait(driver,timeOut/1000);
		wwait.ignoring(NoSuchElementException.class,ElementNotVisibleException.class);    
		wwait.ignoring(InvalidElementStateException.class);
		WebElement ajaxElement = wwait.until(new ExpectedCondition<WebElement>() {
			@Override
			public WebElement apply(WebDriver d) {
				WebElement el=d.findElement(by);
				if (el.isDisplayed() && el.isEnabled()) return el;
				return null;
			}
		});
		return ajaxElement;
	}
	
	public static boolean isElementOfClass(WebElement coreElement,String className){
		String[] classes=coreElement.getAttribute("class").split(" ");
		for (String clz:classes){
			if (clz.equals(className)){
				return true;
			}
		}
		return false;
	}

}