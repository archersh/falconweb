package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Acuserinfo {
	
	private String USERID;
	
	private String USERNAME;
	
	private String USERPASS;
	
	private String USERTYPE;
	
	private String DEPTID;
	
	private String ISUSE;
	
	private String PASSNOTOVER;
	
	private String PASSOVERDATE;
	
	private String INADMIN;
	
	private String OTHERRIGHT;
	
	private String RECORDUSERID;
	
	private String RECORDTIME;

	@JSONField(name="USERID")
	public String getUSERID() {
		return USERID;
	}

	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}

	@JSONField(name="USERNAME")
	public String getUSERNAME() {
		return USERNAME;
	}

	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	@JSONField(name="USERPASS")
	public String getUSERPASS() {
		return USERPASS;
	}

	public void setUSERPASS(String uSERPASS) {
		USERPASS = uSERPASS;
	}

	@JSONField(name="USERTYPE")
	public String getUSERTYPE() {
		return USERTYPE;
	}

	public void setUSERTYPE(String uSERTYPE) {
		USERTYPE = uSERTYPE;
	}

	@JSONField(name="DEPTID")
	public String getDEPTID() {
		return DEPTID;
	}

	public void setDEPTID(String dEPTID) {
		DEPTID = dEPTID;
	}

	@JSONField(name="ISUSE")
	public String getISUSE() {
		return ISUSE;
	}

	public void setISUSE(String iSUSE) {
		ISUSE = iSUSE;
	}

	@JSONField(name="PASSNOTOVER")
	public String getPASSNOTOVER() {
		return PASSNOTOVER;
	}

	public void setPASSNOTOVER(String pASSNOTOVER) {
		PASSNOTOVER = pASSNOTOVER;
	}

	@JSONField(name="PASSOVERDATE")
	public String getPASSOVERDATE() {
		return PASSOVERDATE;
	}

	public void setPASSOVERDATE(String pASSOVERDATE) {
		PASSOVERDATE = pASSOVERDATE;
	}

	@JSONField(name="INADMIN")
	public String getINADMIN() {
		return INADMIN;
	}

	public void setINADMIN(String iNADMIN) {
		INADMIN = iNADMIN;
	}

	@JSONField(name="OTHERRIGHT")
	public String getOTHERRIGHT() {
		return OTHERRIGHT;
	}

	public void setOTHERRIGHT(String oTHERRIGHT) {
		OTHERRIGHT = oTHERRIGHT;
	}

	@JSONField(name="RECORDUSERID")
	public String getRECORDUSERID() {
		return RECORDUSERID;
	}

	public void setRECORDUSERID(String rECORDUSERID) {
		RECORDUSERID = rECORDUSERID;
	}

	@JSONField(name="RECORDTIME")
	public String getRECORDTIME() {
		return RECORDTIME;
	}

	public void setRECORDTIME(String rECORDTIME) {
		RECORDTIME = rECORDTIME;
	}
	
	
}
