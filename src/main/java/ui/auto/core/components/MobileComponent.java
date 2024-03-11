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

package ui.auto.core.components;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

public class MobileComponent extends PageComponent {

    public MobileComponent() {
    }

    public MobileComponent(WebElement element) {
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
        return coreElement.getText();
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        String valData = validationMethod.getData(this);
        if (valData != null)
            Assertions.assertThat(getValue()).isEqualTo(valData);
    }

    public void validateData(String data) {
        Assertions.assertThat(getValue()).isEqualTo(data);
    }

    public void validateData() {
        validateData(DataTypes.Data);
    }

}
