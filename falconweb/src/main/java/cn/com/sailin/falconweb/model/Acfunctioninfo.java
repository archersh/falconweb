package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Acfunctioninfo {
	
	private String FUNCTIONID;
	
	private String FUNCTIONNAME;
	
	private String FUNCTIONDESC;
	
	private String CONTROLNAME;
	
	private String FORMMODAL;
	
	private String PARENTFUNCTIONID;
	
	private String ISPUBLIC;
	
	private String ACTIONCONTROL;
	
	private String ORDERNUM;

	@JSONField(name="FUNCTIONID")
	public String getFUNCTIONID() {
		return FUNCTIONID;
	}

	public void setFUNCTIONID(String fUNCTIONID) {
		FUNCTIONID = fUNCTIONID;
	}

	@JSONField(name="FUNCTIONNAME")
	public String getFUNCTIONNAME() {
		return FUNCTIONNAME;
	}

	public void setFUNCTIONNAME(String fUNCTIONNAME) {
		FUNCTIONNAME = fUNCTIONNAME;
	}

	@JSONField(name="FUNCTIONDESC")
	public String getFUNCTIONDESC() {
		return FUNCTIONDESC;
	}

	public void setFUNCTIONDESC(String fUNCTIONDESC) {
		FUNCTIONDESC = fUNCTIONDESC;
	}

	@JSONField(name="CONTROLNAME")
	public String getCONTROLNAME() {
		return CONTROLNAME;
	}

	public void setCONTROLNAME(String cONTROLNAME) {
		CONTROLNAME = cONTROLNAME;
	}

	@JSONField(name="FORMMODAL")
	public String getFORMMODAL() {
		return FORMMODAL;
	}

	public void setFORMMODAL(String fORMMODAL) {
		FORMMODAL = fORMMODAL;
	}

	@JSONField(name="PARENTFUNCTIONID")
	public String getPARENTFUNCTIONID() {
		return PARENTFUNCTIONID;
	}

	public void setPARENTFUNCTIONID(String pARENTFUNCTIONID) {
		PARENTFUNCTIONID = pARENTFUNCTIONID;
	}

	@JSONField(name="ISPUBLIC")
	public String getISPUBLIC() {
		return ISPUBLIC;
	}

	public void setISPUBLIC(String iSPUBLIC) {
		ISPUBLIC = iSPUBLIC;
	}

	@JSONField(name="ACTIONCONTROL")
	public String getACTIONCONTROL() {
		return ACTIONCONTROL;
	}

	public void setACTIONCONTROL(String aCTIONCONTROL) {
		ACTIONCONTROL = aCTIONCONTROL;
	}

	@JSONField(name="ORDERNUM")
	public String getORDERNUM() {
		return ORDERNUM;
	}

	public void setORDERNUM(String oRDERNUM) {
		ORDERNUM = oRDERNUM;
	}
	
	

}
