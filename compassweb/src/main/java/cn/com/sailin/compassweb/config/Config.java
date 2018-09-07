package cn.com.sailin.compassweb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("config")
public class Config {
	
	//四方平台的url
	private String sfurl;
	
	//四方平台微信url
	private String sfwxurl;
	
	//易港通webserivce
	private String ygturl;

	public String getSfurl() {
		return sfurl;
	}

	public void setSfurl(String sfurl) {
		this.sfurl = sfurl;
	}

	public String getSfwxurl() {
		return sfwxurl;
	}

	public void setSfwxurl(String sfwxurl) {
		this.sfwxurl = sfwxurl;
	}

	public String getYgturl() {
		return ygturl;
	}

	public void setYgturl(String ygturl) {
		this.ygturl = ygturl;
	}

}
