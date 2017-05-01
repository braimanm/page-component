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

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

/**
 * @author Michael Braiman braimanm@gmail.com
 *         <p/>
 *         This is a basic Select component
 */
public class SelectComponent extends PageComponent {
    @XStreamOmitField
    private Select select;

    public SelectComponent() {
    }

    public SelectComponent(WebElement element) {
        super(element);
    }

    public Select getSelectElement() {
        return select;
    }

    public void selectByVisibleText() {
        select.selectByVisibleText(getData());
    }

    public void selectByIndex() {
        select.selectByIndex(Integer.parseInt(getData()));
    }

    public void selectByValue() {
        select.selectByValue(getData());
    }

    public void deselectByValue() {
        select.deselectByValue(getData());
    }

    public void deselectByIndex() {
        select.deselectByIndex(Integer.parseInt(getData()));
    }

    public void deselectByVisibleText() {
        select.deselectByVisibleText(getData());
    }

    @Override
    protected void init() {
        select = new Select(coreElement);
    }

    @Override
    public void setValue() {
        selectByVisibleText();
    }

    @Override
    public String getValue() {
        return select.getFirstSelectedOption().getText();
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        Assert.assertEquals(getValue(), validationMethod.getData(this));
    }


}
