package com.kssj.base.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
* @Description: Net Ip
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-3-12 上午10:57:17
* @version V1.0
*/
@SuppressWarnings("rawtypes")
public class NetUtils {
	
	/**
	 * 获取主机IP
	 * 
	 * @return
	 */
	public static String getLocalHostIP() {
		String ip;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress();
		} catch (Exception ex) {
			ip = "";
		}
		return ip;

	}

	/**
	 * 获取主机名称
	 * 
	 * @return
	 */
	public static String getLocalHostName() {
		String hostName;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (Exception ex) {
			hostName = "";
		}
		return hostName;
	}

	public static String[] getAllLocalHostIP() {

		String[] ret = null;
		try {
			Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
			String add = "";
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					ip = ips.nextElement();
					if (StringUtils.isNotBlank(ip.getHostAddress()) && !ip.getHostAddress().equals("127.0.0.1")
							&& ip.getHostAddress().indexOf(":") == -1) {
						{
							add += ip.getHostAddress() + ",";
						}
					}
				}
			}
			if (add.endsWith(",")) {
				ret = add.split(",");
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			ret = null;
		}
		return ret;
	}

	public static String[] getAllHostIPByName(String hostName) {
		String[] ret = null;
		try {
			if (hostName.length() > 0) {
				InetAddress[] addrs = InetAddress.getAllByName(hostName);
				if (addrs.length > 0) {
					ret = new String[addrs.length];
					for (int i = 0; i < addrs.length; i++) {
						ret[i] = addrs[i].getHostAddress();
					}
				}
			}

		} catch (Exception ex) {
			ret = null;
		}
		return ret;
	}

	/**
	 * 获取用户真实IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemortIP(HttpServletRequest request) {
		String remortIP = "";

		if (request.getHeader("x-forwarded-for") == null) {
			remortIP = request.getRemoteAddr() + "";
		} else {
			remortIP = request.getHeader("x-forwarded-for");
		}

		if (remortIP.contains(",")) {
			remortIP = remortIP.substring(0, remortIP.indexOf(","));
		}

		return remortIP;
	}

	/**
	 * 获取用户真实IP地址 (Nginx+Resin、Apache+WebLogic、Squid+Nginx)
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip != null && ip.contains(",")) {
			ip = ip.substring(0, ip.indexOf(","));
		}
		return ip;
	}

	public static void main(String[] args) {
		System.out.println(getLocalHostIP());
		System.out.println("主机名：" + getLocalHostName());

		String[] localIP = getAllLocalHostIP();
		for (int i = 0; i < localIP.length; i++) {
			System.out.println(localIP[i]);
		}
	}

}
