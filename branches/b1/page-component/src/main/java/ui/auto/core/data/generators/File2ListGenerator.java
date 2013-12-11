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

package ui.auto.core.data.generators;

import java.util.List;

public class File2ListGenerator extends File2ListReader{
	private List<String> list;
	
	public File2ListGenerator(String fileName){
		list=populate("/" + fileName);
	}
	
	public String getValue(){
		if (list.size()==0) 
			throw new RuntimeException("The file is empty!");
		int index=(int) (Math.random()*list.size());
		return list.get(index);
	}
	
}
