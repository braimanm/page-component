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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ui.auto.core.data.ComponentData;
import ui.auto.core.data.DataPersistence;



public enum FieldTypes {
	BYTE(new FieldAction(){
		
		@Override
		public Object instantiate(Type type,Field field, String value) throws Exception {
			byte val=0;
			try {
				val=Byte.parseByte(value);
			} catch (Exception e) {
				System.err.println("[WARNING] Field " + field.getName() + " was set to " + val + " Add @Data annotation for proper data generation");
			}
			return val;
		}

		
	}),
	SHORT(new FieldAction(){

		@Override
		public Object instantiate(Type type,Field field, String value) throws Exception {
			Short val=0;
			try {
				val=Short.parseShort(value);
			} catch (Exception e) {
				System.err.println("[WARNING] Field " + field.getName() + " was set to " + val + " Add @Data annotation for proper data generation");
			}
			return val;
		}

		
	}),
	INTEGER(new FieldAction(){

		@Override
		public Object instantiate(Type type,Field field, String value) throws Exception {
			Integer val=0;
			try {
				val=Integer.parseInt(value);
			} catch (Exception e) {
				System.err.println("[WARNING] Field " + field.getName() + " was set to " + val + " Add @Data annotation for proper data generation");
			}
			return val;
		}

		
	}),
	LONG(new FieldAction(){

		@Override
		public Object instantiate(Type type,Field field, String value) throws Exception {
			Long val=0L;
			try {
				val=Long.parseLong(value);
			} catch (Exception e) {
				System.err.println("[WARNING] Field " + field.getName() + " was set to " + val + " Add @Data annotation for proper data generation");
			}
			return val;
		}

		
	}),
	FLOAT(new FieldAction(){

		@Override
		public Object instantiate(Type type,Field field, String value) throws Exception {
			Float val=0F;
			try {
				val=Float.parseFloat(value);
			} catch (Exception e) {
				System.err.println("[WARNING] Field " + field.getName() + " was set to " + val + " Add @Data annotation for proper data generation");
			}
			return val;
		}

		
	}),
	DOUBLE(new FieldAction(){

		@Override
		public Object instantiate(Type type,Field field, String value) throws Exception {
			Double val=0D;
			try {
				val=Double.parseDouble(value);
			} catch (Exception e) {
				System.err.println("[WARNING] Field " + field.getName() + " was set to " + val + " Add @Data annotation for proper data generation");
			}
			return val;
		}

		
	}),
	BOOLEAN(new FieldAction(){

		@Override
		public Object instantiate(Type type,Field field, String value) throws Exception {
			return value.trim().toLowerCase().equals("true");
		}

		
	}),
	CHARACTER(new FieldAction(){

		@Override
		public Object instantiate(Type type,Field field, String value) throws Exception {
			return value.charAt(0);
		}

			}),
	STRING(new FieldAction(){

		@Override
		public Object instantiate(Type type,Field field, String value) throws Exception {
			return value;
		}

		
	}),
	ARRAY(new FieldAction(){

		@Override
		public Object instantiate(Type type,Field field, String value) throws Exception {
			Object obj=null;
			int n=3;
			if (field.isAnnotationPresent(Data.class)) 
				n=field.getAnnotation(Data.class).n();
			Class<?> arrayType;
			if (type==null){
				arrayType=field.getType().getComponentType();
			} else {
				arrayType=((Class<?>)type).getComponentType();
			}
			obj=Array.newInstance(arrayType, n);
			for (int i=0;i<n;i++){
				String dataValue=gen.generate(field);
				Object item=gen.findBaseClass(arrayType).instantiate(arrayType,field, dataValue);
				Array.set(obj, i, item);
			}
			return obj;
		}

		
	}),
	LIST(new FieldAction(){

		@Override
		public Object instantiate(Type type,Field field, String value) throws Exception {
			int n=3;
			if (field.isAnnotationPresent(Data.class)) 
				n=field.getAnnotation(Data.class).n();
			Type genericType=null;
			if (type!=null){
				genericType=type;
			} else {
				genericType=field.getGenericType();
				
			}
			if (genericType instanceof ParameterizedType){
				genericType=((ParameterizedType)genericType).getActualTypeArguments()[0];
				
			} else {
				//If no generic type is present use String
				genericType=String.class;
			}
			List<Object> list=new ArrayList<Object>();
			for (int i=0;i<n;i++){
				String dataValue=gen.generate(field);
				FieldTypes fieldType=null;
				if (genericType instanceof ParameterizedType){
					fieldType=gen.findBaseClass((Class<?>) ((ParameterizedType) genericType).getRawType());
				} else {
					fieldType=gen.findBaseClass((Class<?>) genericType);
				}
				Object obj=fieldType.instantiate(genericType,field, dataValue);
				if (obj!=null)
					list.add(obj);
			}
			return list;		
		}

		
	}),
	PAGECOMPONENT(new FieldAction(){

		@Override
		public Object instantiate(Type type,Field field, String value) throws Exception{
			Object object=null;
			Class<?> instance=field.getType();
			if (type!=null) 
				instance=(Class<?>) type;
			object=instance.newInstance();
			((ComponentData) object).initializeData(value,null,null);
			return object;
		}

		
	}),
	UNKNOWN_OBJECT(new FieldAction(){
		@Override
		public Object instantiate(Type type,Field field, String value) throws Exception {
			
			Object obj=null;
			if(type!=null){
				if (field.getDeclaringClass().equals(type)) 
					gen.containerLevelCounter++;
				if (gen.containerLevelCounter<2)
					obj=DataPersistence.instantiateClass((Class<?>)type);
			} else {
				obj=DataPersistence.instantiateClass(field.getType());
			}
			gen.generateData(obj,false);	
			return obj;
		}

		
	}),
	IGNORED(new FieldAction(){

		@Override
		public Object instantiate(Type type,Field field, String value) throws Exception {
			return null;
		}

		
	});
	
	private FieldAction action;
	private static DataSetGenerator gen=DataSetGenerator.getInstance(); 
	
	private FieldTypes(FieldAction action){
		this.action=action;
	}
	
	public Object instantiate(Type type, Field field, String dataValue){
		try {
			return action.instantiate(type,field, dataValue);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
