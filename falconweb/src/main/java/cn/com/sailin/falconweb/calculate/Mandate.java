package cn.com.sailin.falconweb.calculate;

import java.util.Calendar;

//记录人员一天的考勤数据，最早考勤，最晚考勤，上午是否考勤，下午是否考勤
public class Mandate {

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