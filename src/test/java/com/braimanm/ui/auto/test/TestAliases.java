package com.braimanm.ui.auto.test;

import com.braimanm.datainstiller.context.DataContext;
import com.braimanm.ui.auto.data.DataTypes;
import com.braimanm.ui.auto.pagecomponent.PageComponent;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class TestAliases {

    private void assertCollection(Object col) {
        if (col.getClass().isArray()) {
            for (Object o : (Object[]) col) {
                assertElement(o, "${alias-l1}", "list of web components");
            }
        }

        if (col instanceof List) {
            for (Object o : (List<?>) col) {
                assertElement(o, "${alias-l1}", "list of web components");
            }
        }
    }

    private void assertElement(Object el, String expectedAlias, String expectedValue) {
        if (el instanceof PageComponent) {
            PageComponent comp = (PageComponent) el;
            Assert.assertEquals(comp.getData(DataTypes.Data, false), expectedAlias);
            Assert.assertEquals(comp.getData(DataTypes.Data, true), expectedValue);
            Assert.assertEquals(comp.getData(), expectedValue);
        } else {
            if (el instanceof String) {
                String comp = (String) el;
                String key = comp.replace("${", "").replace("}", "");
                Object value = DataContext.getGlobalAliases().get(key);
                Assert.assertEquals(comp, expectedAlias);
                Assert.assertEquals(value, expectedValue);
            }
        }
    }

    private void assertDataSet(DataSet1 ds) {
        assertElement(ds.comp1, "comp1", "comp1");
        assertElement(ds.comp2, "", "");
        assertElement(ds.comp3, "${alias3}", "value3");
        assertCollection(ds.list1);
        assertCollection(ds.list2);
        assertElement(ds.dataSet2.comp4, "${alias1}", "");
        assertElement(ds.dataSet2.comp5, "component5", "component5");
        assertElement(ds.dataSet2.list3, "${alias-l1}", "list of web components");
        assertElement(ds.dataSet2.array1, "${array-alias}", "array-value");
        assertElement(ds.dataSet2.string, "${alias3}", "value3");
    }

    @Test
    public void testAliasesFromXML() {
        DataContext.getGlobalAliases().clear();
        DataSet1 ds = new DataSet1().fromResource("ExpectedDataSet.xml");
        String xml = ds.toXML();
        ds = new DataSet1().fromXml(xml);
        assertDataSet(ds);
    }

    @Test
    public void testAliasesFromFile() {
        DataContext.getGlobalAliases().clear();
        String filePath = Objects.requireNonNull(TestAliases.class.getResource("/ExpectedDataSet.xml")).getPath();
        DataSet1 ds = new DataSet1().fromFile(filePath);
        assertDataSet(ds);
    }

    @Test
    public void testAliasesFromResource() {
        DataContext.getGlobalAliases().clear();
        DataSet1 ds = new DataSet1().fromResource("ExpectedDataSet.xml");
        assertDataSet(ds);
    }

    @Test
    public void testAliasesFromInputStream() {
        DataContext.getGlobalAliases().clear();
        InputStream inputStream = TestAliases.class.getResourceAsStream("/ExpectedDataSet.xml");
        DataSet1 ds = new DataSet1().fromInputStream(inputStream);
        assertDataSet(ds);
    }

}
