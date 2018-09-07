package cn.com.sailin.falconweb.calculate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.sailin.falconweb.dao.Data;
import cn.com.sailin.falconweb.publiccode.Code;

public class NewWorker {
	
	private Data _data;
	
	private String _apcd;
	
	private String _bscd;
	
	public NewWorker(String apcd,String bscd,Data data){
		
		_data = data;
		_apcd = apcd;
		_bscd = bscd;
		
	}

	public Object calculate(){
		
		List<Map<String,Object>> lwk=_data.qryWker(_apcd, _bscd);
		HashMap<String,Map<String,Object>> mlwk=new HashMap<String,Map<String,Object>>();
		for (Map<String,Object> m : lwk){
			mlwk.put(Code.getFieldVal(m, "IDCDNO", ""), m);
		}
		
		List<Map<String,String>> newwker=new ArrayList<Map<String,String>>();
		List<Map<String,Object>> rwk=_data.qryAttendWker(_apcd, _bscd);
		for (Map<String,Object> m: rwk){
			Object obj = mlwk.get(Code.getFieldVal(m, "sz_card_id", ""));
			if (obj==null){
				HashMap<String,String> item = new HashMap<String,String>();
				item.put("IDCDNO",Code.getFieldVal(m,"sz_card_id",""));
				item.put("NAME", Code.getFieldVal(m, "sz_name", ""));
				item.put("POST", Code.getFieldVal(m,"post",""));
				
				newwker.add(item);
			}
		}
		
		return newwker;
	}
}
