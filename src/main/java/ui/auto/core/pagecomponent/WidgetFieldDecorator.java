package ui.auto.core.pagecomponent;

import io.appium.java_client.HasSessionDetails;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.DefaultElementByBuilder;
import net.sf.cglib.proxy.Enhancer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.ComponentData;
import ui.auto.core.data.DataTypes;

import java.lang.reflect.Field;

import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.unpackWebDriverFromSearchContext;
import static java.util.Optional.ofNullable;

public class WidgetFieldDecorator extends AppiumFieldDecorator {
    private PageObject page;
    private PageComponentContext context;

    public WidgetFieldDecorator(PageComponentContext context, PageObject page) {
        super(context.getDriver());
        this.context = context;
        this.page = page;
    }

    @Override
    public Object decorate(ClassLoader ignored, Field field) {
        String dataValue = null;
        String initialValue = null;
        String expectedValue = null;

        if (PageComponent.class.isAssignableFrom(field.getType())) {
            String platform = null;
            String automation = null;
            WebDriver webDriver = unpackWebDriverFromSearchContext(this.context.getDriver());
            HasSessionDetails hasSessionDetails = ofNullable(webDriver).map(driver -> {
                if (!HasSessionDetails.class.isAssignableFrom(driver.getClass())) {
                    return null;
                }
                return (HasSessionDetails) driver;
            }).orElse(null);

            if (hasSessionDetails != null) {
                platform = hasSessionDetails.getPlatformName();
                automation = hasSessionDetails.getAutomationName();
            }
            DefaultElementByBuilder builder = new DefaultElementByBuilder(platform, automation);
            builder.setAnnotated(field);
            By by = builder.buildBy();
            if (by == null) return null;

            ComponentData componentData;
            try {
                field.setAccessible(true);
                componentData = (ComponentData) field.get(page);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (componentData != null) {
                dataValue = componentData.getData(DataTypes.Data, false);
                initialValue = componentData.getData(DataTypes.Initial, false);
                expectedValue = componentData.getData(DataTypes.Expected, false);
            }

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(field.getType());
            enhancer.setCallback(new WidgetMethodInterceptor(by, context));
            Object componentProxy = enhancer.create();
            ((ComponentData) componentProxy).initializeData(dataValue, initialValue, expectedValue);
            ((PageComponent) componentProxy).selector = by;
            return componentProxy;
        }

        return super.decorate(ignored, field);
    }
}
