package ui.auto.core.data.generators;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public interface FieldAction {
	Object instantiate(Type type, Field field, String dataValue) throws Exception;
}
