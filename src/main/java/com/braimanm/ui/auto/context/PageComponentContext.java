/*
Copyright 2010-2024 Michael Braiman braimanm@gmail.com

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

package com.braimanm.ui.auto.context;

import com.braimanm.datainstiller.context.DataContext;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class PageComponentContext extends DataContext {
    private static final Logger logger = LoggerFactory.getLogger(PageComponentContext.class);
    private static final ThreadLocal<WebDriverContext> webDriverContextThreadLocal = new ThreadLocal<>();
    private static final ScanResult classGraph = new ClassGraph().enableClassInfo().scan();

    public static WebDriverContext getContext() {
        return webDriverContextThreadLocal.get();
    }

    public static WebDriverContext initContext(Supplier<WebDriver> initDriver) {
        if (getContext() == null) {
            WebDriverContext context = new WebDriverContext(initDriver.get());
            webDriverContextThreadLocal.set(context);
        } else {
            logger.warn("WebDriver already initialized. Please remove existing Context and create new one.");
        }
        return getContext();
    }

    public static void removeContext() {
        if (getContext() != null) {
            getContext().getDriver().quit();
            webDriverContextThreadLocal.remove();
        }
    }

    public static ScanResult getClassGraph() {
        return classGraph;
    }

}
