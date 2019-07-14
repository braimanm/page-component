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
