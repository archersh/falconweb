package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Cyjl {

	private String JLID;
	
	private String WKKD;
	
	private String STARTDATE;
	
	private String ENDDATE;
	
	private String REMARK;

	@JSONField(name="JLID")
	public String getJLID() {
		return JLID;
	}

	public void setJLID(String jLID) {
		JLID = jLID;
	}

	@JSONField(name="WKKD")
	public String getWKKD() {
		return WKKD;
	}

	public void setWKKD(String wKKD) {
		WKKD = wKKD;
	}

	@JSONField(name="STARTDATE")
	public String getSTARTDATE() {
		return STARTDATE;
	}

	public void setSTARTDATE(String sTARTDATE) {
		STARTDATE = sTARTDATE;
	}

	@JSONField(name="ENDDATE")
	public String getENDDATE() {
		return ENDDATE;
	}

	public void setENDDATE(String eNDDATE) {
		ENDDATE = eNDDATE;
	}

	@JSONField(name="REMARK")
	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}	
	
}
