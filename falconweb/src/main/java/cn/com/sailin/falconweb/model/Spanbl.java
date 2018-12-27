package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Spanbl {

	private String APCD;// 接入点代码

	private String BSCD;// 工地代码

	private String BKCD;// 银行代码

	private String SPAN;// 专用账户账户

	private String SPANBL;// 专用账户余额

	private String CHDT;// 修改时间

	private String CHUR;// 修改用户
	
	private String MONTH;//监管月份
	
	private String SVDT;//监管时间点
	
	private String RQANBL;//要求的账户余额
	
	private String CHARGEDATE;//入账日期
	
	@JSONField(name="CHARGEDATE")	
	public String getCHARGEDATE() {
		return CHARGEDATE;
	}

	public void setCHARGEDATE(String cHARGEDATE) {
		CHARGEDATE = cHARGEDATE;
	}

	@JSONField(name="MONTH")
	public String getMONTH() {
		return MONTH;
	}

	public void setMONTH(String mONTH) {
		MONTH = mONTH;
	}

	@JSONField(name="SVDT")
	public String getSVDT() {
		return SVDT;
	}

	public void setSVDT(String sVDT) {
		SVDT = sVDT;
	}

	@JSONField(name="RQANBL")
	public String getRQANBL() {
		return RQANBL;
	}

	public void setRQANBL(String rQANBL) {
		RQANBL = rQANBL;
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

	@JSONField(name = "BKCD")
	public String getBKCD() {
		return BKCD;
	}

	public void setBKCD(String bKCD) {
		BKCD = bKCD;
	}

	@JSONField(name = "SPAN")
	public String getSPAN() {
		return SPAN;
	}

	public void setSPAN(String sPAN) {
		SPAN = sPAN;
	}

	@JSONField(name = "SPANBL")
	public String getSPANBL() {
		return SPANBL;
	}

	public void setSPANBL(String sPANBL) {
		SPANBL = sPANBL;
	}

	@JSONField(name="CHDT")
	public String getCHDT() {
		return CHDT;
	}

	public void setCHDT(String cHDT) {
		CHDT = cHDT;
	}

	@JSONField(name="CHUR")
	public String getCHUR() {
		return CHUR;
	}

	public void setCHUR(String cHUR) {
		CHUR = cHUR;
	}

}
