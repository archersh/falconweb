package cn.com.sailin.compassrest.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.com.sailin.compassrest.config.Config;
import cn.com.sailin.compassrest.dao.Data;
import cn.com.sailin.compassrest.model.Outready;
import cn.com.sailin.compassrest.model.Paydetails;
import cn.com.sailin.compassrest.model.Planoutcn;
import cn.com.sailin.compassrest.model.Upphotolist;
import cn.com.sailin.compassrest.publiccode.Code;
import cn.com.sailin.compassrest.model.CallResult;

@RestController
public class Controller {

	@Autowired
	private Config config;

	@Autowired
	private Data data;
	
	public static SimpleDateFormat dtft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@RequestMapping(value = "/rest", method = RequestMethod.POST)
	public @ResponseBody String invoke(HttpServletRequest request) {
		Date dt = new Date();
		String day = Code.getWeek(dt);
		String pid = data.getPid();
		String result = "";
		try {
			JSONObject obj = reqToJson(request);

			data.insertLog(day, pid, "sfpt", obj.toJSONString());

			String method = obj.getString("callmethod");
			if (method.equals("PlanOutCn")) {
				result = planOutCn(obj);
			}
			if (method.equals("UpPhotoList")) {
				result = upPhotoList(obj);
			}
			if (method.equals("PayDetails")) {
				result = payDetails(obj);
			}
			if (method.equals("OutReady")) {
				result = outReady(obj);
			}
			if (method.equals("dsql")) {
				result = Dsql(obj.getString("applydata"));
			}
			if (result.equals("")) {
				result = resultMsg(false, "非定义的操作");
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = "";
		}

		data.updateLog(day, pid, result);

		return result;
	}

	@RequestMapping(value = "/invoke", method = RequestMethod.POST)
	public @ResponseBody String restinvoke(HttpServletRequest request) throws IOException {
		Date dt = new Date();
		String day = Code.getWeek(dt);
		String pid = data.getPid();
		String result = "";

		BufferedReader buf = request.getReader();

		String xml = "";
		String l = "";

		while ((l = buf.readLine()) != null) {
			xml = xml + l;
		}

		JSONObject obj = JSONObject.parseObject(xml);
		try {

			data.insertLog(day, pid, "sfpt", obj.toJSONString());

			String method = obj.getString("callmethod");

			if (method.equals("login")) {
				result = Login(obj);
			}
			if (method.equals("getrepaircode")) {
				result = getRepaircode(obj);
			}
			if (method.equals("getrepairpurpose")) {
				result = getRepairpurpose();
			}
			if (method.equals("getrepairfee")) {
				JSONObject applydata = JSONObject.parseObject(Code.getFieldVal(obj, "applydata", ""));
				result = getRepairfee(applydata);
			}
			if (method.equals("getrepairid")) {
				String cntrid = Code.getFieldVal(obj, "applydata", "");
				result = getRepairid(cntrid);
			}
			if (method.equals("repairbillinsertdetail")) {
				result = repairbillInsertdetail(obj);
			}
			if (method.equals("repairbilldeletedetail")) {
				result = repairbillDeletedetail(obj);
			}
			if (method.equals("insertuploadfile")) {
				result = insertUploadfile(obj);
			}
			if (method.equals("deleteuploadfile")) {
				result = deleteUploadfile(obj);
			}
			
			if (method.equals("getctnsizetype")) {
				return getCtnsizetype();
			}
			
			if (method.equals("readoutctninfo")) {
				return readOutCtnInfobycntr(Code.getFieldVal(obj, "applydata", ""));
			}

			if (result.equals("")) {
				result = resultMsg(false, "非定义的操作");
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = "";
		}

		data.updateLog(day, pid, result);
		return result;
	}

	@RequestMapping(value = "/updatefile", method = RequestMethod.GET)
	public String getUpdatefile() {
		String encoding = "UTF-8";
		File file = new File(config.getUpdatefile());
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(filecontent, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/rest/upload", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String upload(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		String fileName = "";
		try {
			File uploadPath = new File(config.getUploadpath());
			if (!uploadPath.exists() || uploadPath.isFile()) {
				resetDir(uploadPath);
			}
			fileName = file.getOriginalFilename();
			if (fileName == null || fileName.isEmpty()) {
				return "Filename is null or empty";
			}
			File targetFile = new File(config.getUploadpath(), fileName);
			file.transferTo(targetFile);

			return "OK";

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/invoke/uploadphoto", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String uploadphoto(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		String fileName = "";
		try {
			File uploadPath = new File(config.getUploadpath());
			if (!uploadPath.exists() || uploadPath.isFile()) {
				resetDir(uploadPath);
			}
			fileName = file.getOriginalFilename();
			if (fileName == null || fileName.isEmpty()) {
				return "Filename is null or empty";
			}
			File targetFile = new File(config.getUploadpath(), fileName);
			file.transferTo(targetFile);

			return Code.resultSuccess();

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	private synchronized static void resetDir(File dir) {
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (dir.isFile()) {
			dir.delete();
			dir.mkdirs();
		}
	}

	private String planOutCn(JSONObject obj) {
		Planoutcn cntr = new Planoutcn();

		cntr.setGcpguid(obj.getString("gcpguid"));
		cntr.setCycode(obj.getString("cycode"));
		cntr.setRvn(obj.getString("rvn"));
		cntr.setTruck(obj.getString("truck"));
		cntr.setTruck2(obj.getString("truck2"));
		cntr.setDriver(obj.getString("driver"));
		cntr.setDrivertel(obj.getString("drivertel"));
		cntr.setPickuptype(obj.getString("pickuptype"));
		cntr.setCntype(obj.getString("cntype"));
		cntr.setSurchargebarcode(obj.getString("surchargebarcode"));
		cntr.setShipping(obj.getString("shipping"));
		cntr.setBn(obj.getString("bn"));
		cntr.setEir(obj.getString("eir"));
		cntr.setVsl(obj.getString("vsl"));
		cntr.setVoy(obj.getString("voy"));
		cntr.setDzfxh(obj.getString("dzfxh"));
		cntr.setRemark(obj.getString("remark"));

		// 判断是否有记录，如果已处理返回出错
		List<Map<String, Object>> lcntr = data.qryPlanoutcn(cntr.getGcpguid());
		if (!(lcntr.size() == 0)) {
			if (lcntr.get(0).get("inva").equals("N")) {
				return resultMsg(false, "该ID已处理");
			}
		}
		// 添加
		data.delPlanoutcn(cntr.getGcpguid());
		data.insertPlanoutcn(cntr);

		return resultMsg(true, "OK");
	}

	private String upPhotoList(JSONObject obj) {
		Upphotolist pt = new Upphotolist();

		pt.setGcpguid(obj.getString("gcpguid"));
		pt.setPhotoguid(obj.getString("photoguid"));
		pt.setPhotourl(obj.getString("photourl"));
		pt.setPhotoslturl(obj.getString("photoslturl"));
		pt.setUploaddate(obj.getString("uploaddate"));

		data.delUpphotolist(pt.getGcpguid());
		data.insertUpphotolist(pt);

		return resultMsg(true, "OK");
	}

	private String payDetails(JSONObject obj) {
		Paydetails py = new Paydetails();

		py.setGcpguid(obj.getString("gcpguid"));
		py.setPaydetailsguid(obj.getString("paydetailsguid"));
		py.setShno(obj.getString("shno"));
		py.setPayno(obj.getString("payno"));
		py.setChargename(obj.getString("chargename"));
		py.setAmount(obj.getString("amount"));
		py.setPaymodel(obj.getString("paymodel"));
		py.setPaydate(obj.getString("paydate"));
		py.setPayopenid(obj.getString("payopenid"));

		data.delPaydetails(py.getGcpguid());
		data.insertPaydetails(py);

		return resultMsg(true, "OK");
	}

	private String outReady(JSONObject obj) {
		Outready or = new Outready();

		or.setGcpguid(obj.getString("gcpguid"));
		or.setRvn(obj.getString("rvn"));
		or.setCycode(obj.getString("cycode"));

		data.delOutready(or.getGcpguid());
		data.insertOutread(or);

		return resultMsg(true, "OK");
	}

	public String Dsql(String sql) {

		CallResult result = new CallResult();

		List<Map<String, Object>> list = null;

		try {
			list = data.getJdbc().queryForList(sql);

			result.setMSGID("0000");
			result.setMSGDESC("成功");
			result.setDATA(list);

			return JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);

		} catch (Exception e) {

			return Code.resultError("1111", "失败：" + e.getMessage());
		}
	}

	public String Login(JSONObject obj) {

		String user = Code.getFieldVal(obj, "user", "");
		String password = Code.getFieldVal(obj, "password", "");

		if (user.equals(""))
			return Code.resultError("1111", "用户不能为空");

		if (password.equals(""))
			return Code.resultError("1111", "密码不能为空");

		List<Map<String, Object>> l = data.qryUserinfo(user);

		if (l.size() == 0) {
			return Code.resultError("1111", "没有该用户");
		} else {
			if (Code.getFieldVal(l.get(0), "userpass", "").equals(password))
				return Code.resultSuccess();
			else
				return Code.resultError("1111", "密码有误");
		}

	}

	public String getRepaircode(JSONObject obj) {
		String codetype = Code.getFieldVal(obj, "codetype", "");
		List<Map<String, Object>> l = data.qryRp_repairitemcodebycodetype(codetype);

		return Code.resultSuccess(l);
	}

	public String getRepairpurpose() {
		List<Map<String, Object>> l = data.qryRp_repairitempurpose();
		return Code.resultSuccess(l);
	}
	
	public String getCtnsizetype() {
		List<Map<String,Object>> l=data.qryCtnsizetype();
		return Code.resultSuccess(l);
	}

	private JSONObject reqToJson(HttpServletRequest request) {
		try {
			BufferedReader buf = request.getReader();

			String xml = "";
			String l = "";

			while ((l = buf.readLine()) != null) {
				xml = xml + l;
			}
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(xml.getBytes());

			Document doc = (Document) db.parse(is);

			NodeList xmllist = doc.getElementsByTagName("xml");

			Node root = xmllist.item(0);

			NodeList nodelist = root.getChildNodes();

			JSONObject obj = new JSONObject();

			for (int i = 0; i < nodelist.getLength(); i++) {
				Node node = nodelist.item(i);
				obj.put(node.getNodeName(), node.getTextContent());
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private String resultMsg(boolean flag, String msg) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			doc.setXmlStandalone(true);
			Element root = doc.createElement("xml");
			Element rncode = doc.createElement("return_code");
			CDATASection rncodevalue = doc.createCDATASection("");
			if (flag) {
				rncodevalue.setTextContent("SUCCESS");
			} else {
				rncodevalue.setTextContent("FAIL");
			}
			rncode.appendChild(rncodevalue);
			root.appendChild(rncode);

			Element rnmsg = doc.createElement("return_msg");
			CDATASection rnmsgvalue = doc.createCDATASection("");
			rnmsgvalue.setTextContent(msg);
			rnmsg.appendChild(rnmsgvalue);
			root.appendChild(rnmsg);
			doc.appendChild(root);

			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer tf = tff.newTransformer();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			tf.transform(new DOMSource(doc), new StreamResult(os));
			return os.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	private String getRepairfee(JSONObject obj) {
		String lineCode = Code.getFieldVal(obj, "linecode", "");
		String repairCode = Code.getFieldVal(obj, "repaircode", "");
		String placeCode = Code.getFieldVal(obj, "placecode", "");
		String repairPos = Code.getFieldVal(obj, "repairpos", "");
		String repairSize = Code.getFieldVal(obj, "repairsize", "");
		String workHour = Code.getFieldVal(obj, "workhour", "");
		String szty = Code.getFieldVal(obj, "szty", "");

		return data.getRepairfee(lineCode, repairCode, placeCode, repairPos, repairSize, workHour, szty);
	}

	private String getRepairid(String cntrid) {
		return Code.resultSuccess(data.getRepairid(cntrid));
	}

	private String repairbillInsertdetail(JSONObject obj) {
		JSONObject result = data.toPlsql("RPINSERTDETAIL", "", "", Code.getFieldVal(obj, "applydata", ""));
		return Code.resultSuccess(result);
	}

	private String repairbillDeletedetail(JSONObject obj) {
		JSONObject result = data.toPlsql("RPDELETEDETAIL", "", "", Code.getFieldVal(obj, "applydata", ""));
		return Code.resultSuccess(result);
	}

	private String insertUploadfile(JSONObject obj) {
		try {
			JSONObject applydata = JSONObject.parseObject(Code.getFieldVal(obj, "applydata", ""));
			data.insertUploadfile(Code.getFieldVal(applydata, "repairid", ""), Code.getFieldVal(applydata, "filename", ""));
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "添加文件出错" + e.getMessage());
		}

	}

	private String deleteUploadfile(JSONObject obj) {
		try {
			JSONObject applydata = JSONObject.parseObject(Code.getFieldVal(obj, "applydata", ""));
			data.deleteUploadfile(Code.getFieldVal(applydata, "repairid", ""),Code.getFieldVal(applydata, "filename", ""));
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "删除文件出错" + e.getMessage());
		}
	}
	
	private String readOutCtnInfobycntr(String cntr) {
		try {
			
			data.delOutCtnInfo(cntr);
			
			JSONObject jo=new JSONObject();
			
			long enddate = System.currentTimeMillis();
			long startdate=enddate-30*24*60*60*1000;
			
			String send=dtft.format(new Date(enddate));
			String sstart=dtft.format(new Date(startdate));
			
			jo.put("id",send);
			jo.put("userName","13567910431");
			jo.put("userPassWord","AX27698048");
			jo.put("palceCode","");
			jo.put("unCode", "");
			jo.put("voyage", "");	
			jo.put("ctnNo", cntr);
			jo.put("blNo","");
			jo.put("ctnAwayFlag","");
			jo.put("startTime","");
			jo.put("endTime", "");
			jo.put("vesselNameE", "");
			jo.put("jobId", "OUTCTNBILLINFO");
			
			JSONArray ja = new JSONArray();
			ja.add("OUTCTNBILLINFO");
			ja.add(jo);
			
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			Client client = dcf.createClient(config.getYgturl());
			Object[] objects = new Object[0];
			objects = client.invoke("callEDIESBPub", "AXTC", "ZJYGT", "OUTCTNINFO",ja.toJSONString() , "", "");
			String result= objects[0].toString();
			if (!result.equals("")) {
				JSONObject jr=JSONObject.parseObject(result);
				if (jr.getString("msgId").equals("0000")) {
					JSONArray jar=jr.getJSONArray("CtnInfo");
					for (int i=0;i<jar.size();i++) {
						JSONObject jrm=(JSONObject) jar.get(i);
						data.insertOutCtnInfo(jrm);
						return Code.resultSuccess("Y");
					}
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			return Code.resultSuccess("调用易港通数据出错");
		}
		
		return Code.resultSuccess("N");
	}
}
