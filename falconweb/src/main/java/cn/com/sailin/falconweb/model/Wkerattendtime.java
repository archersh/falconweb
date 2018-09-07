package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Wkerattendtime {

	private String APCD;
	
	private String BSCD;
	
	private String IDCDNO;
	
	private String SZ_EMPLOY_ID;
	
	private String WKKD;
	
	private String BZ;
	
	private String INTIME;
	
	private String OUTTIME;
	
	private String NGID;

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

	@JSONField(name="IDCDNO")
	public String getIDCDNO() {
		return IDCDNO;
	}

	public void setIDCDNO(String iDCDNO) {
		IDCDNO = iDCDNO;
	}

	@JSONField(name="SZ_EMPLOY_ID")
	public String getSZ_EMPLOY_ID() {
		return SZ_EMPLOY_ID;
	}

	public void setSZ_EMPLOY_ID(String sZ_EMPLOY_ID) {
		SZ_EMPLOY_ID = sZ_EMPLOY_ID;
	}

	@JSONField(name="WKKD")
	public String getWKKD() {
		return WKKD;
	}

	public void setWKKD(String wKKD) {
		WKKD = wKKD;
	}

	@JSONField(name="BZ")
	public String getBZ() {
		return BZ;
	}

	public void setBZ(String bZ) {
		BZ = bZ;
	}

	@JSONField(name="INTIME")
	public String getINTIME() {
		return INTIME;
	}

	public void setINTIME(String iNTIME) {
		INTIME = iNTIME;
	}

	@JSONField(name="OUTTIME")
	public String getOUTTIME() {
		return OUTTIME;
	}

	public void setOUTTIME(String oUTTIME) {
		OUTTIME = oUTTIME;
	}

	@JSONField(name="NGID")
	public String getNGID() {
		return NGID;
	}

	public void setNGID(String nGID) {
		NGID = nGID;
	}
	
	
}
