package com.kssj.base.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class WeChatTest {
	public static void main(String[] args) {

		String accesstoken =AccessTokenUtil.accessToken();
		System.out.println(accesstoken);
		Gson gson = new Gson();
		String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+accesstoken; 
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("touser", "@all");
		map.put("msgtype", "textcard");
		map.put("agentid", 1000002);
		Map<String, Object> map1 = new HashMap<String, Object>();
	/*	Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("title", "丁丁测试");
		map2.put("description", "不要害怕，我还是测试");
		map2.put("url", "www.baidu.com");
		map2.put("picurl", "http://wx.qlogo.cn/mmopen/G98WiczhYKGKCtRH9ickPxRht1UoeNNibdd9FGOVrGshIfdnf9ibzicoUu9Xz9iaSgzTcha06RPryfSWnMibwJlVhnxu28miaje76mA4/0");
		map2.put("btntxt", "只有4字");*/
		//map1.put("content", "不要害怕，我是只是测试！");
		//List<Map> list = new ArrayList<Map>();
		//list.add(map2);
		map1.put("title", "领奖通知");
		map1.put("description", "<div class=\"gray\">2016年9月26日</div> <div class=\"normal\">恭喜你抽中iPhone 7一台，领奖码：8888</div><div class=\"highlight\">请于2016年10月10日前联系行政同事领取</div>");
		map1.put("url", "www.baidu.com");
		map1.put("btntxt", "查看详情");
		map.put("textcard", map1);
		System.out.println(gson.toJson(map));
		String json = gson.toJson(map);
		try {
			String str = WebUtils.postData(url, json, "UTF-8", "UTF-8", null);
			System.out.println(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
