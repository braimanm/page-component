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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AlphaNumericGenerator {
	
	private String getLetter(boolean capital){
		int offset=97;
		if (capital) offset=65;
		int i=(int) (Math.random()*26);
		return String.valueOf((char) (offset+i));
	}
	
	private String getDigit(){
		int d=(int) (Math.random()*10);
		return String.valueOf(d);
	}
	
	/* Pattern:
	 * (a)..(z) - lower-case letter 
	 * (A)..(Z) - upper-case letter
	 * [a]..[Z] - digit between 0-9
	 * <a>..<z> - lower-case letter or number
	 * <A>..<Z> - upper-case letter or number
	 * {a}..{Z} - any-case letter or number
	 * !!!! Same symbol represents same letter Ex: '(a)(b){a}[z][Z][z]' - 'drd101' or 'xwx828'
	 * 
	 */
	
	public String generate(String pattern){
		String out=pattern;
		Pattern patt=Pattern.compile("[<\\[\\(\\{][a-zA-Z][>\\]\\)\\}]");
		Matcher matcher=patt.matcher(pattern);

		while (matcher.find()){
			char bracket=matcher.group().charAt(0);
			char c=matcher.group().charAt(1);
			boolean upperCase=false;
			String value=null;
			upperCase=(c>=65 && c<=90);
			if (bracket==40){
				value=getLetter(upperCase);
			} else if (bracket==91){
				value=getDigit();
			} else if (bracket==60){
				int rnd=(int) (Math.random()*2);
				if (rnd==0) {
					value=getLetter(upperCase);
				}else if (rnd==1){
					value=getDigit();
				}
			} else if (bracket==123){
				int rnd=(int) (Math.random()*3);
				if (rnd==0) {
					value=getLetter(true);
				} else if (rnd==1){
					value=getDigit();
				} else {
					value=getLetter(false);
				}
			}
			out=out.replace(matcher.group(),value);
		}
		return out;
		
	}
	
//	@Test
//	public void test(){
//		AlphaNumericGenerator alpha=new AlphaNumericGenerator();
//		System.out.println(alpha.generate("(A)[a](B) [a](C)[b]"));
//		System.out.println(alpha.generate("[a][b][c]-[d][e][f][g][h][i][j][k][l][m][n][o][p]-[a][b][c]"));
//		System.out.println(alpha.generate("(A)(B)(C)(D)(E)"));
//		System.out.println(alpha.generate("{A}{B}{C}{D}{E}"));
//		System.out.println(alpha.generate("<A><B><C><D><0><1><2>"));
//	}
	
}
