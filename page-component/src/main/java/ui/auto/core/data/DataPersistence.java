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

package ui.auto.core.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.generators.DataSetGenerator;
import ui.auto.core.pagecomponent.PageObject;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


public class DataPersistence {
	@XStreamAlias("xmlns")
	@XStreamAsAttribute
	protected String xmlns;
	
	@XStreamAlias("xmlns:xsi")
	@XStreamAsAttribute
	protected String xsi;
	
	@XStreamAlias("xsi:schemaLocation")
	@XStreamAsAttribute
	protected String schemaLocation;
	
	protected DataAliases aliases;
	
	public String getDataAlias(String key){
		if (aliases!=null){
			return aliases.get(key).toString();
		} 
		return null;
	}
	
	public void setDataAlais(String key,String value){
		if (aliases==null){
			aliases=PageComponentContext.getGlobalAliases();
		}
		aliases.put(key, value);
	}
	
	private static XStream getXStream(){
		XStream xstream=new XStream();
		xstream.registerConverter(new PageComponentDataConverter());
		xstream.registerConverter(new DataAliasesConverter());
		return xstream;
	}
	
	protected static XStream initXstream(Class<?> clz){
		XStream xstream=getXStream();
		xstream.processAnnotations(clz);
		return xstream;
	}
	
	public String toXML(){
		String header="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
		XStream xstream=initXstream(this.getClass());
		
		// If this class extends PageObject and it initialised with context 
		// then all the WebComponent fields of this class are CGLIB proxies and by default xml node
		// is market with attribute class. This will remove class attribute from the xml node 
		if ( PageObject.class.isAssignableFrom(this.getClass()) && 
				((PageObject)this).getContext()!=null){
			xstream.aliasSystemAttribute(null,"class");
		}
		String xml=xstream.toXML(this);
		return header + xml;
	}
	
	public void toFile(String filePath){
		FileOutputStream fos=null;
		try {
			fos=new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File " + filePath + " was not found",e);
		}
		try {
			if (fos!=null) fos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n".getBytes());
			initXstream(this.getClass()).toXML(this, fos);
			if (fos!=null) fos.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private static void addAliases(DataPersistence data){
		if (data.aliases!=null){
			for (String key:data.aliases.keySet()){
				PageComponentContext.getGlobalAliases().put(key, data.aliases.get(key));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T fromXml(String xml,Class<T> forClass){	
		DataPersistence data=(DataPersistence) initXstream(forClass).fromXML(xml);
		addAliases(data);
		return (T) data;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T fromFile(String filePath,Class<T> forClass){
		FileInputStream fis=null;
		try {
			fis=new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File " + filePath + " was not found",e);
		}
		DataPersistence data=(DataPersistence) initXstream(forClass).fromXML(fis);
		addAliases(data);
		if (fis!=null){
			try {
				fis.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return (T) data;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T deepCopy(T object){
		XStream xstream=getXStream();
		return (T) xstream.fromXML(xstream.toXML(object));
	}
	
	public static void deepCopy(Object source,Object target){
		XStream xstream=getXStream();
		String xml=xstream.toXML(source);
		xstream.fromXML(xml,target);
	}
	
	public static <T> T instantiateClass(Class<T> forClass){
		if (forClass.isMemberClass()) {
			return fromXml("<" + forClass.getName().replace("$","_-") + "/>", forClass);
		} else {
			return fromXml("<" + forClass.getName() + "/>", forClass);
		}
	}

	public void setAliases(DataAliases aliases) {
		this.aliases=aliases;
	}
	
	public String generateXML() throws Exception{
		DataSetGenerator.getInstance().generateData(this,true);
		return toXML();
	}
	
	public String generateData(){
		return DataSetGenerator.getInstance().generateDataSet(this);
	}

	
}
