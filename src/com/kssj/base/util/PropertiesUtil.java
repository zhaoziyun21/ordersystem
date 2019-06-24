package com.kssj.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
* @Description: properties tools
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014年5月12日 下午5:44:25
* @version V1.0
*/
public class PropertiesUtil {
	/**
	 * get properties from file of properties
	 * 
	 * @param filename include the fullpath ,for example: d:/abc.properties
	 * @param encode
	 * @return
	 */
	public static Properties getFromFile(String filename,String encode){
		FileInputStream fis=null;
		Properties props=new Properties();
		Reader reader=null;
		try{
			fis=new FileInputStream(filename);
			reader = new InputStreamReader(fis, encode); 
			props.load(reader);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				if(reader!=null)reader.close();
				if(fis!=null) fis.close();
			}catch(Exception e){}
		}
		return props;
	}
	
	/**
	 * get properties from file,default encode is utf-8
	 * 
	 * @param filename include the fullpath ,for example: d:/abc.properties
	 * @return
	 */
	public static Properties getFromFile(String filename){
		return getFromFile(filename,"UTF-8");
	}
	
	/**
	 * write the key-value to file
	 * 
	 * @param filename  include the fullpath ,for example: d:/abc.properties
	 * @param key
	 * @param value
	 */
	public static void writeKey(String filename,String key,String value){
		Properties orgProps=getFromFile(filename);
		orgProps.setProperty(key, value);
		try{
			OutputStream outStream=new FileOutputStream(new File(filename));
			orgProps.store(outStream, "set");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * get the value by key from properties 
	 * 
	 * @param filename
	 * @param encode
	 * @param key
	 * @return
	 */
	public static String getValueByKeyFromFile(String filename,String encode,String key){
		String value = null;
		try {
			Properties props = getFromFile(filename,"UTF-8");
			value = (String)props.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			value = null;
		}	
		return value;
	}
	
	/**
	 * get the map data from properties
	 * 
	 * @param filename
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> getMapfromFile(String filename){
		Map<String, String> map = new HashMap<String, String>();
		try {
			Properties props = getFromFile(filename,"UTF-8");
			
			Iterator it = props.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				map.put(key, (String) props.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return map;
	}
}
