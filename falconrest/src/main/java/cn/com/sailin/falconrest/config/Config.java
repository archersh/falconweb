package cn.com.sailin.falconrest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("options")
public class Config {
	
	private String falconweb;
	
	private String uploadpath;

	public String getFalconweb() {
		return falconweb;
	}

	public void setFalconweb(String falconweb) {
		this.falconweb = falconweb;
	}

	public String getUploadpath() {
		return uploadpath;
	}

	public void setUploadpath(String uploadpath) {
		this.uploadpath = uploadpath;
	}

}
