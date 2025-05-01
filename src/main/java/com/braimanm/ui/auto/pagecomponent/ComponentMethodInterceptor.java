/*
 * Copyright 2010–2024 Michael Braiman (braimanm@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.braimanm.ui.auto.pagecomponent;

import com.braimanm.ui.auto.data.ComponentData;
import net.bytebuddy.implementation.bind.annotation.*;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class ComponentMethodInterceptor {

    private final ElementLocator locator;

    // Methods to skip (Object class + ComponentData interface + custom exclusions)
    private static final Set<String> skippedMethods = new HashSet<>();

    static {
        skippedMethods.addAll(Arrays.stream(Object.class.getDeclaredMethods())
                .map(Method::getName).collect(Collectors.toSet()));

        skippedMethods.addAll(Arrays.stream(ComponentData.class.getMethods())
                .map(Method::getName).collect(Collectors.toSet()));

        skippedMethods.add("init");
    }

    public ComponentMethodInterceptor(ElementLocator locator) {
        this.locator = locator;
    }

    @RuntimeType
    public Object intercept(@This Object self,
                            @Origin Method method,
                            @AllArguments Object[] args,
                            @SuperMethod Method superMethod) throws Throwable {

        String methodName = method.getName();

        if (skippedMethods.contains(methodName)) {
            return superMethod.invoke(self, args);
        }

        PageComponent pageComponent = (PageComponent) self;
        WebElement currentElement = pageComponent.coreElement;
        WebElement resolvedElement = locator.findElement();

        boolean elementIsStale = false;

        try {
            if (currentElement != null) {
                currentElement.isDisplayed(); // triggers StaleElementReferenceException if detached
            }
        } catch (StaleElementReferenceException e) {
            elementIsStale = true;
        }

        if (currentElement == null || elementIsStale || !currentElement.equals(resolvedElement)) {
            pageComponent.initComponent(resolvedElement);
        }

        try {
            return superMethod.invoke(self, args);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                throw new RuntimeException("Unexpected checked exception thrown by proxy method", cause);
            }
        }
    }
}