package cn.com.sailin.falconweb.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Bsst {
	
	private String APCD; //接入点
	
	private String BSCD; //工地代码
	
	private String BSDS; //工地名称
	
	private String SVDP; //监管单位
	
	private String BCNM; //建设单位名称
	
	private String CCCD; //承建公司代码
	
	private String CCDS; //承建公司名称
	
	private String BKCD; //银行代码
	
	private String BKDS; //银行名称
	
	private String BLMONTH;//余额查询月份
	
	private String BLDATE;//余额开始预警日期
	
	private String BLDY;//余额预警日
	
	private String CURBL;//当前余额
	
	private String SPANBL;//要求余额
	
	private String BLSTATUS;//余额当前状态 Y 有钱 N 钱不够 E 异常
	
	private String BLSTRM;//余额状态备注
	
	private String PYMONTH;//薪金发放月份
	
	private String PYDATE;//薪金开始发放日期
	
	private String PYDY;//薪金发放日
	
	private String QXSTATUS;//当前欠薪状态 Y 欠薪 N 没欠 E 异常
	
	private String QXRS;//当前发放月份的欠薪人数
	
	private String FFRS;//当前发放月份的发薪人数
	
	private String CKRS;//当前发放月份的考勤人数
	
	private String QXRM;//当前欠薪备注
	
	private String SUMQXRS;//欠薪总人次
	
	private String SUMFFRS;//发薪总人次
	
	private String LCNAME;//发薪单位名称
	
	@JSONField(name="LCNAME")	
	public String getLCNAME() {
		return LCNAME;
	}

	public void setLCNAME(String lCNAME) {
		LCNAME = lCNAME;
	}

	@JSONField(name="SUMQXRS")
	public String getSUMQXRS(){
		return SUMQXRS;
	}
	
	public void setSUMQXRS(String sUMQXRS){
		SUMQXRS=sUMQXRS;
	}
	
	@JSONField(name="QXSTATUS")
	public String getQXSTATUS() {
		return QXSTATUS;
	}

	public void setQXSTATUS(String qXSTATUS) {
		QXSTATUS = qXSTATUS;
	}

	@JSONField(name="BLDY")
	public String getBLDY() {
		return BLDY;
	}

	public void setBLDY(String bLDY) {
		BLDY = bLDY;
	}

	@JSONField(name="PYDY")
	public String getPYDY() {
		return PYDY;
	}

	public void setPYDY(String pYDY) {
		PYDY = pYDY;
	}

	@JSONField(name="CURBL")
	public String getCURBL() {
		return CURBL;
	}

	public void setCURBL(String cURBL) {
		CURBL = cURBL;
	}

	@JSONField(name="SPANBL")
	public String getSPANBL() {
		return SPANBL;
	}

	public void setSPANBL(String sPANBL) {
		SPANBL = sPANBL;
	}
	
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

	@JSONField(name="BSDS")
	public String getBSDS() {
		return BSDS;
	}

	public void setBSDS(String bSDS) {
		BSDS = bSDS;
	}

	@JSONField(name="SVDP")
	public String getSVDP() {
		return SVDP;
	}

	public void setSVDP(String sVDP) {
		SVDP = sVDP;
	}

	@JSONField(name="BCNM")
	public String getBCNM() {
		return BCNM;
	}

	public void setBCNM(String bCNM) {
		BCNM = bCNM;
	}

	@JSONField(name="CCCD")
	public String getCCCD() {
		return CCCD;
	}

	public void setCCCD(String cCCD) {
		CCCD = cCCD;
	}

	@JSONField(name="CCDS")
	public String getCCDS() {
		return CCDS;
	}

	public void setCCDS(String cCDS) {
		CCDS = cCDS;
	}

	@JSONField(name="BKCD")
	public String getBKCD() {
		return BKCD;
	}

	public void setBKCD(String bKCD) {
		BKCD = bKCD;
	}

	@JSONField(name="BKDS")
	public String getBKDS() {
		return BKDS;
	}

	public void setBKDS(String bKDS) {
		BKDS = bKDS;
	}

	@JSONField(name="BLMONTH")
	public String getBLMONTH() {
		return BLMONTH;
	}

	public void setBLMONTH(String bLMONTH) {
		BLMONTH = bLMONTH;
	}

	@JSONField(name="BLDATE")
	public String getBLDATE() {
		return BLDATE;
	}

	public void setBLDATE(String bLDATE) {
		BLDATE = bLDATE;
	}

	@JSONField(name="BLSTATUS")
	public String getBLSTATUS() {
		return BLSTATUS;
	}

	public void setBLSTATUS(String bLSTATUS) {
		BLSTATUS = bLSTATUS;
	}

	@JSONField(name="BLSTRM")
	public String getBLSTRM() {
		return BLSTRM;
	}

	public void setBLSTRM(String bLSTRM) {
		BLSTRM = bLSTRM;
	}

	@JSONField(name="PYMONTH")
	public String getPYMONTH() {
		return PYMONTH;
	}

	public void setPYMONTH(String pYMONTH) {
		PYMONTH = pYMONTH;
	}

	@JSONField(name="PYDATE")
	public String getPYDATE() {
		return PYDATE;
	}

	public void setPYDATE(String pYDATE) {
		PYDATE = pYDATE;
	}

	@JSONField(name="QXRS")
	public String getQXRS() {
		return QXRS;
	}

	public void setQXRS(String qXRS) {
		QXRS = qXRS;
	}

	@JSONField(name="QXRM")
	public String getQXRM() {
		return QXRM;
	}

	public void setQXRM(String qXRM) {
		QXRM = qXRM;
	}

	@JSONField(name="FFRS")
	public String getFFRS() {
		return FFRS;
	}

	public void setFFRS(String fFRS) {
		FFRS = fFRS;
	}

	@JSONField(name="CKRS")
	public String getCKRS() {
		return CKRS;
	}

	public void setCKRS(String cKRS) {
		CKRS = cKRS;
	}

	@JSONField(name="SUMFFRS")
	public String getSUMFFRS() {
		return SUMFFRS;
	}

	public void setSUMFFRS(String sUMFFRS) {
		SUMFFRS = sUMFFRS;
	}	

}
