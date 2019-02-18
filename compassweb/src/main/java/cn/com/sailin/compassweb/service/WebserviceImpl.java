package cn.com.sailin.compassweb.service;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.sailin.compassweb.config.Config;
import cn.com.sailin.compassweb.dao.Data;
import cn.com.sailin.compassweb.model.Checkcnplan;
import cn.com.sailin.compassweb.model.Cnplanorderrefund;
import cn.com.sailin.compassweb.model.Cnplanphotoresend;
import cn.com.sailin.compassweb.model.Cnplanstop;
import cn.com.sailin.compassweb.model.Cnplanupdate;
import cn.com.sailin.compassweb.model.DataCtnTkDriver;
import cn.com.sailin.compassweb.model.DataCtntk;
import cn.com.sailin.compassweb.model.DataCtntkReleaseDetail;
import cn.com.sailin.compassweb.model.Outstart;
import cn.com.sailin.compassweb.model.Outsuccess;
import cn.com.sailin.compassweb.model.ResultCtntk;
import cn.com.sailin.compassweb.model.Uptruck;
import cn.com.sailin.compassweb.publiccode.Code;
import cn.com.sailin.compassweb.publiccode.HttpConnector;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import java.text.SimpleDateFormat;

public class WebserviceImpl implements WebserviceInterface {

	@Autowired
	private Config config;

	@Autowired
	private Data data;

	@Override
	public String sayHello(String user) {
		return "Hello " + user;
	}

	@Override
	public String CallService(String method, String applyData) {
		if (method.equals("sendbarcode"))
			return sendBarcode(applyData);
		if (method.equals("sendhexiao"))
			return sendHexiao(applyData);
		if (method.equals("readbarcode"))
			return readBarcode(applyData);
		if (method.equals("readbooking"))
			return readBooking();
		if (method.equals("confirmbooking"))
			return confirmBooking(applyData);
		if (method.equals("refusebooking"))
			return refuseBooking(applyData);
		if (method.equals("checkcnplan"))
			return checkCnplan(applyData);
		if (method.equals("outsuccess"))
			return outSuccess(applyData);
		if (method.equals("uptruck"))
			return upTruck(applyData);
		if (method.equals("cnplanorderrefund"))
			return cnPlanorderrefund(applyData);
		if (method.equals("outstart"))
			return outStart(applyData);
		if (method.equals("cnplanphotoresend"))
			return cnPlanphotoresend(applyData);
		if (method.equals("cnplanstop"))
			return cnPlanstop(applyData);
		if (method.equals("cnplanupdate"))
			return cnPlanupdate(applyData);
		if (method.equals("sendygtno"))
			return sendYgtno(applyData);
		return "";
	}

	@Override
	public String invoke(String jobId, String applyData) {
		String proid = data.getPid();
		Date dt = new Date();
		String day = Code.getWeek(dt);

		data.insertLog(day, proid, jobId, applyData);

		String result = "";

		if (jobId.equals("ctntk"))
			result = doctntk(applyData);

		return result;
	}

	public String doctntk(String applyData) {
		DataCtntk tk = JSON.parseObject(applyData, DataCtntk.class);
		ResultCtntk rv = new ResultCtntk();

		rv.setId(tk.getID());
		rv.setJobId(tk.getJobId());
		rv.setPlanNumber(tk.getPlanNumber());

		// 判断id是否已经调用
		String resultinfo = data.readResultinfo(tk.getID());
		if (!resultinfo.equals("")) {
			return resultinfo;
		} else {

			List<Map<String, Object>> lid = data.qryctntkbyid(tk.getID());

			if (lid.size() > 0) {
				rv.setMsgId("9999");
				rv.setMsgDESC("ID已存在不能重复调用");
				rv.setYardMsgInfo("ID已存在不能重复调用");
				rv.setAllowFlag("N");
				return JSON.toJSONString(rv);
			}

			try {
				if (!tk.getTkValidityBegin().trim().equals("")) {
					tk.setTkValidityBegin(tk.getTkValidityBegin().substring(0, 4)
							+ tk.getTkValidityBegin().substring(5, 7) + tk.getTkValidityBegin().substring(8, 10));

				}
			} catch (Exception e) {
				rv.setMsgId("9999");
				rv.setMsgDESC("提箱有效开始日期" + tk.getTkValidityBegin() + "格式不对");
				rv.setYardMsgInfo(rv.getMsgDESC());
				rv.setAllowFlag("N");
				return JSON.toJSONString(rv);
			}

			try {
				if (!tk.getTkValidity().trim().equals("")) {
					tk.setTkValidity(tk.getTkValidity().substring(0, 4) + tk.getTkValidity().substring(5, 7)
							+ tk.getTkValidity().substring(8, 10));
				}
			} catch (Exception e) {
				rv.setMsgId("9999");
				rv.setMsgDESC("提箱有效结束日期" + tk.getTkValidity() + "格式有误");
				rv.setYardMsgInfo(rv.getMsgDESC());
				rv.setAllowFlag("N");
				return JSON.toJSONString(rv);
			}

			if (tk.getSubscribFlag().equals("Y")) {
				rv.setMsgId("9999");
				rv.setMsgDESC("没有可留箱的箱子");
				rv.setYardMsgInfo(rv.getMsgDESC());
				return JSON.toJSONString(rv);
			}

		}

		// 加主数据
		data.insertctnck(tk);
		// 加放箱信息
		for (DataCtntkReleaseDetail rd : tk.getReleaseDetail()) {
			data.insertReleaseDetail(tk.getID(), rd);
		}
		// 加集卡预约信息
		for (DataCtnTkDriver truck : tk.getTkDriver()) {
			data.insertTkDriver(tk.getID(), truck);
		}

		HashMap<String, String> rm;

		rm = (HashMap<String, String>) data.storedProcedure("CTNTK", "ZJYGT", "", tk.getID());

		if (rm.get("flag").equals("0")) {
			rv.setMsgId("0000");
			rv.setMsgDESC("预约成功");
			rv.setYardMsgInfo("预约成功");
			rv.setAllowFlag("Y");
			String res = JSON.toJSONString(rv);
			data.insertResultinfo(rv.getId(), res);
			return res;
		} else {
			rv.setMsgId("9999");
			rv.setMsgDESC(rm.get("errmsg"));
			rv.setYardMsgInfo(rm.get("errmsg"));
			rv.setAllowFlag("N");
			if (rm.get("errmsg").equals("找不到该预约信息")) {
				rv.setMsgId("2222");
			}
			return JSON.toJSONString(rv);
		}
	}

	private String getUrl() {
		try {
			Date dt = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String timestamp = df.format(dt);
			String timestamp1 = URLEncoder.encode(timestamp, "utf-8");
			String comcode = data.getCompanycode();
			String user_id="";
			String secret="";
			if (comcode.equals("NB")) {
				user_id = "nbax";
				secret = "b176a43bc589b8f3b37fa7324488asxx";
			}
			if (comcode.equals("YJDC")) {
				user_id = "HXM117321";
				secret = "4kop352e03cmscnbdepoth125462jgf";
			}
			String sign = Code.md5(user_id + secret + timestamp);
			return config.getSfurl() + "random=" + Math.random() + "&sign=" + sign + "&user_id=" + user_id
					+ "&timestamp=" + timestamp1;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private String getWxurl() {
		return config.getSfwxurl();
	}

	private String sendBarcode(String barcode) {
		try {
			Date dt = new Date();
			String day = Code.getWeek(dt);
			String pid = data.getPid();
			String format = "json";
			String method = "yardnew.scan";
			String apiUrl = getUrl() + "&format=" + format + "&method=" + method;
			StringBuffer url = new StringBuffer();
			url.append(apiUrl);
			url.append("&barcode=" + barcode);

			data.insertLog(day, pid, "sfpt", url.toString());

			String result = Code.getHttp(url.toString());

			data.updateLog(day, pid, result);

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private String sendYgtno(String applyData) {
		JSONObject jo = JSONObject.parseObject(applyData);
		String blno = jo.getString("blno");
		String cntr = jo.getString("cntr");
		List<Map<String, Object>> list = data.qrySgdtcfm(blno, cntr);
		for (Map<String, Object> m : list) {
			JSONObject jm = new JSONObject();
			jm.put("ID", Code.dtft.format(Calendar.getInstance().getTime()));
			jm.put("JOBID", "SendYgtNO");
			if (Code.getFieldVal(m, "backpos", "").equals("DC42351333")) {
				jm.put("USERNAME", "17867985342");
				jm.put("USERPASSWORD", "AX27698048");
			} else {
				jm.put("USERNAME", "13567910431");
				jm.put("USERPASSWORD", "AX27698048");
			}
			jm.put("CTNNUMBER", 1);
			jm.put("SENDTIME", Code.dtftfull.format(Calendar.getInstance().getTime()));
			jm.put("BUSINESSTYPE", "I");
			jm.put("RSV", "");
			jm.put("RSV01", "");
			jm.put("RSV02", "");
			jm.put("RSV03", "");
			jm.put("RSV04", "");
			JSONObject jd = new JSONObject();
			jd.put("ACTUALLOCATION", Code.getFieldVal(m, "lcpt", ""));
			jd.put("ACTUALPOSITIONBLOCK", Code.getFieldVal(m, "backpos", ""));
			jd.put("CTNNO", cntr);
			jd.put("BLNO", blno);
			jd.put("SEALNO", "");
			jd.put("FREEDC", Code.getFieldVal(m, "freeday", ""));
			jd.put("VESSELNAMEE", Code.getFieldVal(m, "vessel", ""));
			jd.put("VESSELUNCODE", Code.getFieldVal(m, "uncode", ""));
			jd.put("VESSELVOYAGE", Code.getFieldVal(m, "voyage", ""));
			jd.put("CTNOPERATORCODE", Code.getFieldVal(m, "ctowner", ""));
			jd.put("CTNSIZETYPE", Code.getFieldVal(m, "ctszty", ""));
			jd.put("OUTSTATUS", Code.getFieldVal(m, "ccmd", ""));
			jd.put("INSTATUS", Code.getFieldVal(m, "jcmd", ""));
			jd.put("EIRNO", "");
			jd.put("RELEASETIME", Code.getFieldVal(m, "cmftime", ""));
			jd.put("INBOXCOMPANYNAME", Code.getFieldVal(m, "yxr", ""));
			jd.put("VESSELAGENT", "YM");
			jd.put("REMARKS", Code.getFieldVal(m, "remark", ""));
			jd.put("RSV01", "");
			jd.put("RSV02", "");
			jd.put("RSV03", "");
			jd.put("RSV04", "");
			jd.put("RSV05", "");
			jd.put("RSV06", "");
			jd.put("RSV07", "");
			jd.put("RSV08", "");
			jd.put("RSV09", "");
			jd.put("RSV10", "");
			JSONArray ja = new JSONArray();
			ja.add(jd);
			jm.put("CTNINFOS", ja);
			JSONArray jaa = new JSONArray();
			jaa.add("SendYgtNO");
			jaa.add(jm);
			try {
				JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
				Client client = dcf.createClient(config.getYgturl());
				Object[] objects = new Object[0];
				objects = client.invoke("callEDIESBPub", "ZJYGT", "ZJYGT", "SendYgtNO", jaa.toJSONString(), "", "");
				return objects[0].toString();
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
		return "";
	}

	private String sendHexiao(String xml) {
		try {
			Date dt = new Date();
			String day = Code.getWeek(dt);
			String pid = data.getPid();
			String format = "xml";
			String method = "yardnew.hexiao";
			String apiUrl = getUrl() + "&format=" + format + "&method=" + method;
			StringBuffer url = new StringBuffer();
			url.append(apiUrl);

			Map<String, String> m = new HashMap<String, String>();

			m.put("content", xml);

			data.insertLog(day, pid, "sfpt", url.toString());

			// String result = Code.postHttp(url.toString(), m);

			String result = HttpConnector.doPost(url.toString(), m);

			data.updateLog(day, pid, result);

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private String readBarcode(String barcode) {
		try {
			Date dt = new Date();
			String day = Code.getWeek(dt);
			String pid = data.getPid();
			String format = "json";
			String method = "yardnew.register";
			String apiUrl = getUrl() + "&format=" + format + "&method=" + method;
			StringBuffer url = new StringBuffer();
			url.append(apiUrl);
			url.append("&classid=3");
			url.append("&type=2");
			url.append("&arg=" + barcode);

			data.insertLog(day, pid, "sfpt", url.toString());

			String result = Code.getHttp(url.toString());

			data.updateLog(day, pid, result);

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private String readBooking() {
		try {
			Date dt = new Date();
			String day = Code.getWeek(dt);
			String pid = data.getPid();
			String format = "json";
			String method = "yardnew.register";
			String apiUrl = getUrl() + "&format=" + format + "&method=" + method;
			StringBuffer url = new StringBuffer();
			url.append(apiUrl);
			url.append("&type=1");

			data.insertLog(day, pid, "sfpt", url.toString());

			String result = Code.getHttp(url.toString());

			data.updateLog(day, pid, result);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private String confirmBooking(String applyData) {
		try {
			Date dt = new Date();
			String day = Code.getWeek(dt);
			String pid = data.getPid();
			String format = "json";
			String method = "yardnew.confirm";
			String apiUrl = getUrl() + "&format=" + format + "&method=" + method;
			StringBuffer url = new StringBuffer();
			url.append(apiUrl);

			JSONObject obj = JSON.parseObject(applyData);

			url.append("&businessid=" + Code.getFieldVal(obj, "businessid", ""));
			url.append("&txddcode=" + Code.getFieldVal(obj, "txddcode", ""));
			url.append("&txddplace=" + Code.getFieldVal(obj, "txddplace", ""));
			url.append("&account=" + Code.getFieldVal(obj, "account", ""));
			url.append("&boxshu=" + Code.getFieldVal(obj, "boxshu", ""));
			url.append("&billno=" + Code.getFieldVal(obj, "billno", ""));

			data.insertLog(day, pid, "sfpt", url.toString());

			String result = Code.getHttp(url.toString());

			data.updateLog(day, pid, result);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private String refuseBooking(String applyData) {
		try {
			Date dt = new Date();
			String day = Code.getWeek(dt);
			String pid = data.getPid();
			String format = "json";
			String method = "yardnew.confirm";
			String apiUrl = getUrl() + "&format=" + format + "&method=" + method;
			StringBuffer url = new StringBuffer();
			url.append(apiUrl);

			JSONObject obj = JSON.parseObject(applyData);

			url.append("&businessid=" + Code.getFieldVal(obj, "businessid", ""));
			url.append("&mode=" + Code.getFieldVal(obj, "mode", ""));
			url.append("&rcode=" + Code.getFieldVal(obj, "rcode", ""));
			url.append("&reason=" + Code.getFieldVal(obj, "reason", ""));
			url.append("&account=" + Code.getFieldVal(obj, "account", ""));

			data.insertLog(day, pid, "sfpt", url.toString());

			String result = Code.getHttp(url.toString());

			data.updateLog(day, pid, result);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public String getData() {
		HashSet<String> set = new HashSet<String>();
		set.add("1");
		set.add("2");
		return JSONUtils.toJSONString(set);
	}

	public String checkCnplan(String applyData) {
		try {
			Date dt = new Date();
			String day = Code.getWeek(dt);
			String pid = data.getPid();

			data.insertLog(day, pid, "sfpt", applyData);

			Checkcnplan cntr = new Checkcnplan();
			cntr.applyJson(applyData);

			String result = Code.postHttp(config.getSfwxurl(), cntr.toXml());

			JSONObject obj = Code.xmlToJson(result);

			data.updateLog(day, pid, result);

			return obj.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String outSuccess(String applyData) {
		try {
			Date dt = new Date();
			String day = Code.getWeek(dt);
			String pid = data.getPid();

			data.insertLog(day, pid, "sfpt", applyData);

			Outsuccess cntr = new Outsuccess();
			cntr.applyJson(applyData);

			String result = Code.postHttp(config.getSfwxurl(), cntr.toXml());

			JSONObject obj = Code.xmlToJson(result);

			data.updateLog(day, pid, result);

			return obj.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String upTruck(String applyData) {
		try {
			Date dt = new Date();
			String day = Code.getWeek(dt);
			String pid = data.getPid();

			data.insertLog(day, pid, "sfpt", applyData);

			Uptruck tr = new Uptruck();
			tr.applyJson(applyData);

			String result = Code.postHttp(config.getSfwxurl(), tr.toXml());

			JSONObject obj = Code.xmlToJson(result);

			data.updateLog(day, pid, result);

			return obj.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String cnPlanorderrefund(String applyData) {
		try {
			Date dt = new Date();
			String day = Code.getWeek(dt);
			String pid = data.getPid();

			data.insertLog(day, pid, "sfpt", applyData);

			Cnplanorderrefund cntr = new Cnplanorderrefund();
			cntr.applyJson(applyData);

			String result = Code.postHttp(config.getSfwxurl(), cntr.toXml());

			JSONObject obj = Code.xmlToJson(result);

			data.updateLog(day, pid, result);

			return obj.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String outStart(String applyData) {
		try {
			Date dt = new Date();
			String day = Code.getWeek(dt);
			String pid = data.getPid();

			data.insertLog(day, pid, "sfpt", applyData);

			Outstart ot = new Outstart();
			ot.applyJson(applyData);

			String result = Code.postHttp(config.getSfwxurl(), ot.toXml());

			JSONObject obj = Code.xmlToJson(result);

			data.updateLog(day, pid, result);

			return obj.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String cnPlanphotoresend(String applyData) {
		try {
			Date dt = new Date();
			String day = Code.getWeek(dt);
			String pid = data.getPid();

			data.insertLog(day, pid, "sfpt", applyData);

			Cnplanphotoresend pt = new Cnplanphotoresend();
			pt.applyJson(applyData);

			String result = Code.postHttp(config.getSfwxurl(), pt.toXml());

			JSONObject obj = Code.xmlToJson(result);

			data.updateLog(day, pid, result);

			return obj.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String cnPlanstop(String applyData) {
		try {
			Date dt = new Date();
			String day = Code.getWeek(dt);
			String pid = data.getPid();

			data.insertLog(day, pid, "sfpt", applyData);

			Cnplanstop st = new Cnplanstop();
			st.applyJson(applyData);

			String result = Code.postHttp(config.getSfwxurl(), st.toXml());

			JSONObject obj = Code.xmlToJson(result);

			data.updateLog(day, pid, result);

			return obj.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String cnPlanupdate(String applyData) {
		try {
			Date dt = new Date();
			String day = Code.getWeek(dt);
			String pid = data.getPid();

			data.insertLog(day, pid, "sfpt", applyData);

			Cnplanupdate pl = new Cnplanupdate();
			pl.applyJson(applyData);

			String result = Code.postHttp(config.getSfwxurl(), pl.toXml());

			JSONObject obj = Code.xmlToJson(result);

			data.updateLog(day, pid, result);

			return obj.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
