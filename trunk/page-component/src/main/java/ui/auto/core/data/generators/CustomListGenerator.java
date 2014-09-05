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


import java.util.Arrays;
import java.util.List;

public class CustomListGenerator implements GeneratorInterface{
	private List<String> list;

	CustomListGenerator(){	
	}
	
	public CustomListGenerator(List<String> list) {
		this.list=list;
	}
	
	public CustomListGenerator(String... values){
		list=Arrays.asList(values);
	}
	
	public String getValue(){
		int index=(int) (Math.random()*list.size());
		return list.get(index);
	}
	
	@Override
	public String generate(String pattern, String value) {
		list= Arrays.asList(value.split(","));
		return getValue();
	}
	
//	@Test
//	public static void test(){
//		CustomListGenerator listGenerator=new CustomListGenerator("One","Two","Three");
//		System.out.println(listGenerator.getValue());
//		
//		List<String> l=new ArrayList<String>();
//		l.add("1");
//		l.add("2");
//		l.add("3");
//		CustomListGenerator lg=new CustomListGenerator(l);
//		System.out.println(lg.getValue());
//		
//		String[] ar=new String[3];
//		ar[0]="A";
//		ar[1]="B";
//		ar[2]="C";
//		
//		CustomListGenerator arl=new CustomListGenerator(ar);
//		System.out.println(arl.getValue());
//		
//		System.out.println(arl.generate(null, "aaa,bbb,ccc,ddd"));
//				
//	}

	
	
}
