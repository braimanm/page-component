package com.braimanm.ui.auto.pagefactory;

import com.braimanm.ui.auto.context.PageComponentContext;
import com.braimanm.ui.auto.pagecomponent.LocatorPattern;
import com.braimanm.ui.auto.pagecomponent.LocatorStrategy;
import com.braimanm.ui.auto.pagecomponent.PageComponent;
import io.github.classgraph.ClassInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class ComponentAnnotations extends Annotations {
    private final By parentBy;

    public ComponentAnnotations(Field field, By parentBy) {
        super(field);
        this.parentBy = parentBy;
    }

    private String getFullClassName(String name) {
        Optional<ClassInfo> info = PageComponentContext.getClassGraph().getClassesImplementing(LocatorStrategy.class)
                .stream().filter(classInfo -> classInfo.getSimpleName().equalsIgnoreCase(name)).findFirst();
        return info.isPresent() ? info.get().getName() : name;
    }

    private By buildFromPattern(Field field) {
        if (field.isAnnotationPresent(FindBy.class)) {
            FindBy fb = field.getAnnotation(FindBy.class);
            if (fb.how().equals(How.UNSET) && !fb.using().isEmpty()) {

                //Handling LocatorStrategy
                if (!fb.className().isEmpty()) {
                    try {
                        Class<?> cls = Class.forName(getFullClassName(fb.className()));
                        if (LocatorStrategy.class.isAssignableFrom(cls)) {
                            LocatorStrategy locStrategy = (LocatorStrategy) cls.getDeclaredConstructor().newInstance();
                            return locStrategy.getStrategy(fb.using());
                        } else {
                            throw new RuntimeException("By className parameter is not of LocatorStrategy type!");
                        }
                    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                             IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }

                //Handling component based LocatorPattern annotation
                Class<?> type = field.getType();
                do {
                    if (type.isAnnotationPresent(LocatorPattern.class)) {
                        LocatorPattern lp = type.getAnnotation(LocatorPattern.class);
                        if (lp.how().equals(How.XPATH) || lp.how().equals(How.CSS)) {
                            String loc = lp.pattern().replace("${val}", fb.using());
                            return lp.how().equals(How.XPATH) ? By.xpath(loc) : By.cssSelector(loc);
                        }
                    } else {
                        type = type.getSuperclass();
                    }
                } while (!type.equals(PageComponent.class));
            }
        }
        return null;
    }

    private By chainIfParentExists(By childLocator) {
        By bys;
        if (parentBy != null) {
            bys = new ByChained(parentBy, childLocator);
        } else {
            bys = childLocator;
        }
        return bys;
    }

    @Override
    public By buildBy() {
        By ans;
        By patternBy = buildFromPattern(getField());
        if (patternBy != null) {
            ans = patternBy;
        } else {
            ans = super.buildBy();
        }
        return chainIfParentExists(ans);
    }

    @Override
    protected By buildByFromDefault() {
        return super.buildByFromDefault();
    }

    @Override
    protected void assertValidAnnotations() {
        super.assertValidAnnotations();
    }

}
