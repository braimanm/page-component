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

package ui.auto.core.utils;

public class InvocationInfo {
	private final String className;
	private final String methodName;
	private final int lineNumber;
	private final String fileName;
	static private final String myClassName="ui.auto.core.utils.InvocationInfo";
	
	private InvocationInfo(StackTraceElement stackElement){
		className=stackElement.getClassName();
		methodName=stackElement.getMethodName();
		lineNumber=stackElement.getLineNumber();
		fileName=stackElement.getFileName();
	}
	
	public static InvocationInfo whoAmI(){
		StackTraceElement stackElement=getTraceElement("whoAmI",1);
		if (stackElement!=null){
			return new InvocationInfo(stackElement);
		}
		return null;
	}
	
	public static InvocationInfo whoInvokedMe(){
		StackTraceElement stackElement=getTraceElement("whoInvokedMe",2);
		if (stackElement!=null){
			return new InvocationInfo(stackElement);
		}
		return null;
	}
	
	private static StackTraceElement getTraceElement(String myMethod,int offset){
		StackTraceElement[] els=Thread.currentThread().getStackTrace();
		for (int i=0;i<els.length;i++){
			String classMethod = els[i].getClassName() + "." + els[i].getMethodName();
			if (classMethod.equals(myClassName + "." + myMethod)){
				if (els.length<=i+offset) break;
				return els[i+offset];
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return className + "." + methodName + " (" + fileName + ":" + lineNumber +")";
	}
	
}

