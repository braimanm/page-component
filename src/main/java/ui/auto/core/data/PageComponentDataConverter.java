/*
Copyright 2010-2012 Michael Braiman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package ui.auto.core.data;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import datainstiller.data.DataValueConverter;

import java.lang.reflect.Modifier;

/**
 * @author Michael Braiman braimanm@gmail.com
 *         This is{@link XStream} Converter implementation for marshaling and unmarshaling {@link ComponentData}.
 */
public class PageComponentDataConverter implements DataValueConverter {

    @SuppressWarnings({"rawtypes"})
    @Override
    public boolean canConvert(Class type) {
        return (ComponentData.class.isAssignableFrom(type) && !Modifier.isAbstract(type.getModifiers()));
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        ComponentData dataValue = (ComponentData) value;
        String initial = dataValue.getData(DataTypes.Initial, false);
        String expected = dataValue.getData(DataTypes.Expected, false);
        String data = dataValue.getData(DataTypes.Data, false);
        if (initial != null) writer.addAttribute("initial", initial);
        if (expected != null) writer.addAttribute("expected", expected);
        if (data != null) writer.setValue(data);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String initial = reader.getAttribute("initial");
        String expected = reader.getAttribute("expected");
        String value = reader.getValue();
        Object type = null;
        try {
            type = context.getRequiredType().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        ((ComponentData) type).initializeData(value, initial, expected);
        return type;
    }

    @Override
    public <T> T fromString(String str, Class<T> cls) {
        T obj = null;
        try {
            obj = cls.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Component " + cls.getCanonicalName() +
                    " must have default constructor !", e.getCause());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        ((ComponentData) obj).initializeData(str, null, null);
        return obj;
    }


}
