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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Intercepts method calls on PageComponent proxies to ensure the backing WebElement is fresh.
 * <p>
 * This interceptor checks whether the underlying WebElement is stale or has changed since the last interaction.
 * If so, it reinitializes the component with the latest resolved element from the associated {@link ElementLocator}.
 * <p>
 * In the event of a {@link StaleElementReferenceException} during the method call itself, the interceptor will
 * automatically retry once after reinitializing the component, thus preventing flaky test failures.
 */
@SuppressWarnings("unused")
public class ComponentMethodInterceptor {

    private final ElementLocator locator;

    /**
     * Set of method names that should be skipped (not intercepted).
     * Includes all methods from Object, ComponentData, and "init".
     */
    private static final Set<String> skippedMethods = Stream.concat(
                    Arrays.stream(Object.class.getDeclaredMethods()),
                    Arrays.stream(ComponentData.class.getMethods())
            )
            .map(Method::getName)
            .collect(Collectors.toCollection(HashSet::new));

    static {
        // Add any explicitly skipped method names
        skippedMethods.add("init");
    }

    /**
     * Constructs a new interceptor for a PageComponent proxy.
     *
     * @param locator the {@link ElementLocator} used to resolve the WebElement dynamically
     */
    public ComponentMethodInterceptor(ElementLocator locator) {
        this.locator = locator;
    }

    /**
     * Intercepts method calls on a PageComponent proxy.
     * <p>
     * If the WebElement is stale, null, or has changed, the component is re-initialized.
     * If a {@link StaleElementReferenceException} occurs during method execution, it retries once.
     *
     * @param self         the proxy instance
     * @param method       the method being called
     * @param args         the arguments passed to the method
     * @param superMethod  the actual method to invoke on the proxy
     * @return the result of the method invocation
     * @throws Throwable the original exception if invocation fails
     */
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
        WebElement resolvedElement;

        try {
            resolvedElement = locator.findElement();

            boolean elementIsStale = false;
            try {
                if (currentElement != null) {
                    currentElement.isDisplayed(); // triggers if stale
                }
            } catch (StaleElementReferenceException e) {
                elementIsStale = true;
            }

            if (currentElement == null || elementIsStale || !Objects.equals(currentElement, resolvedElement)) {
                pageComponent.initComponent(resolvedElement);
            }

            return superMethod.invoke(self, args);

        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();

            if (cause instanceof StaleElementReferenceException) {
                // Retry once after reinitializing
                resolvedElement = locator.findElement();
                pageComponent.initComponent(resolvedElement);
                try {
                    return superMethod.invoke(self, args);
                } catch (InvocationTargetException retryEx) {
                    throw unwrapInvocationException(retryEx);
                }
            }

            throw unwrapInvocationException(e);

        } catch (StaleElementReferenceException directStale) {
            // If staleness occurs during findElement or display check
            resolvedElement = locator.findElement();
            pageComponent.initComponent(resolvedElement);
            try {
                return superMethod.invoke(self, args);
            } catch (InvocationTargetException retryEx) {
                throw unwrapInvocationException(retryEx);
            }
        }
    }

    /**
     * Unwraps the cause of an {@link InvocationTargetException} and rethrows it in its proper form.
     *
     * @param e the wrapped exception
     * @return the original runtime exception
     * @throws Error if the cause is an error
     * @throws RuntimeException if the cause is a checked exception
     */
    private RuntimeException unwrapInvocationException(InvocationTargetException e) {
        Throwable cause = e.getCause();
        if (cause instanceof RuntimeException) {
            return (RuntimeException) cause;
        } else if (cause instanceof Error) {
            throw (Error) cause;
        } else {
            return new RuntimeException("Unexpected checked exception from proxy", cause);
        }
    }
}