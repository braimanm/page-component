/*
Copyright 2010-2019 Michael Braiman braimanm@gmail.com

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

import datainstiller.data.DataAliases;
import org.openqa.selenium.WebDriver;

/**
 * @author Michael Braiman braimanm@gmail.com
 *         This class represent context for the page-component library.
 *         It stores instance of the WebDriver, global data aliases and some timeout parameters
 */
public class PageComponentContext {
    private static ThreadLocal<DataAliases> globalAliases = ThreadLocal.withInitial(DataAliases::new);
    protected WebDriver driver;
    private int ajaxTimeOut = 10; //in seconds
    private int waitForUrlTimeOut = 10000; //in milliseconds
    private String dataGenerationPath = System.getProperty("user.dir");


    /**
     * Construct new context with provided {@link WebDriver}
     *
     * @param driver instance of WebDriver
     */
    public PageComponentContext(WebDriver driver) {
        this.driver = driver;
    }

    public PageComponentContext(WebDriver driver, int timeOutInSeconds) {
        this.driver = driver;
        ajaxTimeOut = timeOutInSeconds;
    }


    /**
     * Returns global aliases
     *
     * @return global aliases store
     */
    public static DataAliases getGlobalAliases() {
        return globalAliases.get();
    }

    /**
     * Returns {@link WebDriver}
     *
     * @return WebDriver instance
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Returns global timeout for polling web elements in seconds
     *
     * @return timeout for slowly loaded web elements
     */
    public int getAjaxTimeOut() {
        return ajaxTimeOut;
    }

    /**
     * The default value for AjaxTimeout is 10 seconds
     *
     * @param ajaxTimeOut maximum time to poll for Ajax affected web elements
     */
    public void setAjaxTimeOut(int ajaxTimeOut) {
        this.ajaxTimeOut = ajaxTimeOut;
    }

    /**
     * Returns global timeout for loading web pages in milliseconds
     *
     * @return timeout for loading web pages
     */
    public int getWaitForUrlTimeOut() {
        return waitForUrlTimeOut;
    }

    /**
     * The default value for wait for url is 10000 milliseconds which is 10 seconds
     *
     * @param waitForUrlTimeOut maximum time to poll for url to load
     */
    public void setWaitForUrlTimeOut(int waitForUrlTimeOut) {
        this.waitForUrlTimeOut = waitForUrlTimeOut;
    }

    /**
     * Returns the file path for generated data-sets
     *
     * @return data generation file path
     */
    public String getDataGenerationFilePath() {
        return dataGenerationPath;
    }

    /**
     * Provides the file path to store generated data-sets
     *
     * @param filePath file path to store generated data-sets
     */
    public void setDataGenerationFilePath(String filePath) {
        dataGenerationPath = filePath;
    }


}
