package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class CallResult {
	
	private String MSGID;
	
	private String MSGDESC;
	
	private Object DATA;

	@JSONField(name="MSGID")
	public String getMSGID() {
		return MSGID;
	}

	public void setMSGID(String mSGID) {
		MSGID = mSGID;
	}

	@JSONField(name="MSGDESC")
	public String getMSGDESC() {
		return MSGDESC;
	}

	public void setMSGDESC(String mSGDESC) {
		MSGDESC = mSGDESC;
	}

	@JSONField(name="DATA")
	public Object getDATA() {
		return DATA;
	}

	public void setDATA(Object dATA) {
		DATA = dATA;
	}

}
