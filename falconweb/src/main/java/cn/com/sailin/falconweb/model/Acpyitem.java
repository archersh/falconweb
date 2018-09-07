package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Acpyitem {
	
	private String MONTH;//发放月份
	
	private String APCD;//接入点代码
	
	private String BSCD;//工作代码
	
	private String LCCD;//劳务公司代码
	
	private String SQNB;//发放批次号
	
	private String IDCDNO;//身份证号
	
	private String NAME;//姓名
	
	private String BKCD;//银行代码
	
	private String BKAN;//很行账号
	
	private String ACPY;//核算工资

	private String TASQ;//银行交易流水号 暂留目前没有值
	
	private String INBKCFM;//银行是否确认 Y/E/N 成功，失败，未确认
	
	private String BKCFMDT;//确认时间
	
	private String BKCFERCD;//发放错误代码
	
	private String BKCFER;//发放错误信息
	
	private String INTEMP;//是否临时人员没有人员信息
	
	private String INONEMON;//就否一个月内人员
	
	@JSONField(name="INTEMP")
	public String getINTEMP() {
		return INTEMP;
	}

	public void setINTEMP(String iNTEMP) {
		INTEMP = iNTEMP;
	}

	@JSONField(name="INONEMON")
	public String getINONEMON() {
		return INONEMON;
	}

	public void setINONEMON(String iNONEMON) {
		INONEMON = iNONEMON;
	}

	@JSONField(name="MONTH")
	public String getMONTH() {
		return MONTH;
	}

	public void setMONTH(String mONTH) {
		MONTH = mONTH;
	}

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

	@JSONField(name="LCCD")
	public String getLCCD() {
		return LCCD;
	}

	public void setLCCD(String lCCD) {
		LCCD = lCCD;
	}

	@JSONField(name="SQNB")
	public String getSQNB() {
		return SQNB;
	}

	public void setSQNB(String sQNB) {
		SQNB = sQNB;
	}

	@JSONField(name="IDCDNO")
	public String getIDCDNO() {
		return IDCDNO;
	}

	public void setIDCDNO(String iDCDNO) {
		IDCDNO = iDCDNO;
	}

	@JSONField(name="NAME")
	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	@JSONField(name="BKCD")
	public String getBKCD() {
		return BKCD;
	}

	public void setBKCD(String bKCD) {
		BKCD = bKCD;
	}

	@JSONField(name="BKAN")
	public String getBKAN() {
		return BKAN;
	}

	public void setBKAN(String bKAN) {
		BKAN = bKAN;
	}

	@JSONField(name="ACPY")
	public String getACPY() {
		return ACPY;
	}

	public void setACPY(String aCPY) {
		ACPY = aCPY;
	}

	@JSONField(name="TASQ")
	public String getTASQ() {
		return TASQ;
	}

	public void setTASQ(String tASQ) {
		TASQ = tASQ;
	}

	@JSONField(name="INBKCFM")
	public String getINBKCFM() {
		return INBKCFM;
	}

	public void setINBKCFM(String iNBKCFM) {
		INBKCFM = iNBKCFM;
	}

	@JSONField(name="BKCFMDT")
	public String getBKCFMDT() {
		return BKCFMDT;
	}

	public void setBKCFMDT(String bKCFMDT) {
		BKCFMDT = bKCFMDT;
	}

	@JSONField(name="BKCFERCD")
	public String getBKCFERCD() {
		return BKCFERCD;
	}

	public void setBKCFERCD(String bKCFERCD) {
		BKCFERCD = bKCFERCD;
	}

	@JSONField(name="BKCFER")
	public String getBKCFER() {
		return BKCFER;
	}

	public void setBKCFER(String bKCFER) {
		BKCFER = bKCFER;
	}
	
}
