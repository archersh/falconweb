package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Sybscd {

	private String BSID;// 工地ID

	private String APCD;// 接入点代码

	private String BSCD;// 工地代码

	private String SVDP;// 监管单位

	private String BSDS;// 工地名称

	private String BKCD;// 银行代码

	private String PYDY;// 发薪日期

	private String BCNM;// 建设单位名称

	private String CCCD;// 承建公司

	private String SPAN;// 专用账户账号

	private String SPANRQBL;// 专用账户要求余额

	private String BLWNDY;// 余额预警日期

	private String BSCHUR;// 工地信息修改人员

	private String BSCHDT;// 工地信息修改时间

	private String BKCHUR;// 银行信息修改人员

	private String BKCHDT;// 银行信息修改时间

	private String INDT;// 是否撤除

	private String DTDT;// 撤除时间

	private String DTUR;// 撤除用户

	private String RGDT;// 登记时间

	private String RGUR;// 登记人员

	private String CKBGDY;// 考勤开始日期

	private String BSCDPATH;// 考勤用的路径汉王用

	@JSONField(name = "BSCDPATH")
	public String getBSCDPATH() {
		return BSCDPATH;
	}

	public void setBSCDPATH(String bSCDPATH) {
		BSCDPATH = bSCDPATH;
	}

	@JSONField(name = "INDT")
	public String getINDT() {
		return INDT;
	}

	public void setINDT(String iNDT) {
		INDT = iNDT;
	}

	@JSONField(name = "DTDT")
	public String getDTDT() {
		return DTDT;
	}

	public void setDTDT(String dTDT) {
		DTDT = dTDT;
	}

	@JSONField(name = "DTUR")
	public String getDTUR() {
		return DTUR;
	}

	public void setDTUR(String dTUR) {
		DTUR = dTUR;
	}

	@JSONField(name = "RGDT")
	public String getRGDT() {
		return RGDT;
	}

	public void setRGDT(String rGDT) {
		RGDT = rGDT;
	}

	@JSONField(name = "RGUR")
	public String getRGUR() {
		return RGUR;
	}

	public void setRGUR(String rGUR) {
		RGUR = rGUR;
	}

	@JSONField(name = "CKBGDY")
	public String getCKBGDY() {
		return CKBGDY;
	}

	public void setCKBGDY(String cKBGDY) {
		CKBGDY = cKBGDY;
	}

	@JSONField(name = "BSID")
	public String getBSID() {
		return BSID;
	}

	public void setBSID(String bSID) {
		BSID = bSID;
	}

	@JSONField(name = "APCD")
	public String getAPCD() {
		return APCD;
	}

	public void setAPCD(String aPCD) {
		APCD = aPCD;
	}

	@JSONField(name = "BSCD")
	public String getBSCD() {
		return BSCD;
	}

	public void setBSCD(String bSCD) {
		BSCD = bSCD;
	}

	@JSONField(name = "SVDP")
	public String getSVDP() {
		return SVDP;
	}

	public void setSVDP(String sVDP) {
		SVDP = sVDP;
	}

	@JSONField(name = "BSDS")
	public String getBSDS() {
		return BSDS;
	}

	public void setBSDS(String bSDS) {
		BSDS = bSDS;
	}

	@JSONField(name = "BKCD")
	public String getBKCD() {
		return BKCD;
	}

	public void setBKCD(String bKCD) {
		BKCD = bKCD;
	}

	@JSONField(name = "PYDY")
	public String getPYDY() {
		return PYDY;
	}

	public void setPYDY(String pYDY) {
		PYDY = pYDY;
	}

	@JSONField(name = "BCNM")
	public String getBCNM() {
		return BCNM;
	}

	public void setBCNM(String bCNM) {
		BCNM = bCNM;
	}

	@JSONField(name = "CCCD")
	public String getCCCD() {
		return CCCD;
	}

	public void setCCCD(String cCCD) {
		CCCD = cCCD;
	}

	@JSONField(name = "SPAN")
	public String getSPAN() {
		return SPAN;
	}

	public void setSPAN(String sPAN) {
		SPAN = sPAN;
	}

	@JSONField(name = "SPANRQBL")
	public String getSPANRQBL() {
		return SPANRQBL;
	}

	public void setSPANRQBL(String sPANRQBL) {
		SPANRQBL = sPANRQBL;
	}

	@JSONField(name = "BLWNDY")
	public String getBLWNDY() {
		return BLWNDY;
	}

	public void setBLWNDY(String bLWNDY) {
		BLWNDY = bLWNDY;
	}

	@JSONField(name = "BSCHUR")
	public String getBSCHUR() {
		return BSCHUR;
	}

	public void setBSCHUR(String bSCHUR) {
		BSCHUR = bSCHUR;
	}

	@JSONField(name = "BSCHDT")
	public String getBSCHDT() {
		return BSCHDT;
	}

	public void setBSCHDT(String bSCHDT) {
		BSCHDT = bSCHDT;
	}

	@JSONField(name = "BKCHUR")
	public String getBKCHUR() {
		return BKCHUR;
	}

	public void setBKCHUR(String bKCHUR) {
		BKCHUR = bKCHUR;
	}

	@JSONField(name = "BKCHDT")
	public String getBKCHDT() {
		return BKCHDT;
	}

	public void setBKCHDT(String bKCHDT) {
		BKCHDT = bKCHDT;
	}

}
