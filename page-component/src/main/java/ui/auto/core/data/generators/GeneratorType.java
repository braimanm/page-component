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

public enum GeneratorType implements GeneratorInterface{
	FIXED_VALUE(null),
	ADDRESS(new AddressGenerator()),
	ALPHANUMERIC(new AlphaNumericGenerator()),
	CUSTOM_LIST(new CustomListGenerator()),
	DATE(new DateGenerator()),
	HUMAN_NAMES(new HumanNameGenerator()),
	WORD(new WordGenerator()),
	NUMBER(new NumberGenerator()),
	FILE2LIST(new File2ListGenerator());
	
	private GeneratorInterface gen;
	
	private GeneratorType(GeneratorInterface generator) {
		this.gen=generator;
	}

	@Override
	public String generate(String pattern, String value) {
		if (gen!=null){
			return gen.generate(pattern, value);
		} else {
			return value;
		}
	}
	
	
	
}