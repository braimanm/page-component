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


package ui.auto.core.data;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import org.openqa.selenium.By;
import ui.auto.core.pagecomponent.ComponentMethodInterceptor;
import ui.auto.core.pagecomponent.PageComponent;

/**
 * @author Michael Braiman braimanm@gmail.com
 *         This interface represents data dimension, each {@link PageComponent} have data which can be used to populate
 *         and validate specific page component.
 *         {@link ComponentMethodInterceptor} will not initialize page component during
 *         invocations of the methods of this interface.
 */
@XStreamConverter(PageComponentDataConverter.class)
public interface ComponentData {
    /**
     * Get specific data of {@link DataTypes}
     *
     * @param type           data type to retrieve
     * @param resolveAliases if set to true will resolve alias to the specific value
     * @return returns specific data for the specific data type
     */
    String getData(DataTypes type, boolean resolveAliases);

    /**
     * Initialize three types of data: initial, populated and expected
     *
     * @param data     set populated data value
     * @param initial  set initial data value
     * @param expected set expected data value
     */
    void initializeData(String data, String initial, String expected);

    /**
     * Returns selenium {@link By} locator for the specific page component
     *
     * @return locator which was used to locate this page component
     */
    By getLocator();
}
