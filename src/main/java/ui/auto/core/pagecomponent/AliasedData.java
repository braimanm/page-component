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

package ui.auto.core.pagecomponent;

import org.openqa.selenium.By;
import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.ComponentData;
import ui.auto.core.data.DataTypes;

import java.util.Map;

@SuppressWarnings("unused")
public class AliasedData implements ComponentData {
    String fieldName;
    private String data;
    private String initialData;
    private String expectedData;
    private Map<String, String> customData;

    /**
     * Default constructor is needed for serialization purposes
     */
    public AliasedData() {
    }

    @Override
    public void initializeData(String data, String initialData, String expectedData) {
        this.data = data;
        this.initialData = initialData;
        this.expectedData = expectedData;
    }

    @Override
    public void addCustomData(Map<String, String> customData) {
        this.customData = customData;
    }

    @Override
    public By getLocator() {
        return null;
    }

    private String resolveAliasesForData(String data) {
        return PageComponentContext.getGlobalAliases().resolveData(data);
    }

    @Override
    public String getData(DataTypes type, boolean resolveAliases) {
        String data = null;
        switch (type) {
            case Data:
                data = this.data;
                break;
            case Initial:
                data = this.initialData;
                break;
            case Expected:
                data = this.expectedData;
                break;
        }
        if (data != null && resolveAliases) {
            data = resolveAliasesForData(data);
        }
        return data;
    }

    @Override
    public String getData(String dataName, boolean resolveAliases) {
        if (dataName.equals("initial")) return getData(DataTypes.Initial, resolveAliases);
        if (dataName.equals("expected")) return getData(DataTypes.Expected, resolveAliases);
        String data = null;
        if (customData != null && !customData.isEmpty()) {
            data = customData.get(dataName);
            if (data != null && resolveAliases) {
                data = resolveAliasesForData(data);
            }
        }
        return data;
    }

    public String getData(String dataName) {
        return getData(dataName, true);
    }

    @Override
    public Map<String, String> getCustomData() {
        return customData;
    }

    /**
     * Get populated data for this component
     *
     * @return data
     */
    public String getData() {
        return getData(DataTypes.Data);
    }

    /**
     * @param data string which represent data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Get populated data for this component
     *
     * @param type data type to retrieve
     * @return data for the requested data type
     */
    public String getData(DataTypes type) {
        return getData(type, true);
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
