package cn.com.sailin.falconweb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("schedle")
public class SchedleConfig {
	
	private Boolean importwkds;
	
	private Boolean importwkdsbyweek;
	
	private Boolean housekeeping;
	
	private Boolean uploadwkds;
	
	private Boolean importattenddata;
	
	private Boolean uploadinfo;
	
	private Boolean updatewkeerattendtime;
	
	private Boolean updateyftoken;

	public Boolean getImportwkds() {
		return importwkds;
	}

	public void setImportwkds(Boolean importwkds) {
		this.importwkds = importwkds;
	}

	public Boolean getImportwkdsbyweek() {
		return importwkdsbyweek;
	}

	public void setImportwkdsbyweek(Boolean importwkdsbyweek) {
		this.importwkdsbyweek = importwkdsbyweek;
	}

	public Boolean getHousekeeping() {
		return housekeeping;
	}

	public void setHousekeeping(Boolean housekeeping) {
		this.housekeeping = housekeeping;
	}

	public Boolean getUploadwkds() {
		return uploadwkds;
	}

	public void setUploadwkds(Boolean uploadwkds) {
		this.uploadwkds = uploadwkds;
	}

	public Boolean getImportattenddata() {
		return importattenddata;
	}

	public void setImportattenddata(Boolean importattenddata) {
		this.importattenddata = importattenddata;
	}

	public Boolean getUploadinfo() {
		return uploadinfo;
	}

	public void setUploadinfo(Boolean uploadinfo) {
		this.uploadinfo = uploadinfo;
	}

	public Boolean getUpdatewkeerattendtime() {
		return updatewkeerattendtime;
	}

	public void setUpdatewkeerattendtime(Boolean updatewkeerattendtime) {
		this.updatewkeerattendtime = updatewkeerattendtime;
	}

	public Boolean getUpdateyftoken() {
		return updateyftoken;
	}

	public void setUpdateyftoken(Boolean updateyftoken) {
		this.updateyftoken = updateyftoken;
	}

}
