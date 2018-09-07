package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Sycdtb {
	
	private String SYCDTB="";
	
	private String SYIDTB="";
	
	private String SYDSTB="";
	
	private String SYC1TB="";
	
	private String SYC2TB="";
	
	private String SYC3TB="";
	
	private String SYC4TB="";
	
	private String SYC5TB="";
	
	private String SYC6TB="";
	
	private String INUSCH="";
	
	private String DTTR="";
	
	private String URCD="";

	@JSONField(name="SYCDTB")
	public String getSYCDTB() {
		return SYCDTB;
	}

	public void setSYCDTB(String sYCDTB) {
		SYCDTB = sYCDTB;
	}

	@JSONField(name="SYIDTB")
	public String getSYIDTB() {
		return SYIDTB;
	}

	public void setSYIDTB(String sYIDTB) {
		SYIDTB = sYIDTB;
	}

	@JSONField(name="SYDSTB")
	public String getSYDSTB() {
		return SYDSTB;
	}

	public void setSYDSTB(String sYDSTB) {
		SYDSTB = sYDSTB;
	}

	@JSONField(name="SYC1TB")
	public String getSYC1TB() {
		return SYC1TB;
	}

	public void setSYC1TB(String sYC1TB) {
		SYC1TB = sYC1TB;
	}

	@JSONField(name="SYC2TB")
	public String getSYC2TB() {
		return SYC2TB;
	}

	public void setSYC2TB(String sYC2TB) {
		SYC2TB = sYC2TB;
	}

	@JSONField(name="SYC3TB")
	public String getSYC3TB() {
		return SYC3TB;
	}

	public void setSYC3TB(String sYC3TB) {
		SYC3TB = sYC3TB;
	}

	@JSONField(name="SYC4TB")
	public String getSYC4TB() {
		return SYC4TB;
	}

	public void setSYC4TB(String sYC4TB) {
		SYC4TB = sYC4TB;
	}

	@JSONField(name="SYC5TB")
	public String getSYC5TB() {
		return SYC5TB;
	}

	public void setSYC5TB(String sYC5TB) {
		SYC5TB = sYC5TB;
	}

	@JSONField(name="SYC6TB")
	public String getSYC6TB() {
		return SYC6TB;
	}

	public void setSYC6TB(String sYC6TB) {
		SYC6TB = sYC6TB;
	}

	@JSONField(name="INUSCH")
	public String getINUSCH() {
		return INUSCH;
	}

	public void setINUSCH(String iNUSCH) {
		INUSCH = iNUSCH;
	}

	@JSONField(name="DTTR")
	public String getDTTR() {
		return DTTR;
	}

	public void setDTTR(String dTTR) {
		DTTR = dTTR;
	}

	@JSONField(name="URCD")
	public String getURCD() {
		return URCD;
	}

	public void setURCD(String uRCD) {
		URCD = uRCD;
	}

}
