package ui.auto.core.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class DataAliases  {
	@XStreamOmitField
	Map<String,String> map;
	
	public DataAliases() {
		 map=new HashMap<String,String>();
	}
	
	public String get(String key){
		return map.get(key);
	}
	
	public void put(String key,String value){
		map.put(key, value);
	}

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	public Set<String> getKeys() {
		return map.keySet();
	}
	
}
