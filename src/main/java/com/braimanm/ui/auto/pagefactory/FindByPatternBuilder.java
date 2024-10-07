package com.braimanm.ui.auto.pagefactory;

import org.openqa.selenium.By;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class FindByPatternBuilder {

    static By buildFromPattern(Field field) {
        if (field.isAnnotationPresent(FindByTemplate.class)) {
            FindByTemplate fbt = field.getAnnotation(FindByTemplate.class);
            try {
                LocatorTemplate instance;
                if (fbt.template() != void.class) {
                    instance = (LocatorTemplate) fbt.template().getDeclaredConstructor().newInstance();
                } else {
                    if (LocatorTemplate.class.isAssignableFrom(field.getType())) {
                        instance = (LocatorTemplate) field.getType().getDeclaredConstructor().newInstance();
                    } else {
                        return null;
                    }
                }
                return instance.getBy(fbt.value());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

}
