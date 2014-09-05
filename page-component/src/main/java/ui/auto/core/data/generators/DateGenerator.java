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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateGenerator implements GeneratorInterface {
	DateFormat dateFormater;
	long dateFrom;
	long dateTo;
	
	DateGenerator(){
	}
	
	public DateGenerator(String from,String to,String pattern) throws ParseException {
		dateFormater=new SimpleDateFormat(pattern);
		dateFormater.setTimeZone(TimeZone.getTimeZone("GMT"));
		dateFrom=dateFormater.parse(from).getTime();
		dateTo=dateFormater.parse(to).getTime()+24*60*60*1000+1;
	}
	
	private long getRandomDate(){
		return dateFrom + (long)(Math.random()*(dateTo-dateFrom));
	}
	
	public String getDate(){
		return dateFormater.format(getRandomDate());
	}
	
	public String getDate(String format){
		DateFormat formater=new SimpleDateFormat(format);
		formater.setTimeZone(TimeZone.getTimeZone("GMT"));
		return formater.format(getRandomDate());
	}

	@Override
	public String generate(String pattern, String value) {
		String[] args=pattern.split("\\|");
		try {
			DateGenerator dg=new DateGenerator(args[0],args[1],args[2]);
			if (value==null || value.isEmpty()){
				return dg.getDate();
			} else {
				return dg.getDate(value);
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	
//	@Test
//	public static void test() throws ParseException{
//		DateGenerator generator=new DateGenerator("1920/01/01","2000/12/31","yyyy/MM/dd");
//		System.out.println(generator.getDate());
//		System.out.println(generator.getDate("dd MMM yyyy"));
//		System.out.println(generator.getDate("MMMM dd yyyy hh:mm:ss"));
//		System.out.println(generator.generate("2010/01/01|2013/12/31|yyyy/MM/dd","dd MMM yyyy"));
//	}
}
