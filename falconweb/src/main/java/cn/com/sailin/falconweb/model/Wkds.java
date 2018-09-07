package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Wkds {

	private String APCD;// 接入点代码

	private String BSCD;// 工地代码

	private String MONTH;// 发放月份

	private String IDCDNO;// 身份证号码

	private String NAME;// 姓名

	private String POST;// 岗位

	private String WKDS;// 出勤天数

	private String LCCD;// 分包公司

	private String DT;// 采集时间

	private String UR;// 采集用户

	private String INFF = "N";// 是否发薪

	private String ACPY;// 发放金额
	
	private String INONEMON;//是否是一个月内员工
	
	
	@JSONField(name="INONEMON")
	public String getINONEMON() {
		return INONEMON;
	}

	public void setINONEMON(String iNONEMON) {
		INONEMON = iNONEMON;
	}

	@JSONField(name = "LCCD")
	public String getLCCD() {
		return LCCD;
	}

	public void setLCCD(String lCCD) {
		LCCD = lCCD;
	}

	@JSONField(name = "APCD")
	public String getAPCD() {
		return APCD;
	}

	public void setAPCD(String aPCD) {
		APCD = aPCD;
	}

	@JSONField(name = "BSCD")
	public String getBSCD() {
		return BSCD;
	}

	public void setBSCD(String bSCD) {
		BSCD = bSCD;
	}

	@JSONField(name = "MONTH")
	public String getMONTH() {
		return MONTH;
	}

	public void setMONTH(String mONTH) {
		MONTH = mONTH;
	}

	@JSONField(name = "IDCDNO")
	public String getIDCDNO() {
		return IDCDNO;
	}

	public void setIDCDNO(String iDCDNO) {
		IDCDNO = iDCDNO;
	}

	@JSONField(name = "NAME")
	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	@JSONField(name = "POST")
	public String getPOST() {
		return POST;
	}

	public void setPOST(String pOST) {
		POST = pOST;
	}

	@JSONField(name = "WKDS")
	public String getWKDS() {
		return WKDS;
	}

	public void setWKDS(String wKDS) {
		WKDS = wKDS;
	}

	@JSONField(name = "DT")
	public String getDT() {
		return DT;
	}

	public void setDT(String dT) {
		DT = dT;
	}

	@JSONField(name = "UR")
	public String getUR() {
		return UR;
	}

	public void setUR(String uR) {
		UR = uR;
	}

	@JSONField(name = "INFF")
	public String getINFF() {
		return INFF;
	}

	public void setINFF(String iNFF) {
		if (INFF.equals("N"))
			INFF = iNFF;
	}

	@JSONField(name = "ACPY")
	public String getACPY() {
		return ACPY;
	}

	public void setACPY(String aCPY) {
		ACPY = aCPY;
	}
}
