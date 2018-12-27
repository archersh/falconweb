package cn.com.sailin.compassrest.schedle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.sailin.compassrest.config.Config;
import cn.com.sailin.compassrest.dao.Data;
import cn.com.sailin.compassrest.publiccode.Code;

@Component
@EnableScheduling
public class Schedle {

	@Autowired
	private Config config;

	@Autowired
	private Data data;

	private SimpleDateFormat _ft = new SimpleDateFormat("yyyyMMddHHmmss");

	@Scheduled(initialDelay = 10000, fixedDelay = 6000)
	public void sendYgtctout() {

		List<Map<String, Object>> lygtcntr = data.qryYgtcntrnotsend();

		for (Map<String, Object> mygtcntr : lygtcntr) {

			String plannumber = Code.getFieldVal(mygtcntr, "PLANNUMBER", "");
			String barcode = Code.getFieldVal(mygtcntr, "BARCODE", "");

			List<Map<String, Object>> ltk = data.qryPgctntkbyplanbar(plannumber, barcode);

			for (Map<String, Object> mtk : ltk) {

				JSONObject ctout = new JSONObject();

				ctout.put("id", data.getPid());
				ctout.put("jobId", "CTOUT");
				String cfsid = Code.getFieldVal(mygtcntr, "CFSID", "1");
				if (cfsid.equals("1"))
					ctout.put("actualLocation", "DC48082571");
				if (cfsid.equals("3"))
					ctout.put("actualLocation", "DC42351333");

				JSONObject ch = new JSONObject();

				ch.put("OutType", "TK");
				ch.put("ctnNo", Code.getFieldVal(mygtcntr, "CNTR", ""));
				ch.put("Plannumber", plannumber);
				ch.put("AppearanceTime", Code.getFieldVal(mygtcntr, "gtottm", ""));
				ch.put("blNo", Code.getFieldVal(mtk, "BLNO", ""));
				ch.put("itemCode", "");
				ch.put("itemName", "");
				ch.put("printItemCode", "");
				ch.put("printItemName", "");
				ch.put("itemPrice", "");
				ch.put("quantity", "");
				ch.put("taxRate", "");
				ch.put("taxAmount", "");
				ch.put("ctnSizeType", Code.getFieldVal(mtk, "CTNSIZETYPE", ""));
				ch.put("tradeNw", "");
				ch.put("ctnStatus", "E");
				ch.put("reeferFlag", "N");
				ch.put("hazardFlag", "N");
				ch.put("odFlag", "N");
				ch.put("damageFlag", "N");
				ch.put("unit", "集装箱");
				ch.put("amount", "");
				ch.put("barCode", barcode);
				ch.put("FeeFlag", "Y");
				ch.put("Sealno", Code.getFieldVal(mygtcntr, "SEALNO", ""));
				ch.put("carJobnumber", Code.getFieldVal(mtk, "CARJOBNUMBER", ""));
				ch.put("driverName", Code.getFieldVal(mtk, "DRIVERNAME", ""));
				ch.put("driverTel", Code.getFieldVal(mtk, "DRIVERTEL", ""));
				ch.put("Rsv1", "");
				ch.put("Rsv2", "");
				ch.put("Rsv3", "");
				ch.put("Rsv4", "");
				ch.put("Rsv5", "");

				JSONArray jach = new JSONArray();

				jach.add(ch);

				ctout.put("ctnChargeInfo", jach);

				JSONArray ja = new JSONArray();

				ja.add("CTOUT");
				ja.add(ctout);

				JSONObject jr = new JSONObject();
				try {
					String result = callYgtservice("CTOUT", ja.toJSONString());
					jr = JSONObject.parseObject(result);
				} catch (Exception e) {
					e.printStackTrace();
					jr.put("msgId", "2222");
					jr.put("msgDESC", e.getMessage());
				}

				data.updateYgtcntr(plannumber, barcode, "Y", Code.getFieldVal(jr, "msgId", ""),
						Code.getFieldVal(jr, "msgDESC", "").trim() + Code.getFieldVal(jr, "yardMsgInfo", "").trim());

			}

		}
	}

	@Scheduled(initialDelay = 10000, fixedDelay = 5000)
	public void sendYgtbillcheck() {

		List<Map<String, Object>> lygt = data.qryYgtfeenotsend();

		for (Map<String, Object> m : lygt) {

			sendInctninfo(m);
			sendBillcheck(m);
		}

	}

	private void sendInctninfo(Map<String, Object> m) {

		JSONObject jo = new JSONObject();

		long cur = System.currentTimeMillis();
		jo.put("id", _ft.format(new Date(cur)));
		jo.put("jobId", "INCTNINFO");

		String cfsid = Code.getFieldVal(m, "CFSID", "1");
		if (cfsid.equals("1")) {
			jo.put("userName", "13567910431");
			jo.put("userPassWord", "AX27698048");
		}
		if (cfsid.equals("3")) {
			jo.put("userName", "17867985342");
			jo.put("userPassWord", "AX27698048");
		}

		JSONArray ja = new JSONArray();

		JSONObject jct = new JSONObject();

		if (cfsid.equals("1")) {
			jct.put("palceCode", "DC48082571");
		}
		if (cfsid.equals("3")) {
			jct.put("palceCode", "DC42351333");
		}
		jct.put("ctnInTime", Code.getFieldVal(m, "ctnInTime", ""));
		jct.put("ctnType", "");
		jct.put("ctnSizeType", Code.getFieldVal(m, "CTNSIZETYPE", ""));
		jct.put("ctnNo", Code.getFieldVal(m, "CNTR", ""));
		jct.put("blNo", Code.getFieldVal(m, "BLNO", ""));
		jct.put("vesselNameE", Code.getFieldVal(m, "VESSELNAMEE", ""));
		jct.put("unCode", Code.getFieldVal(m, "UNCODE", ""));
		jct.put("voyage", Code.getFieldVal(m, "VOYAGE", ""));
		jct.put("ctnOperatorCode", Code.getFieldVal(m, "CTOWNER", ""));
		jct.put("truckNo", Code.getFieldVal(m, "TRUCKNO", ""));
		jct.put("truckLicense", Code.getFieldVal(m, "TRUCKNO", ""));
		jct.put("InReason", "KH");
		jct.put("Remarks", "");

		JSONArray japply = new JSONArray();
		japply.add(jct);
		jo.put("applyInfo", japply);

		ja.add("INCTNINFO");
		ja.add(jo);

		String result = callYgtservice("INCTNINFO", ja.toJSONString());

	}

	private void sendBillcheck(Map<String, Object> m) {

		JSONObject jo = new JSONObject();

		long cur = System.currentTimeMillis();
		jo.put("id", _ft.format(new Date(cur)));
		jo.put("jobId", "BILLCHECK");
		if (Code.getFieldVal(m, "CFSID", "1").equals("1")) {
			jo.put("userName", "13567910431");
			jo.put("userPassWord", "AX27698048");
		}
		if (Code.getFieldVal(m, "CFSID", "3").equals("3")) {
			jo.put("userName", "17867985342");
			jo.put("userPassWord", "AX27698048");
		}
		jo.put("rsv1", "");
		jo.put("rsv2", "");
		jo.put("rsv3", "");
		jo.put("rsv4", "");
		jo.put("rsv5", "");
		jo.put("rsv6", "");
		jo.put("rsv7", "");
		jo.put("rsv8", "");
		jo.put("rsv9", "");
		jo.put("rsv10", "");

		JSONObject jctn = new JSONObject();
		jctn.put("ctnInTime", Code.getFieldVal(m, "ctnInTime", ""));
		jctn.put("ctnSizeType", Code.getFieldVal(m, "CTNSIZETYPE", ""));
		jctn.put("ctnNo", Code.getFieldVal(m, "CNTR", ""));
		jctn.put("blNo", Code.getFieldVal(m, "BLNO", ""));
		jctn.put("vesselNameE", Code.getFieldVal(m, "VESSELNAMEE", ""));
		jctn.put("unCode", Code.getFieldVal(m, "UNCODE", ""));
		jctn.put("voyage", Code.getFieldVal(m, "VOYAGE", ""));
		jctn.put("djAmount", Code.getFieldVal(m, "DJAMOUNT", ""));
		jctn.put("djfjAmount", Code.getFieldVal(m, "DJFJAMOUNT", ""));
		jctn.put("rsv01", Code.getFieldVal(m, "RSV1", ""));
		jctn.put("rsv02", Code.getFieldVal(m, "RSV2", ""));
		jctn.put("rsv03", Code.getFieldVal(m, "RSV3", ""));
		jctn.put("rsv04", Code.getFieldVal(m, "RSV4", ""));
		jctn.put("rsv05", Code.getFieldVal(m, "RSV5", ""));
		jctn.put("rsv06", Code.getFieldVal(m, "RSV6", ""));
		jctn.put("rsv07", Code.getFieldVal(m, "RSV7", ""));
		jctn.put("rsv08", Code.getFieldVal(m, "RSV8", ""));
		jctn.put("rsv09", Code.getFieldVal(m, "RSV9", ""));
		jctn.put("rsv010", Code.getFieldVal(m, "RSV10", ""));
		jctn.put("rsv011", Code.getFieldVal(m, "RSV11", ""));
		jctn.put("rsv012", Code.getFieldVal(m, "RSV12", ""));
		jctn.put("rsv013", Code.getFieldVal(m, "RSV13", ""));
		jctn.put("rsv014", Code.getFieldVal(m, "RSV14", ""));
		jctn.put("rsv015", Code.getFieldVal(m, "RSV15", ""));
		jctn.put("rsv016", Code.getFieldVal(m, "RSV16", ""));
		jctn.put("rsv017", Code.getFieldVal(m, "RSV17", ""));
		jctn.put("rsv018", Code.getFieldVal(m, "RSV18", ""));
		jctn.put("rsv019", Code.getFieldVal(m, "RSV19", ""));
		jctn.put("rsv020", Code.getFieldVal(m, "RSV20", ""));

		JSONObject japply = new JSONObject();
		japply.put("ctnNumber", 1);
		japply.put("totalMoeny", Integer.parseInt(Code.getFieldVal(m, "DJAMOUNT", "0"))
				+ Integer.parseInt(Code.getFieldVal(m, "DJFJAMOUNT", "0")));

		JSONArray jactn = new JSONArray();
		jactn.add(jctn);
		japply.put("CtnDetails", jactn);
		japply.put("rsvinfo01", "");
		japply.put("rsvinfo02", "");
		japply.put("rsvinfo03", "");
		japply.put("rsvinfo04", "");
		japply.put("rsvinfo05", "");
		japply.put("rsvinfo06", "");
		japply.put("rsvinfo07", "");
		japply.put("rsvinfo08", "");
		japply.put("rsvinfo09", "");
		japply.put("rsvinfo10", "");
		japply.put("rsvinfo11", "");
		japply.put("rsvinfo12", "");
		japply.put("rsvinfo13", "");
		japply.put("rsvinfo14", "");
		japply.put("rsvinfo15", "");
		japply.put("rsvinfo16", "");
		japply.put("rsvinfo17", "");
		japply.put("rsvinfo18", "");
		japply.put("rsvinfo19", "");
		japply.put("rsvinfo20", "");

		JSONArray jaapply = new JSONArray();
		jaapply.add(japply);
		jo.put("ApplyInfo", jaapply);

		JSONArray ja = new JSONArray();
		ja.add("BILLCHECK");
		ja.add(jo);

		String result = callYgtservice("BILLCHECK", ja.toJSONString());

		JSONObject jr = new JSONObject();

		try {
			jr = JSONObject.parseObject(result);
		} catch (Exception e) {
			e.printStackTrace();
			jr.put("msgId", "2222");
			jr.put("msgDESC", e.getMessage());
		}

		data.updateYgtfee(Code.getFieldVal(m, "ctnInTime", ""), Code.getFieldVal(m, "CNTR", ""), "Y",
				Code.getFieldVal(jr, "msgId", ""), Code.getFieldVal(jr, "msgInfo", "").trim()
						+ Code.getFieldVal(jr, "msgDESC", "").trim() + Code.getFieldVal(jr, "yardMsgInfo", "").trim(),
				Code.getFieldVal(jr, "result", "").trim());

	}

	private String callYgtservice(String esbid, String applydata) {

		JSONObject jo = new JSONObject();

		try {
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			Client client = dcf.createClient(config.getYgturl());
			Object[] objects = new Object[0];

			objects = client.invoke("callEDIESBPub", "AXTC", "ZJYGT", esbid, applydata, "", "");

			String result = objects[0].toString();

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			jo.put("msgId", "2222");
			jo.put("msgDESC", e.getMessage());
			return jo.toJSONString();
		}

	}

}
