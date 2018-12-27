package cn.com.sailin.falconrest.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import cn.com.sailin.falconrest.config.Config;

@RestController
public class Controller {

	@Autowired
	private Config config;

	@RequestMapping("/rest/hello")
	public String hello() {
		return "hello";
	}

	@RequestMapping(value = "/rest/invoke", method = RequestMethod.GET)
	public String invoke(@RequestParam(value = "userid", required = false) String userid,
			@RequestParam(value = "timestamp", required = false) String timestamp,
			@RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "method", required = false) String method,
			@RequestParam(value = "applydata", required = false) String applydata) {

		try {
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			Client client = dcf.createClient(config.getFalconweb());
			Object[] objects = new Object[0];
			objects = client.invoke("webinvoke", userid, timestamp, token, method, applydata);
			return objects[0].toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@RequestMapping(value = "/rest/upload", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String upload(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		String fileName = "";
		try {
			File uploadPath = new File(config.getUploadpath());
			if (!uploadPath.exists() || uploadPath.isFile()) {
				resetDir(uploadPath);
			}
			fileName = file.getOriginalFilename();
			if (fileName == null || fileName.isEmpty()) {
				return "Filename is null or empty";
			}
			File targetFile = new File(config.getUploadpath(), fileName);
			file.transferTo(targetFile);

			return "OK";

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	private String inputStreamToString(InputStream input) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;

		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		String str = sb.toString();
		return str;
	}

	@RequestMapping(value = "/rest/callback", method = RequestMethod.POST)
	public @ResponseBody String yfcallback(HttpServletRequest request) throws IOException {

		String str = inputStreamToString(request.getInputStream());
		JSONObject jo = JSONObject.parseObject(str);

		return invoke("SYS", "", "", "yfattendsubmit", jo.toJSONString());

	}

	@RequestMapping(value = "/rest/getcallback")
	public @ResponseBody String yfgetcallback(HttpServletRequest request) throws IOException {

		JSONObject jo = new JSONObject();

		if (request.getParameter("showTime") != null && !request.getParameter("showTime").trim().equals("")) {

			jo.put("photoUrl", request.getParameter("photoUrl"));
			jo.put("personGuid", request.getParameter("personGuid"));
			jo.put("deviceKey", request.getParameter("deviceKey"));
			jo.put("showTime", request.getParameter("showTime"));

			String result = invoke("SYS", "", "", "yfattendsubmit", jo.toJSONString());
			JSONObject jr = JSONObject.parseObject(result);
			if (jr.getString("MSGID").equals("0000")) {
				return null;
			} else {
				return result;
			}
		}
		return null;
	}

	private synchronized static void resetDir(File dir) {
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (dir.isFile()) {
			dir.delete();
			dir.mkdirs();
		}
	}

}
