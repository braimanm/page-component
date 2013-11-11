package ui.auto.core.data;

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
				writer.setValue(aliases.get(key));
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
			aliases.put(nodeName, value);
			reader.moveUp();
		}
		
		return aliases;
	}
	
}
