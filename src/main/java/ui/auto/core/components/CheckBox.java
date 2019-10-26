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

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

/**
 * @author Michael Braiman braimanm@gmail.com
 *         This is a basic Check Box component
 */
public class CheckBox extends PageComponent {

    public CheckBox() {
    }

    public CheckBox(WebElement element) {
        super(element);
    }

    public void check() {
        if (!coreElement.isSelected()) coreElement.click();
    }

    public void uncheck() {
        if (coreElement.isSelected()) coreElement.click();
    }

    public void check(boolean value) {
        if (value) {
            check();
        } else {
            uncheck();
        }
    }

    @Override
    protected void init() {
    }

    @Override
    public void setValue() {
        check(getData().toLowerCase().equals("true"));
    }

    @Override
    public String getValue() {
        return String.valueOf(coreElement.isSelected());
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        Assert.assertEquals(coreElement.isSelected(), validationMethod.getData(this).toLowerCase().trim().equals("true"));
    }

}


