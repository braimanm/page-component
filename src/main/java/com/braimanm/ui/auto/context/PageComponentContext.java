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
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class PageComponentContext extends DataContext {
    private static final Logger logger = LoggerFactory.getLogger(PageComponentContext.class);
    private static final ThreadLocal<Map<String,WebDriverContext>> webDriverContextThreadLocal = ThreadLocal.withInitial(HashMap::new);
    public static final String DEFAULT = "DEFAULT";

    public static List<String> getAllContextNames() {
        return List.copyOf(webDriverContextThreadLocal.get().keySet());
    }

    public static WebDriverContext getContext(String contextName) {
        return webDriverContextThreadLocal.get().get(contextName);
    }

    public static WebDriverContext getContext() {
        return webDriverContextThreadLocal.get().get(DEFAULT);
    }

    public static WebDriverContext initContext(String contextName, Supplier<WebDriver> initDriver) {
        if (getContext(contextName) == null) {
            WebDriverContext context = new WebDriverContext(initDriver.get());
            context.contextName = contextName;
            webDriverContextThreadLocal.get().put(contextName, context);
        } else {
            logger.warn("WebDriver already initialized. Please remove existing Context and create new one.");
        }
        return getContext(contextName);
    }

    public static WebDriverContext initContext(Supplier<WebDriver> initDriver) {
        return initContext(DEFAULT, initDriver);
    }

    public static void removeContext(String contextName) {
        if (getContext(contextName) != null) {
            getContext(contextName).quit();
            webDriverContextThreadLocal.get().remove(contextName);
        }
    }

    public static void removeContext() {
        removeContext(DEFAULT);
    }

}
