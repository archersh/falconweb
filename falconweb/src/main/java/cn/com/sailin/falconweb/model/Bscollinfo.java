package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Bscollinfo {
	
	private String MONTH;
	
	private String APCD;
	
	private String BSCD;
	
	private String WKRS;
	
	private String FFRS;
	
	private String FFJE;
	
	private String QXRS;
	
	private String KQFFRS;
	
	private String CFBL;
	
	private String NOCFM;//未确认人数
	
	private String QXRSCFM;//以确认为依据的欠薪人数
	
	@JSONField(name="KQFFRS")
	public String getKQFFRS() {
		return KQFFRS;
	}

	public void setKQFFRS(String kQFFRS) {
		KQFFRS = kQFFRS;
	}

	@JSONField(name="CFBL")
	public String getCFBL() {
		return CFBL;
	}

	public void setCFBL(String cFBL) {
		CFBL = cFBL;
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

	@JSONField(name="WKRS")
	public String getWKRS() {
		return WKRS;
	}

	public void setWKRS(String wKRS) {
		WKRS = wKRS;
	}

	@JSONField(name="FFRS")
	public String getFFRS() {
		return FFRS;
	}

	public void setFFRS(String fFRS) {
		FFRS = fFRS;
	}

	@JSONField(name="FFJE")
	public String getFFJE() {
		return FFJE;
	}

	public void setFFJE(String fFJE) {
		FFJE = fFJE;
	}
	
	@JSONField(name="QXRS")
	public String getQXRS() {
		return QXRS;
	}

	public void setQXRS(String qXRS) {
		QXRS = qXRS;
	}

	@JSONField(name="NOCFM")
	public String getNOCFM() {
		return NOCFM;
	}

	public void setNOCFM(String nOCFM) {
		NOCFM = nOCFM;
	}

	@JSONField(name="QXRSCFM")
	public String getQXRSCFM() {
		return QXRSCFM;
	}

	public void setQXRSCFM(String qXRSCFM) {
		QXRSCFM = qXRSCFM;
	}
	
	
}
