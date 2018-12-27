package cn.com.sailin.falconweb.calculate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.com.sailin.falconweb.dao.Data;
import cn.com.sailin.falconweb.publiccode.Code;

public class Attend {

	private Data _data;

	private String _apcd;

	private String _bscd;

	private Calendar _start;

	private Calendar _end;

	private SimpleDateFormat _ft = new SimpleDateFormat("yyyyMMddHHmmss");

	private HashMap<String, Object> _wk;

	public Attend(String apcd, String bscd, String startdate, String enddate, Data data) {

		_data = data;
		_apcd = apcd;
		_bscd = bscd;
		_start = Calendar.getInstance();
		_start.clear();
		_start.set(Integer.parseInt(startdate.substring(0, 4)), Integer.parseInt(startdate.substring(4, 6)) - 1,
				Integer.parseInt(startdate.substring(6, 8)));
		_end = Calendar.getInstance();
		_end.clear();
		_end.set(Integer.parseInt(enddate.substring(0, 4)), Integer.parseInt(enddate.substring(4, 6)) - 1,
				Integer.parseInt(enddate.substring(6, 8)));
		// _end.add(Calendar.DATE, 1);

		_wk = new HashMap<String, Object>();

		List<Map<String, Object>> wkbs = _data.qryWkerbs(_apcd, _bscd);

		for (Map<String, Object> wk : wkbs) {
			_wk.put(Code.getFieldVal(wk, "IDCDNO", ""), wk);
		}

	}

	public List<Map<String, Object>> calculate() {

		// 获取考勤数据
		List<Map<String, Object>> chklist = _data.qryAttendcheckon(_apcd, _bscd, _ft.format(_start.getTime()),
				_ft.format(_end.getTime()));
		// 按人员日期汇总
		HashMap<String, Mandate> mmd = new HashMap<String, Mandate>();

		Calendar cur = Calendar.getInstance();
		for (Map<String, Object> m : chklist) {
			String idcdno = Code.getFieldVal(m, "sz_card_id", "");
			String date = Code.getFieldVal(m, "ts_card", "00000000000000");
			Mandate md = mmd.get(idcdno + "-" + date.substring(0, 8));
			if (md == null) {
				md = new Mandate();
				md.setIdcdno(idcdno);
				md.setWkid(Code.getFieldVal(m, "sz_employ_id", ""));
				md.setDate(date.substring(0, 8));
				md.setName(Code.getFieldVal(m, "sz_name", ""));
				md.setPost(Code.getFieldVal(m, "post", ""));

				Map<String, Object> wk = (Map<String, Object>) _wk.get(md.getIdcdno());
				if (wk == null)
					md.setInonemon("N");
				else
					md.setInonemon(Code.getFieldVal(wk, "INONEMON", ""));
			}
			cur.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1,
					Integer.parseInt(date.substring(6, 8)), Integer.parseInt(date.substring(8, 10)),
					Integer.parseInt(date.substring(10, 12)), Integer.parseInt(date.substring(12, 14)));

			if (md.getStarttime() == null) {
				md.setStarttime((Calendar) cur.clone());
			}
			if (md.getEndtime() == null) {
				md.setEndtime((Calendar) cur.clone());
			}
			if (cur.after(md.getEndtime())) {
				md.setEndtime((Calendar) cur.clone());
			}
			// System.out.println(JSON.toJSON(md) + Code.dtft.format(cur.getTime()));
			mmd.put(idcdno + "-" + date.substring(0, 8), md);
		}

		// System.out.println(JSON.toJSON(mmd));

		// 按人员汇总
		HashMap<String, Man> mman = new HashMap<String, Man>();

		for (Entry<String, Mandate> entry : mmd.entrySet()) {
			Man man = mman.get(entry.getValue().getIdcdno());
			if (man == null) {
				man = new Man();
				man.setIdcdno(entry.getValue().getIdcdno());
				man.setName(entry.getValue().getName());
				man.setSec(0);
				man.setPost(entry.getValue().getPost());
				man.setInonemon(entry.getValue().getInonemon());
				man.setStarttime(Code.dtft.format(entry.getValue().getStarttime().getTime()));
				man.setEndtime(Code.dtft.format(entry.getValue().getEndtime().getTime()));
				man.setWkid(entry.getValue().getWkid());
			}
			man.setSec(man.getSec() + entry.getValue().getSec());
			mman.put(man.getIdcdno(), man);
		}
		// System.out.println(JSON.toJSON(mman));

		// 写分包公司
		List<Map<String, Object>> listlccd = _data.qryWkerbs(_apcd, _bscd);
		for (Map<String, Object> m : listlccd) {
			Man man = mman.get(Code.getFieldVal(m, "IDCDNO", ""));
			if (man != null) {
				man.setLccd(Code.getFieldVal(m, "LCCD", ""));
			}
		}

		// 输出结果
		List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
		for (Entry<String, Man> man : mman.entrySet()) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("IDCDNO", man.getValue().getIdcdno());
			item.put("NAME", man.getValue().getName());
			item.put("WKID", man.getValue().getWkid());
			item.put("POST", man.getValue().getPost());
			float f = man.getValue().getSec();
			f = f / 60 / 60;
			item.put("WKTIME", f);
			f = f / 8;
			item.put("WKDS", String.valueOf(f));
			item.put("LCCD", man.getValue().getLccd());
			item.put("INONEMON", man.getValue().getInonemon());
			item.put("STARTTIME", man.getValue().getStarttime());
			item.put("ENDTIME", man.getValue().getEndtime());

			lm.add(item);
		}

		return lm;
	}

}

class Man {

	private String idcdno;

	private String name;

	private String post;

	private String lccd;

	private int sec;

	private String inonemon;

	// 用于每天发送住建的数据，按月统计的时候这个数据没用
	private String starttime;
	// 用于每天发送住建的数据，按月统计的时候这个数据没用
	private String endtime;

	private String wkid;

	public String getInonemon() {
		return inonemon;
	}

	public void setInonemon(String inonemon) {
		this.inonemon = inonemon;
	}

	public String getLccd() {
		return lccd;
	}

	public void setLccd(String lccd) {
		this.lccd = lccd;
	}

	public String getIdcdno() {
		return idcdno;
	}

	public void setIdcdno(String idcdno) {
		this.idcdno = idcdno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public int getSec() {
		return sec;
	}

	public void setSec(int sec) {
		this.sec = sec;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getWkid() {
		return wkid;
	}

	public void setWkid(String wkid) {
		this.wkid = wkid;
	}

}

// 记录人员一天的考勤数据，最早考勤，最晚考勤，上午是否考勤，下午是否考勤
class Mandate {

	private String idcdno;

	private String wkid;

	private String name;

	private String date;

	private Calendar starttime = null;

	private Calendar endtime = null;

	private String post;

	private String inonemon;

	public String getWkid() {
		return wkid;
	}

	public void setWkid(String wkid) {
		this.wkid = wkid;
	}

	public String getInonemon() {
		return inonemon;
	}

	public void setInonemon(String inonemon) {
		this.inonemon = inonemon;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getIdcdno() {
		return idcdno;
	}

	public void setIdcdno(String idcdno) {
		this.idcdno = idcdno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Calendar getStarttime() {
		return starttime;
	}

	public void setStarttime(Calendar starttime) {
		this.starttime = starttime;
	}

	public Calendar getEndtime() {
		return endtime;
	}

	public void setEndtime(Calendar endtime) {
		this.endtime = endtime;
	}

	public int getSec() {

		// if (starttime == null)
		// return 0;
		// if (endtime == null)
		// return 0;

		// long from = starttime.getTimeInMillis();
		// long to = endtime.getTimeInMillis();

		// return (int) ((to - from) / 1000);

		// 上午有考勤就算0.5天，4小时，240分钟，14400秒

		int sec = 0;

		if (getAm())
			sec = sec + 14400;
		if (getPm())
			sec = sec + 14400;

		return sec;

	}

	// 上午是否考勤
	public boolean getAm() {
		if (starttime != null) {
			if (starttime.get(Calendar.AM_PM) == Calendar.AM)
				return true;
		}
		if (endtime != null) {
			if (endtime.get(Calendar.AM_PM) == Calendar.AM)
				return true;
		}
		return false;
	}

	// 下午是否考勤
	public boolean getPm() {
		if (starttime != null) {
			if (starttime.get(Calendar.AM_PM) == Calendar.PM)
				return true;
		}
		if (endtime != null) {
			if (endtime.get(Calendar.AM_PM) == Calendar.PM)
				return true;
		}
		return false;
	}

}