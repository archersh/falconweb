package cn.com.sailin.falconweb.calculate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import cn.com.sailin.falconweb.dao.Data;
import cn.com.sailin.falconweb.model.Bsst;
import cn.com.sailin.falconweb.publiccode.Code;

public class BuildsiteBalance {

	public BuildsiteBalance(String reqtype, String reqval, Data data) {
		_data = data;
		_dtft = new SimpleDateFormat("yyyyMMddHHmmss");
		_reqtype = reqtype;
		_reqval = reqval;
		_setbs = new HashSet<String>();
		_kxts = _data.getKxts();
	}

	private Data _data;

	private HashMap<String, Object> _svdp = null;

	private HashMap<String, Object> _bkcd = null;

	private HashMap<String, Object> _cccd = null;

	private HashMap<String, Object> _bsbl = null;

	private HashMap<String, Object> _bscoll = null;

	private HashMap<String, Map<String, Object>> _sumrs = null;

	private HashMap<String, String> _bslc = null;

	private SimpleDateFormat _dtft = null;

	private String _reqtype;

	private String _reqval;

	private HashSet<String> _setbs = null;

	private Calendar _today = Code.getToday();

	private int _kxts;

	/*
	 * 获取工地状态信息
	 */
	private void getSvdp() {
		_svdp = new HashMap<String, Object>();
		List<Map<String, Object>> list = _data.qrySvdp();
		for (Map<String, Object> m : list) {
			_svdp.put(Code.getFieldVal(m, "SYIDTB", "").trim(), m);
		}
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

	private void getBsbl(String month) {
		_bsbl = new HashMap<String, Object>();
		List<Map<String, Object>> list = _data.qrySpanbl(month);
		for (Map<String, Object> m : list) {
			_bsbl.put(Code.getFieldVal(m, "APCD", "").trim() + "-" + Code.getFieldVal(m, "BSCD", "").trim(), m);
		}
		return;
	}

	private void getBscoll(String month) {
		_bscoll = new HashMap<String, Object>();
		List<Map<String, Object>> list = _data.qryBscollinfobymonth(month);
		for (Map<String, Object> m : list) {
			_bscoll.put(Code.getFieldVal(m, "APCD", "") + "-" + Code.getFieldVal(m, "BSCD", ""), m);
		}
		return;
	}

	private void getSumrs() {
		_sumrs = new HashMap<String, Map<String, Object>>();
		List<Map<String, Object>> list = _data.qrySumqx();
		for (Map<String, Object> m : list) {
			_sumrs.put(Code.getFieldVal(m, "APCD", "") + "-" + Code.getFieldVal(m, "BSCD", ""), m);
		}

		System.out.println(JSON.toJSONString(_sumrs));
	}

	private void getBslist() {
		List<Map<String, Object>> lbs = null;
		if (_reqtype.equals("SV"))
			lbs = _data.qryBslistbysvdp(_reqval);
		if (_reqtype.equals("BK"))
			lbs = _data.qryFullbslistbybkcd(_reqval);
		if (_reqtype.equals("CC"))
			lbs = _data.qryBslistbycccd(_reqval);
		if (lbs != null) {
			for (Map<String, Object> m : lbs) {
				_setbs.add(Code.getFieldVal(m, "APCD", "") + "-" + Code.getFieldVal(m, "BSCD", ""));
			}
		}
	}

	private void getBsls() {
		List<Map<String, Object>> lbs = _data.qryBslc();
		_bslc = new HashMap<String, String>();
		for (Map<String, Object> m : lbs) {
			_bslc.put(Code.getFieldVal(m, "APCD", "").trim() + "-" + Code.getFieldVal(m, "BSCD", ""),
					Code.getFieldVal(m, "LCCD", ""));
		}
	}

	// private void calculateBalance(Bsst bsst) {
	// Calendar today = Code.getToday();
	// // 获取当月的发薪日和余额预警日
	// Calendar payday = Calendar.getInstance();
	// payday.clear();
	// payday.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
	// Integer.parseInt(bsst.getPYDY()));
	// Calendar blday = Calendar.getInstance();
	// blday.clear();
	// blday.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
	// Integer.parseInt(bsst.getBLDY()));
	// // 余额预警开始结束时间
	// Calendar blbg = (Calendar) blday.clone();
	// Calendar bled = (Calendar) payday.clone();
	// // 是否在监管期
	// boolean inWn = false;
	// // 如果今天比当前月余额早
	// if (today.before(blday)) {
	// // 如果今天比当月发薪早
	// if (today.before(payday)) {
	// // 如果当月余额比当月发薪早
	// if (blday.before(payday)) {
	// inWn = false;
	// payday.add(Calendar.MONTH, -1);// 看上个月欠薪
	// } else {
	// inWn = true;
	// blday.add(Calendar.MONTH, -1);
	// blbg.setTime(blday.getTime());
	// }
	// } else {
	// // 如果当月余额比当月发薪晚而且当月余额早
	// inWn = false;
	// payday.add(Calendar.MONTH, -1);// 看上个月欠薪
	// }
	// } else {
	// // 如果今天比当前月余额晚
	// // 如果今天比当月发薪早
	// if (today.before(payday)) {
	// inWn = true;
	// } else {
	// // 如果今天比当月发薪晚
	// // 如果当月余额比当月发薪早
	// if (blday.before(payday))
	// inWn = false;
	// else {
	// inWn = true;
	// bled.add(Calendar.MONTH, +1);
	// }
	// }
	// }
	// bsst.setBLDATE(_dtft.format(blbg.getTime()));
	// bsst.setBLMONTH(_dtft.format(bled.getTime()).substring(0, 6));
	// bsst.setPYDATE(_dtft.format(payday.getTime()));
	// bsst.setPYMONTH(_dtft.format(payday.getTime()).substring(0, 6));
	// // 如果没在监管期
	// if (!inWn) {
	// bsst.setBLSTATUS("E");
	// bsst.setBLSTRM("还未进入监管期");
	// } else {
	// Map<String, Object> spanbl = Code.getMapval(_bsbl, bsst.getAPCD()
	// + "-" + bsst.getBSCD());
	// if (spanbl == null) {
	// bsst.setBLSTATUS("E");
	// bsst.setBLSTRM("当前余额银行还未登记");
	// } else {
	// // 获取余额时间
	// String spdt = Code
	// .getFieldVal(spanbl, "CHDT", "00000000000000");
	// Calendar spcl = Calendar.getInstance();
	// spcl.clear();
	// spcl.set(Integer.parseInt(spdt.substring(0, 4)),
	// Integer.parseInt(spdt.substring(4, 2)),
	// Integer.parseInt(spdt.substring(6, 2)));
	// // 如果时间有效
	// if (spcl.after(blbg) && spcl.before(bled)) {
	// bsst.setCURBL(Code.getFieldVal(spanbl, "SPANBL", "0"));
	// int currbl = Integer.parseInt(bsst.getCURBL());
	// int setbl = Integer.parseInt(bsst.getSPANBL());
	// if (currbl < setbl) {
	// bsst.setBLSTATUS("Y");
	// bsst.setBLSTRM("当前余额满足");
	// } else {
	// bsst.setBLSTATUS("N");
	// bsst.setBLSTRM("当前余额不足");
	// }
	// } else {
	// bsst.setBLSTATUS("E");
	// bsst.setBLSTRM("当前余额银行还未登记");
	// }
	// }
	// }
	//
	// // 欠薪
	// Map<String, Object> q = Code.getMapval(_bscoll, bsst.getPYMONTH() + "-"
	// + bsst.getAPCD() + "-" + bsst.getBSCD());
	// if (q == null) {
	// bsst.setQXSTATUS("E");
	// bsst.setQXRM("没有计算结果");
	// } else {
	// int qxrs = Integer.parseInt(Code.getFieldVal(q, "QXRS", "-1"));
	// bsst.setQXRS(String.valueOf(qxrs));
	// if (qxrs == 0) {
	// bsst.setQXSTATUS("N");
	// bsst.setQXRM("没有欠薪");
	// }
	// if (qxrs < 0) {
	// bsst.setQXSTATUS("E");
	// bsst.setQXRM("欠薪人数异常");
	// }
	// if (qxrs > 0) {
	// bsst.setQXSTATUS("Y");
	// bsst.setQXRM("存在欠薪");
	// }
	// }
	//
	// //总的欠薪人次
	// String sumqxrs=_sumqx.get(bsst.getAPCD()+ "-" + bsst.getBSCD());
	// if (sumqxrs==null) sumqxrs="0";
	//
	// bsst.setSUMQXRS(sumqxrs);
	//
	// return;
	// }

	private void buildBsstinfo(Map<String, Object> mbs, Bsst bsst) {

		Map<String, Object> m;
		// 初值
		bsst.setAPCD(Code.getFieldVal(mbs, "APCD", ""));
		bsst.setBCNM(Code.getFieldVal(mbs, "BCNM", ""));
		bsst.setBKCD(Code.getFieldVal(mbs, "BKCD", ""));
		m = Code.getMapval(_bkcd, Code.getFieldVal(mbs, "BKCD", ""));
		if (m == null)
			bsst.setBKDS("");
		else
			bsst.setBKDS(Code.getFieldVal(m, "SYDSTB", ""));
		bsst.setBSCD(Code.getFieldVal(mbs, "BSCD", ""));
		bsst.setBSDS(Code.getFieldVal(mbs, "BSDS", ""));
		bsst.setCCCD(Code.getFieldVal(mbs, "CCCD", ""));
		m = Code.getMapval(_cccd, Code.getFieldVal(mbs, "CCCD", ""));
		if (m == null)
			bsst.setCCCD("");
		else
			bsst.setCCDS(Code.getFieldVal(m, "SYDSTB", ""));
	    String lccd=_bslc.get(bsst.getAPCD()+ "-" + bsst.getBSCD());
	    if (lccd==null) { lccd="";};
	    if (!lccd.equals("")) {
	    	m = Code.getMapval(_cccd, lccd);
	    	if (m==null)
	    		bsst.setLCNAME("");
	    	else
	    		bsst.setLCNAME(Code.getFieldVal(m, "SYDSTB", ""));
	    }else
	    {
	    	bsst.setLCNAME("");
	    }
		bsst.setSPANBL(Code.getFieldVal(mbs, "SPANRQBL", "0"));
		bsst.setSVDP(Code.getFieldVal(mbs, "SVDP", ""));
		bsst.setBLDY(Code.getFieldVal(mbs, "BLWNDY", "0"));
		bsst.setPYDY(Code.getFieldVal(mbs, "PYDY", "0"));
		bsst.setPYDATE(bsst.getPYMONTH() + Code.getFillStr(bsst.getPYDY(), "L", 2, "0"));

		m = Code.getMapval(_bsbl, bsst.getAPCD() + "-" + bsst.getBSCD());
		if (m == null) {
			bsst.setBLSTATUS("E");
			bsst.setBLSTRM("当前月进度款额度银行还未登记");
		} else {
			bsst.setCURBL(Code.getFieldVal(m, "SPANBL", "0"));
			float currbl = Float.parseFloat(bsst.getCURBL());
			float setbl = Float.parseFloat(bsst.getSPANBL());
			if (currbl < setbl) {
				bsst.setBLSTATUS("Y");
				bsst.setBLSTRM("当前额度满足");
			} else {
				bsst.setBLSTATUS("N");
				bsst.setBLSTRM("当前额度不足");
			}
		}

		m = Code.getMapval(_bscoll, bsst.getAPCD() + "-" + bsst.getBSCD());
		if (m == null) {
			bsst.setQXSTATUS("E");
			bsst.setQXRM("没有计算结果");
			bsst.setQXRS("0");
		} else {
			// 以确认数据为基础的欠薪人数
			int qxrs = Integer.parseInt(Code.getFieldVal(m, "QXRSCFM", "0"));
			int ffrs = Integer.parseInt(Code.getFieldVal(m, "FFRS", "0"));
			int wkrs = Integer.parseInt(Code.getFieldVal(m, "WKRS", "0"));
			bsst.setQXRS(String.valueOf(qxrs));
			bsst.setFFRS(String.valueOf(ffrs));
			bsst.setCKRS(String.valueOf(wkrs));

			// 发薪日期
			Calendar dtff = Calendar.getInstance();
			dtff.clear();
			dtff.set(Integer.parseInt(bsst.getPYDATE().substring(0, 4)),
					Integer.parseInt(bsst.getPYDATE().substring(4, 6)) - 1,
					Integer.parseInt(bsst.getPYDATE().substring(6, 8)));
			Calendar dtkx = (Calendar) dtff.clone();
			dtkx.add(Calendar.DATE, _kxts);
			if (qxrs == 0) {
				bsst.setQXSTATUS("N");
				bsst.setQXRM("没有欠薪");
			}
			if (qxrs < 0) {
				bsst.setQXSTATUS("E");
				bsst.setQXRM("欠薪人数异常");
			}
			if (qxrs > 0) {
				bsst.setQXSTATUS("Y");
				bsst.setQXRM("存在欠薪");
			}
			if (_today.after(dtff) && _today.before(dtkx))
				bsst.setQXRM(bsst.getQXRM() + "[目前在宽限期内]");
		}

		// 总的欠薪人次
		Map<String, Object> sumrs = _sumrs.get(bsst.getAPCD() + "-" + bsst.getBSCD());
		String sumqxrs = "0";
		String sumffrs = "0";
		if (sumrs == null) {
			sumqxrs = "0";
			sumffrs = "0";
		} else {
			sumqxrs = Code.getFieldVal(sumrs, "sumqxrs", "0");
			sumffrs = Code.getFieldVal(sumrs, "sumffrx", "0");
		}
		bsst.setSUMQXRS(sumqxrs);
		bsst.setSUMFFRS(sumffrs);

		return;
	}

	public Object calculate() {

		Calendar today = Code.getToday();
		// 余额监管月份
		String blmonth = _dtft.format(today.getTime()).substring(0, 6);
		Calendar pyday = Code.getToday();
		pyday.add(Calendar.MONTH, -1);
		// 欠薪监管月份
		String pymonth = _dtft.format(pyday.getTime()).substring(0, 6);
		getSvdp();
		getBkcd();
		getCccd();
		getBsbl(blmonth);
		getBscoll(pymonth);
		getSumrs();
		getBslist();		
		getBsls();

		// 获取工地列表
		List<Map<String, Object>> bslist = _data.qryBsinfonodt();
		// 遍历工地信息
		List<Bsst> bsstlist = new ArrayList<Bsst>();
		for (Map<String, Object> mbs : bslist) {
			// 是否在查询的工地范围内
			if (_setbs.contains(Code.getFieldVal(mbs, "APCD", "") + "-" + Code.getFieldVal(mbs, "BSCD", ""))) {
				Bsst bsst = new Bsst();
				bsst.setBLMONTH(blmonth);
				bsst.setPYMONTH(pymonth);
				buildBsstinfo(mbs, bsst);
				bsstlist.add(bsst);
			}
		}
		return bsstlist;

	}

}
