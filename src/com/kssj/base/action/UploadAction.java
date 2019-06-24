package com.kssj.base.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.kssj.frame.web.action.BaseAction;

@SuppressWarnings("serial")
public class UploadAction extends BaseAction {
	// private File fileName;
	private String fileNameContentType;
	private String fileNameFileName;

	public String uploadProduct() {
		// 得到当前时间开始流逝的毫秒数,将这个毫秒数作为上传文件新的文件名
		String newFilename;
		long now = new Date().getTime();
		// 获取保存路径
		MultiPartRequestWrapper mrw = (MultiPartRequestWrapper) this.getRequest();
		File[] files = mrw.getFiles("Filedata");
		File fileName = files[0];
		String realPath = this.getRequest().getSession().getServletContext().getRealPath("upload/product");
		File file = new File(realPath);
		if (!file.exists()) {
			file.mkdir();
		}
		// 给文件呢起个新名字
		fileNameFileName = mrw.getFileNames("Filedata")[0];
		int lastIndexOf = fileNameFileName.lastIndexOf('.');
		if (lastIndexOf != -1) {
			newFilename = now + fileNameFileName.substring(lastIndexOf);
		} else {
			newFilename = Long.toString(now);
		}
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			// FileUtils.copyFile(fileName, new File(realPath, newFilename));
			FileInputStream fis = new FileInputStream(fileName);
			bis = new BufferedInputStream(fis);
			FileOutputStream fos = new FileOutputStream(new File(realPath, newFilename));
			bos = new BufferedOutputStream(fos);
			// 把file读进目录里
			byte[] buf = new byte[4096];
			int len = -1;
			while ((len = bis.read(buf)) != -1) {
				bos.write(buf, 0, len);

			}
			this.getResponse().setContentType("text/json; charset=utf-8");
			this.getResponse().getWriter().print("upload/product/" + newFilename);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != bis) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != bos) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return NONE;
	}

	public String getFileNameContentType() {
		return fileNameContentType;
	}

	public void setFileNameContentType(String fileNameContentType) {
		this.fileNameContentType = fileNameContentType;
	}

	public String getFileNameFileName() {
		return fileNameFileName;
	}

	public void setFileNameFileName(String fileNameFileName) {
		this.fileNameFileName = fileNameFileName;
	}

}
