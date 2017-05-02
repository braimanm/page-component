package ui.auto.core.test;

import datainstiller.data.Data;
import datainstiller.data.DataPersistence;
import ui.auto.core.components.WebComponent;

import java.util.List;

public class DataSet2 extends DataPersistence {
    @Data(alias = "alias1")
    WebComponent comp4;
    @Data("component5")
    WebComponent comp5;
    @Data(alias = "alias-l1")
    List<WebComponent> list3;
    @Data(alias = "array-alias", value = "array-value", nArray = 10)
    WebComponent[] array1;
    @Data(alias = "alias3")
    String string;
}