package ui.auto.core.context;

import org.openqa.selenium.WebDriver;

public class PageComponentContext {
	protected WebDriver driver;
	private int ajaxTimeOut=10; //in seconds
	private int waitForUrlTimeOut=10000; //in milliseconds 
	private String dataGenerationPath=System.getProperty("user.dir");
	
	public PageComponentContext(WebDriver driver) {
		this.driver=driver;
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	
	public int getAjaxTimeOut() {
		return ajaxTimeOut;
	}

	public void setAjaxTimeOut(int ajaxTimeOut) {
		this.ajaxTimeOut = ajaxTimeOut;
	}
	
	public int getWaitForUrlTimeOut() {
		return waitForUrlTimeOut;
	}

	public void setWaitForUrlTimeOut(int waitForUrlTimeOut) {
		this.waitForUrlTimeOut = waitForUrlTimeOut;
	}

	public String getDataGenerationFilePath(){
		return dataGenerationPath;
	}
	
	public void setDataGenerationFilePath(String filePath){
		dataGenerationPath=filePath;
	}
	
}
