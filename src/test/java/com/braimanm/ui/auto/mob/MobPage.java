package com.braimanm.ui.auto.mob;

import com.braimanm.datainstiller.data.Data;
import com.braimanm.ui.auto.data.DataTypes;
import com.braimanm.ui.auto.pagecomponent.PageObject;
import com.braimanm.ui.auto.pagefactory.FindByTemplate;
import com.braimanm.ui.auto.pagefactory.LocatorTemplate;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

@SuppressWarnings({"NewClassNamingConvention", "unused"})
@XStreamAlias("mob-page")
public class MobPage extends PageObject implements LocatorTemplate {
    @FindByTemplate(template = MobLocatorTemplate.class, value = "radioGroup1")
    private RadioGroup option;

    @FindByTemplate({"editText1", "frenchText"})
    private BitBarComponent name;
    @Data(skip = true)
    @FindByTemplate("button1")
    private BitBarComponent answerButton;
    @FindByTemplate("textView1")
    private BitBarComponent answerText1;
    @FindByTemplate("textView2")
    private BitBarComponent answerText2;


    public void populate() {
        autoFillPage();
    }

    public void submit() {
        answerButton.click();
    }

    public void validate() {
        Assertions.assertThat(answerText1.getData(DataTypes.Expected))
                .withFailMessage("Please provide data for answer line 1").isNotNull();
        Assertions.assertThat(answerText2.getData(DataTypes.Expected))
                .withFailMessage("Please provide data for answer line 2").isNotNull();
        answerText1.validateData(DataTypes.Expected);
        answerText2.validateData(DataTypes.Expected);
    }

    @Override
    public By getBy(String[] value) {
        return new MobLocatorTemplate().getBy(value);
    }

}

