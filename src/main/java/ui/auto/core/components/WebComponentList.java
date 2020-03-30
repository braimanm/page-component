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

package ui.auto.core.components;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class WebComponentList extends PageObject {
    @XStreamImplicit(itemFieldName = "item")
    private List<WebComponent> items;
    @XStreamOmitField
    private LinkedHashMap<String, WebElement> elementsMap;

    public WebComponentList() {
    }

    public WebComponentList(List<WebComponent> items, By locator) {
        this.items = items;
        this.locator = locator;
    }

    private String getValue(WebElement el) {
        String value = el.getAttribute("value");
        if (value == null) {
            value = el.getText();
        }
        if (value != null) {
            value = value.trim().replaceAll("\\n\\s*", "\n");
        }
        return value;
    }

    protected String getData(WebComponent cmp, DataTypes type) {
        String data = cmp.getData(type);
        if (data != null) {
            data = data.trim().replaceAll("\\n\\s*", "\n");
        }
        return data;
    }


    @Override
    public <T extends PageComponentContext> void initPage(T context) {
        super.initPage(context);
        List<WebElement> elementList = getDriver().findElements(getLocator());
        elementsMap = new LinkedHashMap<>();
        elementList.forEach(element -> elementsMap.put(getValue(element), element));
        if (items != null && !items.isEmpty()) {
            items.forEach(comp -> {
                String data = getData(comp, DataTypes.Data);
                String init = getData(comp, DataTypes.Initial);
                String exp = getData(comp, DataTypes.Expected);
                comp.initializeData(data, init, exp);
            });
        }
    }

    public void validateAll() {
        List<String> expectedList = new ArrayList<>();
        if (items == null) {
            throw new RuntimeException("WebComponentList: Please provide data for validation!");
        }
        items.forEach(comp -> expectedList.add(comp.getData(DataTypes.Data)));
        Assertions.assertThat(elementsMap.keySet()).containsExactlyElementsOf(expectedList);
    }

    public void validateUnordered() {
        List<String> expectedList = new ArrayList<>();
        if (items == null) {
            throw new RuntimeException("WebComponentList: Please provide data for validation!");
        }
        items.forEach(comp -> expectedList.add(comp.getData(DataTypes.Data)));
        Assertions.assertThat(elementsMap.keySet()).containsExactlyInAnyOrderElementsOf(expectedList);
    }

    public LinkedHashMap<String, WebElement> getElementsMap() {
        return elementsMap;
    }

    public List<WebComponent> getData() {
        return items;
    }
}
