package ui.auto.core.test;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import ui.auto.core.data.DataTypes;

public class TestCustomAttributes {
    @Test
    public void testCustomAttributesAndAliases() {
        DataSet3 dataSet3 = new DataSet3().fromResource("DataSet3.xml");
        Assertions.assertThat(dataSet3.component.getData()).isEqualTo("component");
        Assertions.assertThat(dataSet3.component.getData(DataTypes.Data)).isEqualTo("component");
        Assertions.assertThat(dataSet3.component.getData(DataTypes.Expected)).isEqualTo("expected");
        Assertions.assertThat(dataSet3.component.getData(DataTypes.Initial)).isEqualTo("initial");
        Assertions.assertThat(dataSet3.component.getData("name", false)).isEqualTo("${name}");
        Assertions.assertThat(dataSet3.component.getData("name", true)).isEqualTo("Michael");
        Assertions.assertThat(dataSet3.component.getData("name")).isEqualTo("Michael");
        Assertions.assertThat(dataSet3.component.getData("email")).isEqualTo("123@gmail.com");
        Assertions.assertThat(dataSet3.component.getData("custom")).isEqualTo("CUSTOM");
        Assertions.assertThat(dataSet3.component.getData("num")).isEqualTo("123");
        System.out.println(dataSet3.toXML());
    }
}
