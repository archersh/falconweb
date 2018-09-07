package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Acmenustruct {
	
	private String MENUID;
	private String MENUTEXT;
	private String PARENTMENUID;
	private String FUNCTIONID;
	private String ORDERNUM;
	private String CLICKNUM;
	private String LASTCLICKUSER;
	private String LASTCLICKTIME;
	
	@JSONField(name="MENUID")
	public String getMENUID() {
		return MENUID;
	}
	public void setMENUID(String mENUID) {
		MENUID = mENUID;
	}
	@JSONField(name="MENUTEXT")
	public String getMENUTEXT() {
		return MENUTEXT;
	}
	public void setMENUTEXT(String mENUTEXT) {
		MENUTEXT = mENUTEXT;
	}
	@JSONField(name="PARENTMENUID")
	public String getPARENTMENUID() {
		return PARENTMENUID;
	}
	public void setPARENTMENUID(String pARENTMENUID) {
		PARENTMENUID = pARENTMENUID;
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
	@JSONField(name="CLICKNUM")
	public String getCLICKNUM() {
		return CLICKNUM;
	}
	public void setCLICKNUM(String cLICKNUM) {
		CLICKNUM = cLICKNUM;
	}
	@JSONField(name="LASTCLICKUSER")
	public String getLASTCLICKUSER() {
		return LASTCLICKUSER;
	}
	public void setLASTCLICKUSER(String lASTCLICKUSER) {
		LASTCLICKUSER = lASTCLICKUSER;
	}
	@JSONField(name="LASTCLICKTIME")
	public String getLASTCLICKTIME() {
		return LASTCLICKTIME;
	}
	public void setLASTCLICKTIME(String lASTCLICKTIME) {
		LASTCLICKTIME = lASTCLICKTIME;
	}
	
	

}
