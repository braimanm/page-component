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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordGenerator extends File2ListReader {
	List<String> words;
	
	public WordGenerator() {
		words=populate("/words_en");
	}

	private String getWord(int lettersCount,boolean fromStart,boolean upperCase){
		int count=lettersCount;
		int index=(int) (Math.random()*words.size());
		String word=words.get(index).toLowerCase();
		if (upperCase) word=word.toUpperCase();
		if (lettersCount==-1) return word;
		if (lettersCount==0) count=(int) (Math.random()*10) + 1;
		if (count>word.length()) return word;
		if (fromStart){
			String ret=word.substring(0,count);
			return ret;
		} else {
			String ret=word.substring(word.length()-count);
			return ret;
		}		
	}
	
//	<A>-<Z> or <a>-<z>:  Replaced by random English word, if tag contains upper-case letter word will be in upper-case and if tag contains lower-case letter word will be in lower-case
//	[A]-[Z] or [ a]-[z]:  Replaced by random English word, with random letter casing
//
//	<A:0-9>-<Z:0-9> or <a:0-9>-<z:0-9>: Replaced by number (1-9) of upper-case or lower-case characters from the start of random English word, if number is 0 then replaced word will have random number between 1 to 9 of characters .  
//	[A:0-9]-[Z:0-9] - Replaced by number (1-9) of random letter casing characters from the end of random English word, if number is 0 then replaced word will have random number between 1 to 9 of characters   
//
//	Additional rules:
//	Same tag in pattern will be replaced by the same word 
//	Ex: <A>,<A> - 'SLEEP,SLEEP', <c:3><c:3> - 'tortor', [B:2][B:2] - 'POPO'
//	[A] and <A> are not replaced by the same word

	public String generate(String pattern){
		Pattern patt=Pattern.compile("[<\\[][a-zA-Z](:\\d)*[>\\]]");
		Matcher matcher=patt.matcher(pattern);
		String out=pattern;
		while (matcher.find()){
			boolean upperCase=false;
			int letterCount=-1;
			char brkt=matcher.group().charAt(0);
			char c=matcher.group().charAt(1);
			if (brkt==60 && c>=65 && c<=90) upperCase=true;
			if (brkt==91) upperCase=Math.round(Math.random())>0;
			if (matcher.end()-matcher.start()>3){
				letterCount=matcher.group().charAt(3)-48;
			}
			out=out.replace(matcher.group(),getWord(letterCount,true,upperCase));
		}
		return out;
	}
	
	
	
//	@Test
//	public void test(){
//		WordGenerator gen=new WordGenerator();
//		System.out.println(gen.generate("<A> [A] <A>"));
//		System.out.println(gen.generate("<a:4><a:4>@yahoo.com"));
//		System.out.println(gen.generate("<a>@<B>.com"));
//		System.out.println(gen.generate("<A:1><a:0>"));
//		System.out.println(gen.generate("<A:1>[a:0] <B:1>[b:0]"));
//		System.out.println(gen.generate("Hello <a> how your <b> is dooing?"));
//	
//	}	
	    
}
