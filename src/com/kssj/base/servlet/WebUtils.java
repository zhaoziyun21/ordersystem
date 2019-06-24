package com.kssj.base.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网络工具包，可用于向服务器发送get或post请求
 * 
 * @author yanweixin
 * @date 2014-5-5
 */
public class WebUtils {
	private static Logger log = LoggerFactory.getLogger(WebUtils.class);

	/**
	 * 向远程的url服务器进行post一个数据包
	 * 
	 * @param url
	 *            需要进行post的服务器
	 * @return
	 * @throws Exception
	 */
	public static String postString(String url, HttpHost proxy) throws Exception {
		return postString(url, null, proxy);
	}

	/**
	 * 向远程的url服务器进行post一个数据包
	 * 
	 * @param url
	 *            需要进行post的服务器
	 * @param paras
	 *            数据包中的参数
	 * @return
	 * @throws Exception
	 */
	public static String postString(String url, List<NameValuePair> paras, HttpHost proxy)
			throws Exception {
		return postString(url, paras, "UTF-8", "UTF-8", proxy);
	}

	/**
	 * 向远程的url服务器进行post一个数据包
	 * 
	 * @param url
	 *            需要进行post的服务器
	 * @param paras
	 *            数据包中的参数
	 * @return
	 * @throws Exception
	 */
	public static String postStringEncrypt(String url, List<NameValuePair> paras, HttpHost proxy)
			throws Exception {
		List<NameValuePair> paras2 = new ArrayList<NameValuePair>();
		// paras 对参数进行加密
		if (paras != null) {
			for (NameValuePair nv : paras) {
				String val = nv.getValue();
				log.debug("==" + nv.getName() + "=Encrypt before is =" + val);
				if (val != null) {
					val = AESUtil.encrypt(val).toUpperCase();
				}
				log.debug("==" + nv.getName() + "=Encrypt after is =" + val);
				paras2.add(new BasicNameValuePair(nv.getName(), val));
			}
		}
		paras.clear();
		String str = postString(url, paras2, "UTF-8", "UTF-8", proxy);
		log.debug("===decrypt before is =" + str);
		if (str != null) {
			str = AESUtil.decrypt(str);
			log.debug("===decrypt after is =" + str);
		}
		return str;
	}

	/**
	 * 向远程的url服务器进行post一个数据包
	 * 
	 * @param url
	 *            需要进行post的服务器
	 * @param paras
	 *            数据包中的参数
	 * @return
	 * @throws Exception
	 */
	public static String postString(String url, List<NameValuePair> paras,
			String uploadEncoding, String responseEncoding, HttpHost proxy) throws Exception {
		RequestConfig config = null;
		if (null != proxy) {
			config = RequestConfig.custom().setProxy(proxy).build();
		} else {
			config = RequestConfig.custom().build();
		}
		HttpClient client = HttpClients.custom()
				.setDefaultRequestConfig(config).build();

		HttpClientContext context = HttpClientContext.create();
		HttpPost post = new HttpPost(url);
		if (paras != null) {
			post.setEntity(new UrlEncodedFormEntity(paras, uploadEncoding));
		}
		CloseableHttpResponse response = (CloseableHttpResponse) client
				.execute(post, context);
		return EntityUtils.toString(response.getEntity(), responseEncoding);
	}

	public static String postData(String url, String data,
			String uploadEncoding, String responseEncoding, HttpHost proxy) throws Exception {
		RequestConfig config = null;
		if (null != proxy) {
			config = RequestConfig.custom().setProxy(proxy).build();
		} else {
			config = RequestConfig.custom().build();
		}
		HttpClient client = HttpClients.custom()
				.setDefaultRequestConfig(config).build();
		HttpClientContext context = HttpClientContext.create();
		HttpPost post = new HttpPost(url);
		if (data != null && !"".equals(data)) {
			post.setEntity(new StringEntity(data, uploadEncoding));
		}
		CloseableHttpResponse response = (CloseableHttpResponse) client
				.execute(post, context);
		return EntityUtils.toString(response.getEntity(), responseEncoding);
	}

	/**
	 * 获取远程文件
	 * @param localAddress
	 * @param localFileName
	 * @param remoteAddress
	 * @return 返回本地全路径
	 */
	public static String getRomoteFile(String localAddress,
			String localFileName, String remoteAddress) throws ClientProtocolException, IOException {
		long begin = System.currentTimeMillis();
		/**校验文件夹是否存在*/
		File path = new File(localAddress);
		if (!path.exists()) {
			path.mkdirs();
		}
		// 生成一个httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(remoteAddress);
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		// 获取问价扩展名
		String fileExt = ".jpg";
		localFileName = localFileName + fileExt;
		String localFilePath = localAddress + localFileName;
		File file = new File(localFilePath);
		try {
			FileOutputStream fout = new FileOutputStream(file);
			int l = -1;
			byte[] tmp = new byte[1024];
			while ((l = in.read(tmp)) != -1) {
				fout.write(tmp, 0, l);
				// 注意这里如果用OutputStream.write(buff)的话，图片会失真
			}
			fout.flush();
			fout.close();
		} catch(Exception e){
			log.error("下载异常."+e.getMessage(),e);
		}finally {
			// 关闭数据流
			in.close();
			httpclient.close();
		}
		log.info("____getRomoteFile____处理耗时m=" + (System.currentTimeMillis() - begin));
		
		//损坏的文件测试使用（linshi）
//		file=new File("/home/imagedata/2016/05/13/EDym1TaopHKgNjWiXGv7RiHXofIV-GjcLTHz2G8QwKYvyy_nRUDofPRGPZR-6oPt.jpg");
		log.info("____当前文件名称为："+file.getParent()+"/"+file.getName());
		log.info("____当前文件大小为："+file.length());
		if(file.length() < 200){
			log.info("____下载文件失败，当前文件大小为："+file.length());
			return "";
		}else{
			return localFileName;
		}
		
	}

	/**
	 * 上传远程文件
	 * @param localFilePath
	 * @param remoteAddress
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void postRomoteFile(String localFilePath,String remoteAddress) throws ParseException, IOException {
		long begin = System.currentTimeMillis();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// 要上传的文件的路径
			String filePath = new String(localFilePath);
			// 把一个普通参数和文件上传给下面这个地址 是一个servlet
			HttpPost httpPost = new HttpPost(remoteAddress);
			// 把文件转换成流对象FileBody
			File file = new File(filePath);
			FileBody bin = new FileBody(file);
			/*StringBody uploadFileName = new StringBody("my.png", 
			        ContentType.create("text/plain", Consts.UTF_8));*/
			// 以浏览器兼容模式运行，防止文件名乱码。
			HttpEntity reqEntity = MultipartEntityBuilder.create()
					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.addPart("uploadFile", bin) // uploadFile对应服务端类的同名属性<File类型>
					// .addPart("uploadFileName", uploadFileName)// uploadFileName对应服务端类的同名属性<String类型>
					.setCharset(CharsetUtils.get("UTF-8")).build();
			httpPost.setEntity(reqEntity);

			log.info("____发起请求的页面地址 " + httpPost.getRequestLine());
			// 发起请求 并返回请求的响应
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				// 打印响应状态
				log.info("____"+response.getStatusLine());
				// 获取响应对象
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					// 打印响应长度
					log.info("____Response content length: "+ resEntity.getContentLength());
					// 打印响应内容
					log.info("____"+EntityUtils.toString(resEntity,Charset.forName("UTF-8")));
				}
				// 销毁
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
		log.info("____getRomoteFile____处理耗时m=" + (System.currentTimeMillis() - begin));
	}

	/** 
	 * 下载文件 
	 *  
	 * @param url 
	 *            http://www.xxx.com/img/333.jpg 
	 * @param destFileName 
	 *            xxx.jpg/xxx.png/xxx.txt 
	 * @throws ClientProtocolException 
	 * @throws IOException 
	 */
	public static void getFile(String url, String destFileName)
			throws ClientProtocolException, IOException {
		// 生成一个httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		File file = new File(destFileName);
		try {
			FileOutputStream fout = new FileOutputStream(file);
			int l = -1;
			byte[] tmp = new byte[1024];
			while ((l = in.read(tmp)) != -1) {
				fout.write(tmp, 0, l);
				// 注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试
			}
			fout.flush();
			fout.close();
		} finally {
			// 关闭低层流。
			in.close();
		}
		httpclient.close();
	}
	public static String httpsGet(String url)
	{
		HttpClient httpclient =getHttpsClient();
		try {
			
			HttpGet httpget = new HttpGet(url);
			HttpParams params = httpclient.getParams();
//			System.out.println("REQUEST:" + httpget.getURI());
			ResponseHandler responseHandler = new BasicResponseHandler();
			String responseBody = (String)httpclient.execute(httpget, responseHandler);
//			System.out.println(responseBody);
			return responseBody;
			// Create a response handler

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {  
            try {   
            	httpclient.getConnectionManager().shutdown();   
            } catch (Exception ex) {  
            	ex.printStackTrace(); 
            }  
        } 
		return null;
	}
	
	public static HttpClient getHttpsClient()
	{
		HttpClient httpclient = new DefaultHttpClient();
        //Secure Protocol implementation.
		SSLContext ctx;
		try {
			ctx = SSLContext.getInstance("SSL");
		
		        //Implementation of a trust manager for X509 certificates
		X509TrustManager tm = new X509TrustManager() {
		
		public void checkClientTrusted(X509Certificate[] xcs,
				String string) throws CertificateException {
		
		}
		
		public void checkServerTrusted(X509Certificate[] xcs,
				String string) throws CertificateException {
		}
		
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
		};
			ctx.init(null, new TrustManager[] { tm }, null);
		
		SSLSocketFactory ssf = new SSLSocketFactory(ctx);
		
		ClientConnectionManager ccm = httpclient.getConnectionManager();
		        //register https protocol in httpclient's scheme registry
		SchemeRegistry sr = ccm.getSchemeRegistry();
		sr.register(new Scheme("https", 443, ssf));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return httpclient;

	}
}
