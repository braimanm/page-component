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

package ui.auto.core.components;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;


/**
 * @author Michael Braiman braimanm@gmail.com
 *         <p/>
 *         This is a generic component which is similar to {@link WebElement} but have data abstraction
 */

@XStreamAlias("web-component")
public class WebComponent extends PageComponent {

    public WebComponent() {
    }

    public WebComponent(WebElement element) {
        super(element);
    }

    @Override
    protected void init() {
        // no additional initialization or verification is needed
    }

    @Override
    public void setValue() {
        coreElement.clear();
        coreElement.sendKeys(getData());
    }

    @Override
    public String getValue() {
        String value = coreElement.getAttribute("value");
        if (value == null) {
            value = coreElement.getText();
        }
        return value;
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        String valData = validationMethod.getData(this);
        if (valData != null)
            Assert.assertEquals(getValue(), valData);
    }

    public void validateData(String data) {
        Assert.assertEquals(getValue(), data);
    }

    public void validateData() {
        validateData(DataTypes.Data);
    }

}
