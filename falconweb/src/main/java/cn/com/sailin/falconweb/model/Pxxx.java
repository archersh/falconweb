package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Pxxx {
	
	private String PXID;
	
	private String PXTITLE;
	
	private String STARTDATE;
	
	private String ENDDATE;
	
	private String REMARK;

	@JSONField(name="PXID")
	public String getPXID() {
		return PXID;
	}

	public void setPXID(String pXID) {
		PXID = pXID;
	}

	@JSONField(name="PXTITLE")
	public String getPXTITLE() {
		return PXTITLE;
	}

	public void setPXTITLE(String pXTITLE) {
		PXTITLE = pXTITLE;
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
