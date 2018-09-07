package cn.com.sailin.falconweb.calculate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.com.sailin.falconweb.dao.Data;
import cn.com.sailin.falconweb.model.Wkds;
import cn.com.sailin.falconweb.publiccode.Code;

public class NoffWorker {

	private Data _data;

	private String _month;

	private String _apcd;

	private String _bscd;

	public NoffWorker(String month, String apcd, String bscd, Data data) {
		_data = data;
		_month = month;
		_apcd = apcd;
		_bscd = bscd;
	}

	public Object calculate() {
		// 获取考勤数据
		List<Map<String, Object>> wkdslist = _data
				.qryWkds(_month, _apcd, _bscd);
		// 获取发薪数据
		List<Map<String, Object>> apitlist = _data.qryAcpyitem(_month, _apcd,
				_bscd);

		// 建考勤数据表
		HashMap<String, Wkds> wkdsmap = new HashMap<String, Wkds>();
		for (Map<String, Object> mwk : wkdslist) {
			Wkds wk = new Wkds();
			wk.setAPCD(_apcd);
			wk.setBSCD(_bscd);
			wk.setDT(Code.getFieldVal(mwk, "DT", ""));
			wk.setIDCDNO(Code.getFieldVal(mwk, "IDCDNO", ""));
			wk.setINFF("N");
			wk.setMONTH(_month);
			wk.setNAME(Code.getFieldVal(mwk, "NAME", ""));
			wk.setPOST(Code.getFieldVal(mwk, "POST", ""));
			wk.setUR(Code.getFieldVal(mwk, "UR", ""));
			wk.setWKDS(Code.getFieldVal(mwk, "WKDS", "0"));

			wkdsmap.put(wk.getIDCDNO(), wk);
		}
		// 在考勤数据表中找已发薪的
		for (Map<String, Object> map : apitlist) {
			Wkds wk = wkdsmap.get(Code.getFieldVal(map, "IDCDNO", ""));
			if (wk != null) {
				wk.setACPY(Code.getFieldVal(map, "ACPY", ""));
				wk.setINFF(Code.getFieldVal(map, "INBKCFM", "N"));
			}
		}
		// 生成结果数据
		List<Wkds> reslist = new ArrayList<Wkds>();
		for (Entry<String, Wkds> entry : wkdsmap.entrySet()) {
			if (entry.getValue().getINFF().equals("N")) {
				reslist.add(entry.getValue());
			}
		}

		return reslist;
	}
}
