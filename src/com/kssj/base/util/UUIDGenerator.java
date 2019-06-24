package com.kssj.base.util;

import java.util.UUID;

/**
* @Description: create UUID 
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午02:57:49
* @version V1.0
*/
public class UUIDGenerator {    
   
	public UUIDGenerator() {    
    } 
	
    /**   
     * get a UUID(32)   
     * @return String UUID   
     */    
    public static String getUUID(){    
        String s = UUID.randomUUID().toString();    
        //Remove “-” of "42961ea7-6998-4cd9-acab-d8218fc56bab"
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);    
    }    
    /**   
     *  Get specify the number of UUID
     *  
     * @param number  	:  Need to get the number of UUID( int )
     * @return String[] :  the array of UUID   
     */    
    public static String[] getUUID(int number){    
        if(number < 1){    
            return null;    
        }    
        String[] ss = new String[number];    
        for(int i=0;i<number;i++){    
            ss[i] = getUUID();    
        }    
        return ss;    
    }  
    
    /**
    * @method: getDecimal
    * @Description: Get the decimal of UUID
    *
    * @return
    * @return String
    *
    * @author: ChenYW
    * @date 2014-1-4 下午03:18:44
    */
    public static String getDecimal(){
    	String reValue = null;
    	//16进制转10进制数字
    	String vars[]=UUID.randomUUID().toString().split("-");
    	for(int i=0;i<vars.length;i++){
    		long var=Long.valueOf(vars[i], 16);
    		
    		reValue += String.valueOf(var);
    	}
    	return reValue;
    }
    
        
}    

