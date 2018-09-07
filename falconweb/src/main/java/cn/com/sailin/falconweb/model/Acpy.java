package cn.com.sailin.falconweb.model;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class Acpy {

	private String MONTH; // 发放月份

	private String APCD;// 接入点代码

	private String BSCD;// 工地代码

	private String LCCD;// 劳务公司代码

	private String SQNB;// 发放批次号

	private String SQRM;// 发放批次备注

	private String UPLDUR;// 上传用户

	private String UPLDDT;// 上传时间

	private String INCCCK;// 承包公司是否已经审核

	private String CCCKUR;// 承包公司审核人代码

	private String CCCKDT;// 承包公司审核时间

	private String INLCCK;// 劳务公司是否已经审核

	private String LCCKUR;// 劳务公司审核人代码

	private String LCCKDT;//劳务公司审核时间
	
	private String INBKOT;//是否已经导出银行发放清单
	
	private String BKOTUR;//银行导出用户
	
	private String BKOTDT;//银行导出时间
	
	private List<Acpyitem> items = new ArrayList<Acpyitem>();
	
	@JSONField(serialize=false)
	public List<Acpyitem> getItems() {
		return items;
	}

	@JSONField(name="MONTH")
	public String getMONTH() {
		return MONTH;
	}

	public void setMONTH(String mONTH) {
		MONTH = mONTH;
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

	@JSONField(name="LCCD")
	public String getLCCD() {
		return LCCD;
	}

	public void setLCCD(String lCCD) {
		LCCD = lCCD;
	}

	@JSONField(name="SQNB")
	public String getSQNB() {
		return SQNB;
	}

	public void setSQNB(String sQNB) {
		SQNB = sQNB;
	}

	@JSONField(name="SQRM")
	public String getSQRM() {
		return SQRM;
	}

	public void setSQRM(String sQRM) {
		SQRM = sQRM;
	}

	@JSONField(name="UPLDUR")
	public String getUPLDUR() {
		return UPLDUR;
	}

	public void setUPLDUR(String uPLDUR) {
		UPLDUR = uPLDUR;
	}

	@JSONField(name="UPLDDT")
	public String getUPLDDT() {
		return UPLDDT;
	}

	public void setUPLDDT(String uPLDDT) {
		UPLDDT = uPLDDT;
	}

	@JSONField(name="INCCCK")
	public String getINCCCK() {
		return INCCCK;
	}

	public void setINCCCK(String iNCCCK) {
		INCCCK = iNCCCK;
	}

	@JSONField(name="CCCKUR")
	public String getCCCKUR() {
		return CCCKUR;
	}

	public void setCCCKUR(String cCCKUR) {
		CCCKUR = cCCKUR;
	}

	@JSONField(name="CCCKDT")
	public String getCCCKDT() {
		return CCCKDT;
	}

	public void setCCCKDT(String cCCKDT) {
		CCCKDT = cCCKDT;
	}

	@JSONField(name="INLCCK")
	public String getINLCCK() {
		return INLCCK;
	}

	public void setINLCCK(String iNLCCK) {
		INLCCK = iNLCCK;
	}

	@JSONField(name="LCCKUR")
	public String getLCCKUR() {
		return LCCKUR;
	}

	public void setLCCKUR(String lCCKUR) {
		LCCKUR = lCCKUR;
	}

	@JSONField(name="LCCKDT")
	public String getLCCKDT() {
		return LCCKDT;
	}

	public void setLCCKDT(String lCCKDT) {
		LCCKDT = lCCKDT;
	}

	@JSONField(name="INBKOT")
	public String getINBKOT() {
		return INBKOT;
	}

	public void setINBKOT(String iNBKOT) {
		INBKOT = iNBKOT;
	}

	@JSONField(name="BKOTUR")
	public String getBKOTUR() {
		return BKOTUR;
	}

	public void setBKOTUR(String bKOTUR) {
		BKOTUR = bKOTUR;
	}

	@JSONField(name="BKOTDT")
	public String getBKOTDT() {
		return BKOTDT;
	}

	public void setBKOTDT(String bKOTDT) {
		BKOTDT = bKOTDT;
	}

}
