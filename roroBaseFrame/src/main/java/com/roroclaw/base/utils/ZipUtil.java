package com.roroclaw.base.utils;

import java.io.File;                        
import java.io.FileInputStream;             
import java.io.FileNotFoundException;       
import java.io.FileOutputStream;            
import java.io.IOException;                 
import java.io.InputStream;                 
import java.io.OutputStream;                
import java.util.ArrayList;                 
import java.util.Enumeration;               
import java.util.List;                      
                                            

import org.apache.tools.zip.ZipEntry;       
import org.apache.tools.zip.ZipFile;        
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZipUtil {
	
	protected static Logger logger = LoggerFactory.getLogger(ZipUtil.class);
	
	private static byte[] _byte = new byte[1024];
	
//	public static void main(String[] args) {
//		List<File> srcFiles = new ArrayList();
//		File file = new File("D:\\courseM\\CIM");
//		srcFiles.add(file);
//		zipFile("D:\\courseM\\CIM_zip.zip",srcFiles);
//	}
	
//	/**
//	 * 压缩文件或路径
//	 * 
//	 * @param zipUri
//	 *            压缩的目的地址
//	 * @param srcFiles
//	 *            压缩的源文件
//	 */
//	public static void zipFileDirect(String zipUri,String srcFileDirect){
//		List<File> srcFiles = new ArrayList();
//		File f = new File(srcFileDirect);
//		if (f.isDirectory()) {
//			File[] fl = f.listFiles();
//			for (int i = 0; i < fl.length; i++) {
//				zipFileDirect(zipUri, fl[i].getPath());
//			}
//		}else{
//			srcFiles.add(f);
//		}
//		zipFile(zipUri, srcFiles);
//		
//	}

	/**
	 * 压缩文件或路径
	 * 
	 * @param zip
	 *            压缩的目的地址
	 * @param srcFiles
	 *            压缩的源文件
	 */
	public static void zipFile(String zip, List<File> srcFiles) {
		try {
			if (zip.endsWith(".zip") || zip.endsWith(".ZIP")) {
				ZipOutputStream _zipOut = new ZipOutputStream(
						new FileOutputStream(new File(zip)));
				_zipOut.setEncoding("GBK");
				for (File _f : srcFiles) {
					handlerFile(zip, _zipOut, _f, "");
				}
				_zipOut.close();
			} else {
				logger.debug("target file[" + zip
						+ "] is not .zip type file");
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	/**
	 * 
	 * @param zip
	 *            压缩的目的地址
	 * @param zipOut
	 * @param srcFile
	 *            被压缩的文件信息
	 * @param path
	 *            在zip中的相对路径
	 * @throws java.io.IOException
	 */
	private static void handlerFile(String zip, ZipOutputStream zipOut,
			File srcFile, String path) throws IOException {
//		logger.debug(" begin to compression file[" + srcFile.getName()
//				+ "]");
		if (!"".equals(path) && !path.endsWith(File.separator)) {
			path += File.separator;
		}
		if (!srcFile.getPath().equals(zip)) {
			if (srcFile.isDirectory()) {
				File[] _files = srcFile.listFiles();
				if (_files.length == 0) {
					zipOut.putNextEntry(new ZipEntry(path + srcFile.getName()
							+ File.separator));
					zipOut.closeEntry();
				} else {
					for (File _f : _files) {
						handlerFile(zip, zipOut, _f, path + srcFile.getName());
					}
				}
			} else {
				InputStream _in = new FileInputStream(srcFile);
				zipOut.putNextEntry(new ZipEntry(path + srcFile.getName()));
				int len = 0;
				while ((len = _in.read(_byte)) > 0) {
					zipOut.write(_byte, 0, len);
				}
				_in.close();
				zipOut.closeEntry();
			}
		}
	}

	/**
	 * 解压缩ZIP文件，将ZIP文件里的内容解压到targetDIR目录下
	 * 
	 * @param zipName
	 *            待解压缩的ZIP文件名
	 * @param targetBaseDirName
	 *            目标目录
	 */
	public static List<File> upzipFile(String zipPath, String descDir) {
		return upzipFile(new File(zipPath), descDir);
	}

	/**
	 * 对.zip文件进行解压缩
	 * 
	 * @param zipFile
	 *            解压缩文件
	 * @param descDir
	 *            压缩的目标地址，如：D:\\测试 或 /mnt/d/测试
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<File> upzipFile(File zipFile, String descDir) {
		List<File> _list = new ArrayList<File>();
		try {
			ZipFile _zipFile = new ZipFile(zipFile, "GBK");
			for (Enumeration entries = _zipFile.getEntries(); entries
					.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				File _file = new File(descDir + File.separator
						+ entry.getName());
				if (entry.isDirectory()) {
					_file.mkdirs();
				} else {
					File _parent = _file.getParentFile();
					if (!_parent.exists()) {
						_parent.mkdirs();
					}
					InputStream _in = _zipFile.getInputStream(entry);
					OutputStream _out = new FileOutputStream(_file);
					int len = 0;
					while ((len = _in.read(_byte)) > 0) {
						_out.write(_byte, 0, len);
					}
					_in.close();
					_out.flush();
					_out.close();
					_list.add(_file);
				}
			}
		} catch (IOException e) {
		}
		return _list;
	}

	/**
	 * 对临时生成的文件夹和文件夹下的文件进行删除
	 */
	public static void deletefile(String delpath) {
		try {
			File file = new File(delpath);
			if (!file.isDirectory()) {
				file.delete();
			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File delfile = new File(delpath + File.separator
							+ filelist[i]);
					if (!delfile.isDirectory()) {
						delfile.delete();
					} else if (delfile.isDirectory()) {
						deletefile(delpath + File.separator + filelist[i]);
					}
				}
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
