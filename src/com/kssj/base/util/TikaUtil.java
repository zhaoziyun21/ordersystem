package com.kssj.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;


/**
 * 
 * @Description: 文本抽取工具类  
 * 目前测试支持格式 .docx .pdf .txt .html .xlsx .pptx .ppt .xml .zip .jar  其他无测试
 * @Company:TGRF
 * @author: lijianjun
 * 
 * @date: 2014-7-2 下午4:29:30
 * @version V1.0
 */
public class TikaUtil {
	/**
	 * 
	 * @method: getBody
	 * @Description: 传入文件路径返回文件文本内容
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 *
	 * @author: lijianjun
	 * @date 2014-7-2 下午4:30:46
	 * @return String 如果文件为空或者不存在将返回null
	 */
	public static String getBody(File file) throws Exception {
		if(file==null||!file.exists()){
			return null;
		}
		Tika tika = new Tika();
		Metadata metadata = new Metadata();
		metadata.set(Metadata.CONTENT_ENCODING, "utf-8");
		InputStream input = new FileInputStream(file);
		String str = tika.parseToString(input,metadata);
		if(input!=null)input.close();
		return str;
	}
}
