package com.kssj.base.util;

//import it.sauronsoftware.jave.Encoder;
//import it.sauronsoftware.jave.EncoderException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import java.sql.Connection;
import java.sql.DriverManager;

/**
* @Description: 操作文件公共类
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午02:24:17
* @version V1.0
*/
public class FileUtil {
	
	private static Log logger=LogFactory.getLog(FileUtil.class);
	private static final int BUFFER_SIZE = 16 * 1024;
	public static final String FILEPATH = "/front/upload/file/";
	/**
	 * 按时间生成文件名
	 * @param originalFilename
	 * @return
	 */
	public static String generateFilename(String originalFilename){
		
		SimpleDateFormat dirSdf=new SimpleDateFormat("yyyyMM");
		String filePre=dirSdf.format(new Date());
		
        String fileExt="";
        int lastIndex=originalFilename.lastIndexOf('.');
        //取得文件的扩展名
        if(lastIndex!=-1){
        	fileExt=originalFilename.substring(lastIndex);
        }

        String filename=filePre+UUIDGenerator.getUUID()+fileExt;
        
        return filename;
	}
	
	/**
	 * 把数据写至文件中
	 * @param filePath
	 * @param data
	 */
	public static void writeFile(String filePath,String data){
		FileOutputStream fos = null;
		OutputStreamWriter writer=null;
		try {
			fos = new FileOutputStream(new File(filePath));
			writer=new OutputStreamWriter(fos, "UTF-8");			
			writer.write(data);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			try {
				if(writer!=null){
					writer.close();
				}
				if (fos != null){
					fos.close();
				}
			} catch (Exception e) {
			}
		}		
	}
	
	/**
	 * 读取文件内容
	 * @param filePath
	 * @return
	 */
	public static String readFile(String filePath){
		 StringBuffer buffer = new StringBuffer();
		// 读出这个文件的内容
		try{
			File file = new File(filePath);
		    FileInputStream fis = null;
		    BufferedReader breader=null;
		    try {
		      fis = new FileInputStream(file);
		      InputStreamReader isReader=new InputStreamReader(fis,"UTF-8");
		      breader=new BufferedReader(isReader);
		      String line;
		      while((line=breader.readLine())!=null) {
		        buffer.append(line);
		        buffer.append("\r\n");
		      }
		      breader.close();
		      isReader.close();
		      fis.close();
		      
		    } catch (FileNotFoundException e) {
		      logger.error(e.getMessage());
		    } catch (IOException e) {
		    	logger.error(e.getMessage());
		    }
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return buffer.toString();
	}
	
	/**
	 * 复制文件
	 * 
	 * @param src
	 * @param dst
	 */
	public static void copyFile(File src, File dst) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst),
					BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取文件名的前缀
	 * @param fileName
	 * @return
	 */
	public static String getFilePrefix(String fileName){
		int splitIndex = fileName.lastIndexOf(".");
		if(splitIndex==-1)
			return "";
		else
			return fileName.substring(0, splitIndex);
	}
	
	/**
	 * 获取文件名的后缀
	 * @param fileName
	 * @return
	 */
	public static String getFileSufix(String fileName){
		int splitIndex = fileName.lastIndexOf(".");
		if(splitIndex==-1)
			return "";
		else
			return fileName.substring(splitIndex + 1);
	}
	
	/*
	 * 删除文件
	 */
	 public static void  deleteFile(String fileName) {
		   File  file = new File(fileName);
		   if(file.exists()) {
			   file.delete();
		   }
	   }
	 
	 //创建文件目录
	public static boolean mkDir(String dir) {
		File file = new File(dir);
		if (!file.exists()) {
			return file.mkdirs();
		}
		return true;
	}
	
	
	//zip
	public static void zipFile(String filename,String tempFilePath) throws Exception{
		FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        File zipFileTemp = new File( filename);
        String [] files = ( new File( tempFilePath ) ).list();
        fos = new FileOutputStream( zipFileTemp );
        zipOut = new ZipOutputStream( fos );
        int i = 0;
    	for ( i = 0; i < files.length; i++ ) {
    		File f = new File( files[i] );
            fis = new FileInputStream( tempFilePath + "/" + files[i] );
            ZipEntry ze = new ZipEntry( f.getName() );
            zipOut.putNextEntry( ze );
            int c = 0;
            while ( ( c = fis.read() ) != -1 ) {
            	zipOut.write( c );
            }
            fis.close();
    	}
        zipOut.close();
        fis.close();
        fos.close();
	}
	
	//delete all files of this dir
	public static void delAllFile(String filePath) {
		File file = new File(filePath);
		File[] fileList = file.listFiles();
		String dirPath = null;
		if (fileList != null) {
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isFile()) {
					fileList[i].delete();
				}
				if (fileList[i].isDirectory()) {
					dirPath = fileList[i].getPath();
					delAllFile(dirPath);
				}
			}
			file.delete();
		}
	}
	
	/**
	 * 
	 * @method: getBySysFilePath
	 * @Description: 根据系统读取服务器路径
	 *
	 * @return 返回到项目名称
	 *
	 * @author: lijianjun
	 * @date 2014-7-4 下午2:55:24
	 * @return String
	 */
	public static String getBySysFilePath(String pa){
		String sysName = System.getProperty("os.name").toLowerCase();
		if(sysName.indexOf("window")>=0){
			pa = pa.substring(1,pa.lastIndexOf("WEB-INF/classes"));
		}else{
			pa = pa.substring(0,pa.lastIndexOf("WEB-INF/classes"));
		}
		return pa;
	}
	
	/**
	 * 
	* @method: getTempFileList
	* @Description: 读取一个文件夹下的所有文件
	*
	* @param path 文件夹路径
	* @return
	* @return List<File>
	*
	* @author: lijianjun
	* @date 2014-7-7 下午6:09:25
	 */
	public static List<File> getTempFileList(String path){
		File root = new File(path);
		List<File> fileList = new ArrayList<File>();
		if(root.isDirectory()){
			String[] fileStr = root.list();
			for (int i = 0; i < fileStr.length; i++) {
				File readfile = new File(path + "/" + fileStr[i]);
				if (!readfile.isDirectory()) {
					fileList.add(readfile);
				}
			}
		}
		return fileList;
	}
	
	/**
	 * 
	 * @method: uploadFile
	 * @Description: 上传一个文件
	 *
	 * @param file 需要上传的文件
	 * @param path 上传文件的地址
	 * @return void
	 *
	 * @author: lijianjun
	 * @date 2014-7-8 下午1:41:03
	 */
	public static void uploadFile(File file,String path){
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(path),
					BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @method: encoderFile
	 * @Description: 截取视频图片
	 *
	 * @param videoFile 视频文件
	 * @param imgFile 图片文件
	 * @param time 时间单位秒
	 * @return void 
	 *
	 * @author: lijianjun
	 * @date 2014-7-9 上午9:24:50
	 */
//	public static void encoderFile(File videoFile,File imgFile,int time){
//		
//		//视频截取对象
//		Encoder encoder = new Encoder();
//		try {
//			
//			//截取videoFile视频保存为jpgFile图片   截取时间为10秒的
//			encoder.getImage(videoFile,imgFile,time);
//			
//			//如果时间大于视频时间  截取不到图片将截取第一秒图片
//			if(!imgFile.exists()){
//				encoder.getImage(videoFile,imgFile,1);
//			}
//		} catch (EncoderException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 判断是否是空行
	 * @param row
	 * @return
	 */
	public static boolean isBlankRow(Row row){
		if(row == null) return true;
		boolean result = true;
		for(int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++){
			Cell cell = row.getCell(i, Row.RETURN_BLANK_AS_NULL);
			String value = "";
			if(cell != null){
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
					break;
				case Cell.CELL_TYPE_NUMERIC:
					value = String.valueOf((int) cell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					value = String.valueOf(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					value = String.valueOf(cell.getCellFormula());
					break;
				default:
					break;
				}
				
				if(!value.trim().equals("")){
					result = false;
					break;
				}
			}
		}
		
		return result;
	}
	
	//判断表格的值属性
	public static String getValue(Cell cell, FormulaEvaluator formulaEvaluator) {
		if (null != cell) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC: // 数字
				//return String.valueOf((int)cell.getNumericCellValue());
				String result = new String();
				if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式  
	                SimpleDateFormat sdf = null;  
	                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd hh:mm:ss")) {  
	                    sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
	                } else {// 日期  
	                    sdf = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");  
	                }  
	                Date date = cell.getDateCellValue();  
	                result = sdf.format(date);  
	            } else if (cell.getCellStyle().getDataFormat() == 58) {  
	                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
	                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	                double value = cell.getNumericCellValue();  
	                Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);  
	                result = sdf.format(date);  
	            } else {  
	                double value = cell.getNumericCellValue();  
	                CellStyle style = cell.getCellStyle();  
	                DecimalFormat format = new DecimalFormat();  
	                String temp = style.getDataFormatString();  
	                // 单元格设置成常规  
	                if (temp.equals("General")) {  
	                    format.applyPattern("#");  
		                result = format.format(value); 
	                }else{
	                	result =Double.toString(value); 
	                }
	            }  
	            return result; 
			case Cell.CELL_TYPE_STRING: // 字符串
				return cell.getStringCellValue();
			case Cell.CELL_TYPE_BOOLEAN: // Boolean
				return String.valueOf(cell.getBooleanCellValue());
			case Cell.CELL_TYPE_FORMULA: // 公式
				CellValue cv = formulaEvaluator.evaluate(cell);
				if (cv.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					return String.valueOf(cv.getNumberValue());
				} else {
					return cv.getStringValue();
				}
			case Cell.CELL_TYPE_BLANK: // 空值
				return "";
			case Cell.CELL_TYPE_ERROR: // 故障
				return "";
			default:
				return cell.getStringCellValue();
			}
		} else {
			return null;
		}
	}
	
	/**数据库联接
	 * @param tableName
	 * @return
	 */
	public static Connection  getConn(String url,String username,String password){
          try {
//	        	  url = "jdbc:mysql://172.0.3.6:3306/etl";    
//	        	  String username = "root";    
//	        	  String password = "12qwaszx"; 
	        	  Class.forName("com.mysql.jdbc.Driver").newInstance();    
	        	 Connection conn = DriverManager.getConnection(url + "?useUnicode=true&characterEncoding=GB2312", username, password);    
	        	 return conn;
          } catch (Exception e) {
        	  System.err.println("数据库连接异常: " + e.getMessage());    
        	  return null; 
          }
	}
	
	
	
	/*  public boolean HasTable(String tableName) {
	        //判断某一个表是否存在
	        boolean result = false;
	        try {
	         
                Connection conn=this.getConn();
	            DatabaseMetaData meta = (DatabaseMetaData) conn.getMetaData();
	            ResultSet set = meta.getTables (null, null, tableName, null);
	            while (set.next()) {
	                result = true;
	            }
	            
	            if(result){
	            	StringBuffer str=new StringBuffer();
	            	str.append("create table "+tableName);
	            	
	            }
	        } catch (Exception eHasTable) {
	            System.err.println(eHasTable);
	            eHasTable.printStackTrace ();
	        }
	        System.out.println("###result:=="+result);
	        return result;
	    } */
	
}
