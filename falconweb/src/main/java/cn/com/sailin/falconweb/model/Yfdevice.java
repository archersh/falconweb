package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Yfdevice {

	private String DEVICEKEY;
	
	private String SN;
	
	private String DEVICENAME;
	
	private String APCD;
	
	private String BSCD;
	
	private String ININOUT;
	
	private String DEVICETYPE;
	
	private String OPUR;
	
	@JSONField(name="DEVICEKEY")
	public String getDEVICEKEY() {
		return DEVICEKEY;
	}

	public void setDEVICEKEY(String dEVICEKEY) {
		DEVICEKEY = dEVICEKEY;
	}

	@JSONField(name="SN")
	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
	}

	@JSONField(name="DEVICENAME")
	public String getDEVICENAME() {
		return DEVICENAME;
	}

	public void setDEVICENAME(String dEVICENAME) {
		DEVICENAME = dEVICENAME;
	}

	@JSONField(name="APCD")
	public String getAPCD() {
		return APCD;
	}

	public void setAPCD(String aPCD) {
		APCD = aPCD;
	}

	@JSONField(name="DEVICETYPE")
	public String getDEVICETYPE() {
		return DEVICETYPE;
	}

	public void setDEVICETYPE(String dEVICETYPE) {
		DEVICETYPE = dEVICETYPE;
	}

	@JSONField(name="BSCD")
	public String getBSCD() {
		return BSCD;
	}

	public void setBSCD(String bSCD) {
		BSCD = bSCD;
	}

	@JSONField(name="ININOUT")
	public String getININOUT() {
		return ININOUT;
	}

	public void setININOUT(String iNINOUT) {
		ININOUT = iNINOUT;
	}

	@JSONField(name="OPUR")
	public String getOPUR() {
		return OPUR;
	}

	public void setOPUR(String oPUR) {
		OPUR = oPUR;
	}

}
