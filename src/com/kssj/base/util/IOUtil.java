package com.kssj.base.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.kssj.base.listener.AppUtil;

/**
* @Description: IO 流处理
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-3-24 下午05:52:14
* @version V1.0
*/
public class IOUtil{

	
	/**
	* @method: strToInputStream
	* @Description: 字符串转化为输入流
	*
	* @author: ChenYW
	* @date 2014-3-25 上午09:07:25
	*/
	public static InputStream strToInputStream(String str) throws UnsupportedEncodingException{
		if(str != null){
			//InputStream   in_nocode   =   new   ByteArrayInputStream(str.getBytes());  
			InputStream in_withcode = new ByteArrayInputStream(str.getBytes("UTF-8")); 
			return in_withcode;
		}
		return null;
	}
	
	/**
	* @method: streamToStr
	* @Description: 输入流转化为字符串
	*
	* @author: ChenYW
	* @date 2014-3-25 上午09:21:06
	*/
	public static String streamToStr(InputStream is) {   
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));   
        
		StringBuilder sb = new StringBuilder();   
        String line = null;   
        try {   
            while ((line = reader.readLine()) != null) {//一行一行的读取   
                sb.append(line + "/n");   
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
        return sb.toString();   
    }    
	
	/**
	* @method: readReplaceFileByName
	* @Description: 根据所在系统内的文件名，获取内容并替换内容
	* 
	* @param fileName : "tms\WebRoot\excelTemplate\TransportDetail.xls"的文件名称，如：\excelTemplate\TransportDetail.xls
	* @param isLocal  : 如果获取的是本地的文件，则设置为true，fileName应为例：“c:/test.txt”
	*
	* @author: ChenYW
	 * @throws FileNotFoundException 
	* @date 2014-3-25 上午09:40:53
	*/
	public static void readReplaceFileByName(String fileName,boolean isLocal,String convertStr) throws FileNotFoundException{
		File file = null;
		if (isLocal) {
			file = new File(fileName);
		}else {
			file = new File(AppUtil.getAppAbsolutePath()+fileName);
		}
		//InputStream is = new FileInputStream(file);
		String fileContent = "";
		try {
			fileContent = FileUtils.readFileToString(file, "GBK");
		} catch (IOException e) {
			e.printStackTrace();
		}
		fileContent += convertStr;
		try {
			FileUtils.writeStringToFile(file, fileContent, "GBK");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	* @method: copyDestFileFromSrcFile
	* @Description: 复制源文件的内容到目标文件
	*
	* @param srcFilename	：源文件
	* @param destFilename	：目标文件
	* @param isLocal		：如果获取的是本地的文件，则设置为true，fileName应为例：“c:/test.txt”
	*
	* @author: ChenYW
	* @date 2014-3-25 上午11:09:04
	*/
	public static void copyDestFileFromSrcFile(String srcFilename, String destFilename,boolean isLocal) throws IOException{
		File srcFile = null;
		File destFile = null;
		if (isLocal) {
			srcFile = new File(srcFilename);
			destFile = new File(destFilename);
		}else {
			srcFile = new File(AppUtil.getAppAbsolutePath()+srcFilename);
			destFile = new File(AppUtil.getAppAbsolutePath()+destFilename);
		}
		
		FileUtils.copyFile(srcFile, destFile);
	}
	
	/**
	* @method: copyOutFromInStream
	* @Description: 输入流复制到 输出流 
	*
	* @param srcFilename	: 源文件
	* @param destFilename	: 如："c:\\kk.dat"
	* @param isLocal		：如果获取的是本地的文件，则设置为true，fileName应为例：“c:/test.txt”
	*
	* @author: ChenYW
	* @date 2014-3-25 上午11:43:13
	*/
	public static void copyOutFromInStream(String srcFilename, String destFilename,boolean isLocal) throws IOException{
		File srcFile = null;
		Writer write  = null;
		if (isLocal) {
			srcFile = new File(srcFilename);
			write = new FileWriter(destFilename);
		}else {
			srcFile = new File(AppUtil.getAppAbsolutePath()+srcFilename);
			write = new FileWriter(AppUtil.getAppAbsolutePath()+destFilename);
		}
		
		InputStream ins = new FileInputStream(srcFile);  
  
		IOUtils.copy(ins, write);  
        
		write.close();  
        ins.close();  
	}
	
	/**
	* @method: copyDirFromFile
	* @Description: 文件复制指定的目录 
	*
	* @param srcFileName	: 源文件
	* @param dir			: 目标目录（"D:\\"）
	* @param isLocal		：如果获取的是本地的文件，则设置为true，fileName应为例：“c:/test.txt”
	*
	* @author: ChenYW
	* @date 2014-3-25 上午11:54:35
	*/
	public static void copyDirFromFile(String srcFileName, String dir, boolean isLocal) throws IOException{
		File srcfile = null;
		if (isLocal) {
			srcfile = new File(srcFileName);
		}else {
			srcfile = new File(AppUtil.getAppAbsolutePath()+srcFileName);
		}
        
        File destDir = new File(dir);  
          
        FileUtils.copyFileToDirectory(srcfile, destDir);  
	}
	
	/**
	* @method: copyURLToFile
	* @Description: 网络流保存为文件 
	*
	* @param url 		：如："http://www.163.com"
	* @param fileAddr	：如："c:\\163.html"
	*
	* @author: ChenYW
	* @date 2014-3-25 下午01:12:25
	*/
	public static void copyURLToFile(String url, String fileAddr) throws Exception{
		URL urlFile = new URL(url);  
        
        File file = new File(fileAddr);  
          
        FileUtils.copyURLToFile(urlFile, file);  
	}
	
	/**
	* @method: deleteDir
	* @Description: 删除目录操作 
	*
	* @author: ChenYW
	* @date 2014-3-25 下午01:15:52
	*/
	public static void deleteDir(String dir) throws IOException{
		File dirFile = new File(dir);  
		  
        FileUtils.cleanDirectory(dirFile);//清空目录下的文件  
  
        FileUtils.deleteDirectory(dirFile);//删除目录和目录下的文件  
  
	}
	
	/**
	* @method: readWebCodeByUrl
	* @Description: 根据url获取一个网页的源代码的代码
	*
	* @param url : 如：“http://www.baidu.com”
	*
	* @author: ChenYW
	* @date 2014-3-25 上午09:54:45
	*/
	public static String readWebCodeByUrl(String url) throws MalformedURLException, IOException{
		StringBuffer webCode = new StringBuffer();
		InputStream in = new URL(url).openStream();
	    try {
	       InputStreamReader inR = new InputStreamReader( in );
	       BufferedReader buf = new BufferedReader( inR );
	       String line;
	       while ( ( line = buf.readLine() ) != null ) {
	       		webCode.append(line);
	       }
	    } finally {
	       in.close();
	    }

	    return webCode.toString();
	}
	
	/**
	* @method: downWebByUrl
	* @Description: 方便地下载文 件到本地
	*
	* @param url : 如："http://www.baidu.com/img/baidu_logo.gif"
	* @param isLocal  : 如果获取的是本地的文件，则设置为true，fileName应为例：“c:/test.txt”
	*
	* @author: ChenYW
	 * @throws IOException 
	 * @throws MalformedURLException 
	* @date 2014-3-25 上午11:26:34
	*/
	public static void downWebByUrl(String url,boolean isLocal, String destFileName) throws MalformedURLException, IOException{
		File file = null;
		if (isLocal) {
			file = new File(destFileName);
		}else {
			file = new File(AppUtil.getAppAbsolutePath()+destFileName);
		}
		
		InputStream in = new URL(url).openStream();
		byte [] downFile = IOUtils.toByteArray(in);
		//IOUtils.write(downFile,new FileOutputStream(new File("c:/test.gif")));
		FileUtils.writeByteArrayToFile(file,downFile);
		IOUtils.closeQuietly(in);
	}
	
	/**
	* @method: readWebCodeByUrlOfIoutils
	* @Description: 根据url获取一个网页的源代码的代码,通过apache的IO工具类IOUtils(可以大大简化代码)--（将输入流转换成文本 ）
	*
	* @param url : 如：“http://www.baidu.com”
	*
	* @author: ChenYW
	* @date 2014-3-25 上午10:03:01
	*/
	public static String readWebCodeByUrlOfIoutils(String url) throws MalformedURLException, IOException{
		String webCode = new String();
		InputStream in = new URL(url).openStream();
	    try {
	    	webCode = IOUtils.toString(in,"UTF-8");
	    } finally {
	    	IOUtils.closeQuietly(in);//关闭相关流
	    }

	    return webCode;
	}
	
	/**
	* @method: readStringByFilename
	* @Description: 根据所在系统内的文件名称获取文件内容。
	*
	* @param fileName : "tms\WebRoot\excelTemplate\TransportDetail.xls"的文件名称，如：\excelTemplate\TransportDetail.xls
	* @param isLocal  : 如果获取的是本地的文件，则设置为true，fileName应为例：“c:/test.txt”
	*
	* @author: ChenYW
	 * @throws IOException 
	* @date 2014-3-25 上午10:08:03
	*/
	public static String readStringByFilename(String fileName,boolean isLocal) throws IOException{
		File file = null;
		if (isLocal) {
			file = new File(fileName);
		}else {
			file = new File(AppUtil.getAppAbsolutePath()+fileName);
		}
	    List<String> list = new ArrayList<String>();
	    list = FileUtils.readLines(file, "UTF-8");
	    
	    StringBuffer reStr = new StringBuffer();
	    for (String str : list) {
			reStr.append(str);
		}
	    
	    return reStr.toString();
	}
	
	/**
	* @method: getFreespace
	* @Description: 获得指定磁盘路径的可用空间（G）
	*
	* @param disk ： 如：“C:\\”
	* @author: ChenYW
	* @date 2014-3-25 上午10:57:55
	*/
	public static Double getFreespace(String disk) throws IOException{
	    long freeSpace = FileSystemUtils.freeSpaceKb(disk);
	    Double freeDuo = 0d;
	    if (freeSpace > 0L) {
	    	freeDuo = (double)freeSpace/(1024*1024);
		}
	    
	    DecimalFormat df = new DecimalFormat("#.##");
	    return Double.parseDouble(df.format(freeDuo));
	}

	
}