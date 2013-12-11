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
