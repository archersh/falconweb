package cn.com.sailin.falconweb.schedle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.sailin.falconweb.dao.Data;
import cn.com.sailin.falconweb.model.Uploaddata;
import cn.com.sailin.falconweb.model.Wkerattendtime;
import cn.com.sailin.falconweb.publiccode.Code;
import cn.com.sailin.falconweb.publiccode.sh;

@Component
@EnableScheduling
public class Schedle {

	@Autowired
	private Data data;

	private SimpleDateFormat _ft = new SimpleDateFormat("yyyyMMddHHmmss");

	@Scheduled(cron = "0 0 1 * * ?")
	public void importWkds() {
		String day = Code.getWeek(new Date());
		String pid = null;
		List<Map<String, Object>> lbsif = data.qryBsinfonodt();
		for (Map<String, Object> m : lbsif) {
			try {

				String ckbgdy = Code.getFieldVal(m, "CKBGDY", "");
				pid = data.getPid();
				Calendar today = Code.getToday();
				Calendar ckday = Calendar.getInstance();
				ckday.clear();
				String temp = _ft.format(today.getTime());
				ckday.set(Integer.parseInt(temp.substring(0, 4)), Integer.parseInt(temp.substring(4, 6)) - 1,
						Integer.parseInt(ckbgdy));

				if (ckday.after(today))
					ckday.add(Calendar.MONTH, -1);

				Calendar bgdy = (Calendar) ckday.clone();
				bgdy.add(Calendar.MONTH, -1);

				Calendar eddy = (Calendar) ckday.clone();

				Map<String, String> mbs = new HashMap<String, String>();
				mbs.put("APCD", Code.getFieldVal(m, "APCD", ""));
				mbs.put("BSCD", Code.getFieldVal(m, "BSCD", ""));
				mbs.put("STARTDATE", _ft.format(bgdy.getTime()));
				mbs.put("ENDDATE", _ft.format(eddy.getTime()));
				mbs.put("MONTH", _ft.format(bgdy.getTime()).substring(0, 6));

				String applydata = JSON.toJSONString(mbs);

				data.insertLog(day, pid, "importwkdsbybscdmonth", "Localhost:" + applydata, "SYS");

				String result = sh.importWkdsBybscdmonth("SYS", applydata, data);

				data.updateLog(day, pid, result);

			} catch (Exception e) {
				e.printStackTrace();
				data.updateLog(day, pid, "Error:" + e.getMessage());
			}
		}
	}

	@Scheduled(cron = "0 0 2 * * ?")
	public void housekeeping() {
		String day = Code.getWeek(new Date());
		String pid = data.getPid();
		try {
			Calendar clday = Code.getToday();
			clday.add(Calendar.DATE, 1);
			;
			String logday = Code.getWeek(clday.getTime());
			data.insertLog(day, pid, "truncatelog", day, "SYS");
			data.truncateLog(logday);
			data.updateLog(day, pid, "OK");
		} catch (Exception e) {
			e.printStackTrace();
			data.updateLog(day, pid, "Error:" + e.getMessage());
		}
	}

	@Scheduled(cron = "0 30 1 * * ?")
	public void uploadWkds() {
		String day = Code.getWeek(new Date());
		String pid = null;
		List<Map<String, Object>> lbsif = data.qryBsinfonodt();
		for (Map<String, Object> m : lbsif) {
			try {

				pid = data.getPid();
				Calendar today = Code.getToday();
				Calendar endday = Calendar.getInstance();
				String temp = _ft.format(today.getTime());
				endday.set(Integer.parseInt(temp.substring(0, 4)), Integer.parseInt(temp.substring(4, 6)) - 1,
						Integer.parseInt(temp.substring(6, 8)));
				Calendar startday = (Calendar) endday.clone();
				startday.add(Calendar.DATE, -1);

				Map<String, String> mbs = new HashMap<String, String>();
				mbs.put("APCD", Code.getFieldVal(m, "APCD", ""));
				mbs.put("BSCD", Code.getFieldVal(m, "BSCD", ""));
				mbs.put("STARTDATE", _ft.format(startday.getTime()));
				mbs.put("ENDDATE", _ft.format(endday.getTime()));
				mbs.put("MONTH", _ft.format(startday.getTime()).substring(0, 6));

				String applydata = JSON.toJSONString(mbs);

				data.insertLog(day, pid, "uploadwkds", "Localhost:" + applydata, "SYS");

				String result = sh.uploadwkds("SYS", applydata, data);

				data.updateLog(day, pid, result);

			} catch (Exception e) {
				e.printStackTrace();
				data.updateLog(day, pid, "Error:" + e.getMessage());
			}
		}
	}

	@Scheduled(initialDelay = 10000, fixedDelay = 5000)
	public void importAttendData() {
		List<Map<String, Object>> list = data.qryOplistbytype("attenddata");
		for (Map<String, Object> m : list) {
			JSONObject jcon = JSON.parseObject(Code.getFieldVal(m, "content", ""));
			jcon.put("apcd", Code.getFieldVal(m, "apcd", ""));
			JSONObject jo = JSON.parseObject(sh.importAttendData(jcon.toJSONString(), data));
			if (!Code.getFieldVal(jo, "MSGID", "").equals("0000")) {
				data.updateOplistresult(Code.getFieldVal(m, "opid", ""), jo.toJSONString());
			} else {
				data.delOplist(Code.getFieldVal(m, "opid", ""));
			}
		}
	}

	@Scheduled(initialDelay = 10000, fixedDelay = 5000)
	public void uploadInfo() {

		try {
			List<Map<String, Object>> list = data.qryOplistupload();
			for (Map<String, Object> m : list) {
				Uploaddata up = Uploaddata.buildObject(Code.getFieldVal(m, "apcd", ""), Code.getFieldVal(m, "bscd", ""),
						Code.getFieldVal(m, "type", ""), data);
				up.setContent(Code.getFieldVal(m, "content", ""));
				JSONObject jo = JSON.parseObject(uploadpost(up));
				if (!Code.getFieldVal(jo, "MSGID", "").equals("0000")) {
					data.updateOplistresult(Code.getFieldVal(m, "opid", ""), jo.toJSONString());
				} else {
					data.insertOplistsuccess(Code.getFieldVal(m, "apcd", ""), Code.getFieldVal(m, "bscd", ""),
							Code.getFieldVal(m, "opid", ""), Code.getFieldVal(m, "type", ""),
							Code.getFieldVal(m, "content", ""));
					data.delOplist(Code.getFieldVal(m, "opid", ""));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String uploadpost(Uploaddata up) {
		try {
			Map<String, String> m = new HashMap<String, String>();
			m.put("user", up.getUser());
			m.put("pass", up.getPass());
			m.put("content", up.getContent());
			String req = Code.postUploadHttp(up.getUrl(), m);
			JSONObject jo = JSON.parseObject(req);
			if (jo.getString("statusCode").equals("200")) {
				return Code.resultSuccess();
			} else {
				return Code.resultError("1111", req);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", e.getMessage());
		}
	}

	@Scheduled(initialDelay = 5000, fixedDelay = 5000)
	public void updateWkerattendtime() {
		int maxngid = data.getMaxngid();

		List<Map<String, Object>> list = data.qryAttenddata(maxngid);

		String place = "";
		String tscard = "";
		String apcd = "";
		String bscd = "";
		String wkerid = "";
		Wkerattendtime wktm = new Wkerattendtime();
		for (Map<String, Object> m : list) {
			place = Code.getFieldVal(m, "sz_dev_place", "");
			tscard = Code.getFieldVal(m, "attendtime", "");
			apcd = Code.getFieldVal(m, "apcd", "");
			if (apcd.equals(""))
				apcd = "HW";
			bscd = Code.getFieldVal(m, "branchid", "");
			wkerid = Code.getFieldVal(m, "sz_employ_id", "");

			wktm.setAPCD(apcd);
			wktm.setBSCD(bscd);
			wktm.setSZ_EMPLOY_ID(wkerid);
			wktm.setNGID(Code.getFieldVal(m, "ng_id", "0"));

			// 获取员工信息
			List<Map<String, Object>> wklist = data.qryWkerbsbyemploy(apcd, bscd, wkerid);

			if (wklist.size() > 0) {
				wktm.setBZ(Code.getFieldVal(wklist.get(0), "BZ", ""));
				wktm.setWKKD(Code.getFieldVal(wklist.get(0), "WKKD", ""));
				wktm.setIDCDNO(Code.getFieldVal(wklist.get(0), "IDCDNO", ""));
			}

			// 读取考勤时间
			List<Map<String, Object>> tmlist = data.qryWkerattendtime(apcd, bscd, wkerid);

			if (tmlist.size() > 0) {
				wktm.setINTIME(Code.getFieldVal(tmlist.get(0), "INTIME", ""));
				wktm.setOUTTIME(Code.getFieldVal(tmlist.get(0), "OUTTIME", ""));
			} else {
				wktm.setINTIME(Code.ZeroDatetime);
				wktm.setOUTTIME(Code.ZeroDatetime);
			}

			// 如果是进的话
			if (place.indexOf("入") != -1) {
				if (tscard.compareTo(wktm.getINTIME()) > 0)
					wktm.setINTIME(tscard);
			}
			// 如果是出的话
			if (place.indexOf("出") != -1) {
				if (tscard.compareTo(wktm.getOUTTIME()) > 0)
					wktm.setOUTTIME(tscard);
			}

			// 如果有记录
			if (tmlist.size() > 0)
				data.updateWkerattendtime(wktm);
			else
				data.insertWkerattendtime(wktm);
		}
	}

}
