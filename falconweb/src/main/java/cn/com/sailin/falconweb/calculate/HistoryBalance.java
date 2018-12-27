package cn.com.sailin.falconweb.calculate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import cn.com.sailin.falconweb.dao.Data;
import cn.com.sailin.falconweb.model.Hisbsst;
import cn.com.sailin.falconweb.publiccode.Code;

public class HistoryBalance {

	private Data _data;

	private HashMap<String, Object> _bkcd = null;

	private HashMap<String, Object> _cccd = null;

	private HashSet<String> _setbs = null;

	private String _reqtype;

	private String _reqval;

	private HashMap<String, Object> _nobl = null;

	private HashMap<String, Object> _bscoll = null;

	private String _startdate;

	private String _enddate;

	public HistoryBalance(String reqtype, String reqval, String startdate, String enddate, Data data) {
		_data = data;
		_reqtype = reqtype;
		_reqval = reqval;
		_setbs = new HashSet<String>();
		_startdate = startdate;
		_enddate = enddate;
	}

	private void getBkcd() {
		_bkcd = new HashMap<String, Object>();
		List<Map<String, Object>> list = _data.qryBkcd();
		for (Map<String, Object> m : list) {
			_bkcd.put(Code.getFieldVal(m, "SYIDTB", "").trim(), m);
		}
		return;
	}

	private void getCccd() {
		_cccd = new HashMap<String, Object>();
		List<Map<String, Object>> list = _data.qryCccd();
		for (Map<String, Object> m : list) {
			_cccd.put(Code.getFieldVal(m, "SYIDTB", "").trim(), m);
		}
		return;
	}

	private void getBslist() {
		HashSet<String> setreq = new HashSet<String>();

		List<Map<String, Object>> lbs = null;
		if (_reqtype.equals("SV"))
			lbs = _data.qryBslistbysvdp(_reqval);
		if (_reqtype.equals("BK"))
			lbs = _data.qryFullbslistbybkcd(_reqval);
		if (_reqtype.equals("CC"))
			lbs = _data.qryBslistbycccd(_reqval);
		if (lbs != null) {
			for (Map<String, Object> m : lbs) {
				setreq.add(Code.getFieldVal(m, "APCD", "") + "-" + Code.getFieldVal(m, "BSCD", ""));
			}
		}
		// 获取指定登记时间范围的工地
		lbs = _data.qryBscdbyrgdt(_startdate, _enddate);
		for (Map<String, Object> m : lbs) {
			String bskey = Code.getFieldVal(m, "APCD", "") + "-" + Code.getFieldVal(m, "BSCD", "");
			// 如果要示的类型里有这个工地，就把这个工地加入工地列表
			if (setreq.contains(bskey))
				_setbs.add(bskey);
		}
	}

	public Object calculate() {
		getBkcd();
		getCccd();
		getBslist();

		float cfbl = _data.getCfbl();

		_nobl=new HashMap<String,Object>();
		_bscoll=new HashMap<String,Object>();
		List<Map<String, Object>> lbsbl = _data.qryHisBsbl();
		for (Map<String, Object> m : lbsbl) {
			_nobl.put(Code.getFieldVal(m, "APCD", "") + "-" + Code.getFieldVal(m, "BSCD", ""), m);
		}
		List<Map<String, Object>> lcfbl = _data.qryHisBscoll(String.valueOf(cfbl));
		for (Map<String, Object> m : lcfbl) {
			_bscoll.put(Code.getFieldVal(m, "APCD", "") + "-" + Code.getFieldVal(m, "BSCD", ""), m);
		}
		// 获取工地列表
		List<Map<String, Object>> bslist = _data.qryBsinfo();
		// 遍历工地信息
		List<Hisbsst> bsstlist = new ArrayList<Hisbsst>();
		for (Map<String, Object> mbs : bslist) {
			// 是否在查询的工地范围内
			if (_setbs.contains(Code.getFieldVal(mbs, "APCD", "") + "-" + Code.getFieldVal(mbs, "BSCD", ""))) {
				Hisbsst hbst = new Hisbsst();
				buildBsstinfo(mbs, hbst);
				bsstlist.add(hbst);
			}
		}
		
		return bsstlist; 
	}

	private void buildBsstinfo(Map<String, Object> mbs, Hisbsst hbst) {

		hbst.setAPCD(Code.getFieldVal(mbs, "APCD", ""));
		hbst.setBSDS(Code.getFieldVal(mbs, "BSDS", ""));
		hbst.setBSCD(Code.getFieldVal(mbs, "BSCD", ""));
		Map<String, Object> m;
		hbst.setBKCD(Code.getFieldVal(mbs, "BKCD", ""));
		m = Code.getMapval(_bkcd, hbst.getBKCD());
		if (m == null)
			hbst.setBKDS("");
		else
			hbst.setBKDS(Code.getFieldVal(m, "SYDSTB", ""));
		hbst.setCCCD(Code.getFieldVal(mbs, "CCCD", ""));
		m = Code.getMapval(_cccd, hbst.getCCCD());
		if (m == null)
			hbst.setCCDS("");
		else
			hbst.setCCDS(Code.getFieldVal(m, "SYDSTB", ""));
		
		String key=hbst.getAPCD()+ "-" + hbst.getBSCD();
		
		m=Code.getMapval(_nobl, key);
		if (m==null) hbst.setNOBL("0");
		else hbst.setNOBL(Code.getFieldVal(m, "NOBL", "0"));
		
		m=Code.getMapval(_bscoll, key);
		if (m==null) {
			hbst.setSUMQX("0");
			hbst.setSUMCFBL("0");
		}else {
			hbst.setSUMQX(Code.getFieldVal(m, "QXRS", "0"));
			hbst.setSUMCFBL(Code.getFieldVal(m, "CFBL", "0"));
		}
		hbst.setINDT(Code.getFieldVal(mbs, "INDT", "N"));
	}

}
