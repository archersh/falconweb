package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Zyjn {

	private String JNID;
	
	private String JNNAME;
	
	private String REMARK;

	@JSONField(name="JNID")
	public String getJNID() {
		return JNID;
	}

	public void setJNID(String jNID) {
		JNID = jNID;
	}

	@JSONField(name="JNNAME")
	public String getJNNAME() {
		return JNNAME;
	}

	public void setJNNAME(String jNNAME) {
		JNNAME = jNNAME;
	}

	@JSONField(name="REMARK")
	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
	
	
	
}
