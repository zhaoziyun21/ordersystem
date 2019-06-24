package com.kssj.base.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
* @Description: String - inputstream - file -->convert
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-7-4 下午12:42:05
* @version V1.0
*/
public class StrInsFile {
	
	/**
	* @method: inputStreamToString
	* @Description: InputStream To(convert) String
	*
	* @param is
	* @return
	* @return String
	*
	* @author: ChenYW
	* @date 2014-7-4 上午11:16:48
	*/
	public static String  inputStreamToString(InputStream is){
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));   

        StringBuilder sb = new StringBuilder();   
        String line = null;   
        try {   
            while ((line = reader.readLine()) != null) {   
                sb.append(line + "/n");   
            }   
        } catch (IOException e) {   
            e.printStackTrace();   
        } finally {   
            try {   
                is.close();   
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }   

        return sb.toString();   
	}
	
	/**
	* @method: stringToInputStream
	* @Description: String To(convert) InputStream
	*
	* @param str
	* @return
	* @return InputStream
	*
	* @author: ChenYW
	* @date 2014-7-4 上午11:28:32
	*/
	public static InputStream stringToInputStream(String str){
		if (str != null) {
			try {
				InputStream   in_withcode   =   new   ByteArrayInputStream(str.getBytes("UTF-8"));
				return in_withcode;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	/**
	* @method: fileToInputStream
	* @Description: File To(convent) InputStream
	*
	* @param file
	* @return
	* @return InputStream
	*
	* @author: ChenYW
	* @date 2014-7-4 上午11:37:56
	*/
	public static InputStream fileToInputStream(File file){
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return is;
	}
	
	/**
	* @method: inputStreamToFile
	* @Description: InputStream To(convert) File
	*
	* @param is
	* @param file   --> return
	*
	* @author: ChenYW
	* @date 2014-7-4 上午11:39:23
	*/
	public static void inputStreamToFile(InputStream is,File file){
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			
			while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}