package ui.auto.core.test;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import datainstiller.data.Data;
import org.testng.annotations.Test;
import ui.auto.core.components.WebComponent;
import ui.auto.core.pagecomponent.PageObject;

import java.util.List;

@XStreamAlias("dataset1")
class DataSet1 extends PageObject {
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

    @Test
    public void generate() {
        System.out.println(generateXML());
    }

}
