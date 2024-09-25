package com.braimanm.ui.auto.test;

import com.braimanm.datainstiller.data.Data;
import com.braimanm.datainstiller.data.DataPersistence;
import com.braimanm.ui.auto.components.WebComponent;


import java.util.List;

@SuppressWarnings("NewClassNamingConvention")
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