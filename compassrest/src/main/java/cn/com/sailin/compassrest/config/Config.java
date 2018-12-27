package cn.com.sailin.compassrest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("options")
public class Config {
	
	private String url;
	
	private String uploadpath;
	
	private String updatefile;
	
	private String ygturl;
	
	

	public String getYgturl() {
		return ygturl;
	}

	public void setYgturl(String ygturl) {
		this.ygturl = ygturl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUploadpath() {
		return uploadpath;
	}

	public void setUploadpath(String uploadpath) {
		this.uploadpath = uploadpath;
	}

	public String getUpdatefile() {
		return updatefile;
	}

	public void setUpdatefile(String updatefile) {
		this.updatefile = updatefile;
	}

}
