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

public class AddressGenerator extends File2ListReader{
	List<String> streets;
	List<String> cities;
	
	
	public static class Address{
		public String country;
		public String streetNumber;
		public String streetName;
		public String city;
		public String provinceName;
		public String provinceCode;
		public String postalCode;
		
		@Override
		public String toString(){
			String str="Country: " + country + "\n";
			str+="Street #: " + streetNumber +"\n";
			str+="Street Name: " + streetName +"\n";
			str+="City: " + city +"\n";
			str+="Province Name: " + provinceName + "\n";
			str+="Province Code: " + provinceCode + "\n";
			str+="Postal Code: " +postalCode;
			return str;
		}
		
		/* Pattern:
		 * <C> - country
		 * <#> - street number
		 * <S> - street name
		 * <T> - city
		 * <P> - province name
		 * <K> - province code
		 * <O> - postal code
		 */
		public String toString(String pattern){
			String out=pattern;
			out=out.replace("<C>", country);
			out=out.replace("<#>", streetNumber);
			out=out.replace("<S>", streetName);
			out=out.replace("<T>", city);
			out=out.replace("<P>", provinceName);
			out=out.replace("<K>", provinceCode);
			out=out.replace("<O>", postalCode);
			return out;
		}
	}
	
	public AddressGenerator() {
		streets=populate("/streets");
		cities=populate("/canada_cities");
	}
	
	public Address generateAddress(){
		return generateAddress(false);
	}
	
	public Address generateAddress(boolean generatePostalCodes){
		Address address=new Address();
		address.country="CANADA";
		int m=(int) Math.pow(10,(int)(Math.random()*5+1));
		address.streetNumber=(String.valueOf((int)(Math.random() * m)+1));
		address.streetName=streets.get((int) (Math.random()* streets.size()));
		String[] city=cities.get((int) (Math.random()* cities.size())).split("\\t");
		address.city=city[0];
		address.provinceName=city[1];
		address.provinceCode=city[2];
		address.postalCode=city[3];
		if (generatePostalCodes){
			address.postalCode=new AlphaNumericGenerator().generate("(A)[a](B) [b](C)[c]");
		} 
		return address;
	}
	
//	@Test
//	public static void test(){
//		AddressGenerator addrGen=new AddressGenerator();
//		System.out.println(addrGen.generateAddress() + "\n");
//		System.out.println(addrGen.generateAddress(true) + "\n");
//		Address address=addrGen.generateAddress();
//		System.out.println(address.toString("<#> <S>, <T>, <O>, <K> (<P>)"));
//	}


}


