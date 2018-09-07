package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Atpyitem {
	
	private String BKCD;//银行代码
	
	private String TASQ;//交易流水号
	
	private String TADT;//交易时间
	
	private String NAME;//姓名
	
	private String IDCDNO;//身份证号码
	
	private String BKAN;//银行账号
	
	private String PAYERAN;//付款方账号
	
	private String BSCD;//工地代码
	
	private String AMOUNT;//实际发放工资数量
	
	private String RDDT;//获取记录的时间
	
	private String RDUR;//获取记录的用户

	@JSONField(name="BKCD")
	public String getBKCD() {
		return BKCD;
	}

	public void setBKCD(String bKCD) {
		BKCD = bKCD;
	}

	@JSONField(name="TASQ")
	public String getTASQ() {
		return TASQ;
	}

	public void setTASQ(String tASQ) {
		TASQ = tASQ;
	}

	@JSONField(name="TADT")
	public String getTADT() {
		return TADT;
	}

	public void setTADT(String tADT) {
		TADT = tADT;
	}

	@JSONField(name="NAME")
	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	@JSONField(name="IDCDNO")
	public String getIDCDNO() {
		return IDCDNO;
	}

	public void setIDCDNO(String iDCDNO) {
		IDCDNO = iDCDNO;
	}

	@JSONField(name="BKAN")
	public String getBKAN() {
		return BKAN;
	}

	public void setBKAN(String bKAN) {
		BKAN = bKAN;
	}

	@JSONField(name="PAYERAN")
	public String getPAYERAN() {
		return PAYERAN;
	}

	public void setPAYERAN(String pAYERAN) {
		PAYERAN = pAYERAN;
	}

	@JSONField(name="BSCD")
	public String getBSCD() {
		return BSCD;
	}

	public void setBSCD(String bSCD) {
		BSCD = bSCD;
	}

	@JSONField(name="AMOUNT")
	public String getAMOUNT() {
		return AMOUNT;
	}

	public void setAMOUNT(String aMOUNT) {
		AMOUNT = aMOUNT;
	}

	@JSONField(name="RDDT")
	public String getRDDT() {
		return RDDT;
	}

	public void setRDDT(String rDDT) {
		RDDT = rDDT;
	}

	@JSONField(name="RDUR")
	public String getRDUR() {
		return RDUR;
	}

	public void setRDUR(String rDUR) {
		RDUR = rDUR;
	}

}
