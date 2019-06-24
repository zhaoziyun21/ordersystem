package com.kssj.base.util;
import sun.misc.BASE64Decoder;

/**
* @Description: 加/解 密--Base64
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 上午10:42:21
* @version V1.0
*/
public class Base64 {
	
	/**①
	 * 将 s 进行 BASE64 编码 
	 * @param s
	 * @return
	 */
	public static String getBASE64(String s) 
	{ 
		if (s == null) {
			return null; 
		}
		return (new sun.misc.BASE64Encoder()).encode( s.getBytes() ); 
	} 

	/**
	 *  将 BASE64 编码的字符串 s 进行解码 
	 * @param s
	 * @return
	 */
	public static String getFromBASE64(String s) 
	{ 
		if (s == null){
			return null;
		}
		BASE64Decoder decoder = new BASE64Decoder(); 
		try 
		{
			byte[] b = decoder.decodeBuffer(s); 
			return new String(b); 
		} 
		catch (Exception e) 
		{ 
			return null; 
		} 
	}
	
	/**②
	 * 使用apache包进行base64编码，可解决!"换行符"!的问题
	 * @param plainText
	 * @return
	 */
	public static String encodeStrbyBase64(String plainText){  
        byte[] b=plainText.getBytes();  
        org.apache.commons.codec.binary.Base64 base64=new org.apache.commons.codec.binary.Base64();  
        b=base64.encode(b);  
        String s=new String(b);  
        return s;  
    }  
	
	/**
	 * 使用apache包进行base64解码，可解决!"换行符"!的问题
	 * @param encodeStr
	 * @return
	 */
	public static String decodeStrbyBase64(String encodeStr){  
        byte[] b=encodeStr.getBytes();  
        org.apache.commons.codec.binary.Base64 base64=new org.apache.commons.codec.binary.Base64(); 
        b=base64.decode(b);  
        String s=new String(b);  
        return s;  
    }  
}