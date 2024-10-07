package com.braimanm.ui.auto;

import com.braimanm.datainstiller.data.Data;
import com.braimanm.ui.auto.components.WebComponent;
import com.braimanm.ui.auto.pagecomponent.PageObject;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.List;

@SuppressWarnings({"NewClassNamingConvention", "unused"})
@XStreamAlias("dataset1")
public class DataSet1 extends PageObject {
    WebComponent comp1;
    @Data()
    WebComponent comp2;
    @Data(alias = "alias3", value = "value3")
    WebComponent comp3;
    @Data(alias = "alias-l1", value = "list of web components")
    List<WebComponent> list1;
    @Data(alias = "alias-l1")
    List<WebComponent> list2;
    DataSet2 dataSet2;
    @Data(alias = "alias-int", value = "33")
    Short integer;

}
