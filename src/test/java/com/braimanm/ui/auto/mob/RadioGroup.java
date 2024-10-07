package com.braimanm.ui.auto.mob;

import com.braimanm.ui.auto.components.ScreenComponent;
import com.braimanm.ui.auto.data.DataTypes;
import io.appium.java_client.AppiumBy;
import org.assertj.core.api.Assertions;

public class RadioGroup extends ScreenComponent {

    @Override
    protected void init() {
    }

    @Override
    public void setValue() {
        coreElement.findElement(AppiumBy.xpath(".//*[@text='" + getData() + "']")).click();
    }

    @Override
    public String getValue() {
        return coreElement.findElement(AppiumBy.xpath(".//*[@checked = 'true']")).getText();
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        Assertions.assertThat(getValue()).isEqualTo(validationMethod.getData(this));
    }
}
