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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;

import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.generators.DataSetGenerator;
import ui.auto.core.pagecomponent.PageObject;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * @author Michael Braiman braimanm@gmail.com
 *
 * This class encapsulate object persistence capabilities. It allows to persist any derived from this class object with all his data.
 * All the class members which are not annotated with {@link XStreamOmitField} are serialized and deserialized to and from various formats 
 */
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
	
	/**
	 * Returns alias for the given key
	 * @param key key for the alias 
	 * @return local alias
	 */
	public String getDataAlias(String key){
		if (aliases!=null){
			return aliases.get(key).toString();
		} 
		return null;
	}
	
	/**
	 * Set local and global alias
	 * This method duplicates local alias to the global alias store to be resolved in any other inherited {@link DataPersistence} object
	 * @param key provides the key for the alias to store
	 * @param value value to store for the alias
	 */
	public void setDataAlias(String key,String value){
		if (aliases==null){
			aliases=new DataAliases();
		}
		aliases.put(key, value);
		PageComponentContext.getGlobalAliases().put(key, value);
	}
	
	private static XStream getXStream(){
		XStream xstream=new XStream();
		xstream.registerConverter(new PageComponentDataConverter());
		xstream.registerConverter(new DataAliasesConverter());
		return xstream;
	}
	
	private static XStream initXstream(Class<?> clz){
		XStream xstream=getXStream();
		xstream.processAnnotations(clz);
		return xstream;
	}
	
	/**
	 * This method serializes this object to the given XML string
	 * @return XML representation of this object 
	 */
	public String toXML(){
		String header="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
		XStream xstream=initXstream(this.getClass());
		
		// If this class extends PageObject and it initialized with context 
		// then all the WebComponent fields of this class are CGLIB proxies and by default xml node
		// is marked with attribute class. This will remove class attribute from the xml node 
		if ( PageObject.class.isAssignableFrom(this.getClass()) && 
				((PageObject)this).getContext()!=null){
			xstream.aliasSystemAttribute(null,"class");
		}
		String xml=xstream.toXML(this);
		return header + xml;
	}
	
	/**
	 * This method serializes this object to the given file
	 * @param filePath file path to serialize this object
	 */
	public void toFile(String filePath){
		
		FileOutputStream fos=null;
		Writer writer=null;
		try {
			fos=new FileOutputStream(filePath);
			writer=new OutputStreamWriter(fos, "UTF-8");
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");
			initXstream(this.getClass()).toXML(this, writer);
		} catch ( IOException e) {
			throw new RuntimeException(e);
		} finally{
			
				try {
					if (writer!=null) writer.close();
					if (fos!=null) fos.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			
		}
		
	}
	
	/**
	 * This method deserialize given XML string to the object 	
	 * @param xml XML string which represents deserialized object
	 * @param forClass class to deserialize 
	 * @return deserialized object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromXml(String xml,Class<T> forClass){	
		DataPersistence data=(DataPersistence) initXstream(forClass).fromXML(xml);
		return (T) data;
	}
	
	/**
	 * This method deserialize file represented by URL to the object
	 * @param url URL pointer to the file to be deserialized
	 * @param forClass class to deserialize
	 * @return deserialized object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromURL(URL url,Class<T> forClass){
		DataPersistence data=(DataPersistence) initXstream(forClass).fromXML(url);
		return  (T) data;
	}
	
	/**
	 * This method deserialize {@link InputStream} to the object
	 * @param inputStream input stream to deserialize from
	 * @param forClass class to deserialize
	 * @return deserialized object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromInputStream(InputStream inputStream,Class<T> forClass){
		DataPersistence data=(DataPersistence) initXstream(forClass).fromXML(inputStream);
		return  (T) data;
	}
	
	/**
	 * This method deserialize given resource file to the object 
	 * @param resourceFile resource file to deserialize from
	 * @param forClass class to deserialize
	 * @return deserialized object
	 */
	public static <T> T fromResource(String resourceFile,Class<T> forClass){
		URL url=Thread.currentThread().getContextClassLoader().getResource(resourceFile);
		if (url==null){
			throw new RuntimeException("Resource File " + resourceFile + " was not found");
		}
		return fromURL(url, forClass);
	}
	
	/**
	 * This method deserialize given file to the object 
	 * @param filePath file path to deserialize from
	 * @param forClass class to deserialize
	 * @return deserialized object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromFile(String filePath,Class<T> forClass){
		File file=new File(filePath);
		if (!file.exists()){
			throw new RuntimeException("File " + filePath + " was not found");
		}
		DataPersistence data=(DataPersistence) initXstream(forClass).fromXML(file);
		return (T) data;
	}
	
	/**
	 * Creates exact copy of the given object 
	 * @param object Object to copy
	 * @return copy of the given object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deepCopy(T object){
		XStream xstream=getXStream();
		return (T) xstream.fromXML(xstream.toXML(object));
	}
	
	/**
	 * Copying one object to another
	 * @param source object to copy from
	 * @param target object to copy to
	 */
	public static void deepCopy(Object source,Object target){
		XStream xstream=getXStream();
		String xml=xstream.toXML(source);
		xstream.fromXML(xml,target);
	}
	
	/**
	 * Instantiates given class without invoking any declared constructor. 
	 * Class members are not instantiated and they will equal to null even if they declared with assignments.
	 * 
	 * @param forClass class to instantiate
	 * @return Instantiated object for given class
	 */
	public static <T> T instantiateClass(Class<T> forClass){
		if (forClass.isMemberClass()) {
			return fromXml("<" + forClass.getName().replace("$","_-") + "/>", forClass);
		} else {
			return fromXml("<" + forClass.getName() + "/>", forClass);
		}
	}
	
	/**
	 * This method will generate XML template from this class
	 * @return XML generated from this class 
	 * @throws Exception
	 */
	public String generateXML() throws Exception{
		DataSetGenerator.getInstance().generateData(this,true);
		return toXML();
	}
	
	/**
	 * This method will generate XML file from this class
	 * 
	 * @return XML generated from this class 
	 */
	public String generateData(){
		return DataSetGenerator.getInstance().generateDataSet(this);
	}

	
}
