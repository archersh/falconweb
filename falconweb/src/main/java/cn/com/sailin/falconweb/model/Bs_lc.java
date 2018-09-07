package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Bs_lc {
	
	private String APCD;//接入点代码
	
	private String BSCD;//工地代码
	
	private String LCCD;//劳务公司代码
	
	private String CHDT;//修改时间
	
	private String CHUR;//修改用户代码
	
	private String INCC;//是否是承包公司

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

	@JSONField(name="INCC")
	public String getINCC() {
		return INCC;
	}

	public void setINCC(String iNCC) {
		INCC = iNCC;
	}
	
}
