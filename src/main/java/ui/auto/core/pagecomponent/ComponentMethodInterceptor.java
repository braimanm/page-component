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

package ui.auto.core.pagecomponent;

import java.lang.reflect.Method;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import ui.auto.core.data.ComponentData;

import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ComponentMethodInterceptor implements MethodInterceptor {
	private ElementLocator locator;
	
	public ComponentMethodInterceptor(ElementLocator locator) {
		this.locator=locator;
	}
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args,MethodProxy proxy) throws Throwable {
			String methodName=method.getName();
			//Don't Initialize element if methods belongs to ComponentData interface
			boolean dataMethodFound=false;
			for (Method dataMethod: ComponentData.class.getMethods()){
				if (dataMethod.getName().equals(methodName)){
					dataMethodFound=true;
					break;
				}
			}
			
			if (!dataMethodFound && !"finalize".equals(methodName)){		
				//System.out.println("Method: " + method.getName() + " invoked!");
				WebElement elm=locator.findElement();
				PageComponent webc=(PageComponent)obj;
				if (webc.coreElement==null || !webc.coreElement.equals(elm)){
					webc.setCoreElement(elm);
					MethodProxy.find(obj.getClass(),new Signature("init","()V")).invokeSuper(obj,null);	
				}
				
			}
			
			Object retValue=proxy.invokeSuper(obj, args);
			return retValue;	
	}
	
}


