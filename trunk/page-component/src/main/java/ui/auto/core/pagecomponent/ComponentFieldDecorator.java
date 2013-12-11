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


import java.lang.reflect.Field;

import net.sf.cglib.proxy.Enhancer;

import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import ui.auto.core.data.ComponentData;

public class ComponentFieldDecorator extends DefaultFieldDecorator {
	PageObject page;
	
	public ComponentFieldDecorator(ElementLocatorFactory factory, PageObject page) {
		super(factory);
		this.page=page;
	}

	@Override
	public Object decorate(ClassLoader loader, final Field field) {
		String dataValue=null;
		String initialValue=null;
		String expectedValue=null;
		
		if (PageComponent.class.isAssignableFrom(field.getType())){
			ElementLocator locator = factory.createLocator(field);
			if (locator == null) return null;
			
			ComponentData componentData = null;
			try {
				field.setAccessible(true);
				componentData = (ComponentData) field.get(page);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
            if (componentData!=null) {
				dataValue=componentData.getData();
				initialValue=componentData.getInitialData();
				expectedValue=componentData.getExpectedData();
            }
          
			Enhancer enhancer=new Enhancer();
			enhancer.setSuperclass(field.getType());
			enhancer.setCallback(new ComponentMethodInterceptor(locator));
			Object componentProxy=enhancer.create();
			((ComponentData) componentProxy).initializeData(dataValue, initialValue, expectedValue);
			return componentProxy;
		}
		
		return super.decorate(loader, field);
	}
	
	
	
	

}
