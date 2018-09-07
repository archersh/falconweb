package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Hisbsst {

	private String APCD;//接入点
	
	private String BSCD;//工地代码
	
	private String BSDS;//工地名称
	
	private String BKCD;//银行代码
	
	private String BKDS;//银行名称
	
	private String CCCD;//承建公司代码
	
	private String CCDS;//承建公司名称
	
	private String NOBL;//余额不足月份数
	
	private String SUMQX;//欠薪人次
	
	private String SUMCFBL;//超发比例月份数
	
	private String INDT;//是否撤除

	@JSONField(name="APCD")
	public String getAPCD() {
		return APCD;
	}

	public void setAPCD(String aPCD) {
		APCD = aPCD;
	}

	@JSONField(name="BSCD")
	public String getBSCD() {
		return BSCD;
	}

	public void setBSCD(String bSCD) {
		BSCD = bSCD;
	}

	@JSONField(name="BSDS")
	public String getBSDS() {
		return BSDS;
	}

	public void setBSDS(String bSDS) {
		BSDS = bSDS;
	}

	@JSONField(name="BKCD")
	public String getBKCD() {
		return BKCD;
	}

	public void setBKCD(String bKCD) {
		BKCD = bKCD;
	}

	@JSONField(name="BKDS")
	public String getBKDS() {
		return BKDS;
	}

	public void setBKDS(String bKDS) {
		BKDS = bKDS;
	}

	@JSONField(name="CCCD")
	public String getCCCD() {
		return CCCD;
	}

	public void setCCCD(String cCCD) {
		CCCD = cCCD;
	}

	@JSONField(name="CCDS")
	public String getCCDS() {
		return CCDS;
	}

	public void setCCDS(String cCDS) {
		CCDS = cCDS;
	}

	@JSONField(name="NOBL")
	public String getNOBL() {
		return NOBL;
	}

	public void setNOBL(String nOBL) {
		NOBL = nOBL;
	}

	@JSONField(name="SUMQX")
	public String getSUMQX() {
		return SUMQX;
	}

	public void setSUMQX(String sUMQX) {
		SUMQX = sUMQX;
	}

	@JSONField(name="SUMCFBL")
	public String getSUMCFBL() {
		return SUMCFBL;
	}

	public void setSUMCFBL(String sUMCFBL) {
		SUMCFBL = sUMCFBL;
	}

	@JSONField(name="INDT")
	public String getINDT() {
		return INDT;
	}

	public void setINDT(String iNDT) {
		INDT = iNDT;
	}
	
}
