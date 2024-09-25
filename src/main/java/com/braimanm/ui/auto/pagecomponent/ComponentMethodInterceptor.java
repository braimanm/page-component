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

package com.braimanm.ui.auto.pagecomponent;

import com.braimanm.ui.auto.data.ComponentData;
import net.bytebuddy.implementation.bind.annotation.*;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class ComponentMethodInterceptor {
    private final ElementLocator locator;

    public ComponentMethodInterceptor(ElementLocator locator) {
        this.locator = locator;
    }

    @RuntimeType
    public Object intercept(@This Object self,
                                   @Origin Method method,
                                   @AllArguments Object[] args,
                                   @SuperMethod Method superMethod) throws Throwable {

        String methodName = method.getName();

        //Skip methods belonging to Object class and ComponentData interface
        Set<Method> skippedMethods = new HashSet<>();
        skippedMethods.addAll(Arrays.asList(Object.class.getDeclaredMethods()));
        skippedMethods.addAll(Arrays.asList(ComponentData.class.getMethods()));
        for (Method dataMethod : skippedMethods) {
            if (dataMethod.getName().equals(methodName) || methodName.equals("init")) {
                return superMethod.invoke(self, args);
            }
        }

        PageComponent pageComponent = (PageComponent) self;
        WebElement currentCoreElement = pageComponent.coreElement;
        WebElement newCoreElement = locator.findElement();
        boolean staleElement = false;
        try {
            if (currentCoreElement != null)
                currentCoreElement.isDisplayed();//This should trigger exception if element is detached from Dom
        } catch (StaleElementReferenceException e) {
            staleElement = true;
        }
        if (currentCoreElement == null || staleElement || !currentCoreElement.equals(newCoreElement)) {
            pageComponent.initComponent(newCoreElement);
        }

        return superMethod.invoke(self, args);
    }
}


