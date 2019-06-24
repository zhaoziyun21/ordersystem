package com.kssj.base.util;


import java.security.MessageDigest;
import sun.misc.BASE64Encoder;

/**
* @Description: MD5 level data encryption
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-15 上午11:29:30
* @version V1.0
*/
public class Md5Security{
 
	 /**
		* @method: getMD5
		* @Description: 获得32位长度的字符串 ,16位的数组经过编码转换后的变换
		*
		* @param str
		* @return
		* @return String
		*/
	public static String getMD5(String str){
		  byte[] bs=getmd5Digest(str);
		  if(bs==null) return null;
		  
		  StringBuffer sb=new StringBuffer();
		  int itmp=0;
		  
		  for(int i=0;i<bs.length;i++){
			   if(bs[i]<0){
				   itmp=256+bs[i];
			   }else{
				   itmp=bs[i];
			   }
			   
			   if(itmp<16){
				   sb.append("0");
			   }
			   sb.append(Integer.toHexString(itmp));
		  }
		  return sb.toString();
	 }
	
	 /**
		* @method: getmd5Digest
		* @Description: md5 encryption
		*
		* @param str
		* @return
		* @return byte[]
		*/
	public static byte[] getmd5Digest(String str){
		  try{
		   MessageDigest md=MessageDigest.getInstance("md5");
		   md.update(str.getBytes());
		   return md.digest();
		   //return(new BASE64Encoder().encode(digest));
		  }catch(Exception e){
		   return null;
		  }
	 }
	 
	 //获得24位长度的字符,16位的数组经过base转换后的变换
	 public static String getmd5base64(String str){
		  byte[] bs=getmd5Digest(str);
		  if(bs==null) return null;
		  return(new BASE64Encoder().encode(bs));
	 }
	 
	 /*public static void main(String[] args)
	 {
		 //System.out.println(getMD5("123456"));

		 System.out.println(getmd5Digest("e10adc3949ba59abbe56e057f20f883e"));
		 byte[] bs=getmd5Digest("e10adc3949ba59abbe56e057f20f883e");
		 System.out.println(new BASE64Encoder().encode(bs));
	 }*/
	 
}