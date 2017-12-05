package com.roroclaw.base.handler;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roroclaw.base.bean.PageBean;
import com.roroclaw.base.utils.JsonKit;

/**
 * http 请求帮助类
 * 
 * @author dxz
 * 
 */
public class HttpClientHandler {
	private static Logger logger = LoggerFactory
			.getLogger(HttpClientHandler.class);

	public final static String METHOD_GET = "GET";
	public final static String METHOD_PUT = "PUT";
	public final static String METHOD_DELETE = "DELETE";
	public final static String METHOD_POST = "POST";
	public final static String MAP_KEY_COOKINFO = "cookInfo";
	public final static String MAP_KEY_HTMLSTR = "htmlStr";
	public final static String SYS_DEFAULT_ENCODE = "utf-8";

	public final static String MEDIA_INFO_CONTENTTYPE = "contentType";
	public final static String MEDIA_INFO_FILENAME = "fileName";
	public final static String MEDIA_INFO_STREAMDATA = "streamData";

	/**
	 * 发起请求
	 * 
	 * @param serviceUrl
	 * @param parameter
	 * @param restMethod
	 * @return
	 */
	public static String sendRequest(String serviceUrl, String parameter,
			String restMethod) {
		String resultStr = null;
		HttpURLConnection urlConnection = null;
		try {
			logger.debug("请求连接="+serviceUrl);
			logger.debug("请求参数="+parameter);
			URL url = new URL(serviceUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
			initRequestParams(urlConnection, parameter, restMethod);
			// 请求方法为GET时执行
			InputStream in = urlConnection.getInputStream();
			resultStr = IOUtils.toString(in, SYS_DEFAULT_ENCODE);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			urlConnection.disconnect();
		}

		return resultStr;
	}

	/**
	 * 发起请求
	 * 
	 * @param serviceUrl
	 * @param parameter
	 * @param restMethod
	 * @return
	 */
	public static String sendRequest(String serviceUrl, Map<String,Object> paramMap,
			String restMethod) {
		String resultStr = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(serviceUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
			// 组装参数
			String parameter = "";
			for (String dataKey : paramMap.keySet()) {
				String val = String.valueOf(paramMap.get(dataKey));
				parameter += dataKey+"="+val+"&";
			}
			parameter = !"".equals(parameter)?parameter.substring(0,parameter.length()-1):"";
			initRequestParams(urlConnection,parameter , restMethod);
			// 请求方法为GET时执行
			InputStream in = urlConnection.getInputStream();
			resultStr = IOUtils.toString(in, SYS_DEFAULT_ENCODE);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			urlConnection.disconnect();
		}

		return resultStr;
	}

	/**
	 * 发起请求
	 * 
	 * @param serviceUrl
	 * @param parameter
	 * @param restMethod
	 * @return
	 * @throws Exception
	 */
	public static String sendRequest4Json(String serviceUrl, String jsonStr)
			throws Exception {
		String resStr = null;
		try {
			// 创建连接
			URL url = new URL(serviceUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.connect();

			// POST请求
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());
			out.write(jsonStr.getBytes(SYS_DEFAULT_ENCODE));
			out.flush();
			out.close();

			// 读取响应
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String lines;
			StringBuffer sb = new StringBuffer("");
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), SYS_DEFAULT_ENCODE);
				sb.append(lines);
			}
			reader.close();
			resStr = sb.toString();
			logger.debug("发送Json请求返回=" + resStr);
			// 断开连接
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return resStr;
	}

	/**
	 * 发起请求
	 * 
	 * @param serviceUrl
	 * @return
	 * @throws Exception
	 */
	public static String uploadMedia(String serviceUrl, String filepath)
			throws Exception {
		String resStr = null;
		try {
			String boundary = "---------------------------123821742118716";
			File file = new File(filepath);
			String filename = file.getName();
			// 创建连接
			URL url = new URL(serviceUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			// connection.setDoOutput(true);
			// connection.setDoInput(true);
			// connection.setUseCaches(false);
			// connection.setRequestMethod("POST");
			// connection.setRequestProperty("connection", "Keep-Alive");
			// connection.setRequestProperty("User-Agent",
			// "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			// connection.setRequestProperty("Content-Type",
			// "multipart/form-data;; boundary=----"+boundary);
			// connection.setRequestProperty("Charsert", "UTF-8");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection
					.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);
			// connection.connect();

			// POST请求
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());
			String contentType = "application/octet-stream";

			// 写文件头说明
			StringBuffer strBuf = new StringBuffer();
			strBuf.append("\r\n").append("--").append(boundary).append("\r\n");
			strBuf.append("Content-Disposition: form-data; name=\"adfile\"; filename=\""
					+ filename + "\"\r\n");
			strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
			out.write(strBuf.toString().getBytes());

			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			byte[] endData = ("\r\n--" + boundary + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取响应
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String lines;
			StringBuffer sb = new StringBuffer("");
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), SYS_DEFAULT_ENCODE);
				sb.append(lines);
			}
			reader.close();
			resStr = sb.toString();
			logger.debug("发送Json请求返回=" + resStr);
			// 断开连接
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return resStr;
	}

	// /**
	// * 利用HttpClient上传
	// * @param serviceUrl
	// * @param filepath
	// * @return
	// * @throws Exception
	// */
	// public static String uploadMediaByHttpClient(String serviceUrl, String
	// filepath)
	// throws Exception {
	// String resStr = null;
	// HttpClient httpclient = new DefaultHttpClient();
	// try {
	// HttpPost httppost = new HttpPost(serviceUrl);
	// FileBody bin = new FileBody(new File(filepath));
	// MultipartEntity reqEntity = new MultipartEntity();
	// reqEntity.addPart("media", bin);// upload为请求后台的File upload;属性
	// httppost.setEntity(reqEntity);
	// HttpResponse response = httpclient.execute(httppost);
	// int statusCode = response.getStatusLine().getStatusCode();
	// if (statusCode == HttpStatus.SC_OK) {
	// HttpEntity resEntity = response.getEntity();
	// resStr = new String(EntityUtils.toString(resEntity));
	// EntityUtils.consume(resEntity);
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// throw e;
	// } finally {
	// try {
	// httpclient.getConnectionManager().shutdown();
	// } catch (Exception ignore) {
	//
	// }
	// }
	// return resStr;
	// }

	/**
	 * 发起请求
	 * 
	 * @param serviceUrl
	 * @param parameter
	 * @param restMethod
	 * @return
	 */
	public static Map sendRequestGetMedia(String serviceUrl, String parameter,
			String restMethod) {
		Map infoMap = null;
		try {
			URL url = new URL(serviceUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			logger.debug("访问url=" + url);
			initRequestParams(urlConnection, parameter, restMethod);
			infoMap = getMediaInfo(urlConnection);
			// 请求方法为GET时执行
			InputStream in = urlConnection.getInputStream();
			infoMap.put(MEDIA_INFO_STREAMDATA, in);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return infoMap;
	}

	/**
	 * 初始化request模拟参数
	 * 
	 * @throws java.io.IOException
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static void initRequestParams(HttpURLConnection con,
			String parameter, String restMethod)
			throws UnsupportedEncodingException, IOException {
		con.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)");
		// 设置请求接受语言
		con.setRequestProperty("Accept-Language", "zh-cn,zh,en;q=0.5");
		// 浏览器可以接受的字符编码集
		con.setRequestProperty("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
		// 如果请求方法为PUT,POST和DELETE设置DoOutput为真
		if (restMethod != null
				&& !HttpClientHandler.METHOD_GET.equals(restMethod)) {
			con.setRequestMethod(restMethod);
			con.setDoOutput(true);
			// 请求方法为PUT或POST时执行,往请求体重写参数
			if (!HttpClientHandler.METHOD_DELETE.equals(restMethod)) {
				OutputStream os = con.getOutputStream();
				if (parameter != null) {
					os.write(parameter.getBytes(SYS_DEFAULT_ENCODE));
					os.flush();
					os.close();
				}
			}
		}
	}

	private static String getHeaderCharset(HttpURLConnection urlConnection) {
		String charset = null;
		Map<String, List<String>> map = urlConnection.getHeaderFields();
		Set<String> keys = map.keySet();
		Iterator<String> iterator = keys.iterator();

		// 遍历,查找字符编码
		String key = null;
		String tmp = null;
		while (iterator.hasNext()) {
			key = iterator.next();
			tmp = map.get(key).toString().toLowerCase();
			// 获取content-type charset
			if (key != null && key.equals("Content-Type")) {
				int m = tmp.indexOf("charset=");
				if (m != -1) {
					charset = tmp.substring(m + 8).replace("]", "");
					break;
				}
			}
		}
		return charset;
	}

	private static Map getMediaInfo(HttpURLConnection urlConnection) {
		Map infoMap = new HashMap();
		Map<String, List<String>> map = urlConnection.getHeaderFields();
		Set<String> keys = map.keySet();
		Iterator<String> iterator = keys.iterator();

		// 遍历,查找字符编码
		String key = null;
		String tmp = null;
		while (iterator.hasNext()) {
			key = iterator.next();
			tmp = map.get(key).toString().toLowerCase();
			// 获取content-type charset
			if (key != null && key.toLowerCase().equals("content-type")) {
				logger.debug("Content-Type=" + tmp);
				infoMap.put(MEDIA_INFO_CONTENTTYPE, tmp);
			}

			if (key != null && key.toLowerCase().equals("content-disposition")) {
				logger.debug("Content-disposition=" + tmp);
				int m = tmp.indexOf("filename=");
				if (m != -1) {
					String filename = tmp.substring(m + 9);
					infoMap.put(MEDIA_INFO_FILENAME, filename);
				}
			}
		}
		return infoMap;
	}

	/**
	 * 处理接口返回信息,获取数据对象map
	 * 
	 * @return
	 * @throws com.roroclaw.base.handler.BizException
	 * @throws java.io.IOException
	 * @throws org.codehaus.jackson.map.JsonMappingException
	 * @throws org.codehaus.jackson.JsonParseException
	 */
	public static Object processJsonRes(String jsonStr) throws BizException,
			JsonParseException, JsonMappingException, IOException {
		Object resObj = null;
		Map jsonMap = JsonKit.json2Map(jsonStr);
		String status = (String) jsonMap.get("status");
		if ("1".equals(status)) {
			resObj = jsonMap.get("object");
		} else {
			String describe = (String) jsonMap.get("describe");
			throw new BizException(describe);
		}
		return resObj;
	}

	/**
	 * 处理接口返回信息,获取数据对象Pagebean
	 * 
	 * @return
	 * @throws com.roroclaw.base.handler.BizException
	 * @throws java.io.IOException
	 * @throws org.codehaus.jackson.map.JsonMappingException
	 * @throws org.codehaus.jackson.JsonParseException
	 */
	public static PageBean processJsonRes4PageBean(String jsonStr)
			throws BizException, JsonParseException, JsonMappingException,
			IOException {
		PageBean pageBean = new PageBean();
		Map jsonMap = JsonKit.json2Map(jsonStr);
		String status = (String) jsonMap.get("status");
		if ("1".equals(status)) {
			Map resMap = (Map) jsonMap.get("object");
			pageBean.setCurPage((Integer) resMap.get("curPage"));
			pageBean.setData((List) resMap.get("data"));
			pageBean.setTotalPage((Integer) resMap.get("totalPage"));
			pageBean.setTotalRecords((Integer) resMap.get("totalRecords"));
		} else {
			String describe = (String) jsonMap.get("describe");
			throw new BizException(describe);
		}
		return pageBean;
	}
}
