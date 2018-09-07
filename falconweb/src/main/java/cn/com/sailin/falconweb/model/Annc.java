package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Annc {
	
	private String ANDATE;
	
	private String ANTT;
	
	private String ANNC;
	
	private String CHUR;
	
	private String SVDP;
	
	private String ANID;
	
	@JSONField(name="SVDP")
	public String getSVDP() {
		return SVDP;
	}
	
	public void setSVDP(String sVDP) {
		SVDP=sVDP;
	}
	
	@JSONField(name="ANID")
	public String getANID() {
		return ANID;
	}
	
	public void setANID(String aNID) {
		ANID=aNID;
	}

	@JSONField(name="ANDATE")
	public String getANDATE() {
		return ANDATE;
	}

	public void setANDATE(String aNDATE) {
		ANDATE = aNDATE;
	}

	@JSONField(name="ANTT")
	public String getANTT() {
		return ANTT;
	}

	public void setANTT(String aNTT) {
		ANTT = aNTT;
	}

	@JSONField(name="ANNC")
	public String getANNC() {
		return ANNC;
	}

	public void setANNC(String aNNC) {
		ANNC = aNNC;
	}

	@JSONField(name="CHUR")
	public String getCHUR() {
		return CHUR;
	}

	public void setCHUR(String cHUR) {
		CHUR = cHUR;
	}
	
	

}
