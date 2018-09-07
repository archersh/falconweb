package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Actoolcfg {

	private String BUTTONID;
	private String BUTTONTEXT;
	private String ICONINDEX;
	private String ISSHOW;
	private String FUNCTIONID;
	private String ORDERNUM;
	
	@JSONField(name="BUTTONID")
	public String getBUTTONID() {
		return BUTTONID;
	}
	public void setBUTTONID(String bUTTONID) {
		BUTTONID = bUTTONID;
	}
	@JSONField(name="BUTTONTEXT")
	public String getBUTTONTEXT() {
		return BUTTONTEXT;
	}
	public void setBUTTONTEXT(String bUTTONTEXT) {
		BUTTONTEXT = bUTTONTEXT;
	}
	@JSONField(name="ICONINDEX")
	public String getICONINDEX() {
		return ICONINDEX;
	}
	public void setICONINDEX(String iCONINDEX) {
		ICONINDEX = iCONINDEX;
	}
	@JSONField(name="ISSHOW")
	public String getISSHOW() {
		return ISSHOW;
	}
	public void setISSHOW(String iSSHOW) {
		ISSHOW = iSSHOW;
	}
	@JSONField(name="FUNCTIONID")
	public String getFUNCTIONID() {
		return FUNCTIONID;
	}
	public void setFUNCTIONID(String fUNCTIONID) {
		FUNCTIONID = fUNCTIONID;
	}
	@JSONField(name="ORDERNUM")
	public String getORDERNUM() {
		return ORDERNUM;
	}
	public void setORDERNUM(String oRDERNUM) {
		ORDERNUM = oRDERNUM;
	}
	
	
}
