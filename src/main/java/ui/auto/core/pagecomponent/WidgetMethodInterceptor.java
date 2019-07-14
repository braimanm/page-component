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

package ui.auto.core.pagecomponent;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.ComponentData;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WidgetMethodInterceptor implements MethodInterceptor {
    private By locator;
    private PageComponentContext context;

    public WidgetMethodInterceptor(By locator, PageComponentContext context) {
        this.locator = locator;
        this.context = context;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String methodName = method.getName();

        //Skip methods belonging to Object class and ComponentData interface
        Set<Method> skippedMethods = new HashSet<>();
        skippedMethods.addAll(Arrays.asList(Object.class.getDeclaredMethods()));
        skippedMethods.addAll(Arrays.asList(ComponentData.class.getMethods()));
        for (Method dataMethod : skippedMethods) {
            if (dataMethod.getName().equals(methodName) || methodName.equals("init")) {
                return proxy.invokeSuper(obj, args);
            }
        }

        PageComponent pageComponent = (PageComponent) obj;
        WebDriverWait wait = new WebDriverWait(context.getDriver(), context.getAjaxTimeOut(), 200);
        WebElement coreElement = wait.ignoring(StaleElementReferenceException.class).
                until(ExpectedConditions.visibilityOfElementLocated(locator));
        if (coreElement != null) {
            pageComponent.initComponent(coreElement);
        }
        return proxy.invokeSuper(obj, args);
    }

}


