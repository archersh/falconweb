package cn.com.sailin.falconweb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.sailin.falconweb.config.Config;
import cn.com.sailin.falconweb.dao.Data;
import cn.com.sailin.falconweb.publiccode.Code;
import cn.com.sailin.falconweb.publiccode.Sh;
import cn.com.sailin.falconweb.schedle.Schedle;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

public class WebserviceImpl implements WebserviceInterface {

	@Autowired
	private Data data;
	
	@Autowired
	private Config config;
	
	@Autowired
	private Sh sh;

	// @Autowired
	// private Schedle schedle;

	@Resource
	private WebServiceContext wsContext;

	private final static Logger log = LoggerFactory.getLogger("cn.com.sailin.falconweb.publiccode.sh");

	private boolean hasAuth(String userid, String timestamp, String token, String method, String applydata) {

		return true;

		/*if (!userid.equals("SYS")) {
			String pw = data.getPassword(userid);
			String md5source1=userid.trim() + pw.trim() + timestamp.trim() + method.trim() + applydata.trim();
			String md5source2=userid.trim()+pw.trim() + timestamp.trim() + method.trim();
			String m51 = Code.md5(md5source1);
			String m52=Code.md5(md5source2);
			if (m51.toUpperCase().equals(token.toUpperCase())||m52.toUpperCase().equals(token.toUpperCase())) {
				long curr = System.currentTimeMillis();
				long priv = curr - 10 * 60 * 1000;
				long back = curr + 10 * 60 * 1000;

				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String starttime = df.format(new Date(priv));
				String endtime = df.format(new Date(back));

				if (timestamp.compareTo(starttime) > 0 && timestamp.compareTo(endtime) < 0) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return true;
		}*/
	}

	private boolean hasAuthweb(String userid, String timestamp, String token, String method, String applydata) {

		return true;

		// String pw = data.getPasswordweb(userid);
		// String m5 = Code.md5(userid + pw + timestamp + method + applydata);

		// System.out.println("Remote:");
		// System.out.println(" userid:" + userid);
		// System.out.println(" timestamp:" + timestamp);
		// System.out.println(" method:" + method);
		// System.out.println(" applydata:" + applydata);
		// System.out.println(" token:" + token);
		// System.out.println("Local:");
		// System.out.println(" string:" + userid + pw + timestamp + method +
		// applydata);
		// System.out.println(" md5:" + m5);

		// if (m5.toUpperCase().equals(token.toUpperCase())) {
		// long curr = System.currentTimeMillis();
		// long priv = curr - 10 * 60 * 1000;
		// long back = curr + 10 * 60 * 1000;

		// String starttime = Code.dtft.format(new Date(priv));
		// String endtime = Code.dtft.format(new Date(back));

		// if (timestamp.compareTo(starttime) > 0 && timestamp.compareTo(endtime) < 0) {
		// return true;
		// } else {
		// return false;
		// }
		// } else {
		// return false;
		// }

	}

	private String getClientInfo() {
		try {
			MessageContext mc = wsContext.getMessageContext();

			HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
			return req.getRemoteAddr() + ":" + String.valueOf(req.getRemotePort());
		} catch (Exception e) {
			e.printStackTrace();
			return "iperror";
		}
	}

	@Transactional
	@Override
	public String invoke(String userid, String timestamp, String token, String method, String applydata){

		String res = null;

		String day = Code.getWeek(new Date());
		String pid = data.getPid();

		log.info("pid:" + pid + " method:" + method + " applydata:" + applydata);

		data.insertLog(day, pid, method, getClientInfo() + ":" + applydata, userid);
		
		if (hasAuth(userid, timestamp, token, method, applydata)) {

			// check
			if (method.equals("dsql"))
				res = sh.Dsql(applydata, data);
			// check
			//if (method.equals("esql"))
			//	res = sh.Esql(applydata, data);
			// check 添加审核数据
			if (method.equals("insertacpy"))
				res = sh.insertAcpy(userid, applydata, data);
			// check 删除审核数据
			if (method.equals("delacpy"))
				res = sh.delAcpy(applydata, data);
			// check 更新审核数据--承包公司
			if (method.equals("updateacpyccck"))
				res = sh.updateAcpyccck(userid, applydata, data);
			// check 更新审核数据--劳务公司
			if (method.equals("updateacpylcck"))
				res = sh.updateAcpylcck(userid, applydata, data);
			// check 更新审核数据--银行导出
			if (method.equals("updateacpybkot"))
				res = sh.updateAcpybkot(userid, applydata, data);
			// check 更新审核数据--银行确认
			if (method.equals("updateacpybkcfm"))
				res = sh.updateAcpybkcfm(userid, applydata, data);
			// check 删除考勤数据
			if (method.equals("delwkds"))
				res = sh.delWkds(applydata, data);
			// check
			if (method.equals("getnextval"))
				res = sh.getNextval(applydata, data);
			// check 添加工地和分包公司对应关系
			if (method.equals("insertbslc"))
				res = sh.insertBslc(userid, applydata, data);
			// check 添加工地和分包公司对应关系
			if (method.equals("insertbsls"))
				res = sh.insertBsls(userid, applydata, data);
			// check 删除工地和分包公司对应关系
			if (method.equals("delbslc"))
				res = sh.delbslc(applydata, data);
			// check 删除工地和分包公司对应关系
			if (method.equals("delbsls"))
				res = sh.delbsls(applydata, data);
			// check 添加工地专用账号余额
			if (method.equals("insertspanbl"))
				res = sh.insertSpanbl(userid, applydata, data);
			// check
			if (method.equals("nextval"))
				res = sh.getNextval(applydata, data);
			if (method.equals("test"))
				res = sh.getTest(applydata, data);
			if (method.equals("getnoffwork"))
				res = sh.getNoffWorker(applydata, data);
			// check 添加考勤数据
			if (method.equals("insertwkds"))
				res = sh.insertWkds(applydata, data);
			// check 添加工地数据
			if (method.equals("insertsybscd"))
				res = sh.insertSybscd(applydata, data);
			// check 删除工地数据
			if (method.equals("delsybscd"))
				res = sh.delSybscd(applydata, data);
			// check 更新工地的工地部分数据
			if (method.equals("updatesybscdbybs"))
				res = sh.updateSybscdByBs(userid, applydata, data);
			// check 更新工地的银行部分数据
			if (method.equals("updatesybscdbybank"))
				res = sh.updateSybscdByBank(userid, applydata, data);
			// check 添加系统代码数据
			if (method.equals("insertsycdtb"))
				res = sh.insertSycdtb(userid, applydata, data);
			// check 删除系统代码数据
			if (method.equals("delsycdtb"))
				res = sh.delSycdtb(userid, applydata, data);
			if (method.equals("getbsmonitor"))
				res = sh.getBsmonitor(userid, applydata, data);
			// check 添加员工数据
			if (method.equals("insertwker"))
				res = sh.insertWker(userid, applydata, data);
			// check 删除员工数据
			if (method.equals("delwker"))
				res = sh.delWker(userid, applydata, data);
			// check 更改民工数据
			if (method.equals("updatewker"))
				res = sh.updateWker(userid, applydata, data);
			// check 添加功能
			if (method.equals("insertacfunctioninfo"))
				res = sh.insertAcfunctioninfo(userid, applydata, data);
			// check 删除功能
			if (method.equals("delacfunctioninfo"))
				res = sh.delAcfunctioninfo(userid, applydata, data);
			// check 添加菜单
			if (method.equals("insertacmenustruct"))
				res = sh.insertAcmenustruct(userid, applydata, data);
			// check 删除菜单
			if (method.equals("delacmenustruct"))
				res = sh.delAcmenustruct(userid, applydata, data);
			// check 添加工具条
			if (method.equals("insertactoolcfg"))
				res = sh.insertActoolcfg(userid, applydata, data);
			// check 删除工具条
			if (method.equals("delactoolcfg"))
				res = sh.delActoolcfg(userid, applydata, data);
			// check 添加角色
			if (method.equals("insertacroleinfo"))
				res = sh.insertAcroleinfo(userid, applydata, data);
			// check 删除角色（多张表）
			if (method.equals("delacrole"))
				res = sh.delAcrole(userid, applydata, data);
			// check 添加用户信息
			if (method.equals("insertacuserinfo"))
				res = sh.insertAcuserinfo(userid, applydata, data);
			// check 删除用户信息
			if (method.equals("delacuserinfo"))
				res = sh.delAcuserinfo(userid, applydata, data);
			// check 更改用户密码
			if (method.equals("updatepassword"))
				res = sh.updatePassword(userid, applydata, data);
			// check 添加角色与用户关系
			if (method.equals("insertacroleuser"))
				res = sh.insertAcroleuser(userid, applydata, data);
			// check 删除角色与用户关系根据用户id
			if (method.equals("delacroleuserbyuserid"))
				res = sh.delAcroleuserbyuserid(userid, applydata, data);
			// check 更改菜单信息
			if (method.equals("updateacmenustructinfo"))
				res = sh.updateAcmenustrucinfo(userid, applydata, data);
			// check 更改菜单顺序
			if (method.equals("updateacmenustructordernum"))
				res = sh.updateAcmenustructordernum(userid, applydata, data);
			// check 更改功能信息
			if (method.equals("updateacfunctioninfo"))
				res = sh.updateAcfunctioninfo(userid, applydata, data);
			// check 更改功能顺序
			if (method.equals("updateacfunctioninfoordernum"))
				res = sh.updateAcfunctioninfoordernum(userid, applydata, data);
			// check 更改工具条数据
			if (method.equals("updateactoolcfginfo"))
				res = sh.updateActoolcfginfo(userid, applydata, data);
			// check 更改工具条顺序
			if (method.equals("updateactoolcfgordernum"))
				res = sh.updateActoolcfgordernum(userid, applydata, data);
			// check 添加菜单点击数
			if (method.equals("incmenuclicknum"))
				res = sh.incMenuclicknum(userid, applydata, data);
			// check 更改用户信息
			if (method.equals("updateacuserinfo"))
				res = sh.updateAcuserinfo(userid, applydata, data);
			// check 获取新的工地
			if (method.equals("getnewbs"))
				res = sh.getNewbs(applydata, data);
			// check 获取新的人员
			if (method.equals("getnewworker"))
				res = sh.getNewworker(applydata, data);
			// check 获取考勤系统考勤数据
			if (method.equals("getwkds"))
				res = sh.getWkds(applydata, data);
			// check 添加公告
			if (method.equals("insertannc"))
				res = sh.insertAnnc(userid, applydata, data);
			// check 添加角色功能对应
			if (method.equals("insertacrolefunction"))
				res = sh.insertAcrolefunction(userid, applydata, data);
			// check 撤除工地
			if (method.equals("updatesybscdbydt"))
				res = sh.updateSybscdByDt(userid, applydata, data);
			// check 更改公告数据
			if (method.equals("updateannc"))
				res = sh.updateAnnc(userid, applydata, data);
			// check 删除公告
			if (method.equals("delannc"))
				res = sh.delAnnc(userid, applydata, data);
			if (method.equals("getcheckno"))
				res = sh.getCheckon(applydata, data);
			if (method.equals("importwkdsbybscdmonth"))
				res = sh.importWkdsBybscdmonth(userid, applydata, data);
			// check 获取工作详细数据
			if (method.equals("getbsdetail"))
				res = sh.getBsdetail(userid, applydata, data);
			if (method.equals("getbshisinfo"))
				res = sh.getBshisinfo(userid, applydata, data);
			if (method.equals("dobscoll"))
				res = sh.doBscoll(userid, applydata, data);
			// check 导入银行反馈数据
			if (method.equals("importbankback"))
				res = sh.importBankback(userid, applydata, data);
			if (method.equals("getfillstr")) {
				JSONObject obj = JSON.parseObject(applydata);
				String sSrc = obj.getString("src");
				String sSign = obj.getString("sgin");
				String sLen = obj.getString("len");
				String sFill = obj.getString("fill");
				return Code.getFillStr(sSrc, sSign, Integer.parseInt(sLen), sFill);
			}
			// 添加操作列表
			if (method.equals("insertoplist"))
				res = sh.insertOplist(applydata, data);
			// 删除操作列表
			if (method.equals("deloplist"))
				res = sh.delOplist(applydata, data);
			// 更新操作列表出错返回值
			if (method.equals("updateoplistresult"))
				res = sh.updateOplistresult(applydata, data);
			// check 重置密码
			if (method.equals("resetpassword"))
				res = sh.resetPassword(userid, applydata, data);
			// 第三门考勤数据上传接口
			if (method.equals("attendsubmit"))
				res = sh.attendSubmit(userid, applydata, data);
			if (method.equals("yfattendsubmit"))
				res=sh.yfattendSubmit(userid, applydata, data);
			// 工人离职
			if (method.equals("wkerleave"))
				res = sh.wkerleave(userid, applydata, data);
			// 银行流水
			if (method.equals("bankrecord"))
				res = sh.bankrecord(userid, applydata, data);

			// 测试上传考勤数据
			if (method.equals("testuploadwkds"))
				res = sh.uploadwkds(userid, applydata, data);
			// if (method.equals("test")) {
			// schedle.importWkds();

			// 测试上传工地人员数据
			if (method.equals("testuploadhuman"))
				res = sh.uploadhumanbybscd(userid, applydata, data);
			if (method.equals("testuploadsalary"))
				res=sh.uploadsalarybysqnb(userid,applydata,data);
			//测试上传工地数据
			if (method.equals("testuploadproject"))
				res=sh.testuploadproject(userid,applydata,data);
			
			
			if (method.equals("getyftoken"))
				res=sh.getYftoken(data);
			
			if (method.equals("insertyfdevice"))
				res=sh.insertyfdevice(userid,applydata,data);
			if (method.equals("updateyfdevice"))
				res=sh.updateyfdevice(userid,applydata,data);
			if (method.equals("delyfdevice"))
				res=sh.delyfdevice(userid,applydata,data);
			
			if (method.equals("testbklist"))
				res=data.getAllbkcdbybkcd(applydata);
			
			//上传照片
			if (method.equals("uploadfile"))
				res=sh.uploadfile(applydata);
			
			if (method.equals("importwkdsbyweek"))
			    res=sh.importWkdsbyweek(userid,applydata,data);
			
	
			// }
		} else

		{
			res = Code.resultError("1111", "鉴权失败，非法调用");
		}
		if (res == null)
			res = Code.resultError("1111", "非法method[" + method + "]");

		int len = res.length();
		if (len > 100)
			len = 100;

		log.info("pid:" + pid + " res:" + res.substring(0, len));

		data.updateLog(day, pid, res);

		return res;
	}

	@Override
	public String webinvoke(String userid, String timestamp, String token, String method, String applydata) {
		String res = null;

		String day = Code.getWeek(new Date());
		String pid = data.getPid();

		log.info("pid:" + pid + " method:" + method + " applydata:" + applydata);
		
		data.insertLog(day, pid, method, getClientInfo() + ":" + applydata, userid);

		if (hasAuthweb(userid, timestamp, token, method, applydata)) {
			if (method.equals("getworkerinfo"))
				res = sh.getWorkinfo(applydata, data);
			if (method.equals("updatepasswordworker"))
				res = sh.updatePasswordworker(applydata, data);
			if (method.equals("getannc"))
				res = sh.getAnnc(data);
			if (method.equals("getcheckno"))
				res = sh.getCheckon(applydata, data);
			if (method.equals("getacpyinfo"))
				res = sh.getAcpyinfo(applydata, data);
			if (method.equals("dsql"))
				res = sh.Dsql(applydata, data);
			// 添加操作列表
			if (method.equals("insertoplist"))
				res = sh.insertOplist(applydata, data);
			// 删除操作列表
			if (method.equals("deloplist"))
				res = sh.delOplist(applydata, data);
			// 更新操作列表出错返回值
			if (method.equals("updateoplistresult"))
				res = sh.updateOplistresult(applydata, data);
			if (method.equals("yfattendsubmit"))
				res=sh.yfattendSubmit("SYS", applydata, data);
		} else {
			res = Code.resultError("1111", "鉴权失败，非法调用");
		}
		if (res == null)
			res = Code.resultError("1111", "非法method[" + method + "]");
		
		int len = res.length();
		if (len > 100)
			len = 100;
		
		log.info("pid:" + pid + " res:" + res.substring(0, len));
		
		data.updateLog(day, pid, res);

		return res;
	}

	@Override
	public String postbytes(String userid, String timestamp, String token, String applydata, byte[] bytes) {
		return "";
	}

}
