/*
Copyright 2010-2012 Michael Braiman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package ui.auto.core.context;

import org.openqa.selenium.WebDriver;

import ui.auto.core.data.DataAliases;

public class PageComponentContext {
	protected WebDriver driver;
	private int ajaxTimeOut=10; //in seconds
	private int waitForUrlTimeOut=10000; //in milliseconds 
	private String dataGenerationPath=System.getProperty("user.dir");
	private static ThreadLocal<DataAliases> globalAliases=new ThreadLocal<DataAliases>(){

		@Override
		protected DataAliases initialValue() {
			return new DataAliases();
		}
		
	};

	
	public static DataAliases getGlobalAliases() {
		return globalAliases.get();
	}
	
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
