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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.generators.GeneratorType;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DataAliasesConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return (type.equals(DataAliases.class));
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		if (source!=null){
			DataAliases aliases=(DataAliases)source;
			for (String key:aliases.map.keySet()){
				writer.startNode(key);
				writer.setValue(aliases.get(key).toString());
				writer.endNode();
			}
		}
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,UnmarshallingContext context) {
		DataAliases aliases=new DataAliases();
		String nodeName;
		String value;
		while(reader.hasMoreChildren()){
			reader.moveDown();
			nodeName=reader.getNodeName();
			value=reader.getValue();
			if (value.matches("\\$\\[.+\\]")){
				Pattern pattern=Pattern.compile("\\$\\[(.+)\\(\\s*'\\s*(.*)\\s*'\\s*\\,\\s*'\\s*(.*)\\s*'\\s*\\)");
				Matcher matcher=pattern.matcher(value);
				if (matcher.find()!=true){
					throw new RuntimeException(value + " - invalid dynamic expression!");
				}
				GeneratorType genType=GeneratorType.valueOf(matcher.group(1).trim());
				String init=matcher.group(2);
				//if (init.toLowerCase().trim().equals("null")) init=null;
				String val=matcher.group(3);
				//if (val.toLowerCase().trim().equals("null")) val=null;
				value=genType.generate(init, val);
			}
			aliases.put(nodeName, value);
			PageComponentContext.getGlobalAliases().put(nodeName,value);
			reader.moveUp();
		}
		
		return aliases;
	}
	
}
