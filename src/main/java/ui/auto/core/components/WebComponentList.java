package ui.auto.core.components;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WebComponentList extends PageObject {
    @XStreamImplicit(itemFieldName = "item")
    private List<WebComponent> items;

    @Override
    public <T extends PageComponentContext> void initPage(T context) {
        super.initPage(context);
        List<WebElement> elements = getDriver().findElements(getLocator());
        List<WebComponent> components = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            WebComponent component = new WebComponent(elements.get(i));
            if (items != null && items.size() > i) {
                String data = items.get(i).getData().trim().replaceAll("\\n\\s", "\n");
                String init = items.get(i).getData(DataTypes.Initial).replaceAll("\\n\\s", "\n");
                String expected = items.get(i).getData(DataTypes.Expected).replaceAll("\\n\\s", "\n");
                component.initializeData(data, init, expected);
            }
            components.add(component);
        }
        items = components;
    }

    public void validateAll() {
        enumerate(el -> Assertions.assertThat(el.getText()).isEqualTo((el.getData())));
    }

    public void enumerate(Consumer<WebComponent> consumer) {
        items.forEach(consumer);
    }

}
