package com.kssj.base.util;


import java.util.Random;

/**
* @Description: create random password
* 				(use for mail)
* 				(*****shielding the function of development******)
* 
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-15 上午11:50:49
* @version V1.0
*/
public class RandomPasswordUtil {
	
	
	/**
	* @method: genRandomNum to password
	* @Description: create word and number random
	*
	* @param pwd_len : pwd length
	* @return
	* @return String
	*
	*/
	public static String getRandomNum(int pwd_len) {
	   // 35是因为数组是从0开始的，26个字母+10个数字
	   final int maxNum = 36;
	   // create random number position
	   int i; 
	   int count = 0;
	   char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
	     '9' };

	   StringBuffer pwd = new StringBuffer("");
	   Random r = new Random();
	   while (count < pwd_len) {
	    
		   // 生成随机数，"Math.abs"取绝对值，防止生成负数，
		   i = Math.abs(r.nextInt(maxNum)); // create num：36-1
	  
		   if (i >= 0 && i < str.length) {
			   pwd.append(str[i]);
			   count++;
		   }
	   }

	   return pwd.toString();
	}
	
	/**
	* @method: genRandomNum to password
	* @Description: create word and number random
	*
	* @param pwd_len : pwd length
	* @return
	* @return String
	*
	*/
	public static String getRandomChar(int pwd_len) {
	   // 26个字母
	   final int maxNum = 36;
	   // create random number position
	   int i; 
	   int count = 0;
	   char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	   StringBuffer pwd = new StringBuffer("");
	   Random r = new Random();
	   while (count < pwd_len) {
	    
		   // 生成随机数，"Math.abs"取绝对值，防止生成负数，
		   i = Math.abs(r.nextInt(maxNum)); // create num：36-1
	  
		   if (i >= 0 && i < str.length) {
			   pwd.append(str[i]);
			   count++;
		   }
	   }

	   return pwd.toString();
	}
	
}
