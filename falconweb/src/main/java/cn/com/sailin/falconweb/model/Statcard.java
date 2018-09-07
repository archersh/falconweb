package cn.com.sailin.falconweb.model;

import java.sql.Timestamp;

public class Statcard {
	
	private int st_kind;
	
	private int ng_user_id;
	
	private int ng_branch_id;
	
	private int ng_dev_id;
	
	private String ts_card;
	
	private Timestamp ts_create;
	
	private String sz_employ_id;
	
	private String sz_dev_name;
	
	private String apcd;
	
	private int apid;

	public int getSt_kind() {
		return st_kind;
	}

	public void setSt_kind(int st_kind) {
		this.st_kind = st_kind;
	}

	public int getNg_user_id() {
		return ng_user_id;
	}

	public void setNg_user_id(int ng_user_id) {
		this.ng_user_id = ng_user_id;
	}

	public int getNg_branch_id() {
		return ng_branch_id;
	}

	public void setNg_branch_id(int ng_branch_id) {
		this.ng_branch_id = ng_branch_id;
	}

	public int getNg_dev_id() {
		return ng_dev_id;
	}

	public void setNg_dev_id(int ng_dev_id) {
		this.ng_dev_id = ng_dev_id;
	}

	public String getTs_card() {
		return ts_card;
	}

	public void setTs_card(String ts_card) {
		this.ts_card = ts_card;
	}

	public Timestamp getTs_create() {
		return ts_create;
	}

	public void setTs_create(Timestamp ts_create) {
		this.ts_create = ts_create;
	}

	public String getSz_employ_id() {
		return sz_employ_id;
	}

	public void setSz_employ_id(String sz_employ_id) {
		this.sz_employ_id = sz_employ_id;
	}

	public String getSz_dev_name() {
		return sz_dev_name;
	}

	public void setSz_dev_name(String sz_dev_name) {
		this.sz_dev_name = sz_dev_name;
	}

	public String getApcd() {
		return apcd;
	}

	public void setApcd(String apcd) {
		this.apcd = apcd;
	}

	public int getApid() {
		return apid;
	}

	public void setApid(int apid) {
		this.apid = apid;
	}
	
}
