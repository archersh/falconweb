package cn.com.sailin.falconweb.publiccode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.com.sailin.falconweb.model.CallResult;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Code {

	public static SimpleDateFormat dtft = new SimpleDateFormat("yyyyMMddHHmmss");

	public static SimpleDateFormat dtftzk = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String ZeroDatetime = "19000101000000";

	public static String md5(String text) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = text.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return "";
		}
	}

	public static String callHttp(String sb) {
		try {
			URL url = new URL(sb);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String result = "";
			String str = "";
			while ((str = in.readLine()) != null) {
				result += str;
			}
			in.close();
			return result;
		} catch (Exception e) {
			return "";
		}
	}

	public static String getWeek(Date dt) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(dt);
		Integer week = cl.get(Calendar.DAY_OF_WEEK) - 1;
		if (week == 0)
			week = 7;
		return week.toString();
	}

	public static String getHttp(String url) {
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}

	public static String postUploadHttp(String url, Map<String, String> param) throws IOException {
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody body = RequestBody.create(mediaType, "user=" + param.get("user") + "&pass=" + param.get("pass")
				+ "&content=" + URLEncoder.encode(param.get("content"), "utf-8"));
		Request request = new Request.Builder()
				.url(url).post(body)
				.addHeader("Content-Type", "application/x-www-form-urlencoded").addHeader("Cache-Control", "no-cache")
				.addHeader("Postman-Token", "9e99036c-287e-425e-b6b1-b1630258ab7b").build();

		Response response = client.newCall(request).execute();

		return response.body().string();
	}

	public static String postHttp(String url, Map<String, String> param) {
		try {
			URL realUrl = new URL(url);// 创建连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // 设置请求方式
			connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
			connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
			connection.connect();
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
			out.append(JSONUtils.toJSONString(param));
			out.flush();
			out.close();

			int code = connection.getResponseCode();
			InputStream is = null;
			if (code == 200) {
				is = connection.getInputStream();
			} else {
				is = connection.getErrorStream();
			}

			// 读取响应
			int length = (int) connection.getContentLength();// 获取长度
			if (length != -1) {
				byte[] data = new byte[length];
				byte[] temp = new byte[512];
				int readLen = 0;
				int destPos = 0;
				while ((readLen = is.read(temp)) > 0) {
					System.arraycopy(temp, 0, data, destPos, readLen);
					destPos += readLen;
				}
				String result = new String(data, "UTF-8"); // utf-8编码
				return result;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error"; // 自定义错误信息
	}

	public static String resultSuccess() {

		CallResult result = new CallResult();

		result.setMSGID("0000");
		result.setMSGDESC("成功");
		result.setDATA(null);

		return JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);

	}

	public static String resultSuccess(Object data) {
		CallResult result = new CallResult();

		result.setMSGID("0000");
		result.setMSGDESC("成功");
		result.setDATA(data);

		return JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);
	}

	public static String resultError(String msgid, String msgdesc) {

		return resultError(msgid, msgdesc, null);
	}

	public static String resultError(String msgid, String msgdesc, Object data) {
		CallResult result = new CallResult();

		result.setMSGID(msgid);
		result.setMSGDESC(msgdesc);
		result.setDATA(data);

		return JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);

	}

	public static String getFieldVal(Map<String, Object> m, String key, String def) {
		if (m == null)
			return def;
		if (m.get(key) == null)
			return def;
		else
			return m.get(key).toString().trim();
	}

	public static String getFieldVal(JSONObject obj, String field, String def) {
		try {
			if (obj == null)
				return def;
			if (obj.get(field) == null)
				return def;
			else
				return obj.getString(field);
		} catch (Exception e) {
			e.printStackTrace();
			return def;
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMapval(HashMap<String, Object> hm, String key) {
		if (hm == null)
			return null;
		Object obj = hm.get(key);
		if (obj != null) {
			Map<String, Object> m = (Map<String, Object>) obj;
			return m;
		} else {
			return null;
		}
	}

	public static Calendar getToday() {
		Calendar now = Calendar.getInstance();

		Calendar result = Calendar.getInstance();

		result.clear();

		result.set(Calendar.YEAR, now.get(Calendar.YEAR));
		result.set(Calendar.MONTH, now.get(Calendar.MONTH));
		result.set(Calendar.DATE, now.get(Calendar.DATE));

		return result;
	}

	public static Map<String, String> getErroritem(String id, String errmsg) {
		Map<String, String> err = new HashMap<String, String>();
		err.put("ID", id);
		err.put("ERRMSG", errmsg);
		return err;
	}

	public static String getFillStr(String sSrc, String sSign, Integer iLen, String sFill) {
		String stemp = "";
		for (int i = 0; i < iLen - sSrc.length(); i++)
			stemp = stemp + sFill;
		if (iLen - sSrc.length() < 0) {
			return sSrc.substring(0, iLen);
		}
		if (sSign.toUpperCase().equals("L"))
			return stemp + sSrc;
		if (sSign.toUpperCase().equals("R"))
			return sSrc + stemp;
		return sSrc;
	}

}
