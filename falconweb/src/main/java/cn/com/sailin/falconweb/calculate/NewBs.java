package cn.com.sailin.falconweb.calculate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.sailin.falconweb.dao.Data;
import cn.com.sailin.falconweb.publiccode.Code;

public class NewBs {

	private Data _data;

	private String _apcd;

	public NewBs(String apcd, Data data) {
		_data = data;
		_apcd = apcd;
	}

	public Object calculate() {

		List<Map<String, Object>> lbs = _data.qryBsinfo(_apcd);
		HashMap<String, Map<String, Object>> mlbs = new HashMap<String, Map<String, Object>>();
		for (Map<String, Object> m : lbs) {
			mlbs.put(Code.getFieldVal(m, "BSCD", ""), m);
		}

		List<Map<String, String>> newbs = new ArrayList<Map<String, String>>();
		List<Map<String, Object>> rbs = _data.qryAttendbsinfo(_apcd);
		for (Map<String, Object> m : rbs) {
			Object obj = mlbs.get(Code.getFieldVal(m, "sz_set_id", ""));
			if (obj == null) {
				Map<String, String> item = new HashMap<String, String>();
				item.put("CODE", Code.getFieldVal(m, "sz_set_id", ""));
				item.put("NAME", Code.getFieldVal(m, "sz_name", ""));
				newbs.add(item);
			}
		}
		
		return newbs;
		
	}
}
