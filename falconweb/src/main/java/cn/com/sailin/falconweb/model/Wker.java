package cn.com.sailin.falconweb.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class Wker {

	private String IDCDNO;
	
	private String NAME;
	
	private String LCCD;
	
	private String APCD;
	
	private String BSCD;
	
	private String USERPASS;
	
	private String RGDT;
	
	private String RGUR;
	
	private String BKCD;
	
	private String BKAN;
	
	private String WKKD;//工种
	
	private String WKNAME;//工种名称
	
	private String SZ_EMPLOY_ID;//工地工号
	
	private String INONEMON;//是否一个月内用工
	
	private String LSCD;
	
	private String WORKERTYPE;
	
	private String DAYSALARY;
	
	private String MONTHSALARY;
	
	private String PXQK;//培训
	
	private String ZYJN;//职业技能
	
	private String CYJL;//从业记录
	
	private String INDY;//是不是党员
	
	private String DLUR;//删除用户
	
	private String INSEX;//性别
	
	private String BRDT;//出生日期
	
	private String HOMEADD;//家庭住址
	
	private String ETHNIC;//民族
	
	private String PIC1;//图片1
	
	private String PIC2;
	
	private String PIC3;
	
	private String PIC4;
	
	private String PIC5;
	
	private String BZ;//班组
	
	private String BZNAME;//班组名称
	
	private String ISCERT;//是否实名认证
	
	private String EMPDATE;//入职时间
	
	private String REGADDRESS;//户籍
	
	private String IDSTARTDATE;//身份证有效期开始日期
	
	private String IDENDDATE;//身份证有效期结束日期
    
	private String INMARRY;//婚姻状态 1已婚2未婚
    
	private String TEL;//手机号
    
	private String EDUCATION;//学历
    
	private String INMIN;//是否少数民族
    
	private String HOUSEHOLDTYPE;//户口性质(1本地农村/2本地城市/3外地农村/4外地城市/5港澳台及其他)
    
	private String TIMECARD;//考勤卡号
    
	private String INGROUPER;//是否班组长
    
	private String INMIGRANT;//是否农民工
	
	private List<Pxxx> PX;
	
	private List<Cyjl> JL;
	
	private List<Zyjn> JN;
	
	@JSONField(name="IDSTARTDATE")	
	public String getIDSTARTDATE() {
		return IDSTARTDATE;
	}

	public void setIDSTARTDATE(String iDSTARTDATE) {
		IDSTARTDATE = iDSTARTDATE;
	}

	@JSONField(name="IDENDDATE")
	public String getIDENDDATE() {
		return IDENDDATE;
	}

	public void setIDENDDATE(String iDENDDATE) {
		IDENDDATE = iDENDDATE;
	}

	@JSONField(name="REGADDRESS")
	public String getREGADDRESS() {
		return REGADDRESS;
	}

	public void setREGADDRESS(String rEGADDRESS) {
		REGADDRESS = rEGADDRESS;
	}

	@JSONField(name="INMARRY")
	public String getINMARRY() {
		return INMARRY;
	}

	public void setINMARRY(String iNMARRY) {
		INMARRY = iNMARRY;
	}

	@JSONField(name="TEL")
	public String getTEL() {
		return TEL;
	}

	public void setTEL(String tEL) {
		TEL = tEL;
	}

	@JSONField(name="EDUCATION")
	public String getEDUCATION() {
		return EDUCATION;
	}

	public void setEDUCATION(String eDUCATION) {
		EDUCATION = eDUCATION;
	}

	@JSONField(name="INMIN")
	public String getINMIN() {
		return INMIN;
	}

	public void setINMIN(String iNMIN) {
		INMIN = iNMIN;
	}

	@JSONField(name="HOUSEHOLDTYPE")
	public String getHOUSEHOLDTYPE() {
		return HOUSEHOLDTYPE;
	}

	public void setHOUSEHOLDTYPE(String hOUSEHOLDTYPE) {
		HOUSEHOLDTYPE = hOUSEHOLDTYPE;
	}

	@JSONField(name="TIMECARD")
	public String getTIMECARD() {
		return TIMECARD;
	}

	public void setTIMECARD(String tIMECARD) {
		TIMECARD = tIMECARD;
	}

	@JSONField(name="INGROUPER")
	public String getINGROUPER() {
		return INGROUPER;
	}

	public void setINGROUPER(String iNGROUPER) {
		INGROUPER = iNGROUPER;
	}

	@JSONField(name="INMIGRANT")
	public String getINMIGRANT() {
		return INMIGRANT;
	}

	public void setINMIGRANT(String iNMIGRANT) {
		INMIGRANT = iNMIGRANT;
	}

	@JSONField(name="PX")
	public List<Pxxx> getPX() {
		return PX;
	}

	public void setPX(List<Pxxx> pX) {
		PX = pX;
	}

	@JSONField(name="JL")
	public List<Cyjl> getJL() {
		return JL;
	}

	public void setJL(List<Cyjl> jL) {
		JL = jL;
	}

	@JSONField(name="JN")
	public List<Zyjn> getJN() {
		return JN;
	}

	public void setJN(List<Zyjn> jN) {
		JN = jN;
	}

	@JSONField(name="EMPDATE")	
	public String getEMPDATE() {
		return EMPDATE;
	}

	public void setEMPDATE(String eMPDATE) {
		EMPDATE = eMPDATE;
	}

	@JSONField(name="WKNAME")	
	public String getWKNAME() {
		return WKNAME;
	}

	public void setWKNAME(String wKNAME) {
		WKNAME = wKNAME;
	}

	@JSONField(name="BZNAME")
	public String getBZNAME() {
		return BZNAME;
	}

	public void setBZNAME(String bZNAME) {
		BZNAME = bZNAME;
	}

	@JSONField(name="BZ")
	public String getBZ() {
		return BZ;
	}

	public void setBZ(String bZ) {
		BZ = bZ;
	}

	@JSONField(name="ISCERT")
	public String getISCERT() {
		return ISCERT;
	}

	public void setISCERT(String iSCERT) {
		ISCERT = iSCERT;
	}

	@JSONField(name="LSCD")	
	public String getLSCD() {
		return LSCD;
	}

	public void setLSCD(String lSCD) {
		LSCD = lSCD;
	}

	@JSONField(name="WORKERTYPE")
	public String getWORKERTYPE() {
		return WORKERTYPE;
	}

	public void setWORKERTYPE(String wORKERTYPE) {
		WORKERTYPE = wORKERTYPE;
	}

	@JSONField(name="DAYSALARY")
	public String getDAYSALARY() {
		return DAYSALARY;
	}

	public void setDAYSALARY(String dAYSALARY) {
		DAYSALARY = dAYSALARY;
	}

	@JSONField(name="MONTHSALARY")
	public String getMONTHSALARY() {
		return MONTHSALARY;
	}

	public void setMONTHSALARY(String mONTHSALARY) {
		MONTHSALARY = mONTHSALARY;
	}

	@JSONField(name="PIC2")
	public String getPIC2() {
		return PIC2;
	}

	public void setPIC2(String pIC2) {
		PIC2 = pIC2;
	}

	@JSONField(name="PIC3")
	public String getPIC3() {
		return PIC3;
	}

	public void setPIC3(String pIC3) {
		PIC3 = pIC3;
	}

	@JSONField(name="PIC4")
	public String getPIC4() {
		return PIC4;
	}

	public void setPIC4(String pIC4) {
		PIC4 = pIC4;
	}

	@JSONField(name="PIC5")
	public String getPIC5() {
		return PIC5;
	}

	public void setPIC5(String pIC5) {
		PIC5 = pIC5;
	}

	@JSONField(name="INONEMON")
	public String getINONEMON() {
		return INONEMON;
	}

	public void setINONEMON(String iNONEMON) {
		INONEMON = iNONEMON;
	}

	@JSONField(name="PXQK")
	public String getPXQK() {
		return PXQK;
	}

	public void setPXQK(String pXQK) {
		PXQK = pXQK;
	}

	@JSONField(name="ZYJN")
	public String getZYJN() {
		return ZYJN;
	}

	public void setZYJN(String zYJN) {
		ZYJN = zYJN;
	}

	@JSONField(name="CYJL")
	public String getCYJL() {
		return CYJL;
	}

	public void setCYJL(String cYJL) {
		CYJL = cYJL;
	}

	@JSONField(name="INDY")
	public String getINDY() {
		return INDY;
	}

	public void setINDY(String iNDY) {
		INDY = iNDY;
	}

	@JSONField(name="IDCDNO")
	public String getIDCDNO() {
		return IDCDNO;
	}

	public void setIDCDNO(String iDCDNO) {
		IDCDNO = iDCDNO;
	}

	@JSONField(name="NAME")
	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	@JSONField(name="LCCD")
	public String getLCCD() {
		return LCCD;
	}

	public void setLCCD(String lCCD) {
		LCCD = lCCD;
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

	@JSONField(name="USERPASS")
	public String getUSERPASS() {
		return USERPASS;
	}

	public void setUSERPASS(String uSERPASS) {
		USERPASS = uSERPASS;
	}

	@JSONField(name="RGDT")
	public String getRGDT() {
		return RGDT;
	}

	public void setRGDT(String rGDT) {
		RGDT = rGDT;
	}

	@JSONField(name="RGUR")
	public String getRGUR() {
		return RGUR;
	}

	public void setRGUR(String rGUR) {
		RGUR = rGUR;
	}

	@JSONField(name="BKCD")
	public String getBKCD() {
		return BKCD;
	}

	public void setBKCD(String bKCD) {
		BKCD = bKCD;
	}

	@JSONField(name="BKAN")
	public String getBKAN() {
		return BKAN;
	}

	public void setBKAN(String bKAN) {
		BKAN = bKAN;
	}

	@JSONField(name="DLUR")
	public String getDLUR() {
		return DLUR;
	}

	public void setDLUR(String dLUR) {
		DLUR = dLUR;
	}

	@JSONField(name="INSEX")
	public String getINSEX() {
		return INSEX;
	}

	public void setINSEX(String iNSEX) {
		INSEX = iNSEX;
	}

	@JSONField(name="BRDT")
	public String getBRDT() {
		return BRDT;
	}

	public void setBRDT(String bRDT) {
		BRDT = bRDT;
	}

	@JSONField(name="HOMEADD")
	public String getHOMEADD() {
		return HOMEADD;
	}

	public void setHOMEADD(String hOMEADD) {
		HOMEADD = hOMEADD;
	}

	@JSONField(name="ETHNIC")
	public String getETHNIC() {
		return ETHNIC;
	}

	public void setETHNIC(String eTHNIC) {
		ETHNIC = eTHNIC;
	}

	@JSONField(name="WKKD")
	public String getWKKD() {
		return WKKD;
	}

	public void setWKKD(String wKKD) {
		WKKD = wKKD;
	}

	@JSONField(name="SZ_EMPLOY_ID")
	public String getSZ_EMPLOY_ID() {
		return SZ_EMPLOY_ID;
	}

	public void setSZ_EMPLOY_ID(String sZ_EMPLOY_ID) {
		SZ_EMPLOY_ID = sZ_EMPLOY_ID;
	}

	@JSONField(name="PIC1")
	public String getPIC1() {
		return PIC1;
	}

	public void setPIC1(String pIC1) {
		PIC1 = pIC1;
	}
	
	
	
}
