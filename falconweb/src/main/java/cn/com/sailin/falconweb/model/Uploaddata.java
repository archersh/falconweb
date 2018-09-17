package cn.com.sailin.falconweb.model;

import java.util.List;
import java.util.Map;

import cn.com.sailin.falconweb.dao.Data;
import cn.com.sailin.falconweb.publiccode.Code;

public class Uploaddata {
	
	private String user;
	
	private String pass;
	
	private String content;
	
	private String url;
	
	private static String host="http://api.pm361.cn/";
	
	public static Uploaddata buildObject(String apcd,String bscd,String method,Data data) {
		
		Uploaddata up=new Uploaddata();

		List<Map<String,Object>> conf=data.qryUploadconfig(apcd, bscd, method);
		
		if (conf.size()>0)
		{
			up.setUser(Code.getFieldVal(conf.get(0), "USER", ""));
			up.setPass(Code.getFieldVal(conf.get(0), "PASS", ""));
			up.setUrl(host + Code.getFieldVal(conf.get(0), "URL", ""));
		}
		
		return up;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
