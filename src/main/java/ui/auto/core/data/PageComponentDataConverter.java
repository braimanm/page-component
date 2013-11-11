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

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


public class PageComponentDataConverter implements Converter{

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return ComponentData.class.isAssignableFrom(type);
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		ComponentData dataValue=(ComponentData) value;
		if (dataValue.getInitialData()!=null) writer.addAttribute("initial", dataValue.getInitialData());
		if (dataValue.getData()!=null) writer.setValue(dataValue.getData());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object unmarshal(HierarchicalStreamReader reader ,UnmarshallingContext context) {
		String initial=reader.getAttribute("initial");
		String value=reader.getValue();
		Object type = null;
		try {
			type=context.getRequiredType().getConstructor(String.class,String.class).newInstance(value,initial);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return type;
	}

}
