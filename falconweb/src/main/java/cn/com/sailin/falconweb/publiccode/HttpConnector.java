package cn.com.sailin.falconweb.publiccode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpConnector {
	
	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";

	public static int connectTimeout = 60000;

	public static int readTimeout = 60000;

	public static String doPostCookie(String url, Map<String, String> params, Map<String, String> cookies)
			throws IOException {
		return doPost(url, params, cookies, DEFAULT_CHARSET, connectTimeout, readTimeout);
	}

	public static String doPostCookie(String url, Map<String, String> params, Map<String, String> cookies,
			int connectTimeout, int readTimeout) throws IOException {
		return doPost(url, params, cookies, DEFAULT_CHARSET, connectTimeout, readTimeout);
	}

	public static String doPost(String url, Map<String, String> params) throws IOException {
		return doPost(url, params, null, DEFAULT_CHARSET, connectTimeout, readTimeout);
	}

	/**
	 * 执行HTTP POST请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, Map<String, String> params, int connectTimeout, int readTimeout)
			throws IOException {
		return doPost(url, params, null, DEFAULT_CHARSET, connectTimeout, readTimeout);
	}

	/**
	 * 执行HTTP POST请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param charset
	 *            字符集，如UTF-8, GBK, GB2312
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, Map<String, String> params, Map<String, String> cookies, String charset,
			int connectTimeout, int readTimeout) throws IOException {
		String ctype = "application/x-www-form-urlencoded;charset=" + charset;
		String query = buildQuery(params, charset);
		byte[] content = {};
		if (query != null) {
			content = query.getBytes(charset);
		}
		return doPost(url, ctype, content, cookies, connectTimeout, readTimeout);
	}

	/**
	 * 执行HTTP POST请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param ctype
	 *            请求类型
	 * @param content
	 *            请求字节数组
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, String ctype, byte[] content, Map<String, String> cookies,
			int connectTimeout, int readTimeout) throws IOException {
		HttpURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;
		try {
			try {
				conn = getConnection(new URL(url), cookies, METHOD_POST, ctype);
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
			} catch (IOException e) {
				throw e;
			}
			try {

				out = conn.getOutputStream();
				out.write(content);
				rsp = getResponseAsString(conn);
			} catch (IOException e) {
				throw e;
			}

		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}

	/**
	 * 执行带文件上传的HTTP POST请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param textParams
	 *            文本请求参数
	 * @param fileParams
	 *            文件请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */

	private static HttpURLConnection getConnection(URL url, Map<String, String> cookies, String method, String ctype)
			throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (cookies != null) {
			for (String key : cookies.keySet()) {
				String value = cookies.get(key);

				conn.addRequestProperty(key, value);
			}
		}
		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		if (ctype != null) {
			conn.setRequestProperty("Content-Type", ctype);
		}
		return conn;
	}

	protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
		String charset = getResponseCharset(conn.getContentType());
		InputStream es = conn.getErrorStream();
		if (es == null) {
			return getStreamAsString(conn.getInputStream(), charset);
		} else {
			String msg = getStreamAsString(es, charset);
			if (isEmpty(msg)) {
				throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
			} else {
				throw new IOException(msg);
			}
		}
	}

	private static String getResponseCharset(String ctype) {
		String charset = DEFAULT_CHARSET;

		if (!isEmpty(ctype)) {
			String[] params = ctype.split(";");
			for (int i = 0; i < params.length; i++) {
				String param = params[i];
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (!isEmpty(pair[1])) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}

		return charset;
	}

	private static String getStreamAsString(InputStream stream, String charset) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
			StringWriter writer = new StringWriter();

			char[] chars = new char[256];
			int count = 0;
			while ((count = reader.read(chars)) > 0) {
				writer.write(chars, 0, count);
			}

			return writer.toString();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	public static String buildQuery(Map<String, String> params, String charset) throws IOException {
		if (params == null || params.isEmpty()) {
			return null;
		}

		StringBuffer query = new StringBuffer();
		// Set entries = params.entrySet();
		boolean hasParam = false;

		Iterator it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			String name = (String) entry.getKey();
			String value = (String) entry.getValue();
			// 忽略参数名或参数值为空的参数
			if (isNotEmpty(name)) {
				if (value == null) {
					value = "";
				}
				if (hasParam) {
					query.append("&");
				} else {
					hasParam = true;
				}

				query.append(name).append("=").append(URLEncoder.encode(value, charset));
			}
		}

		return query.toString();
	}

	private static URL buildGetUrl(String strUrl, String query) throws IOException {
		URL url = new URL(strUrl);
		if (isEmpty(query)) {
			return url;
		}

		if (isEmpty(url.getQuery())) {
			if (strUrl.endsWith("?")) {
				strUrl = strUrl + query;
			} else {
				strUrl = strUrl + "?" + query;
			}
		} else {
			if (strUrl.endsWith("&")) {
				strUrl = strUrl + query;
			} else {
				strUrl = strUrl + "&" + query;
			}
		}

		return new URL(strUrl);
	}

	private static Map<String, String> getParamsFromUrl(String url) {
		Map<String,String> map = null;
		if (url != null && url.indexOf('?') != -1) {
			map = splitUrlQuery(url.substring(url.indexOf('?') + 1));
		}
		if (map == null) {
			map = new HashMap<String,String>();
		}
		return map;
	}

	/**
	 * 从URL中提取所有的参数。
	 * 
	 * @param query
	 *            URL地址
	 * @return 参数映射
	 */
	public static Map<String, String> splitUrlQuery(String query) {
		Map<String,String> result = new HashMap<String,String>();

		String[] pairs = query.split("&");
		if (pairs != null && pairs.length > 0) {
			for (int i = 0; i < pairs.length; i++) {
				String pair = pairs[i];
				String[] param = pair.split("=", 2);
				if (param != null && param.length == 2) {
					result.put(param[0], param[1]);
				}
			}
		}

		return result;
	}

	/**
	 * 执行HTTP GET请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doGet(String url, Map params) throws IOException {
		return doGet(url, params, DEFAULT_CHARSET, connectTimeout, readTimeout);
	}

	public static String doGet(String url, Map<String, String> params, String charset) throws IOException {
		return doGet(url, params, charset, connectTimeout, readTimeout);
	}

	/**
	 * 执行HTTP GET请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param charset
	 *            字符集，如UTF-8, GBK, GB2312
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doGet(String url, Map<String, String> params, String charset, int connectTimeout,
			int readTimeout) throws IOException {
		HttpURLConnection conn = null;
		String rsp = null;

		try {
			String ctype = "application/x-www-form-urlencoded;charset=" + charset;
			String query = buildQuery(params, charset);
			try {
				conn = getConnection(buildGetUrl(url, query), null, METHOD_GET, ctype);
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
			} catch (IOException e) {
				// Map<String, String> map=getParamsFromUrl(url);
				// TaobaoLogger.logCommError(e,
				// url,map.get("app_key"),map.get("method"), params);
				throw e;
			}

			try {
				rsp = getResponseAsString(conn);
			} catch (IOException e) {
				// Map<String, String> map=getParamsFromUrl(url);
				// TaobaoLogger.logCommError(e,
				// conn,map.get("app_key"),map.get("method"), params);
				throw e;
			}

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}

	private static byte[] getFileEntry(String fieldName, String fileName, String mimeType, String charset)
			throws IOException {
		StringBuilder entry = new StringBuilder();
		entry.append("Content-Disposition:form-data;name=\"");
		entry.append(fieldName);
		entry.append("\";filename=\"");
		entry.append(fileName);
		entry.append("\"\r\nContent-Type:");
		entry.append(mimeType);
		entry.append("\r\n\r\n");
		return entry.toString().getBytes(charset);
	}

	public static boolean isEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(value.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}

}
