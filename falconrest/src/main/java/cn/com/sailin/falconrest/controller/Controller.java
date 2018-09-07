package cn.com.sailin.falconrest.controller;

import java.io.File;

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
		String fileName="";
		try {
			File uploadPath=new File(config.getUploadpath());
			if (!uploadPath.exists()||uploadPath.isFile()) {
				resetDir(uploadPath);
			}
			fileName=file.getOriginalFilename();
			if (fileName==null||fileName.isEmpty()) {
				return "Filename is null or empty";
			}
			File targetFile = new File(config.getUploadpath(),fileName);
			file.transferTo(targetFile);
			
			return "OK";
			
		}catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
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
