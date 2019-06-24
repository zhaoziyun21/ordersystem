package com.kssj.frame.model;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import flexjson.JSON;

/**
* @Description: base公共model类
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午03:04:11
* @version V1.0
*/
public class BaseModel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected Log logger=LogFactory.getLog(BaseModel.class);
	//protected Logger logger= Logger.getLogger(BaseModel.class);
	protected Integer version;
	
	@JSON(include=false)
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
}
