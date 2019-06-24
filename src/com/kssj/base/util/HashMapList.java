package com.kssj.base.util;

import java.util.HashMap;
import java.util.List;

/**
* @Description: hashMap tool 工具类
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-2-11 下午02:59:16
* @version V1.0
*/
@SuppressWarnings({"unchecked","rawtypes"})
public class HashMapList {
	private List mList = new java.util.ArrayList();
    public void put(String name, String[] paramValues){  
        if(paramValues == null){
            return;
        }
        
        for(int i = 0; i < paramValues.length; i ++){     
            HashMap map = get(i);
            map.put(name, paramValues[i]);
        }
    }
    
    public HashMap get(int index){
        HashMap retMap;
        if(mList.size() > index){ 
            retMap = (HashMap)mList.get(index);
        } else {
            retMap = new HashMap();
            mList.add(retMap);
        }
        return retMap;
    }
    
    public int size(){
        return mList.size();
    }
}
