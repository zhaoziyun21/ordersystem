package com.kssj.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MessageUtil {
// sb:http://web.wasun.cn/asmx/smsservice.aspx?name=13811670696&pwd=68AF31244EA5A2C2F00A82561430&mobile=18310252061&content=+JAVA%E6%8E%A5%E5%8F%A3%E7%9F%AD%E4%BF%A1%E5%8F%91%E9%80%81%E7%A4%BA%E4%BE%8B&stime=&sign=%E8%AF%B7%E6%82%A8%E4%BF%AE%E6%94%B9%E7%AD%BE%E5%90%8D&type=pt&extno=
//	0,2016060118063275363056821,0,1,0,提交成功
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
				//发送内容
				String content = "尊敬的某负责人,发生井盖丢失应急事件，请您及时处理"; 
				String sign="中昌天盛";
				
				// 创建StringBuffer对象用来操作字符串
				StringBuffer sb = new StringBuffer("http://web.wasun.cn/asmx/smsservice.aspx?");
				// 向StringBuffer追加用户名
				sb.append("name=13811670696");

				// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
				sb.append("&pwd=68AF31244EA5A2C2F00A82561430");

				// 向StringBuffer追加手机号码
				sb.append("&mobile=18310252061");

				// 向StringBuffer追加消息内容转URL标准码
				sb.append("&content="+URLEncoder.encode(content,"UTF-8"));
				
				//追加发送时间，可为空，为空为及时发送
				sb.append("&stime=");
				
				//加签名
				sb.append("&sign="+URLEncoder.encode(sign,"UTF-8"));
				
				//type为固定值pt  extno为扩展码，必须为数字 可为空
				sb.append("&type=pt&extno=");
				// 创建url对象
				//String temp = new String(sb.toString().getBytes("GBK"),"UTF-8");
				System.out.println("sb:"+sb.toString());
				URL url = new URL(sb.toString());

				// 打开url连接
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				// 设置url请求方式 ‘get’ 或者 ‘post’
				connection.setRequestMethod("POST");

				// 发送
				InputStream is =url.openStream();

				//转换返回值
				String returnStr = MessageUtil.convertStreamToString(is);
				
				// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功   具体见说明文档
				System.out.println(returnStr);
				// 返回发送结果

	}
	/**
	 * 转换返回值类型为UTF-8格式.
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {    
        StringBuilder sb1 = new StringBuilder();    
        byte[] bytes = new byte[4096];  
        int size = 0;  
        
        try {    
        	while ((size = is.read(bytes)) > 0) {  
                String str = new String(bytes, 0, size, "UTF-8");  
                sb1.append(str);  
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
        return sb1.toString();    
    }
	
	public static String sendMessage(String content,String receiveTel) throws Exception{
		String sign="中昌天盛";
		
		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer("http://web.wasun.cn/asmx/smsservice.aspx?");
		// 向StringBuffer追加用户名
		sb.append("name=13811670696");

		// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
		sb.append("&pwd=68AF31244EA5A2C2F00A82561430");

		// 向StringBuffer追加手机号码
		sb.append("&mobile="+receiveTel);

		// 向StringBuffer追加消息内容转URL标准码
		sb.append("&content="+URLEncoder.encode(content,"UTF-8"));
		
		//追加发送时间，可为空，为空为及时发送
		sb.append("&stime=");
		
		//加签名
		sb.append("&sign="+URLEncoder.encode(sign,"UTF-8"));
		
		//type为固定值pt  extno为扩展码，必须为数字 可为空
		sb.append("&type=pt&extno=");
		// 创建url对象
		//String temp = new String(sb.toString().getBytes("GBK"),"UTF-8");
		System.out.println("sb:"+sb.toString());
		URL url = new URL(sb.toString());

		// 打开url连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 设置url请求方式 ‘get’ 或者 ‘post’
		connection.setRequestMethod("POST");

		// 发送
		InputStream is =url.openStream();

		//转换返回值
		String returnStr = MessageUtil.convertStreamToString(is);
		
		// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功   具体见说明文档
		System.out.println(returnStr);
		return returnStr;
		// 返回发送结果

	}
}
