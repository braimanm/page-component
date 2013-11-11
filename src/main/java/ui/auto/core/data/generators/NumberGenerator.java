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

import java.text.DecimalFormat;


public class NumberGenerator {
	
	long minNum;
	long maxNum;
	String format;
	
	public NumberGenerator(String min,String max,String format){
		minNum=Long.parseLong(min);
		maxNum=Long.parseLong(max);
		this.format=format;
	}
	
	public String getNum(){
		double num=minNum + Math.random() * (maxNum-minNum);
		DecimalFormat dec=new DecimalFormat(format);
		return dec.format(num);
	}
	
//	@Test
//	public static void test(){
//		NumberGenerator gen=new NumberGenerator("0", "1000","$###.##");
//		System.out.println(gen.getNum());
//	}
	
}
