package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Bs_ls {

	private String APCD;// 接入点代码

	private String BSCD;// 工地代码

	private String LSCD;// 劳务公司代码

	private String CHDT;// 修改时间

	private String CHUR;// 修改用户代码

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

	@JSONField(name="LSCD")
	public String getLSCD() {
		return LSCD;
	}

	public void setLSCD(String lSCD) {
		LSCD = lSCD;
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
