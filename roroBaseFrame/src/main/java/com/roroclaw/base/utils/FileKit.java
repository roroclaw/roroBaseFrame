package com.roroclaw.base.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class FileKit {
	
	protected static Logger logger = LoggerFactory.getLogger(FileKit.class);
	
	/**
	 * 保存文件
	 * 
	 * @param stream
	 * @param path
	 * @param filename
	 * @throws java.io.IOException
	 */
	public static String saveFileFromInputStream(InputStream stream,
			String path, String filename) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		String filePath = path + File.separator + filename;
		FileOutputStream fs = new FileOutputStream(filePath);
		byte[] buffer = new byte[1024 * 1024];
		int bytesum = 0;
		int byteread = 0;
		while ((byteread = stream.read(buffer)) != -1) {
			bytesum += byteread;
			fs.write(buffer, 0, byteread);
			fs.flush();
		}
		fs.close();
		stream.close();
		return filePath;
	}

	/**
	 * 保存文件
	 *
	 * @param contentStr
	 * @param path
	 * @param filename
	 * @throws java.io.IOException
	 */
	public static String saveFileFromString(String contentStr,
			String path, String filename) throws IOException {
		File dirfile = new File(path);
		if (!dirfile.exists()) {
			dirfile.mkdir();
		}
		String filePath = path + File.separator + filename;
		try {
			File file = new File(filePath);
			FileOutputStream fops = new FileOutputStream(file);
			Writer out = new OutputStreamWriter(fops, "UTF-8");
			out.write(contentStr);
			out.close();
			fops.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}

	/*
	 * Java文件操作 获取文件扩展名
	 * 
	 * Created on: 2011-8-2 Author: blueeagle
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/*
	 * Java文件操作 获取不带扩展名的文件名
	 * 
	 * Created on: 2011-8-2 Author: blueeagle
	 */
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	/**
	 * 读取文件内容
	 * 
	 * @return
	 * @throws java.io.IOException
	 */
	public static String readFile(String uri,String encode) throws IOException {
		String content = "";
		File file = new File(uri.replace("file:/", ""));
		if(StringUtils.isEmpty(encode)){
			encode = "utf-8";
		}
		content = FileUtils.readFileToString(file, encode);
		return content;
	}

	/**
	 * 删除文件
	 * 
	 * @return
	 * @throws java.io.IOException
	 */
	public static boolean delFile(String uri) throws IOException {
		boolean bol = true;
		File file = new File(uri.replace("file:/", ""));
		if (file.exists()) {
			bol = file.delete();
		}
		return bol;
	}

	/**
	 * 获取随机文件名
	 * 
	 * @return
	 */
	public static String getRadomFileName() {
		String filename = "";
		filename = String.valueOf(System.currentTimeMillis());
		return filename;
	}

//	/**
//	 * 压缩
//	 * 
//	 * @param zipFileName
//	 * @param inputFile
//	 * @throws Exception
//	 */
//	public static void zip(String zipFileUri, String sourceFileUri) throws Exception {
//		logger.debug("压缩中...");
//		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
//				zipFileUri));
//		BufferedOutputStream bo = new BufferedOutputStream(zos);
//		File sourcefile = new File(sourceFileUri);
//		zip(zos, sourcefile, sourcefile.getName(), bo);
//		bo.close();
//		zos.close(); // 输出流关闭
//		logger.debug("压缩完成");
//	}
//
//	/**
//	 * 压缩
//	 * 
//	 * @param zipFileName
//	 * @param inputFile
//	 * @throws Exception
//	 */
//	public static void zip(ZipOutputStream out, File f, String base,
//			BufferedOutputStream bo) throws Exception { // 方法重载
//		if (f.isDirectory()) {
//			File[] fl = f.listFiles();
//			if (fl.length == 0) {
//				out.putNextEntry(new ZipEntry(base + "/")); // 创建zip压缩进入点base
//			}
//			for (int i = 0; i < fl.length; i++) {
//				zip(out, fl[i], base + "/" + fl[i].getName(), bo); // 递归遍历子文件夹
//			}
//		} else {
//			out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
//			FileInputStream in = new FileInputStream(f);
//			BufferedInputStream bi = new BufferedInputStream(in);
//			int b;
//			while ((b = bi.read()) != -1) {
//				bo.write(b); // 将字节流写入当前zip目录
//			}
//			bi.close();
//			in.close(); // 输入流关闭
//		}
//	}

	// ==================文件复制=============================================================
	/**
	 * 复制单个文件
	 * 
	 * @param srcFileName
	 *            待复制的文件名
	 * @param descFileName
	 *            目标文件名
	 * @param overlay
	 *            如果目标文件存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyFile(String srcFileName, String destFileName,
			boolean overlay) {
		File srcFile = new File(srcFileName);

		// 判断源文件是否存在
		if (!srcFile.exists()) {
			logger.debug("源文件：" + srcFileName + "不存在！");
			return false;
		} else if (!srcFile.isFile()) {
			logger.debug("复制文件失败，源文件：" + srcFileName + "不是一个文件！");
			return false;
		}

		// 判断目标文件是否存在
		File destFile = new File(destFileName);
		if (destFile.exists()) {
			// 如果目标文件存在并允许覆盖
			if (overlay) {
				// 删除已经存在的目标文件，无论目标文件是目录还是单个文件
				new File(destFileName).delete();
			}
		} else {
			// 如果目标文件所在目录不存在，则创建目录
			if (!destFile.getParentFile().exists()) {
				// 目标文件所在目录不存在
				if (!destFile.getParentFile().mkdirs()) {
					// 复制文件失败：创建目标文件所在目录失败
					return false;
				}
			}
		}

		// 复制文件
		int byteread = 0; // 读取的字节数
		InputStream in = null;
		OutputStream out = null;

		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];

			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 复制整个目录的内容
	 * 
	 * @param srcDirName
	 *            待复制目录的目录名
	 * @param destDirName
	 *            目标目录名
	 * @param overlay
	 *            如果目标目录存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectory(String srcDirName, String destDirName,
			boolean overlay) {
		// 判断源目录是否存在
		File srcDir = new File(srcDirName);
		if (!srcDir.exists()) {
			logger.debug("复制目录失败：源目录" + srcDirName + "不存在！");
			return false;
		} else if (!srcDir.isDirectory()) {
			logger.debug("复制目录失败：" + srcDirName + "不是目录！");
			return false;
		}

		// 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		File destDir = new File(destDirName);
		// 如果目标文件夹存在
		if (destDir.exists()) {
			// 如果允许覆盖则删除已存在的目标目录
			if (overlay) {
				new File(destDirName).delete();
			} else {
				logger.debug("复制目录失败：目的目录" + destDirName + "已存在！");
				return false;
			}
		} else {
			// 创建目的目录
			logger.debug("目的目录不存在，准备创建。。。");
			if (!destDir.mkdirs()) {
				logger.debug("复制目录失败：创建目的目录失败！");
				return false;
			}
		}

		boolean flag = true;
		File[] files = srcDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 复制文件
			if (files[i].isFile()) {
				flag = copyFile(files[i].getAbsolutePath(), destDirName
						+ files[i].getName(), overlay);
				if (!flag)
					break;
			} else if (files[i].isDirectory()) {
				flag = copyDirectory(files[i].getAbsolutePath(), destDirName
						+ files[i].getName(), overlay);
				if (!flag)
					break;
			}
		}
		if (!flag) {
			logger.debug("复制目录" + srcDirName + "至" + destDirName + "失败！");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 输入流转换成File
	 * @param ins
	 * @param file
	 * @throws java.io.IOException
	 */
	public static void inputstream2file(InputStream ins, File file)
			throws IOException {
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		ins.close();
	}
	
	/**
	 * 将二进制数据写入到磁盘
	 * @param bytes 字节流
	 */
	public static void writeBytesToDisk(byte[] bytes, String Url){
		try {
			File file = new File(Url);
			FileOutputStream fops = new FileOutputStream(file);
			fops.write(bytes);
			fops.flush();
			fops.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建文件夹
	 * @param uri
	 */
	public static boolean createFolder(String uri){
		 File folder = new File(uri);
		 return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
	}
	
	public static void main(String[] args) {
//		String sourceFile = "D:\\courseM\\CIM";
//		try {
//			FileKit.zip("D:\\courseM\\CIM_zip.zip",sourceFile);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile4Folder(folderPath); //删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); //删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//删除指定文件夹下所有文件
	public static boolean delAllFile4Folder(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile4Folder(path + "/" + tempList[i]);//先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);//再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

}
