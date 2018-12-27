package cn.com.sailin.falconweb.publiccode;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.com.sailin.falconweb.calculate.Attend;
import cn.com.sailin.falconweb.calculate.BuildsiteBalance;
import cn.com.sailin.falconweb.calculate.HistoryBalance;
import cn.com.sailin.falconweb.calculate.NewBs;
import cn.com.sailin.falconweb.calculate.NewWorker;
import cn.com.sailin.falconweb.calculate.NoffWorker;
import cn.com.sailin.falconweb.dao.Data;
import cn.com.sailin.falconweb.model.Acfunctioninfo;
import cn.com.sailin.falconweb.model.Acmenustruct;
import cn.com.sailin.falconweb.model.Acpy;
import cn.com.sailin.falconweb.model.Acpyitem;
import cn.com.sailin.falconweb.model.Actoolcfg;
import cn.com.sailin.falconweb.model.Acuserinfo;
import cn.com.sailin.falconweb.model.Annc;
import cn.com.sailin.falconweb.model.Bs_lc;
import cn.com.sailin.falconweb.model.Bs_ls;
import cn.com.sailin.falconweb.model.Bscollinfo;
import cn.com.sailin.falconweb.model.CallResult;
import cn.com.sailin.falconweb.model.Cyjl;
import cn.com.sailin.falconweb.model.Pxxx;
import cn.com.sailin.falconweb.model.Spanbl;
import cn.com.sailin.falconweb.model.Statcard;
import cn.com.sailin.falconweb.model.Sybscd;
import cn.com.sailin.falconweb.model.Sycdtb;
import cn.com.sailin.falconweb.model.Wkds;
import cn.com.sailin.falconweb.model.Wker;
import cn.com.sailin.falconweb.model.Wkerzk;
import cn.com.sailin.falconweb.model.Yfdevice;
import cn.com.sailin.falconweb.model.Zyjn;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class sh {

	private final static Logger log = LoggerFactory.getLogger("cn.com.sailin.falconweb.publiccode.sh");

	public static String Dsql(String sql, Data data) {

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

	public static String Esql(String sql, Data data) {

		try {
			data.getJdbc().execute(sql);

			return Code.resultSuccess();

		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "失败：" + e.getMessage());
		}
	}

	private static HashMap<String, Object> getLccdbybscd(String apcd, String bscd, Data data) {

		List<Map<String, Object>> lc = data.qryLccdbybscd(apcd, bscd);

		HashMap<String, Object> mbslc = new HashMap<String, Object>();

		for (Map<String, Object> mb : lc) {
			mbslc.put(Code.getFieldVal(mb, "LCCD", ""), mb);
		}

		List<Map<String, Object>> list = data.qryCccd();

		HashMap<String, Object> mc = new HashMap<String, Object>();

		for (Map<String, Object> m : list) {
			mbslc.get(Code.getFieldVal(m, "SYIDTB", ""));
			if (mbslc != null) {
				HashMap<String, String> c = new HashMap<String, String>();
				c.put("cd", Code.getFieldVal(m, "SYIDTB", ""));
				c.put("ds", Code.getFieldVal(m, "SYDSTB", ""));
				mc.put(c.get("cd"), c);
				mc.put(c.get("ds"), c);
			}
		}

		return mc;
	}

	/**
	 * 添加审核批次数据
	 * 
	 * @param applydata
	 * @param data
	 * @return
	 */

	public static String insertAcpy(String userid, String applydata, Data data) {

		try {
			JSONObject obj = JSON.parseObject(applydata);
			String month = Code.getFieldVal(obj, "MONTH", "");
			if (month.equals(""))
				return Code.resultError("1111", "发放月份不能为空");
			String apcd = Code.getFieldVal(obj, "APCD", "");
			if (apcd.equals(""))
				return Code.resultError("1111", "接入点不能为空");
			String bscd = Code.getFieldVal(obj, "BSCD", "");
			if (bscd.equals(""))
				return Code.resultError("1111", "工地代码不能为空");
			String sqnb = Code.getFieldVal(obj, "SQNB", "");
			if (sqnb.equals(""))
				return Code.resultError("1111", "发放批送不能为空");
			String sqrm = Code.getFieldVal(obj, "SQRM", "");

			// 获取该工地相关发薪公司代码
			HashMap<String, Object> mbslc = getLccdbybscd(apcd, bscd, data);
			// 检查数据是否有效
			// 获取人员信息
			List<Map<String, Object>> wkerlist = data.qryWker(apcd, bscd);
			HashMap<String, Object> mwk = new HashMap<String, Object>();
			if (wkerlist.size() == 0)
				return Code.resultError("1111", "没有该工地的人员信息");
			else {
				// hash
				for (Map<String, Object> m : wkerlist) {
					mwk.put(Code.getFieldVal(m, "IDCDNO", ""), m);
				}
			}
			// 将来要写到数据库的结构
			HashMap<String, Acpy> macpy = new HashMap<String, Acpy>();
			Acpy acpy = null;

			JSONArray array = obj.getJSONArray("ITEMS");
			List<Map<String, String>> errlist = new ArrayList<Map<String, String>>();
			if (array.size() == 0)
				return Code.resultError("1111", "审核数据不能为空");
			else {
				for (int i = 0; i < array.size(); i++) {
					JSONObject item = array.getJSONObject(i);
					Acpyitem acpyitem = new Acpyitem();
					acpyitem.setMONTH(month);
					acpyitem.setAPCD(apcd);
					acpyitem.setBSCD(bscd);
					acpyitem.setSQNB(sqnb);
					acpyitem.setIDCDNO(Code.getFieldVal(item, "IDCDNO", ""));
					if (acpyitem.getIDCDNO().equals(""))
						continue;
					acpyitem.setNAME(Code.getFieldVal(item, "NAME", ""));
					if (acpyitem.getNAME().equals("")) {
						errlist.add(Code.getErroritem(acpyitem.getIDCDNO(), "姓名不能为空"));
						continue;
					}
					acpyitem.setACPY(Code.getFieldVal(item, "ACPY", ""));
					if (acpyitem.getACPY().equals("")) {
						errlist.add(Code.getErroritem(acpyitem.getIDCDNO(), "发放金额不能为空"));
						continue;
					}
					acpyitem.setINTEMP(Code.getFieldVal(item, "INTEMP", ""));
					if (acpyitem.getINTEMP().equals("Y")) {

						Map<String, Object> wker = Code.getMapval(mwk, acpyitem.getIDCDNO());

						if (wker != null) {
							errlist.add(Code.getErroritem(acpyitem.getIDCDNO(), "该人员不是临时人员"));
							continue;
						}

						acpyitem.setLCCD(Code.getFieldVal(item, "LCCD", ""));

						acpyitem.setBKCD(Code.getFieldVal(item, "BKCD", ""));
						acpyitem.setBKAN(Code.getFieldVal(item, "BKAN", ""));
					} else {
						Map<String, Object> wker = Code.getMapval(mwk, acpyitem.getIDCDNO());
						if (wker == null) {
							errlist.add(Code.getErroritem(acpyitem.getIDCDNO(), "找不到该人员信息"));
							continue;
						} else {
							acpyitem.setLCCD(Code.getFieldVal(wker, "LCCD", ""));
							acpyitem.setBKCD(Code.getFieldVal(wker, "BKCD", ""));
							acpyitem.setBKAN(Code.getFieldVal(wker, "BKAN", ""));
							if (!acpyitem.getNAME().equals(Code.getFieldVal(wker, "NAME", ""))) {
								errlist.add(Code.getErroritem(acpyitem.getIDCDNO(), "人员信息姓名不符"));
								continue;
							}
						}
					}
					if (acpyitem.getLCCD().equals("")) {
						errlist.add(Code.getErroritem(acpyitem.getIDCDNO(), "人员信息中发薪公司不能为空"));
						continue;
					}

					Object bslc = mbslc.get(acpyitem.getLCCD());
					if (bslc == null) {
						errlist.add(Code.getErroritem(acpyitem.getIDCDNO(), "人员信息中发薪公司不是工地对应的发薪公司"));
						continue;
					}
					// if (acpyitem.getBKCD().equals("")) {
					// errlist.add(Code.getErroritem(acpyitem.getIDCDNO(), "人员信息中银行代码不能为空"));
					// continue;
					// }
					if (acpyitem.getBKAN().equals("")) {
						errlist.add(Code.getErroritem(acpyitem.getIDCDNO(), "人员信息中银行账号不能为空"));
						continue;
					}

					acpy = macpy.get(acpyitem.getLCCD());
					if (acpy == null) {
						acpy = new Acpy();
						acpy.setMONTH(month);
						acpy.setAPCD(apcd);
						acpy.setBSCD(bscd);
						acpy.setLCCD(acpyitem.getLCCD());
						acpy.setSQNB(sqnb);
						acpy.setSQRM(sqrm);
						acpy.setUPLDUR(userid);
						macpy.put(acpy.getLCCD(), acpy);
					}
					acpy.getItems().add(acpyitem);

				}

				if (errlist.size() > 0)
					return Code.resultError("1002", JSON.toJSONString(errlist));
			}

			// 删除原数据
			CallResult result = JSON.parseObject(delAcpy(acpy.getSQNB(), data), CallResult.class);
			// 如果删除出错
			if (!result.getMSGID().equals("0000"))
				return JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);

			// 添加发放审核数据
			for (Entry<String, Acpy> entry : macpy.entrySet()) {

				Acpy ay = entry.getValue();
				data.insertAcpy(ay);

				for (int i = 0; i < ay.getItems().size(); i++) {
					data.insertAcpyitem(ay.getItems().get(i));
				}

				doBscoll(ay.getMONTH(), ay.getAPCD(), ay.getBSCD(), ay.getSQNB(), ay.getLCCD(), data);

			}

			doBscoll(month, apcd, bscd, data);

			return Code.resultSuccess();

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加批次数据出错" + e.getMessage());
		}
	}

	/**
	 * 删除审批次数据
	 * 
	 * @param applydata
	 *            直接是sqnb
	 * @param data
	 * @return
	 */
	public static String delAcpy(String applydata, Data data) {

		try {
			// 读取该批次的数据
			List<Map<String, Object>> list = data.qryAcpy(applydata);

			String month = "";
			String apcd = "";
			String bscd = "";

			for (Map<String, Object> m : list) {

				if (month.equals(""))
					month = Code.getFieldVal(m, "MONTH", "");
				if (apcd.equals(""))
					apcd = Code.getFieldVal(m, "APCD", "");
				if (bscd.equals(""))
					bscd = Code.getFieldVal(m, "BSCD", "");

				// 如果承包公司审核
				if (Code.getFieldVal(m, "INCCCK", "N").equals("Y")) {
					return Code.resultError("1111", "该批次承包公司已经审核不能删除");
				}
				// 如果劳务公司审核
				if (Code.getFieldVal(m, "INLCCK", "N").equals("Y")) {
					return Code.resultError("1111", "该批次发薪公司已经审核不能删除");
				}
				// 如果银行导出
				if (Code.getFieldVal(m, "INBKOT", "N").equals("Y")) {
					return Code.resultError("1111", "该批次银行已经导出不能删除");
				}
			}

			// 删除该发放月份工地发放批次的发放数据
			data.delAcpy(applydata);
			data.delAcpyitem(applydata);

			doBscoll(month, apcd, bscd, data);

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "删除原批次数据出错" + e.getMessage());
		}

		return Code.resultSuccess();

	}

	/**
	 * 设置承包公司审核
	 */
	public static String updateAcpyccck(String userid, String applydata, Data data) {

		try {
			JSONObject obj = JSON.parseObject(applydata);
			String sqnb = Code.getFieldVal(obj, "SQNB", "");
			if (sqnb.equals(""))
				return Code.resultError("1111", "批次号不能为空");
			String inccck = Code.getFieldVal(obj, "INCCCK", "");
			if (!inccck.equals("Y") && !inccck.equals("N"))
				return Code.resultError("1111", "非法的操作");
			// 读取该批次的数据
			List<Map<String, Object>> list = data.qryAcpy(sqnb);
			if (list.size() == 0)
				return Code.resultError("1111", "没有本批次的审核数据");

			String apcd = Code.getFieldVal(list.get(0), "APCD", "");
			String bscd = Code.getFieldVal(list.get(0), "BSCD", "");

			// 读取承包公司
			List<Map<String, Object>> lbs = data.qryBsinfo(apcd, bscd);
			if (lbs.size() == 0)
				return Code.resultError("1111", "没有该工地信息");

			String cccd = Code.getFieldVal(lbs.get(0), "CCCD", "");

			for (Map<String, Object> m : list) {
				// 如果审核是Y要求所有的发薪公司都审核是Y
				if (inccck.equals("Y")) {
					if (Code.getFieldVal(m, "INLCCK", "").equals("N")) {
						// 承包公司和发薪公司是同一家的话
						if (Code.getFieldVal(m, "LCCD", "").equals(cccd)) {
							data.updateAcpylcck(sqnb, cccd, inccck, userid);
						} else {
							return Code.resultError("1111", "发薪公司" + Code.getFieldVal(m, "LCCD", "") + "还未审核");
						}
					}
				} else {
					// 承包公司和发薪公司是同一家的话
					if (Code.getFieldVal(m, "LCCD", "").equals(cccd)) {
						data.updateAcpylcck(sqnb, cccd, inccck, userid);
					}
				}
			}

			for (Map<String, Object> m : list) {
				// 如果银行导出
				if (Code.getFieldVal(m, "INBKOT", "N").equals("Y")) {
					return Code.resultError("1111", "该批次银行已经导出不能更改");
				}
			}
			if (data.updateAcpyccck(sqnb, inccck, userid) == 0)
				return Code.resultError("2222", "没有更改数据");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("updateacpyccck:applydata:" + applydata + "message:" + e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "承包公司审核出错" + e.getMessage());
		}

		return Code.resultSuccess();

	}

	/**
	 * 设置劳务公司审核
	 */
	public static String updateAcpylcck(String userid, String applydata, Data data) {

		try {
			JSONObject obj = JSON.parseObject(applydata);
			String sqnb = Code.getFieldVal(obj, "SQNB", "");
			if (sqnb.equals(""))
				return Code.resultError("1111", "批次号不能为空");
			String lccd = Code.getFieldVal(obj, "LCCD", "");
			if (lccd.equals(""))
				return Code.resultError("1111", "发薪公司不能为空");
			String inlcck = Code.getFieldVal(obj, "INLCCK", "");
			if (!inlcck.equals("Y") && !inlcck.equals("N"))
				return Code.resultError("1111", "非法的操作");

			// 读取该批次的数据
			List<Map<String, Object>> list = data.qryAcpy(sqnb, lccd);

			if (list.size() > 0) {
				Map<String, Object> m = list.get(0);

				String apcd = Code.getFieldVal(m, "APCD", "");
				String bscd = Code.getFieldVal(m, "BSCD", "");

				// 读取承包公司
				List<Map<String, Object>> lbs = data.qryBsinfo(apcd, bscd);
				if (lbs.size() == 0)
					return Code.resultError("1111", "没有该工地信息");

				String cccd = Code.getFieldVal(lbs.get(0), "CCCD", "");

				// 如果银行导出
				if (Code.getFieldVal(m, "INBKOT", "N").equals("Y")) {
					return Code.resultError("1111", "该批次银行已经导出不能更改");
				}

				// 如果承包公司和发薪公司是同一家的话就调用承包公司确认
				if (lccd.equals(cccd)) {
					JSONObject req = new JSONObject();
					req.put("SQNB", sqnb);
					req.put("LCCD", lccd);
					req.put("INCCCK", inlcck);
					return updateAcpyccck(userid, req.toJSONString(), data);
				}

				// 如果是取消审核
				if (inlcck.equals("N")) {
					if (Code.getFieldVal(m, "INCCCK", "").equals("Y"))
						return Code.resultError("1111", "承包公司已审核不能取消审核");
				}
			} else {
				return Code.resultError("1111", "没有本批次的审核数据");
			}
			if (data.updateAcpylcck(sqnb, lccd, inlcck, userid) == 0)
				return Code.resultError("2222", "没有更改数据");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("updateacpylcck:applydata:" + applydata + "message:" + e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "发薪公司审核出错" + e.getMessage());
		}

		return Code.resultSuccess();

	}

	/**
	 * 银行导出
	 */
	public static String updateAcpybkot(String userid, String applydata, Data data) {

		try {
			JSONObject obj = JSON.parseObject(applydata);
			String sqnb = Code.getFieldVal(obj, "SQNB", "");
			if (sqnb.equals(""))
				return Code.resultError("1111", "批次号不能为空");
			String lccd = Code.getFieldVal(obj, "LCCD", "");
			if (lccd.equals(""))
				return Code.resultError("1111", "发薪公司不能为空");
			String inbkot = Code.getFieldVal(obj, "INBKOT", "");
			if (!inbkot.equals("Y") && !inbkot.equals("N"))
				return Code.resultError("1111", "非法的操作");
			// 读取该批次的数据
			List<Map<String, Object>> list = data.qryAcpy(sqnb, lccd);
			if (inbkot.equals("Y")) {
				// if (list.size() > 0) {
				// Map<String, Object> m = list.get(0);
				// // 如果银行导出
				// if (!Code.getFieldVal(m, "INCCCK", "N").equals("Y")) {
				// return Code.resultError("1111", "该批次承包公司未审核不能导出");
				// }
				// if (!Code.getFieldVal(m, "INLCCK", "N").equals("Y")) {
				// return Code.resultError("1111", "该批次劳务公司未审核不能导出");
				// }
				// } else {
				// return Code.resultError("1111", "没有本批次的审核数据");
				// }
			} else {
				if (list.size() > 0) {
					Map<String, Object> m = list.get(0);
					if (Code.getFieldVal(m, "INBKCFM", "N").equals("Y")) {
						return Code.resultError("1111", "该批次银行已确认不能取消导出");
					}
				}
			}
			if (data.updateAcpybkot(sqnb, lccd, inbkot, userid) == 0)
				return Code.resultError("2222", "没有更改数据");
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "设置银行导出出错" + e.getMessage());
		}

		return Code.resultSuccess();

	}

	/*
	 * 银行确认
	 */
	public static String updateAcpybkcfm(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String sqnb = Code.getFieldVal(obj, "SQNB", "");
			if (sqnb.equals(""))
				return Code.resultError("1111", "批次号不能为空");
			String lccd = Code.getFieldVal(obj, "LCCD", "");
			if (lccd.equals(""))
				return Code.resultError("1111", "发薪公司不能为空");
			String inbkcfm = Code.getFieldVal(obj, "INBKCFM", "");
			if (!inbkcfm.equals("Y") && !inbkcfm.equals("N"))
				return Code.resultError("1111", "非法的操作");
			// 读取该批次的数据
			List<Map<String, Object>> list = data.qryAcpy(sqnb, lccd);
			if (inbkcfm.equals("Y")) {
				if (list.size() > 0) {
					// 如果银行确认
					if (!Code.getFieldVal(list.get(0), "INBKOT", "N").equals("Y"))
						return Code.resultError("1111", "数据还未导出不能确认");

				} else {
					return Code.resultError("1111", "没有本批次的审核数据");
				}
			}
			if (data.updateAcpybkcfm(sqnb, lccd, inbkcfm, userid) == 0)
				return Code.resultError("2222", "没有更改数据");
			if (inbkcfm.equals("N")) {
				// 如果是取消确认，把所有item改为未确认并要计算汇总数据
				// 如果是确认，item确认与否并不在本接口处理
				if (data.updateAcpyitembkcfm(sqnb, lccd, inbkcfm, "", "") == 0)
					return Code.resultError("2222", "没有更改数据");
				List<Map<String, Object>> lacpy = data.qryAcpy(sqnb);

				String month = "";
				String apcd = "";
				String bscd = "";
				if (lacpy.size() > 0) {
					Map<String, Object> m = lacpy.get(0);
					month = Code.getFieldVal(m, "MONTH", "");
					apcd = Code.getFieldVal(m, "APCD", "");
					bscd = Code.getFieldVal(m, "BSCD", "");
					doBscoll(month, apcd, bscd, sqnb, lccd, data);
				}
				doBscoll(month, apcd, bscd, data);
			}

			uploadsalary(sqnb, data);

			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "设置银行确认出错" + e.getMessage());
		}
	}

	public static String insertBslc(String userid, String applydata, Data data) {

		try {
			Bs_lc bslc = new Bs_lc();

			JSONObject obj = JSON.parseObject(applydata);

			bslc.setAPCD(Code.getFieldVal(obj, "APCD", ""));
			if (bslc.getAPCD().equals(""))
				return Code.resultError("1111", "接入点不能为空");
			bslc.setBSCD(Code.getFieldVal(obj, "BSCD", ""));
			if (bslc.getBSCD().equals(""))
				return Code.resultError("1111", "工地代码不能为空");
			bslc.setCHUR(userid);

			data.delBslc(bslc.getAPCD(), bslc.getBSCD());

			List<Map<String, Object>> lbs = data.qryBsinfo(bslc.getAPCD(), bslc.getBSCD());
			if (lbs.size() == 0) {
				return Code.resultError("1111", "没有工地数据");
			}
			String cccd = Code.getFieldVal(lbs.get(0), "CCCD", "");
			JSONArray ary = obj.getJSONArray("LCCD");

			for (int i = 0; i < ary.size(); i++) {
				bslc.setLCCD(ary.get(i).toString());
				if (bslc.getLCCD().equals(cccd))
					bslc.setINCC("Y");
				else
					bslc.setINCC("N");
				data.insertBslc(bslc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加工地劳务公司对应关系出错" + e.getMessage());
		}

		return Code.resultSuccess();

	}

	public static String insertBsls(String userid, String applydata, Data data) {

		try {
			Bs_ls bsls = new Bs_ls();

			JSONObject obj = JSON.parseObject(applydata);

			bsls.setAPCD(Code.getFieldVal(obj, "APCD", ""));
			if (bsls.getAPCD().equals(""))
				return Code.resultError("1111", "接入点不能为空");
			bsls.setBSCD(Code.getFieldVal(obj, "BSCD", ""));
			if (bsls.getBSCD().equals(""))
				return Code.resultError("1111", "工地代码不能为空");
			bsls.setCHUR(userid);

			data.delBsls(bsls.getAPCD(), bsls.getBSCD());

			List<Map<String, Object>> lbs = data.qryBsinfo(bsls.getAPCD(), bsls.getBSCD());
			if (lbs.size() == 0) {
				return Code.resultError("1111", "没有工地数据");
			}
			JSONArray ary = obj.getJSONArray("LSCD");

			for (int i = 0; i < ary.size(); i++) {
				bsls.setLSCD(ary.get(i).toString());
				data.insertBsls(bsls);
			}
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加工地劳务公司对应关系出错" + e.getMessage());
		}

		return Code.resultSuccess();

	}

	public static String delbslc(String applydata, Data data) {

		try {
			JSONObject obj = JSON.parseObject(applydata);
			String apcd = Code.getFieldVal(obj, "APCD", "");
			String bscd = Code.getFieldVal(obj, "BSCD", "");
			data.delBslc(apcd, bscd);
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "删除工地劳务公司对应关系出错" + e.getMessage());
		}

		return Code.resultSuccess();

	}

	public static String delbsls(String applydata, Data data) {

		try {
			JSONObject obj = JSON.parseObject(applydata);
			String apcd = Code.getFieldVal(obj, "APCD", "");
			String bscd = Code.getFieldVal(obj, "BSCD", "");
			data.delBsls(apcd, bscd);
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "删除工地劳务公司对应关系出错" + e.getMessage());
		}

		return Code.resultSuccess();

	}

	public static String delWkds(String applydata, Data data) {

		try {
			JSONObject obj = JSON.parseObject(applydata);
			String opcd = Code.getFieldVal(obj, "OPCD", "");
			String apcd = Code.getFieldVal(obj, "APCD", "");
			String bscd = Code.getFieldVal(obj, "BSCD", "");
			String month = Code.getFieldVal(obj, "MONTH", "");

			if (opcd.equals("MONTH")) {
				data.delWkds(apcd, bscd, month);
				return Code.resultSuccess();
			}
			if (opcd.equals("WORKER")) {
				JSONArray array = obj.getJSONArray("IDCDNOS");
				for (int i = 0; i < array.size(); i++) {
					data.delWkds(apcd, bscd, month, array.get(i).toString());
				}
				return Code.resultSuccess();
			}
			return Code.resultError("1111", "删除考勤数据出错意外的opcd值");
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "删除考勤数据出错" + e.getMessage());
		}
	}

	public static String insertWkds(String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String month = obj.getString("MONTH");
			String apcd = obj.getString("APCD");
			String bscd = obj.getString("BSCD");

			// 获取员工信息
			List<Map<String, Object>> wklist = data.qryWkerbs(apcd, bscd);
			// 遍历员工信息把数据全的员工加入一个set中
			HashSet<String> wkset = new HashSet<String>();
			for (Map<String, Object> mwk : wklist) {
				if (Code.getFieldVal(mwk, "LCCD", "").trim().equals(""))
					continue;
				if (Code.getFieldVal(mwk, "BKCD", "").trim().equals(""))
					continue;
				if (Code.getFieldVal(mwk, "BKAN", "").trim().equals(""))
					continue;
				wkset.add(Code.getFieldVal(mwk, "IDCDNO", "").trim());
			}

			JSONArray array = obj.getJSONArray("ITEMS");

			// 要导入的数据里人员信息有问题的人员
			HashSet<String> dsset = new HashSet<String>();
			// 遍历考勤数据如果能添加进正常员工信息的set说明有问题
			for (int i = 0; i < array.size(); i++) {
				JSONObject item = array.getJSONObject(i);
				if (wkset.add(item.getString("IDCDNO").trim()))
					dsset.add(item.getString("IDCDNO").trim());
			}
			// 如果有问题的员工set里有数据就返回1001以及集合的内容
			if (dsset.size() > 0)
				return Code.resultError("1001", JSON.toJSONString(dsset));

			// 添发薪公司
			HashMap<String, Map<String, Object>> wkmap = new HashMap<String, Map<String, Object>>();
			for (Map<String, Object> m : wklist) {
				wkmap.put(Code.getFieldVal(m, "IDCDNO", ""), m);
			}
			for (int i = 0; i < array.size(); i++) {
				Wkds wkds = new Wkds();
				JSONObject item = array.getJSONObject(i);
				wkds.setAPCD(apcd);
				wkds.setBSCD(bscd);
				wkds.setMONTH(month);
				wkds.setIDCDNO(item.getString("IDCDNO"));
				wkds.setNAME(item.getString("NAME"));
				wkds.setPOST(item.getString("POST"));
				wkds.setWKDS(item.getString("WKDS"));
				Map<String, Object> m = wkmap.get(wkds.getIDCDNO());
				if (m == null)
					wkds.setLCCD("");
				else
					wkds.setLCCD(Code.getFieldVal(m, "LCCD", ""));

				data.delWkds(wkds.getAPCD(), wkds.getBSCD(), wkds.getMONTH(), wkds.getIDCDNO());
				data.insertWkds(wkds);
			}

			List<Map<String, Object>> lacpy = data.qryAcpy(month, apcd, bscd);
			for (Map<String, Object> acpy : lacpy) {
				doBscoll(month, apcd, bscd, Code.getFieldVal(acpy, "SQNB", ""), Code.getFieldVal(acpy, "LCCD", ""),
						data);
			}

			doBscoll(month, apcd, bscd, data);

			return Code.resultSuccess();

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "导入考勤数据出错" + e.getMessage());
		}
	}

	public static String getNextval(String applydata, Data data) {

		try {

			String val = data.getNextval(applydata);

			if (!val.equals("")) {
				CallResult result = new CallResult();
				result.setMSGID("0000");
				result.setMSGDESC("成功");
				result.setDATA(val);
				return JSON.toJSONString(result);
			} else {
				return Code.resultError("1111", "读取序列号出错");
			}

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "读取序列号出错");
		}

	}

	public static String insertSpanbl(String userid, String applydata, Data data) {

		try {
			List<Spanbl> list = new ArrayList<Spanbl>();
			list = JSON.parseArray(applydata, Spanbl.class);

			for (int i = 0; i < list.size(); i++) {
				Spanbl sp = list.get(i);
				if (sp.getMONTH().equals(""))
					return Code.resultError("1111", "监管月份不能为空");
				if (sp.getAPCD().equals(""))
					return Code.resultError("1111", "接入点不能为空");
				if (sp.getBSCD().equals(""))
					return Code.resultError("1111", "工地代码不能为空");
				List<Map<String, Object>> msp = data.qrySpanbl(sp.getMONTH(), sp.getAPCD(), sp.getBSCD());
				if (msp.size() > 0) {
					sp.setSVDT(Code.getFieldVal(msp.get(0), "SVDT", ""));
					sp.setRQANBL(Code.getFieldVal(msp.get(0), "RQANBL", ""));
				} else {
					List<Map<String, Object>> mbs = data.qryBsinfo(sp.getAPCD(), sp.getBSCD());
					if (mbs.size() > 0) {
						sp.setSVDT(sp.getMONTH()
								+ Code.getFillStr(Code.getFieldVal(mbs.get(0), "BLWNDY", ""), "L", 2, "0"));
						sp.setRQANBL(Code.getFieldVal(mbs.get(0), "SPANRQBL", "0"));
					}
				}
				data.delSpanbl(sp);
				sp.setCHUR(userid);
				data.insertSpanbl(sp);
				data.insertSpanbllog(sp, userid);
			}

			return Code.resultSuccess();

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "导入专用账户余额出错" + e.getMessage());
		}
	}

	public static String getTest(String applydata, Data data) {

		return null;
	}

	public static String getNoffWorker(String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String month = Code.getFieldVal(obj, "MONTH", "");
			if (month.equals(""))
				return Code.resultError("1111", "发放月份不能为空");
			String apcd = Code.getFieldVal(obj, "APCD", "");
			if (apcd.equals(""))
				return Code.resultError("1111", "接入点不能为空");
			String bscd = Code.getFieldVal(obj, "BSCD", "");
			if (bscd.equals(""))
				return Code.resultError("1111", "工地代码不能为空");

			return Code.resultSuccess(new NoffWorker(month, apcd, bscd, data).calculate());
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "获取欠薪员工数据出错" + e.getMessage());
		}
	}

	private static Bscollinfo buildBscollinfo(String month, String apcd, String bscd, Data data) {
		Bscollinfo bs = new Bscollinfo();
		bs.setMONTH(month);
		bs.setAPCD(apcd);
		bs.setBSCD(bscd);

		// 获取考勤数据
		List<Map<String, Object>> wkdslist = data.qryWkds(month, apcd, bscd);
		// 获取发薪数据
		List<Map<String, Object>> apitlist = data.qryAcpyitem(month, apcd, bscd);
		// 生成考勤人员表，以身份证为key，初始值为N表示未发薪
		HashMap<String, String> wkdsmap = new HashMap<String, String>();
		for (Map<String, Object> mwk : wkdslist) {
			wkdsmap.put(Code.getFieldVal(mwk, "IDCDNO", ""), "N");
		}
		// 考勤人数
		int wkrs = wkdsmap.size();
		// 生成发薪的集合
		HashSet<String> apset = new HashSet<String>();
		// 发放总额
		float totalAmount = 0;
		// 未确认数
		int nocfm = 0;
		for (Map<String, Object> map : apitlist) {
			totalAmount += Float.parseFloat(Code.getFieldVal(map, "ACPY", "0"));
			if (apset.add(Code.getFieldVal(map, "IDCDNO", ""))) {
				wkdsmap.put(Code.getFieldVal(map, "IDCDNO", ""), "Y");
			}
			if (!Code.getFieldVal(map, "INBKCFM", "N").equals("Y"))
				nocfm++;
		}

		// 获取欠薪人数
		int qxrs = 0;
		for (Entry<String, String> entry : wkdsmap.entrySet()) {
			if (entry.getValue().equals("N"))
				qxrs += 1;
		}

		int ffrs = apset.size();
		int kqffrs = wkrs - qxrs;
		float cfbl = 0;
		if (ffrs != 0)
			cfbl = (ffrs - kqffrs) / ffrs * 100;

		bs.setFFJE(String.valueOf(totalAmount));
		bs.setFFRS(String.valueOf(apset.size()));
		bs.setQXRS(String.valueOf(qxrs));
		bs.setWKRS(String.valueOf(wkrs));
		bs.setKQFFRS(String.valueOf(kqffrs));
		bs.setCFBL(String.valueOf(cfbl));
		bs.setNOCFM(String.valueOf(nocfm));

		return bs;
	}

	private static Bscollinfo buildBscollinfo(String month, String apcd, String bscd, String sqnb, String lccd,
			Data data) {
		Bscollinfo bs = new Bscollinfo();
		bs.setMONTH(month);
		bs.setAPCD(apcd);
		bs.setBSCD(bscd);

		// 获取考勤数据
		List<Map<String, Object>> wkdslist = data.qryWkds(month, apcd, bscd);
		// 获取发薪数据
		List<Map<String, Object>> apitlist = data.qryAcpyitem(month, apcd, bscd, sqnb, lccd);
		// 生成考勤人员表，以身份证为key，初始值为N表示未发薪
		// 按审核数据为基础
		HashMap<String, String> wkdsmap = new HashMap<String, String>();
		// 生成考勤人员表，以身份证为key，初始值为N表示未发薪
		// 按确认数据为基础
		HashMap<String, String> wkdscfmmap = new HashMap<String, String>();
		for (Map<String, Object> mwk : wkdslist) {
			wkdsmap.put(Code.getFieldVal(mwk, "IDCDNO", ""), "N");
			wkdscfmmap.put(Code.getFieldVal(mwk, "IDCDNO", ""), "N");
		}
		// 考勤人数
		int wkrs = wkdsmap.size();
		// 生成发薪的集合以审核数据为基础
		HashSet<String> apset = new HashSet<String>();
		// 生成发薪的集合以确认数据为基础
		HashSet<String> apcfmset = new HashSet<String>();
		// 发放总额
		float totalAmount = 0;
		// 未确认数
		int nocfm = 0;
		for (Map<String, Object> map : apitlist) {
			totalAmount += Float.parseFloat(Code.getFieldVal(map, "ACPY", "0"));
			String idcdno = Code.getFieldVal(map, "IDCDNO", "");
			if (apset.add(idcdno)) {
				wkdsmap.put(idcdno, "Y");
			}
			if (!Code.getFieldVal(map, "INBKCFM", "N").equals("Y")) {
				nocfm++;
				if (apcfmset.add(idcdno)) {
					wkdscfmmap.put(idcdno, "Y");
				}
			}
		}

		// 获取欠薪人数以审核数据为基础
		int qxrs = 0;
		for (Entry<String, String> entry : wkdsmap.entrySet()) {
			if (entry.getValue().equals("N"))
				qxrs += 1;
		}

		// 获取欠薪人数以确认数据为基础
		int qxrscfm = 0;
		for (Entry<String, String> entry : wkdscfmmap.entrySet()) {
			if (entry.getValue().equals("N"))
				qxrscfm++;
		}

		int ffrs = apset.size();
		int kqffrs = wkrs - qxrs;
		float cfbl = 0;
		if (ffrs != 0)
			cfbl = (ffrs - kqffrs) / ffrs * 100;

		bs.setFFJE(String.valueOf(totalAmount));
		bs.setFFRS(String.valueOf(apset.size()));
		bs.setQXRS(String.valueOf(qxrs));
		bs.setWKRS(String.valueOf(wkrs));
		bs.setKQFFRS(String.valueOf(kqffrs));
		bs.setCFBL(String.valueOf(cfbl));
		bs.setNOCFM(String.valueOf(nocfm));
		bs.setQXRSCFM(String.valueOf(qxrscfm));

		return bs;
	}

	private static void doBscoll(String month, String apcd, String bscd, Data data) {
		data.delBscollinfo(month, apcd, bscd);
		Bscollinfo bs = buildBscollinfo(month, apcd, bscd, data);
		data.insertBscollinfo(bs);
		return;
	}

	private static void doBscoll(String month, String apcd, String bscd, String sqnb, String lccd, Data data) {
		Bscollinfo bs = buildBscollinfo(month, apcd, bscd, sqnb, lccd, data);
		data.updateAcpy(sqnb, lccd, bs);
	}

	public static String doBscoll(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String month = Code.getFieldVal(obj, "MONTH", "");
			String apcd = Code.getFieldVal(obj, "APCD", "");
			String bscd = Code.getFieldVal(obj, "BSCD", "");

			doBscoll(month, apcd, bscd, data);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "计算工地数据出错" + e.getMessage());
		}
	}

	public static String delSybscd(String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String apcd = obj.getString("APCD");
			String bscd = obj.getString("BSCD");
			data.delSybscd(apcd, bscd);
			data.delAttendbscd(apcd, bscd);
			if (apcd.equals("ZK")) {
				String opid = data.getNextval("OPID");
				JSONObject jo = new JSONObject();
				jo.put("deptnumber", bscd);
				data.insertOplist("ZK", opid, "delbs", jo.toJSONString());
			}
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "删除工地信息出错" + e.getMessage());
		}
	}

	public static String insertSybscd(String applydata, Data data) {
		try {
			Sybscd bs = JSON.parseObject(applydata, Sybscd.class);
			data.insertSybscd(bs);
			data.insertAttendbscd(bs);
			List<Map<String, Object>> list = data.qryAttendbsinfo(bs.getAPCD(), bs.getBSCD());
			if (list.size() == 0)
				return Code.resultError("1111", "添加工地到考勤系统出错");
			bs.setBSCDPATH("1,2," + Code.getFieldVal(list.get(0), "ng_id", "") + ",");
			data.updateAttendbscd(bs);
			if (bs.getAPCD().equals("ZK")) {
				String opid = data.getNextval("OPID");
				JSONObject jo = new JSONObject();
				jo.put("deptnumber", bs.getBSCD());
				jo.put("deptname", bs.getBSDS());
				jo.put("parentnumber", "1");
				data.insertOplist("ZK", opid, "updatebs", "[" + jo.toJSONString() + "]");
			}

			uploadproject(bs.getAPCD(), bs.getBSCD(), data);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加工地信息出错" + e.getMessage());
		}
	}

	public static String updateSybscdByBs(String userid, String applydata, Data data) {
		try {
			Sybscd bs = new Sybscd();
			JSONObject obj = JSON.parseObject(applydata);
			bs.setAPCD(obj.getString("APCD"));
			bs.setBSCD(obj.getString("BSCD"));
			bs.setSVDP(obj.getString("SVDP"));
			bs.setBSDS(obj.getString("BSDS"));
			bs.setBKCD(obj.getString("BKCD"));
			bs.setPYDY(obj.getString("PYDY"));
			bs.setBCNM(obj.getString("BCNM"));
			bs.setCCCD(obj.getString("CCCD"));
			bs.setBSCHUR(userid);
			bs.setCKBGDY(Code.getFieldVal(obj, "CKBGDY", "1"));
			bs.setAJCODE(Code.getFieldVal(obj, "AJCODE", ""));
			bs.setTENDERID(Code.getFieldVal(obj, "TENDERID", ""));
			;// 中标编号
			bs.setPROJECTTYPE(Code.getFieldVal(obj, "PROJECTTYPE", ""));// 项目类别
			bs.setPRINNAME(Code.getFieldVal(obj, "PRINNAME", ""));// 负责人名称
			bs.setPRINTEL(Code.getFieldVal(obj, "PRINTEL", ""));// 负责人电话
			bs.setBSADDRESS(Code.getFieldVal(obj, "BSADDRESS", ""));// 详细地址
			bs.setLICENSEKEY(Code.getFieldVal(obj, "LICENSEKEY", ""));// 施工许可证号
			bs.setSTARTDATE(Code.getFieldVal(obj, "STARTDATE", ""));// 项目开始时间
			bs.setENDDATE(Code.getFieldVal(obj, "ENDDATE", ""));// 项目结束时间
			bs.setPROJECTCOST(Code.getFieldVal(obj, "PROJECTCOST", ""));// 工程造价
			bs.setSALARYCOST(Code.getFieldVal(obj, "SALARYCOST", ""));// 工资性工程款
			bs.setLEASESTARTDATE(Code.getFieldVal(obj, "LEASESTARTDATE", ""));
			bs.setLEASEENDDATE(Code.getFieldVal(obj, "LEASEENDDATE", ""));
			bs.setSOFTUSETYPE(Code.getFieldVal(obj, "SOFTUSETYPE", ""));

			List<Map<String, Object>> list = data.qrySybscd(bs.getAPCD(), bs.getBSCD());
			if (list.size() == 0)
				return Code.resultError("1111", "更改工地信息出错，找不到该工地，可能已被删除。");
			list = data.qryAttendbsinfo(bs.getAPCD(), bs.getBSCD());
			if (list.size() == 0)
				return Code.resultError("1111", "更改工地信息出错，在考勤系统中找不到该工地");
			bs.setBSCDPATH("1,2," + Code.getFieldVal(list.get(0), "ng_id", "") + ",");

			if (data.updateSybscdByBs(bs) == 0)
				return Code.resultError("2222", "没有更改数据");
			if (data.updateAttendbscd(bs) == 0)
				return Code.resultError("2222", "没有更改数据");

			if (bs.getAPCD().equals("ZK")) {
				String opid = data.getNextval("OPID");
				JSONObject jo = new JSONObject();
				jo.put("deptnumber", bs.getBSCD());
				jo.put("deptname", bs.getBSDS());
				jo.put("parentnumber", "1");
				data.insertOplist("ZK", opid, "updatebs", "[" + jo.toJSONString() + "]");
			}

			uploadproject(bs.getAPCD(), bs.getBSCD(), data);

			return Code.resultSuccess();

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "更改工地信息出错" + e.getMessage());
		}
	}

	public static String updateSybscdByBank(String userid, String applydata, Data data) {
		try {
			Sybscd bs = new Sybscd();
			JSONObject obj = JSON.parseObject(applydata);
			bs.setAPCD(obj.getString("APCD"));
			bs.setBSCD(obj.getString("BSCD"));
			bs.setSPAN(obj.getString("SPAN"));
			bs.setSPANRQBL(obj.getString("SPANRQBL"));
			bs.setBLWNDY(obj.getString("BLWNDY"));
			bs.setSPANPREBL(Code.getFieldVal(obj, "SPANPREBL", ""));
			bs.setBKCHUR(userid);

			List<Map<String, Object>> list = data.qrySybscd(bs.getAPCD(), bs.getBSCD());
			if (list.size() == 0)
				return Code.resultError("1111", "更改工地信息出错，找不到该工地，可能已被删除。");

			if (data.updateSybscdByBank(bs) == 0)
				return Code.resultError("2222", "没有更改数据");

			uploadproject(bs.getAPCD(), bs.getBSCD(), data);

			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "更改工地信息出错" + e.getMessage());
		}
	}

	public static String updateSybscdByDt(String userid, String applydata, Data data) {
		try {
			Sybscd bs = new Sybscd();
			JSONObject obj = JSON.parseObject(applydata);
			bs.setAPCD(Code.getFieldVal(obj, "APCD", ""));
			if (bs.getAPCD().equals(""))
				return Code.resultError("1111", "接入点不能为空");
			bs.setBSCD(Code.getFieldVal(obj, "BSCD", ""));
			if (bs.getBSCD().equals(""))
				return Code.resultError("1111", "工地代码不能为空");
			bs.setINDT(Code.getFieldVal(obj, "INDT", ""));
			if (bs.getINDT().equals(""))
				return Code.resultError("1111", "是否撤除不能为空");
			bs.setENDDATE(Code.getFieldVal(obj, "ENDDATE", ""));
			if (bs.getENDDATE().equals(""))
				return Code.resultError("1111", "项目结束时间不能为空");
			bs.setDTUR(userid);
			if (data.updateSybscdByDt(bs) == 0)
				return Code.resultError("2222", "没有更改数据");

			uploadproject(bs.getAPCD(), bs.getBSCD(), data);

			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "撤除操作有误" + e.getMessage());
		}
	}

	public static String insertSycdtb(String userid, String applydata, Data data) {
		try {
			Sycdtb cd = JSON.parseObject(applydata, Sycdtb.class);
			data.delSycdtb(cd.getSYCDTB(), cd.getSYIDTB());
			data.insertSycdtb(cd);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加系统代码有误" + e.getMessage());
		}
	}

	public static String delSycdtb(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String sycd = obj.getString("SYCDTB");
			String syid = obj.getString("SYIDTB");
			data.delSycdtb(sycd, syid);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "删除系统代码有误" + e.getMessage());
		}
	}

	public static String getBsmonitor(String userid, String applydata, Data data) {
		try {

			JSONObject obj = JSON.parseObject(applydata);
			String reqtype = Code.getFieldVal(obj, "REQTYPE", "");
			if (reqtype.equals(""))
				return Code.resultError("1111", "类型不能为空");
			String reqval = Code.getFieldVal(obj, "REQVAL", "");
			if (reqval.equals(""))
				return Code.resultError("1111", "值不能为空");

			BuildsiteBalance bsbl = new BuildsiteBalance(reqtype, reqval, data);

			return Code.resultSuccess(bsbl.calculate());

		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "获取工地监管数据出错" + e.getMessage());
		}
	}

	public static String getBshisinfo(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String reqtype = Code.getFieldVal(obj, "REQTYPE", "");
			if (reqtype.equals(""))
				return Code.resultError("1111", "类型不能为空");
			String reqval = Code.getFieldVal(obj, "REQVAL", "");
			if (reqval.equals(""))
				return Code.resultError("1111", "值不能为空");
			String startdate = Code.getFieldVal(obj, "STARTDATE", "");
			if (startdate.equals(""))
				return Code.resultError("1111", "登记开始日期不能为空");
			String enddate = Code.getFieldVal(obj, "ENDDATE", "");
			if (enddate.equals(""))
				return Code.resultError("1111", "登记结束日期不能为空");

			return Code.resultSuccess(new HistoryBalance(reqtype, reqval, startdate, enddate, data).calculate());
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "获取工地历史数据出错" + e.getMessage());
		}
	}

	private static Wker getWker(String apcd, String bscd, String idcdno, Data data) {
		Wker wker = new Wker();
		wker.setAPCD(apcd);
		wker.setBSCD(bscd);
		wker.setIDCDNO(idcdno);

		List<Map<String, Object>> list = null;
		Map<String, Object> m = null;

		list = data.qryWker(idcdno);
		if (list.size() > 0) {
			m = list.get(0);
			wker.setNAME(Code.getFieldVal(m, "NAME", ""));
			wker.setRGDT(Code.getFieldVal(m, "RGDT", ""));
			wker.setRGUR(Code.getFieldVal(m, "RGUR", ""));
			wker.setPXQK(Code.getFieldVal(m, "PXQK", ""));
			wker.setZYJN(Code.getFieldVal(m, "ZYJN", ""));
			wker.setCYJL(Code.getFieldVal(m, "CYJL", ""));
			wker.setINDY(Code.getFieldVal(m, "INDY", ""));
		} else {
			return null;
		}
		list = data.qryWkerbs(apcd, bscd, idcdno);
		if (list.size() > 0) {
			m = list.get(0);
			wker.setLCCD(Code.getFieldVal(m, "LCCD", ""));
			wker.setBKCD(Code.getFieldVal(m, "BKCD", ""));
			wker.setBKAN(Code.getFieldVal(m, "BKAN", ""));
			wker.setSZ_EMPLOY_ID(Code.getFieldVal(m, "SZ_EMPLOY_ID", ""));
		} else {
			return null;
		}

		return wker;
	}

	public static String delWker(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String apcd = obj.getString("APCD");
			String bscd = obj.getString("BSCD");
			String idcdno = obj.getString("IDCDNO");

			Wker wker = getWker(apcd, bscd, idcdno, data);

			if (wker == null) {
				return Code.resultSuccess();
			}

			// 获取考勤系统工人数据
			List<Map<String, Object>> lwk = data.qryAttendUser(apcd, wker.getSZ_EMPLOY_ID());
			if (lwk.size() == 0)
				return "考勤系统没有工人信息" + wker.getSZ_EMPLOY_ID();
			String employid = Code.getFieldVal(lwk.get(0), "ng_id", "");
			// 获取考勤系统工地数据
			List<Map<String, Object>> lbs = data.qryAttendbsinfo(wker.getAPCD(), wker.getBSCD());
			if (lbs.size() == 0)
				return "考勤系统没有工地信息" + wker.getBSCD();
			String bsid = Code.getFieldVal(lbs.get(0), "ng_id", "");

			if (data.delAttendWker(apcd, employid) == 0)
				return Code.resultError("1111", "考勤系统删除工人信息出错");
			data.delAttendUserbranch(apcd, employid, bsid);

			wker.setDLUR(userid);
			data.insertWkerlog(wker);
			data.delWkerbs(apcd, bscd, idcdno);

			// 职业技能
			data.delZyjn(wker.getIDCDNO());

			// 从业记录
			// 删除平台数据
			deleteexprience(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO(), data);
			data.delCyjl(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO());
			// 培训记录
			// 删除平台数据
			deletetrain(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO(), data);
			data.delPxxx(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO());

			// 删除平台数据
			deletehuman(wker.getAPCD(), wker.getBSCD(), idcdno, employid, data);

			// 添加中控操作
			if (wker.getAPCD().equals("ZK")) {

				String opid = data.getNextval("OPID");

				Wkerzk wkerzk = new Wkerzk();

				wkerzk.setPin(wker.getSZ_EMPLOY_ID());

				data.insertOplist(wker.getAPCD(), opid, "delwker", JSON.toJSONString(wkerzk));

			}

			return Code.resultSuccess();

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "删除员工信息出错" + e.getMessage());
		}

	}

	private static String updateWker(Wker wker, Data data) {
		List<Map<String, Object>> list = data.qryAttendUser("HW", wker.getSZ_EMPLOY_ID());
		if (list.size() > 0) {
			if (data.updateAttendWker(wker) == 0)
				return "没有更新考勤系统人员数据";
		} else {
			data.insertAttendWker(wker);
		}

		data.updateWker(wker);

		return "";
	}

	private static String updateWkerbs(Wker wker, Data data) {
		// 获取考勤系统工人数据
		List<Map<String, Object>> lwk = data.qryAttendUser(wker.getAPCD(), wker.getSZ_EMPLOY_ID());
		if (lwk.size() == 0)
			return "考勤系统没有工人信息" + wker.getSZ_EMPLOY_ID();
		String employid = Code.getFieldVal(lwk.get(0), "ng_id", "");
		// 获取考勤系统工地数据
		List<Map<String, Object>> lbs = data.qryAttendbsinfo(wker.getAPCD(), wker.getBSCD());
		if (lbs.size() == 0)
			return "考勤系统没有工地信息" + wker.getBSCD();
		String bsid = Code.getFieldVal(lbs.get(0), "ng_id", "");
		// 获取考勤系统工种数据
		List<Map<String, Object>> lwkkd = data.qryAttendPost(wker.getAPCD(), wker.getWKKD());
		if (lwkkd.size() == 0)
			return "考勤系统没有工种信息" + wker.getWKKD();
		String wkkdid = Code.getFieldVal(lwkkd.get(0), "ng_id", "");
		if (data.updateAttendUserbranch(wker.getAPCD(), employid, bsid, wkkdid) == 0)
			return "没有更新考勤系统的工人和工地对应数据";
		if (data.updateWkerbs(wker) == 0)
			return "没有更新工人和工地对应数据";
		return "";
	}

	private static String insertWker(Wker wker, Data data) {
		List<Map<String, Object>> list = data.qryAttendUser("HW", wker.getSZ_EMPLOY_ID());
		if (list.size() > 0) {
			if (data.updateAttendWker(wker) == 0)
				return "没有更新考勤系统人员数据";
		} else {
			data.insertAttendWker(wker);
		}

		data.insertWker(wker);
		return "";
	}

	private static String insertWkerbs(Wker wker, Data data) {
		// 获取考勤系统工人数据
		List<Map<String, Object>> lwk = data.qryAttendUser(wker.getAPCD(), wker.getSZ_EMPLOY_ID());
		if (lwk.size() == 0)
			return "考勤系统没有工人信息" + wker.getSZ_EMPLOY_ID();
		String employid = Code.getFieldVal(lwk.get(0), "ng_id", "");
		// 获取考勤系统工地数据
		List<Map<String, Object>> lbs = data.qryAttendbsinfo(wker.getAPCD(), wker.getBSCD());
		if (lbs.size() == 0)
			return "考勤系统没有工地信息" + wker.getBSCD();
		String bsid = Code.getFieldVal(lbs.get(0), "ng_id", "");
		// 获取考勤系统工种数据
		List<Map<String, Object>> lwkkd = data.qryAttendPost(wker.getAPCD(), wker.getWKKD());
		if (lwkkd.size() == 0)
			return "考勤系统没有工种信息" + wker.getWKKD();
		String wkkdid = Code.getFieldVal(lwkkd.get(0), "ng_id", "");
		// 获取对应关系
		List<Map<String, Object>> lwkbskd = data.qryAttendUserbranch(wker.getAPCD(), employid, bsid, wkkdid);
		if (lwkbskd.size() == 0)
			data.insertAttendUserbranch(wker.getAPCD(), employid, bsid, wkkdid);
		data.insertWkerbs(wker);
		return "";
	}

	public static String insertWker(String userid, String applydata, Data data) {
		try {
			Wker wker = JSON.parseObject(applydata, Wker.class);

			if (wker.getIDCDNO().equals(""))
				return Code.resultError("1111", "身份证号码不能为空");
			if (wker.getAPCD().equals(""))
				return Code.resultError("1111", "接入点不能为空");
			if (wker.getBSCD().equals(""))
				return Code.resultError("1111", "工地代码不能为空");
			if (wker.getSZ_EMPLOY_ID().equals(""))
				return Code.resultError("1111", "工号不能为空");
			wker.setRGUR(userid);

			// 检查银行卡号是否可用
			if (!wker.getBKAN().trim().equals("")) {
				String bk = data.hasBankcard(wker.getIDCDNO(), wker.getBKAN());
				if (!bk.equals("Y")) {
					return Code.resultError("1111", bk);
				}
			}

			List<Map<String, Object>> lsTemp = null;
			// 工种检查
			lsTemp = data.qryAttendPostbyname("HW", wker.getWKNAME());
			// 如果是新的工种
			if (lsTemp.size() == 0) {
				data.insertAttendPost("HW", wker.getWKKD(), wker.getWKNAME());
			} else {
				wker.setWKKD(Code.getFieldVal(lsTemp.get(0), "SZ_CODE", ""));
			}
			// 班组检查
			lsTemp = data.qryBzbycd(wker.getAPCD(), wker.getBSCD(), wker.getBZ());
			// 如果是新的班组
			if (lsTemp.size() == 0) {
				Sycdtb cd = new Sycdtb();
				cd.setSYCDTB("BZCD");
				cd.setSYIDTB(wker.getBZ());
				cd.setSYDSTB(wker.getBZNAME());
				cd.setSYC1TB(wker.getBSCD());
				cd.setSYC2TB(wker.getAPCD());
				cd.setINUSCH("Y");
				data.insertSycdtb(cd);
			}

			List<Map<String, Object>> list = null;

			list = data.qryWker(wker.getIDCDNO());

			String res = "";

			if (list.size() > 0) {
				res = updateWker(wker, data);
				if (!res.equals("")) {
					log.warn(res + " " + JSON.toJSONString(wker));
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return Code.resultError("1111", res);
				}
			} else {
				res = insertWker(wker, data);
				if (!res.equals("")) {
					log.warn(res + " " + JSON.toJSONString(wker));
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return Code.resultError("1111", res);
				}
			}

			list = data.qryWkerbs(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO());

			if (list.size() > 0) {
				res = updateWkerbs(wker, data);
				if (!res.equals("")) {
					log.warn(res + " " + JSON.toJSONString(wker));
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return Code.resultError("1111", res);
				}
			} else {
				res = insertWkerbs(wker, data);
				if (!res.equals("")) {
					log.warn(res + " " + JSON.toJSONString(wker));
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return Code.resultError("1111", res);
				}
			}

			// 职业技能
			data.delZyjn(wker.getIDCDNO());
			if (wker.getJN() != null) {
				for (Zyjn jn : wker.getJN()) {
					data.insertZyjn(wker.getIDCDNO(), jn);
				}
			}

			// 从业记录
			// 删除平台数据
			deleteexprience(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO(), data);
			data.delCyjl(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO());
			// 添加数据
			if (wker.getJL() != null) {
				for (Cyjl jl : wker.getJL()) {
					data.insertCyjl(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO(), jl);
				}
			}
			// 培训记录
			// 删除平台数据
			deletetrain(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO(), data);
			data.delPxxx(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO());
			// 添加数据
			if (wker.getPX() != null) {
				for (Pxxx px : wker.getPX()) {
					data.insertPxxx(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO(), px);
				}
			}

			// 添加中控操作
			if (wker.getAPCD().equals("ZK")) {
				String opid = data.getNextval("OPID");
				Wkerzk wkerzk = new Wkerzk();

				wkerzk.setPin(wker.getSZ_EMPLOY_ID());
				wkerzk.setName(wker.getNAME());
				wkerzk.setDeptnumber(wker.getBSCD());
				// if (wker.getINSEX().equals("男")) wkerzk.setGender("M");
				// if (wker.getINSEX().equals("女")) wkerzk.setGender("F");
				// wkerzk.setIdentitycard(wker.getIDCDNO());
				// wkerzk.setInlate(1);
				// wkerzk.setOutearly(1);
				// wkerzk.setAddress(wker.getHOMEADD());
				// wkerzk.setNational(wker.getETHNIC());
				// wkerzk.setHiretype(0);
				// wkerzk.setSelfpassword(wker.getUSERPASS());
				// wkerzk.setAtt(1);
				// wkerzk.setComverifys(0);

				JSONArray ja = new JSONArray();
				ja.add(wkerzk);

				data.insertOplist(wker.getAPCD(), opid, "wkerupdate", JSON.toJSONString(ja));
			}

			// 写设备人脸识别
			// if (!wker.getPIC3().trim().equals("") || !wker.getPIC4().trim().equals("")
			// || !wker.getPIC5().trim().equals("")) {
			// List<Map<String, Object>> wk = data.qryAttendUserbyidcdno(wker.getAPCD(),
			// wker.getIDCDNO());
			// if (wk.size() > 0) {
			// Statcard sc = new Statcard();
			// sc.setNg_user_id(Integer.parseInt(Code.getFieldVal(wk.get(0), "ng_id",
			// "0")));
			// // 写设备人脸识别
			// List<Map<String, Object>> l = data.qryAttenddevfeature(sc.getNg_user_id());
			// if (l.size() == 0)
			// data.insertAttenddevfeature(sc.getNg_user_id());
			// }
			// }

			// 写设备人脸识别
			if (!wker.getPIC3().trim().equals("") || !wker.getPIC4().trim().equals("")
					|| !wker.getPIC5().trim().equals("")) {
				List<Map<String, Object>> wk = data.qryAttendUserbywkid(wker.getAPCD(), wker.getSZ_EMPLOY_ID());
				if (wk.size() > 0) {
					Statcard sc = new Statcard();
					sc.setNg_user_id(Integer.parseInt(Code.getFieldVal(wk.get(0), "ng_id", "0")));
					// 写设备人脸识别
					List<Map<String, Object>> l = data.qryAttenddevfeature(sc.getNg_user_id());
					if (l.size() == 0)
						data.insertAttenddevfeature(sc.getNg_user_id());
				}
			}

			// 上传数据
			uploadhuman(wker.getAPCD(), wker.getBSCD(), wker.getSZ_EMPLOY_ID(), data);
			uploadexprience(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO(), data);
			uploadtrain(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO(), data);

			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加员工信息出错" + e.getMessage());
		}
	}

	public static String updateWker(String userid, String applydata, Data data) {
		try {
			Wker wker = JSON.parseObject(applydata, Wker.class);
			wker.setRGUR(userid);

			// 检查银行卡号是否可用
			if (!wker.getBKAN().trim().equals("")) {
				String bk = data.hasBankcard(wker.getIDCDNO(), wker.getBKAN());
				if (!bk.equals("Y")) {
					return Code.resultError("1111", bk);
				}
			}

			List<Map<String, Object>> lsTemp = null;
			// 工种检查
			lsTemp = data.qryAttendPostbyname("HW", wker.getWKNAME());
			// 如果是新的工种
			if (lsTemp.size() == 0) {
				data.insertAttendPost("HW", wker.getWKKD(), wker.getWKNAME());
			} else {
				wker.setWKKD(Code.getFieldVal(lsTemp.get(0), "SZ_CODE", ""));
			}
			// 班组检查
			lsTemp = data.qryBzbycd(wker.getAPCD(), wker.getBSCD(), wker.getBZ());
			// 如果是新的班组
			if (lsTemp.size() == 0) {
				Sycdtb cd = new Sycdtb();
				cd.setSYCDTB("BZCD");
				cd.setSYIDTB(wker.getBZ());
				cd.setSYDSTB(wker.getBZNAME());
				cd.setSYC1TB(wker.getBSCD());
				cd.setSYC2TB(wker.getAPCD());
				cd.setINUSCH("Y");
				data.insertSycdtb(cd);
			}

			String res = "";

			res = updateWkerbs(wker, data);
			if (!res.equals(""))
				return Code.resultError("1111", res);

			res = updateWker(wker, data);
			if (!res.equals(""))
				return Code.resultError("1111", res);

			// 职业技能
			data.delZyjn(wker.getIDCDNO());
			if (wker.getJN() != null) {
				for (Zyjn jn : wker.getJN()) {
					data.insertZyjn(wker.getIDCDNO(), jn);
				}
			}

			// 从业记录
			// 删除平台数据
			deleteexprience(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO(), data);
			data.delCyjl(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO());
			// 添加数据
			if (wker.getJL() != null) {
				for (Cyjl jl : wker.getJL()) {
					data.insertCyjl(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO(), jl);
				}
			}
			// 培训记录
			// 删除平台数据
			deletetrain(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO(), data);
			data.delPxxx(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO());
			// 添加数据
			if (wker.getPX() != null) {
				for (Pxxx px : wker.getPX()) {
					data.insertPxxx(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO(), px);
				}
			}

			// 添加中控操作
			if (wker.getAPCD().equals("ZK")) {
				String opid = data.getNextval("OPID");
				Wkerzk wkerzk = new Wkerzk();

				wkerzk.setPin(wker.getSZ_EMPLOY_ID());
				wkerzk.setName(wker.getNAME());
				wkerzk.setDeptnumber(wker.getBSCD());
				// if (wker.getINSEX().equals("男")) wkerzk.setGender("M");
				// if (wker.getINSEX().equals("女")) wkerzk.setGender("F");
				// wkerzk.setIdentitycard(wker.getIDCDNO());
				// wkerzk.setInlate(1);
				// wkerzk.setOutearly(1);
				// wkerzk.setAddress(wker.getHOMEADD());
				// wkerzk.setNational(wker.getETHNIC());
				// wkerzk.setHiretype(0);
				// wkerzk.setSelfpassword(wker.getUSERPASS());
				// wkerzk.setAtt(1);
				// wkerzk.setComverifys(0);

				JSONArray ja = new JSONArray();
				ja.add(wkerzk);

				data.insertOplist(wker.getAPCD(), opid, "wkerupdate", JSON.toJSONString(ja));
			}

			// 写设备人脸识别
			if (!wker.getPIC3().trim().equals("") || !wker.getPIC4().trim().equals("")
					|| !wker.getPIC5().trim().equals("")) {
				List<Map<String, Object>> wk = data.qryAttendUserbywkid(wker.getAPCD(), wker.getSZ_EMPLOY_ID());
				if (wk.size() > 0) {
					Statcard sc = new Statcard();
					sc.setNg_user_id(Integer.parseInt(Code.getFieldVal(wk.get(0), "ng_id", "0")));
					// 写设备人脸识别
					List<Map<String, Object>> l = data.qryAttenddevfeature(sc.getNg_user_id());
					if (l.size() == 0)
						data.insertAttenddevfeature(sc.getNg_user_id());
				}
			}

			// 上传数据
			uploadhuman(wker.getAPCD(), wker.getBSCD(), wker.getSZ_EMPLOY_ID(), data);
			uploadexprience(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO(), data);
			uploadtrain(wker.getAPCD(), wker.getBSCD(), wker.getIDCDNO(), data);

			return Code.resultSuccess();

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "更改民工数据出错" + e.getMessage());
		}
	}

	public static String resetPassword(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String user = Code.getFieldVal(obj, "USERID", "");
			if (user.equals(""))
				return Code.resultError("1111", "用户名不能为空");
			String newpassword = obj.getString("NEWPASSWORD");
			if (newpassword.equals(""))
				return Code.resultError("1111", "新的密码不能为空");
			if (data.resetPassword(user, newpassword) == 0)
				return Code.resultError("2222", "没有更改数据");
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "重置密码出错" + e.getMessage());
		}
	}

	public static String attendSubmit(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);

			String apcd = "ZQ";
			String idcdno = "";
			idcdno = Code.getFieldVal(obj, "idcdno", "");
			String dt = "";

			List<Map<String, Object>> wk = data.qryAttendUserbyidcdno("ZQ", idcdno);

			if (wk.size() > 0) {

				Statcard sc = new Statcard();
				sc.setApcd(apcd);
				sc.setApid(obj.getInteger("seqno"));
				sc.setNg_branch_id(getAttendbranchid("ZQ", Code.getFieldVal(obj, "bscd", ""), data));
				sc.setNg_dev_id(0);
				sc.setNg_user_id(Integer.parseInt(Code.getFieldVal(wk.get(0), "ng_id", "0")));
				sc.setSt_kind(0);
				sc.setSz_dev_name(Code.getFieldVal(obj, "drivename", ""));
				sc.setSz_employ_id(Code.getFieldVal(wk.get(0), "sz_employ_id", ""));
				dt = Code.getFieldVal(obj, "attendtime", "");
				sc.setTs_card(dt.substring(0, 4) + "-" + dt.substring(4, 6) + "-" + dt.substring(6, 8) + " "
						+ dt.substring(8, 10) + ":" + dt.substring(10, 12) + ":" + dt.substring(12, 14));
				data.delAttendData(apcd, sc.getApid());
				data.insertAttendData(apcd, sc);

				// 写设备人脸识别
				List<Map<String, Object>> l = data.qryAttenddevfeature(sc.getNg_user_id());
				if (l.size() == 0)
					data.insertAttenddevfeature(sc.getNg_user_id());

				return Code.resultSuccess();
			} else {
				return Code.resultError("1111", "没有该人员信息" + idcdno);
			}

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加考勤数据出错" + e.getMessage());
		}
	}

	public static String yfattendSubmit(String userid, String applydata, Data data) {

		try {
			JSONObject jo = JSONObject.parseObject(applydata);

			String userguid = Code.getFieldVal(jo, "personGuid", "");
			if (userguid.equals(""))
				return Code.resultError("1111", "用户不能为空");

			String devsn = Code.getFieldVal(jo, "deviceKey", "");
			if (devsn.equals(""))
				return Code.resultError("1111", "设备不能为空");
			List<Map<String, Object>> ldev = data.qryDevinfobydevsn(devsn);
			String bscd = "";
			String devname = "";
			if (ldev.size() > 0) {
				bscd = Code.getFieldVal(ldev.get(0), "BSCD", "");
				devname = Code.getFieldVal(ldev.get(0), "DEVICENAME", "");
			}
			String idcdno = data.getIdcdnobyyf(userguid);
			if (idcdno.equals(""))
				return Code.resultError("1111", "找不到该用户信息");
			if (bscd.equals(""))
				return Code.resultError("1111", "找不到工地信息");
			if (devname.equals(""))
				return Code.resultError("1111", "找不到设备信息");
			String showtime = Code.getFieldVal(jo, "showTime", "");
			if (showtime.equals(""))
				return Code.resultError("1111", "考勤时间不能为空");

			String apcd = "YF";

			String apid = data.getNextval("APID");

			String employid = "";
			List<Map<String, Object>> wkbs = data.qryWkerbs(apcd, bscd, idcdno);
			if (wkbs.size() > 0) {
				employid = Code.getFieldVal(wkbs.get(0), "SZ_EMPLOY_ID", "");
			}
			if (employid.equals(""))
				return Code.resultError("1111", "没有该人员信息" + idcdno);

			List<Map<String, Object>> wk = data.qryAttendUserbywkid(apcd, employid);

			if (wk.size() > 0) {

				Statcard sc = new Statcard();
				sc.setApcd(apcd);
				sc.setApid(Integer.parseInt(apid));
				sc.setNg_branch_id(getAttendbranchid(apcd, bscd, data));
				sc.setNg_dev_id(0);
				sc.setNg_user_id(Integer.parseInt(Code.getFieldVal(wk.get(0), "ng_id", "0")));
				sc.setSt_kind(0);
				sc.setSz_dev_name(devname);
				sc.setSz_employ_id(Code.getFieldVal(wk.get(0), "sz_employ_id", ""));
				sc.setTs_card(showtime);
				sc.setSz_photo_path(Code.getFieldVal(jo, "photoUrl", ""));
				data.delAttendData(apcd, sc.getApid());
				data.insertAttendData(apcd, sc);

				// 写设备人脸识别
				List<Map<String, Object>> l = data.qryAttenddevfeature(sc.getNg_user_id());
				if (l.size() == 0)
					data.insertAttenddevfeature(sc.getNg_user_id());

				return Code.resultSuccess();
			} else {
				return Code.resultError("1111", "没有该人员信息" + idcdno);
			}

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加考勤数据出错" + e.getMessage());
		}

	}

	public static String updatePassword(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String user = Code.getFieldVal(obj, "USERID", "");
			if (user.equals(""))
				return Code.resultError("1111", "用户名不能为空");
			String newpassword = obj.getString("NEWPASSWORD");
			if (newpassword.equals(""))
				return Code.resultError("1111", "新的密码不能为空");
			String oldpassword = Code.getFieldVal(obj, "OLDPASSWORD", "");
			if (oldpassword.equals(""))
				return Code.resultError("1111", "老的密码不能为空");

			if (data.updatePassword(user, oldpassword, newpassword) == 0)
				return Code.resultError("2222", "没有更改数据");
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "更改密码出错" + e.getMessage());
		}
	}

	public static String updatePasswordworker(String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String idcdno = Code.getFieldVal(obj, "IDCDNO", "");
			if (idcdno.equals(""))
				return Code.resultError("1111", "身份证不能为空");
			String newpassword = Code.getFieldVal(obj, "NEWPASSWORD", "");
			if (newpassword.equals(""))
				return Code.resultError("1111", "新密码不能为空");
			String oldpassword = Code.getFieldVal(obj, "OLDPASSWORD", "");
			if (oldpassword.equals(""))
				return Code.resultError("1111", "老密码不能为空");
			if (data.updatePasswordworker(idcdno, oldpassword, newpassword) == 0)
				return Code.resultError("2222", "没有更改数据");
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "更改密码出错" + e.getMessage());
		}
	}

	public static String insertAcfunctioninfo(String userid, String applydata, Data data) {
		try {
			Acfunctioninfo func = JSON.parseObject(applydata, Acfunctioninfo.class);
			data.insertAcfunctioninfo(func);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加功能出错" + e.getMessage());
		}

	}

	public static String insertAcmenustruct(String userid, String applydata, Data data) {
		try {
			Acmenustruct menu = JSON.parseObject(applydata, Acmenustruct.class);
			data.insertAcmenustruct(menu);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加菜单出错" + e.getMessage());
		}
	}

	public static String insertActoolcfg(String userid, String applydata, Data data) {
		try {
			Actoolcfg cfg = JSON.parseObject(applydata, Actoolcfg.class);
			data.insertActoolcfg(cfg);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加工具条出错" + e.getMessage());
		}
	}

	public static String insertAcroleinfo(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String roleid = obj.getString("ROLEID");
			String rolename = obj.getString("ROLENAME");
			String roledesc = obj.getString("ROLEDESC");
			data.delAcroleinfo(roleid);
			data.insertAcroleinfo(roleid, rolename, roledesc);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加角色出错" + e.getMessage());
		}
	}

	public static String delAcrole(String userid, String applydata, Data data) {
		try {
			data.delAcrolefunctionbyroleid(applydata);
			data.delAcroleinfo(applydata);
			data.delAcroleuserbyroleid(applydata);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "删除角色出错" + e.getMessage());
		}
	}

	public static String insertAcrolefunction(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String roleid = obj.getString("ROLEID");
			data.delAcrolefunctionbyroleid(roleid);
			JSONArray array = obj.getJSONArray("FUNCTIONID");
			for (int i = 0; i < array.size(); i++) {
				data.insertAcrolefunction(roleid, array.get(i).toString());
			}
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加角色功能出错" + e.getMessage());
		}

	}

	public static String insertAcuserinfo(String userid, String applydata, Data data) {
		try {
			Acuserinfo ur = JSON.parseObject(applydata, Acuserinfo.class);
			data.insertAcuserinfo(ur);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加用户出错" + e.getMessage());
		}
	}

	public static String insertAcroleuser(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String user = obj.getString("USERID");
			JSONArray array = obj.getJSONArray("ROLEID");
			data.delAcroleuserbyuserid(user);
			for (int i = 0; i < array.size(); i++)
				data.insertAcroleuser(array.get(i).toString(), user);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加角色与用户关系出错" + e.getMessage());
		}
	}

	public static String delAcroleuserbyuserid(String userid, String applydata, Data data) {
		try {
			data.delAcroleuserbyuserid(applydata);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "删除角色与用户关系出错" + e.getMessage());
		}
	}

	public static String delAcuserinfo(String userid, String applydata, Data data) {
		try {
			data.delAcuserinfo(applydata);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "删除用户信息出错" + e.getMessage());
		}
	}

	public static String delAcmenustruct(String userid, String applydata, Data data) {
		try {
			data.delAcmenustruct(applydata);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "删除菜单出错" + e.getMessage());
		}
	}

	public static String delAcfunctioninfo(String userid, String applydata, Data data) {
		try {
			data.delAcfunctioninfo(applydata);
			data.delAcrolefunctionbyfuncid(applydata);
			data.clearAcmenustructbyfuncid(applydata);
			data.clearActoolcfgbyfuncid(applydata);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "删除功能出错" + e.getMessage());
		}
	}

	public static String delActoolcfg(String userid, String applydata, Data data) {
		try {
			data.delActoolcfg(applydata);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "删除工具条出错" + e.getMessage());
		}
	}

	public static String updateAcmenustrucinfo(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String mt = obj.getString("MENUTEXT");
			String pmid = obj.getString("PARENTMENUID");
			String fid = obj.getString("FUNCTIONID");
			String mid = obj.getString("MENUID");
			if (data.updateAcmenustructinfo(mt, pmid, fid, mid) == 0)
				return Code.resultError("1111", "没有更改数据");
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "更改菜单信息出错" + e.getMessage());
		}
	}

	public static String updateAcmenustructordernum(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String menuid1 = obj.getString("MENUID1");
			String ordernum1 = obj.getString("ORDERNUM1");
			String menuid2 = obj.getString("MENUID2");
			String ordernum2 = obj.getString("ORDERNUM2");
			if (data.updateAcmenustructordernum(menuid1, ordernum1) == 0)
				return Code.resultError("2222", "没有更改数据");
			if (data.updateAcmenustructordernum(menuid2, ordernum2) == 0)
				return Code.resultError("2222", "没有更改数据");
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "更改菜单顺序出错" + e.getMessage());
		}
	}

	public static String updateAcfunctioninfo(String userid, String applydata, Data data) {
		try {
			Acfunctioninfo func = JSON.parseObject(applydata, Acfunctioninfo.class);
			if (data.updateAcfunctioninfo(func) == 0)
				return Code.resultError("2222", "没有更改数据");
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "更改功能数据出错" + e.getMessage());
		}
	}

	public static String updateAcfunctioninfoordernum(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String funcid1 = obj.getString("FUNCTIONID1");
			String ordernum1 = obj.getString("ORDERNUM1");
			String funcid2 = obj.getString("FUNCTIONID2");
			String ordernum2 = obj.getString("ORDERNUM2");
			if (data.updateAcfunctioninfoordernum(funcid1, ordernum1) == 0)
				return Code.resultError("2222", "没有更改数据");
			if (data.updateAcfunctioninfoordernum(funcid2, ordernum2) == 0)
				return Code.resultError("2222", "没有更改数据");
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "更改功能顺序出错" + e.getMessage());
		}
	}

	public static String updateActoolcfginfo(String userid, String applydata, Data data) {
		try {
			Actoolcfg tool = JSON.parseObject(applydata, Actoolcfg.class);
			if (data.updateActoolcfginfo(tool) == 0)
				return Code.resultError("2222", "没有更改数据");
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "更改工具条数据出错" + e.getMessage());
		}
	}

	public static String updateActoolcfgordernum(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String buttonid = obj.getString("BUTTONID");
			String ordernum = obj.getString("ORDERNUM");
			if (data.updateActoolcfgordernum(buttonid, ordernum) == 0)
				return Code.resultError("2222", "没有更改数据");
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "更改工具条顺序出错" + e.getMessage());
		}
	}

	public static String incMenuclicknum(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String funcid = obj.getString("FUNCTIONID");
			String menutext = obj.getString("MENUTEXT");
			data.incMenuclicknum(userid, funcid, menutext);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加菜单点击数出错" + e.getMessage());
		}
	}

	public static String updateAcuserinfo(String userid, String applydata, Data data) {
		try {
			Acuserinfo user = JSON.parseObject(applydata, Acuserinfo.class);
			if (data.updateAcuserinfo(user) == 0)
				return Code.resultError("2222", "没有更改数据");
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "更改用户信息出错" + e.getMessage());
		}
	}

	public static String getNewbs(String applydata, Data data) {

		try {
			if (applydata.equals(""))
				return Code.resultError("1111", "接入点不能为空");

			return Code.resultSuccess(new NewBs(applydata, data).calculate());

		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "读取新工地代码出错" + e.getMessage());
		}

	}

	public static String getNewworker(String applydata, Data data) {

		try {
			JSONObject obj = JSON.parseObject(applydata);
			String apcd = Code.getFieldVal(obj, "APCD", "");
			if (apcd.equals(""))
				return Code.resultError("1111", "接入点不能为空");
			String bscd = Code.getFieldVal(obj, "BSCD", "");
			if (bscd.equals(""))
				return Code.resultError("1111", "工地代码不能为空");
			return Code.resultSuccess(new NewWorker(apcd, bscd, data).calculate());
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "读取新人员数据出错" + e.getMessage());
		}
	}

	public static String getWkds(String applydata, Data data) {

		try {
			JSONObject obj = JSON.parseObject(applydata);
			String apcd = Code.getFieldVal(obj, "APCD", "");
			if (apcd.equals(""))
				return Code.resultError("1111", "接入点不能为空");
			String bscd = Code.getFieldVal(obj, "BSCD", "");
			if (bscd.equals(""))
				return Code.resultError("1111", "工地代码不能为空");
			String startdate = Code.getFieldVal(obj, "STARTDATE", "");
			if (startdate.equals(""))
				return Code.resultError("1111", "开始日期不能为空");
			String enddate = Code.getFieldVal(obj, "ENDDATE", "");
			if (enddate.equals(""))
				return Code.resultError("1111", "结束日期不能为空");
			return Code.resultSuccess(new Attend(apcd, bscd, startdate, enddate, data).calculate());
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "读取考勤系统考勤数据出错" + e.getMessage());
		}
	}

	public static String insertAnnc(String userid, String applydata, Data data) {
		try {
			Annc annc = JSON.parseObject(applydata, Annc.class);
			annc.setCHUR(userid);
			if (annc.getANDATE().equals(""))
				return Code.resultError("1111", "公告日期不能为空");
			data.insertAnnc(annc);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "添加公告出错" + e.getMessage());
		}
	}

	public static String updateAnnc(String userid, String applydata, Data data) {
		try {
			Annc annc = JSON.parseObject(applydata, Annc.class);
			annc.setCHUR(userid);
			if (annc.getANID().equals(""))
				return Code.resultError("1111", "公告号不能为空");
			if (annc.getANDATE().equals(""))
				return Code.resultError("1111", "公告日期不为空");
			if (data.updateAnnc(annc) == 0)
				return Code.resultError("2222", "没有更改数据");
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "更改公告出错" + e.getMessage());
		}
	}

	public static String delAnnc(String userid, String applydata, Data data) {
		try {
			if (applydata.equals(""))
				return Code.resultError("1111", "公告号不能为空");
			data.delAnnc(applydata);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "删除公告出错" + e.getMessage());
		}
	}

	public static String getWorkinfo(String applydata, Data data) {
		try {
			if (applydata.equals(""))
				return Code.resultError("1111", "身份证不能为空");
			List<Map<String, Object>> list = data.qryWorkerinfo(applydata);
			if (list.isEmpty()) {
				return Code.resultError("1111", "没有该人员信息");
			} else {
				return Code.resultSuccess(list.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "获取人员信息出错" + e.getMessage());
		}
	}

	public static String getAnnc(Data data) {
		try {
			List<Map<String, Object>> list = data.qryAnnc();
			return Code.resultSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "获取公告数据出错" + e.getMessage());
		}
	}

	public static String getCheckon(String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String idcdno = Code.getFieldVal(obj, "IDCDNO", "");
			if (idcdno.equals(""))
				return Code.resultError("1111", "身份证不能为空");
			String startdate = Code.getFieldVal(obj, "STARTDATE", "");
			if (startdate.equals(""))
				return Code.resultError("1111", "开始日期不能为空");
			String enddate = Code.getFieldVal(obj, "ENDDATE", "");
			if (enddate.equals(""))
				return Code.resultError("1111", "结束日期不能为空");
			List<Map<String, Object>> list = data.qryCheckon(idcdno, startdate, enddate);
			return Code.resultSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "获取考勤数据出错" + e.getMessage());
		}
	}

	public static String getAcpyinfo(String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String month = Code.getFieldVal(obj, "MONTH", "");
			if (month.equals(""))
				return Code.resultError("1111", "发放月份不能为空");
			String idcdno = Code.getFieldVal(obj, "IDCDNO", "");
			if (idcdno.equals(""))
				return Code.resultError("1111", "身份证不能为空");

			Calendar firstday = Calendar.getInstance();
			firstday.clear();
			firstday.set(Integer.parseInt(month.substring(0, 4)), Integer.parseInt(month.substring(4, 6)) - 1, 1);
			Calendar lastday = (Calendar) firstday.clone();
			lastday.add(Calendar.MONTH, 1);
			List<Map<String, Object>> list = data.qryAcpyinfo(Code.dtft.format(firstday.getTime()),
					Code.dtft.format(lastday.getTime()), idcdno);
			return Code.resultSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "获取薪水信息出错" + e.getMessage());
		}
	}

	public static String importWkdsBybscdmonth(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String apcd = Code.getFieldVal(obj, "APCD", "");
			if (apcd.equals(""))
				return Code.resultError("1111", "接入点不能为空");
			String bscd = Code.getFieldVal(obj, "BSCD", "");
			if (bscd.equals(""))
				return Code.resultError("1111", "工地代码不能为空");
			String startdate = Code.getFieldVal(obj, "STARTDATE", "");
			if (startdate.equals(""))
				return Code.resultError("1111", "开始日期不能为空");
			String enddate = Code.getFieldVal(obj, "ENDDATE", "");
			if (enddate.equals(""))
				return Code.resultError("1111", "结束日期不能为空");
			String month = Code.getFieldVal(obj, "MONTH", "");
			if (month.equals(""))
				return Code.resultError("1111", "考勤月份不能为空");
			return importWkdsBybscdmonth(userid, apcd, bscd, startdate, enddate, month, data);
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "导入考勤出错" + e.getMessage());
		}
	}

	public static String importWkdsBybscdmonth(String userid, String apcd, String bscd, String startdate,
			String enddate, String month, Data data) {
		try {
			List<Map<String, Object>> lwkds = (List<Map<String, Object>>) new Attend(apcd, bscd, startdate, enddate,
					data).calculate();
			data.delWkds(apcd, bscd, month);
			Wkds wkds = new Wkds();
			wkds.setAPCD(apcd);
			wkds.setBSCD(bscd);
			wkds.setMONTH(month);
			wkds.setUR(userid);
			for (int i = 0; i < lwkds.size(); i++) {
				Map<String, Object> wk = lwkds.get(i);
				wkds.setIDCDNO(wk.get("IDCDNO").toString());
				wkds.setNAME(Code.getFieldVal(wk, "NAME", ""));
				wkds.setPOST(Code.getFieldVal(wk, "POST", ""));
				wkds.setWKDS(Code.getFieldVal(wk, "WKDS", ""));
				wkds.setLCCD(Code.getFieldVal(wk, "LCCD", ""));
				wkds.setINONEMON(Code.getFieldVal(wk, "INONEMON", ""));
				data.insertWkds(wkds);
			}
			doBscoll(month, apcd, bscd, data);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "导入考勤数据出错" + e.getMessage());
		}
	}

	public static String uploadwkds(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String apcd = Code.getFieldVal(obj, "APCD", "");
			if (apcd.equals(""))
				return Code.resultError("1111", "接入点不能为空");
			String bscd = Code.getFieldVal(obj, "BSCD", "");
			if (bscd.equals(""))
				return Code.resultError("1111", "工地代码不能为空");
			String startdate = Code.getFieldVal(obj, "STARTDATE", "");
			if (startdate.equals(""))
				return Code.resultError("1111", "开始日期不能为空");
			String enddate = Code.getFieldVal(obj, "ENDDATE", "");
			if (enddate.equals(""))
				return Code.resultError("1111", "结束日期不能为空");
			String month = Code.getFieldVal(obj, "MONTH", "");
			if (month.equals(""))
				return Code.resultError("1111", "考勤月份不能为空");
			List<Map<String, Object>> lwkds = (List<Map<String, Object>>) new Attend(apcd, bscd, startdate, enddate,
					data).calculate();
			String recorddate = startdate.substring(0, 4) + "-" + startdate.substring(4, 6) + "-"
					+ startdate.substring(6, 8);
			List<Map<String, Object>> lbs = data.qryBsinfo(apcd, bscd);
			String ajcode = "";
			if (lbs.size() > 0) {
				ajcode = Code.getFieldVal(lbs.get(0), "AJCODE", "");
			}
			for (int i = 0; i < lwkds.size(); i++) {
				Map<String, Object> wk = lwkds.get(i);
				wk.put("BSCD", bscd);
				wk.put("DATE", recorddate);
				wk.put("AJCODE", ajcode);
				wk.put("APCD", apcd);
				wk.put("BSCD", bscd);
				uploadattend(wk, data);
			}
			return Code.resultSuccess();

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "导入考勤出错" + e.getMessage());
		}
	}

	public static String uploadhumanbybscd(String userid, String applydata, Data data) {

		JSONObject obj = JSON.parseObject(applydata);
		String apcd = Code.getFieldVal(obj, "apcd", "");
		String bscd = Code.getFieldVal(obj, "bscd", "");
		List<Map<String, Object>> lwk = data.qryWker(apcd, bscd);

		for (Map<String, Object> wk : lwk) {
			uploadhuman(apcd, bscd, Code.getFieldVal(wk, "SZ_EMPLOY_ID", ""), data);
		}

		return Code.resultSuccess();

	}

	public static String uploadsalarybysqnb(String userid, String applydata, Data data) {
		uploadsalary(applydata, data);
		return Code.resultSuccess();
	}

	public static String importBankback(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String sqnb = Code.getFieldVal(obj, "SQNB", "");
			if (sqnb.equals(""))
				return Code.resultError("1111", "发放批次不能为空");
			String lccd = Code.getFieldVal(obj, "LCCD", "");
			if (lccd.equals(""))
				return Code.resultError("1111", "劳务公司不能为空");
			JSONArray array = obj.getJSONArray("ITEMS");

			// 读取该批次的数据
			List<Map<String, Object>> list = data.qryAcpy(sqnb, lccd);
			String month = "";
			String apcd = "";
			String bscd = "";
			if (list.size() > 0) {
				Map<String, Object> m = list.get(0);
				month = Code.getFieldVal(m, "MONTH", "");
				apcd = Code.getFieldVal(m, "APCD", "");
				bscd = Code.getFieldVal(m, "BSCD", "");
				// 如果银行确认
				if (!Code.getFieldVal(m, "INBKOT", "N").equals("Y"))
					return Code.resultError("1111", "数据还未导出不能确认");
			} else {
				return Code.resultError("1111", "没有本批次的审核数据");
			}

			// 先把所有item都设为确认
			data.updateAcpybkcfm(sqnb, lccd, "Y", userid);
			data.updateAcpyitembkcfm(sqnb, lccd, "Y", "", "");
			// 没有更新的账号
			HashSet<String> set = new HashSet<String>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject bk = array.getJSONObject(i);

				if (data.updateAcpyitembkcfm(sqnb, lccd, Code.getFieldVal(bk, "IDCDNO", ""),
						Code.getFieldVal(bk, "BKAN", ""), Code.getFieldVal(bk, "STATE", ""),
						Code.getFieldVal(bk, "ERRCODE", ""), Code.getFieldVal(bk, "ERRMSG", "")) == 0)
					set.add(Code.getFieldVal(bk, "BKAN", ""));

			}

			if (list.size() > 0) {
				doBscoll(month, apcd, bscd, sqnb, lccd, data);
			}
			doBscoll(month, apcd, bscd, data);

			if (set.size() == 0)
				return Code.resultSuccess();
			else
				return Code.resultError("1002", JSON.toJSONString(set));

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "导入银行发放数据出错" + e.getMessage());
		}
	}

	public static String getBsdetail(String userid, String applydata, Data data) {
		try {
			JSONObject obj = JSON.parseObject(applydata);
			String month = Code.getFieldVal(obj, "MONTH", "");
			if (month.equals(""))
				return Code.resultError("1111", "月份不能为空");
			String apcd = Code.getFieldVal(obj, "APCD", "");
			if (apcd.equals(""))
				return Code.resultError("1111", "接入点不能为空");
			String bscd = Code.getFieldVal(obj, "BSCD", "");
			if (bscd.equals(""))
				return Code.resultError("1111", "工地不能为空");
			HashMap<String, Object> mr = new HashMap<String, Object>();

			mr.put("BSINFO", data.qryBsinfobydetail(apcd, bscd));
			mr.put("NOFFWORKER", new NoffWorker(month, apcd, bscd, data).calculate());

			Calendar clbl = Calendar.getInstance();
			clbl.clear();
			clbl.set(Integer.parseInt(month.substring(0, 4)), Integer.parseInt(month.substring(4, 6)) - 1, 1);
			clbl.add(Calendar.MONTH, 1);
			String blmonth = Code.dtft.format(clbl.getTime()).substring(0, 6);
			mr.put("BLINFO", data.qrySpanbl(blmonth, apcd, bscd));
			mr.put("BSCOLLINFO", data.qryBscollinfobyapcdbscdmonth(month, apcd, bscd));
			return Code.resultSuccess(mr);
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "获取详细信息有误" + e.getMessage());
		}
	}

	private static int getAttendbranchid(String apcd, String bscd, Data data) {
		try {
			List<Map<String, Object>> bs = data.qryAttendbsinfo(apcd, bscd);

			if (bs.size() > 0) {
				return Integer.parseInt(Code.getFieldVal(bs.get(0), "ng_id", "0"));
			} else {
				return -1;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	private static int getAttendWkerid(String apcd, String wkid, Data data) {
		try {
			List<Map<String, Object>> wk = data.qryAttendUser(apcd, wkid);

			if (wk.size() > 0) {
				return Integer.parseInt(Code.getFieldVal(wk.get(0), "ng_id", "0"));
			} else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	private static int getAttendWkeridbyidcdno(String apcd, String idcdno, Data data) {
		try {
			List<Map<String, Object>> wk = data.qryAttendUserbyidcdno(apcd, idcdno);

			if (wk.size() > 0) {
				return Integer.parseInt(Code.getFieldVal(wk.get(0), "ng_id", "0"));
			} else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static String importAttendData(String applydata, Data data) {
		try {
			JSONObject jo = JSON.parseObject(applydata);
			// ret=0表示有效数据
			if (jo.getString("ret").equals("0")) {
				// 如果有数据
				if (jo.getJSONObject("data").getInteger("count") > 0) {
					JSONArray ja = jo.getJSONObject("data").getJSONArray("items");
					String apcd = jo.getString("apcd");
					for (Object obj : ja) {
						JSONObject ji = (JSONObject) obj;
						Statcard sc = new Statcard();
						sc.setApcd(apcd);
						sc.setApid(ji.getInteger("id"));
						sc.setNg_branch_id(getAttendbranchid("HW", Code.getFieldVal(ji, "deptnumber", ""), data));
						sc.setNg_dev_id(0);
						sc.setNg_user_id(getAttendWkerid("HW", Code.getFieldVal(ji, "pin", ""), data));
						sc.setSt_kind(0);
						sc.setSz_dev_name(Code.getFieldVal(ji, "alias", ""));
						sc.setSz_employ_id(Code.getFieldVal(ji, "pin", ""));
						sc.setTs_card(Code.getFieldVal(ji, "checktime", ""));
						data.delAttendData(apcd, sc.getApid());
						data.insertAttendData(apcd, sc);

						// 写设备人脸识别
						List<Map<String, Object>> l = data.qryAttenddevfeature(sc.getNg_user_id());
						if (l.size() == 0)
							data.insertAttenddevfeature(sc.getNg_user_id());
					}
				}
			}
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "导入考勤数据有误" + e.getMessage());
		}
	}

	public static String insertOplist(String applydata, Data data) {
		try {
			JSONObject jo = JSON.parseObject(applydata);
			String apcd = Code.getFieldVal(jo, "apcd", "");
			String opid = data.getNextval("OPID");
			String type = Code.getFieldVal(jo, "type", "");
			String content = Code.getFieldVal(jo, "content", "");
			data.insertOplist(apcd, opid, type, content);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "添加操作列表有误" + e.getMessage());
		}
	}

	public static String delOplist(String applydata, Data data) {
		try {
			data.delOplist(applydata);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "删除操作列表有误" + e.getMessage());
		}
	}

	public static String wkerleave(String userid, String applydata, Data data) {
		try {
			JSONObject jo = JSON.parseObject(applydata);
			String apcd = Code.getFieldVal(jo, "APCD", "");
			if (apcd.equals(""))
				return Code.resultError("1111", "接入点不能为空");
			String bscd = Code.getFieldVal(jo, "BSCD", "");
			if (bscd.equals(""))
				return Code.resultError("1111", "工地代码不能为空");
			String wkid = Code.getFieldVal(jo, "WKID", "");
			if (wkid.equals(""))
				return Code.resultError("1111", "工号不能为空");
			String disdate = Code.getFieldVal(jo, "DISDATE", "");
			if (disdate.equals(""))
				return Code.resultError("1111", "离职日期不能为空");
			if (data.wkerleave(apcd, bscd, wkid, disdate) == 0)
				return Code.resultError("2222", "没有更改数据");

			if (apcd.equals("ZK")) {

				JSONObject wk = new JSONObject();
				wk.put("pin", wkid);
				wk.put("leavedate", disdate);
				wk.put("leavetype", "0");
				wk.put("reason", "");

				String opid = data.getNextval("OPID");
				data.insertOplist(apcd, opid, "wkerleave", wk.toJSONString());

			}

			// 上传数据到平台
			uploadhuman(apcd, bscd, wkid, data);

			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "离职有误" + e.getMessage());
		}
	}

	public static String updateOplistresult(String applydata, Data data) {
		try {
			JSONObject jo = JSON.parseObject(applydata);
			String opid = Code.getFieldVal(jo, "opid", "");
			String result = Code.getFieldVal(jo, "result", "");
			data.updateOplistresult(opid, result);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", "更新操作列表返回值有误" + e.getMessage());
		}
	}

	private static void uploadproject(String apcd, String bscd, Data data) {
		List<Map<String, Object>> list = data.qryBsinfo(apcd, bscd);
		if (list.size() > 0) {

			Map<String, Object> bs = list.get(0);
			JSONObject jo = new JSONObject();
			jo.put("project_id", Code.getFieldVal(bs, "BSCD", ""));
			jo.put("project_name", Code.getFieldVal(bs, "BSDS", ""));
			jo.put("project_num", Code.getFieldVal(bs, "AJCODE", ""));
			jo.put("tender_num", Code.getFieldVal(bs, "TENDERID", ""));
			jo.put("project_type", Code.getFieldVal(bs, "PROJECTTYPE", ""));
			jo.put("prin_name", Code.getFieldVal(bs, "PRINNAME", ""));
			jo.put("prin_mobile", Code.getFieldVal(bs, "PRINTEL", ""));
			if (Code.getFieldVal(bs, "INDT", "Y").equals("Y"))
				jo.put("project_status", "1");
			if (Code.getFieldVal(bs, "INDT", "N").equals("N"))
				jo.put("project_status", "2");
			jo.put("project_address", Code.getFieldVal(bs, "BSADDRESS", ""));
			jo.put("license_key", Code.getFieldVal(bs, "LICENSEKEY", ""));
			jo.put("begin_date", Code.getFieldVal(bs, "STARTDATE", ""));
			jo.put("end_date", Code.getFieldVal(bs, "ENDDATE", ""));
			jo.put("company_name", Code.getFieldVal(bs, "BCNM", ""));
			jo.put("project_cost", Code.getFieldVal(bs, "PROJECTCOST", ""));
			jo.put("salary_cost", Code.getFieldVal(bs, "SALARYCOST", ""));
			jo.put("bank", data.getSycdds("BKCD", Code.getFieldVal(bs, "BKCD", "")));
			String opid = data.getNextval("OPID");
			data.insertOplist(apcd, bscd, opid, "newuploadproject", "[" + jo.toJSONString() + "]");
		}
	}

	private static void uploadhuman(String apcd, String bscd, String wkerid, Data data) {

		List<Map<String, Object>> lbs = data.qryBsinfo(apcd, bscd);
		String projectnum = "";
		if (lbs.size() > 0)
			projectnum = Code.getFieldVal(lbs.get(0), "AJCODE", "");

		List<Map<String, Object>> lwkbs = data.qryWkerbsbyemploy(apcd, bscd, wkerid);

		if (lwkbs.size() > 0) {
			Map<String, Object> wkbs = lwkbs.get(0);
			String idcdno = Code.getFieldVal(wkbs, "IDCDNO", "");

			List<Map<String, Object>> lwk = data.qryWker(idcdno);

			if (lwk.size() > 0) {
				Map<String, Object> wk = lwk.get(0);
				JSONObject jo = new JSONObject();
				jo.put("project_id", bscd);
				jo.put("worker_id", wkerid);
				jo.put("project_num", projectnum);
				jo.put("worker_name", Code.getFieldVal(wk, "NAME", ""));
				jo.put("cooperator_unit_name", data.getSycdds("LCCD", Code.getFieldVal(wkbs, "LCCD", "")));
				jo.put("group_name", data.getSycdds("BZCD", Code.getFieldVal(wkbs, "BZ", "")));
				jo.put("profession_id", Code.getFieldVal(wkbs, "WKKDSV", ""));
				if (Code.getFieldVal(wk, "INSEX", "").equals("男"))
					jo.put("gender_code", "1");
				if (Code.getFieldVal(wk, "INSEX", "").equals("女"))
					jo.put("gender_code", "2");
				jo.put("birthday", Code.getFieldVal(wk, "BRDT", ""));
				jo.put("census_register", Code.getFieldVal(wk, "REGADDRESS", ""));
				jo.put("id_card", idcdno);
				jo.put("start_date", Code.getFieldVal(wk, "IDSTARTDATE", ""));
				jo.put("end_date", Code.getFieldVal(wk, "IDENDDATE", ""));
				jo.put("mobile", Code.getFieldVal(wk, "TEL", ""));
				jo.put("timecard", Code.getFieldVal(wkbs, "TIMECARD", ""));
				jo.put("entry_time", Code.getFieldVal(wkbs, "EMPDATE", ""));
				jo.put("dimission_time", Code.getFieldVal(wkbs, "DISDATE", ""));
				jo.put("education", Code.getFieldVal(wk, "EDUCATION", ""));
				String indy = Code.getFieldVal(wk, "INDY", "");
				if (indy.equals("Y"))
					indy = "2";// 党员
				if (indy.equals("N"))
					indy = "3";// 群众
				if (indy.equals("T"))
					indy = "1";// 团员
				jo.put("political_status", indy);
				jo.put("marital_status", Code.getFieldVal(wk, "INMARRY", ""));
				String inmin = Code.getFieldVal(wk, "INMIN", "");
				if (inmin.equals("Y"))
					inmin = "1";
				if (inmin.equals("N"))
					inmin = "0";
				jo.put("is_minority", inmin);
				jo.put("nation", Code.getFieldVal(wk, "ETHNIC", ""));
				jo.put("address", Code.getFieldVal(wk, "HOMEADD", ""));
				jo.put("worker_photo", "");
				jo.put("status", Code.getFieldVal(wkbs, "INEMP", ""));
				String iscert = Code.getFieldVal(wkbs, "ISCERT", "");
				if (iscert.equals("Y"))
					iscert = "1";
				if (iscert.equals("N"))
					iscert = "2";
				jo.put("is_certification", iscert);

				List<Map<String, Object>> ljn = data.qryZyjn(idcdno);

				if (ljn.size() > 0) {
					jo.put("qualification_name", Code.getFieldVal(ljn.get(0), "JNNAME", ""));
					jo.put("qualification_number", Code.getFieldVal(ljn.get(0), "JNID", ""));
				} else {
					jo.put("qualification_name", "");
					jo.put("qualification_number", "");
				}

				jo.put("id_card_pic", "");
				jo.put("household_type", Code.getFieldVal(wk, "HOUSEHOLDTYPE", ""));

				String isgroupmonitor = Code.getFieldVal(wkbs, "ISGROUPER", "");
				if (isgroupmonitor.equals("Y"))
					isgroupmonitor = "1";
				if (isgroupmonitor.equals("N"))
					isgroupmonitor = "2";
				jo.put("is_group_monitor", isgroupmonitor);

				String ismigrantworker = Code.getFieldVal(wkbs, "ISMIGRANT", "");
				if (ismigrantworker.equals("Y"))
					ismigrantworker = "1";
				if (ismigrantworker.equals("N"))
					ismigrantworker = "2";
				jo.put("is_migrant_worker", ismigrantworker);

				String opid = data.getNextval("OPID");
				data.insertOplist(apcd, bscd, opid, "newuploadhuman", "[" + jo.toJSONString() + "]");
			}
		}
	}

	private static void deletehuman(String apcd, String bscd, String idcdno, String wkid, Data data) {

		List<Map<String, Object>> lbs = data.qryBsinfo(apcd, bscd);

		if (lbs.size() > 0) {

			String projectnum = Code.getFieldVal(lbs.get(0), "AJCODE", "");
			String workername = "";
			List<Map<String, Object>> lwk = data.qryWker(idcdno);
			if (lwk.size() > 0) {
				workername = Code.getFieldVal(lwk.get(0), "NAME", "");
			}

			JSONObject jo = new JSONObject();
			jo.put("project_id", bscd);
			jo.put("worker_id", wkid);
			jo.put("project_num", projectnum);
			jo.put("worker_name", workername);

			String opid = data.getNextval("OPID");
			data.insertOplist(apcd, bscd, opid, "newdeletehuman", "[" + jo.toJSONString() + "]");
		}
	}

	private static void uploadattend(Map<String, Object> m, Data data) {
		JSONObject jo = new JSONObject();
		jo.put("project_id", Code.getFieldVal(m, "BSCD", ""));
		jo.put("record_date", Code.getFieldVal(m, "DATE", ""));
		jo.put("worker_id", Code.getFieldVal(m, "WKID", ""));
		jo.put("project_num", Code.getFieldVal(m, "AJCODE", ""));
		String temp = Code.getFieldVal(m, "STARTTIME", "");
		if (!temp.equals("")) {
			temp = temp.substring(0, 4) + "-" + temp.substring(4, 6) + "-" + temp.substring(6, 8) + " "
					+ temp.substring(8, 10) + ":" + temp.substring(10, 12) + ":" + temp.substring(12, 14);
		}
		jo.put("on_time", temp);
		temp = Code.getFieldVal(m, "ENDTIME", "");
		if (!temp.equals("")) {
			temp = temp.substring(0, 4) + "-" + temp.substring(4, 6) + "-" + temp.substring(6, 8) + " "
					+ temp.substring(8, 10) + ":" + temp.substring(10, 12) + ":" + temp.substring(12, 14);
		}
		jo.put("off_time", temp);
		float f = (Float) m.get("WKTIME");
		jo.put("time_count", String.valueOf(f));

		String opid = data.getNextval("OPID");
		data.insertOplist(Code.getFieldVal(m, "APCD", ""), Code.getFieldVal(m, "BSCD", ""), opid, "newuploadattend",
				"[" + jo.toJSONString() + "]");
	}

	private static void uploadexprience(String apcd, String bscd, String idcdno, Data data) {

		List<Map<String, Object>> ljl = data.qryCyjl(apcd, bscd, idcdno);

		List<Map<String, Object>> lbs = data.qryBsinfo(apcd, bscd);

		if (lbs.size() > 0) {

			String projectnum = Code.getFieldVal(lbs.get(0), "AJCODE", "");
			String projectname = Code.getFieldVal(lbs.get(0), "BSDS", "");
			String licensekey = Code.getFieldVal(lbs.get(0), "LICENSEKEY", "");

			String workername = "";
			List<Map<String, Object>> lwk = data.qryWker(idcdno);
			if (lwk.size() > 0) {
				workername = Code.getFieldVal(lwk.get(0), "NAME", "");
			}

			for (Map<String, Object> jl : ljl) {
				JSONObject jo = new JSONObject();
				jo.put("project_num", projectnum);
				jo.put("project_id", bscd);
				jo.put("project_name", projectname);
				jo.put("license_key", licensekey);
				jo.put("worker_exprience_id", Code.getFieldVal(jl, "JLID", ""));
				jo.put("id_card", idcdno);
				jo.put("worker_name", workername);
				jo.put("begin_time", Code.getFieldVal(jl, "STARTDATE", ""));
				jo.put("end_time", Code.getFieldVal(jl, "ENDDATE", ""));
				jo.put("profession_id", Code.getFieldVal(jl, "WKKD", ""));

				String opid = data.getNextval("OPID");
				data.insertOplist(apcd, bscd, opid, "newuploadexprience", "[" + jo.toJSONString() + "]");
			}
		}

	}

	private static void deleteexprience(String apcd, String bscd, String idcdno, Data data) {

		List<Map<String, Object>> ljl = data.qryCyjl(apcd, bscd, idcdno);

		List<Map<String, Object>> lbs = data.qryBsinfo(apcd, bscd);

		if (lbs.size() > 0) {

			String projectnum = Code.getFieldVal(lbs.get(0), "AJCODE", "");
			String projectname = Code.getFieldVal(lbs.get(0), "BSDS", "");
			String licensekey = Code.getFieldVal(lbs.get(0), "LICENSEKEY", "");

			String workername = "";
			List<Map<String, Object>> lwk = data.qryWker(idcdno);
			if (lwk.size() > 0) {
				workername = Code.getFieldVal(lwk.get(0), "NAME", "");
			}

			for (Map<String, Object> jl : ljl) {

				JSONObject jo = new JSONObject();
				jo.put("project_num", projectnum);
				jo.put("project_id", bscd);
				jo.put("project_name", projectname);
				jo.put("license_key", licensekey);
				jo.put("worker_exprience_id", Code.getFieldVal(jl, "JLID", ""));
				jo.put("id_card", idcdno);
				jo.put("worker_name", workername);
				jo.put("profession_id", Code.getFieldVal(jl, "WKKD", ""));

				String opid = data.getNextval("OPID");
				data.insertOplist(apcd, bscd, opid, "newdeleteexprience", "[" + jo.toJSONString() + "]");
			}
		}

	}

	private static void uploadtrain(String apcd, String bscd, String idcdno, Data data) {

		List<Map<String, Object>> lpx = data.qryPxxx(apcd, bscd, idcdno);
		List<Map<String, Object>> lbs = data.qryBsinfo(apcd, bscd);

		if (lbs.size() > 0) {
			String projectnum = Code.getFieldVal(lbs.get(0), "AJCODE", "");
			String projectname = Code.getFieldVal(lbs.get(0), "BSDS", "");

			String workername = "";
			List<Map<String, Object>> lwk = data.qryWker(idcdno);
			if (lwk.size() > 0) {
				workername = Code.getFieldVal(lwk.get(0), "NAME", "");
			}

			for (Map<String, Object> px : lpx) {
				JSONObject jo = new JSONObject();
				jo.put("project_num", projectnum);
				jo.put("project_id", bscd);
				jo.put("project_name", projectname);
				jo.put("worker_train_id", Code.getFieldVal(px, "PXID", ""));
				jo.put("id_card", idcdno);
				jo.put("worker_name", workername);
				jo.put("begin_time", Code.getFieldVal(px, "STARTDATE", ""));
				jo.put("end_time", Code.getFieldVal(px, "ENDDATE", ""));
				jo.put("train_name", Code.getFieldVal(px, "PXTITLE", ""));
				jo.put("remark", Code.getFieldVal(px, "REMARK", ""));

				String opid = data.getNextval("OPID");
				data.insertOplist(apcd, bscd, opid, "newuploadtrain", "[" + jo.toJSONString() + "]");

			}
		}
	}

	private static void deletetrain(String apcd, String bscd, String idcdno, Data data) {

		List<Map<String, Object>> lpx = data.qryPxxx(apcd, bscd, idcdno);
		List<Map<String, Object>> lbs = data.qryBsinfo(apcd, bscd);

		if (lbs.size() > 0) {
			String projectnum = Code.getFieldVal(lbs.get(0), "AJCODE", "");
			String projectname = Code.getFieldVal(lbs.get(0), "BSDS", "");

			String workername = "";
			List<Map<String, Object>> lwk = data.qryWker(idcdno);
			if (lwk.size() > 0) {
				workername = Code.getFieldVal(lwk.get(0), "NAME", "");
			}

			for (Map<String, Object> px : lpx) {
				JSONObject jo = new JSONObject();
				jo.put("project_num", projectnum);
				jo.put("project_id", bscd);
				jo.put("project_name", projectname);
				jo.put("worker_train_id", Code.getFieldVal(px, "PXID", ""));
				jo.put("id_card", idcdno);
				jo.put("worker_name", workername);
				jo.put("train_name", Code.getFieldVal(px, "PXTITLE", ""));

				String opid = data.getNextval("OPID");
				data.insertOplist(apcd, bscd, opid, "newdeletetrain", "[" + jo.toJSONString() + "]");
			}
		}

	}

	public static HashMap<String, Map<String, String>> qryUploadsalarysum(String sqnb, Data data) {
		HashMap<String, Map<String, String>> sumdata = new HashMap<String, Map<String, String>>();

		List<Map<String, Object>> lattend = data.qryUploadtotalattend(sqnb);

		if (lattend.size() > 0) {
			for (Map<String, Object> attend : lattend) {
				Map<String, String> item = new HashMap<String, String>();
				item.put("IDCDNO", Code.getFieldVal(attend, "IDCDNO", ""));
				item.put("ATTEND", Code.getFieldVal(attend, "TOTAL", "0"));

				sumdata.put(Code.getFieldVal(attend, "IDCDNO", ""), item);
			}
		}

		List<Map<String, Object>> lsalary = data.qryUploadtotalsalary(sqnb);
		if (lsalary.size() > 0) {
			for (Map<String, Object> salary : lsalary) {
				Map<String, String> item = sumdata.get(Code.getFieldVal(salary, "IDCDNO", ""));
				if (item == null) {
					item = new HashMap<String, String>();
					item.put("IDCDNO", Code.getFieldVal(salary, "IDCDNO", ""));
					item.put("ATTEND", "0");
				}
				item.put("SALARY", Code.getFieldVal(salary, "TOTAL", "0"));

				sumdata.put(Code.getFieldVal(salary, "IDCDNO", ""), item);
			}
		}

		List<Map<String, Object>> lwkds = data.qryUploadwkds(sqnb);
		if (lwkds.size() > 0) {
			for (Map<String, Object> wkds : lwkds) {
				Map<String, String> item = sumdata.get(Code.getFieldVal(wkds, "IDCDNO", ""));
				if (item == null) {
					item = new HashMap<String, String>();
					item.put("IDCDNO", Code.getFieldVal(wkds, "IDCDNO", ""));
					item.put("ATTEND", "0");
					item.put("SALARY", "0");
				}
				item.put("WKDS", Code.getFieldVal(wkds, "WKDS", ""));

				sumdata.put(Code.getFieldVal(wkds, "IDCDNO", ""), item);
			}
		}

		return sumdata;
	}

	private static void uploadsalary(String sqnb, Data data) {

		List<Map<String, Object>> lacpy = data.qryAcpyitem(sqnb);

		HashMap<String, Map<String, String>> sumdata = qryUploadsalarysum(sqnb, data);

		if (lacpy.size() > 0) {

			String apcd = Code.getFieldVal(lacpy.get(0), "APCD", "");
			String bscd = Code.getFieldVal(lacpy.get(0), "BSCD", "");
			List<Map<String, Object>> lbs = data.qryBsinfo(apcd, bscd);

			if (lbs.size() > 0) {
				String projectnum = Code.getFieldVal(lbs.get(0), "AJCODE", "");
				String projectname = Code.getFieldVal(lbs.get(0), "BSDS", "");

				for (Map<String, Object> acpy : lacpy) {

					JSONObject jo = new JSONObject();
					jo.put("project_num", projectnum);
					jo.put("project_id", bscd);
					jo.put("project_name", projectname);

					String month = Code.getFieldVal(acpy, "MONTH", "");
					String idcdno = Code.getFieldVal(acpy, "IDCDNO", "");
					jo.put("worker_salary_id", month + idcdno);
					jo.put("data_time",
							Code.getFillStr(Code.getFieldVal(acpy, "BKCFMDT", ""), "L", 10, "0").substring(0, 10));
					jo.put("month", month.substring(0, 4) + "-" + month.substring(4, 6));

					List<Map<String, Object>> lwkbs = data.qryWkerbs(apcd, bscd, idcdno);
					String wkid = "";
					if (lwkbs.size() > 0)
						wkid = Code.getFieldVal(lwkbs.get(0), "SZ_EMPLOY_ID", "");
					if (wkid.equals("")) {
						wkid = "00000";
					}
					jo.put("worker_id", wkid);
					jo.put("worker_name", Code.getFieldVal(acpy, "NAME", ""));
					jo.put("id_card", idcdno);
					jo.put("bank_no", Code.getFieldVal(acpy, "BKAN", ""));

					// List<Map<String, Object>> lwk = data.qryWker(idcdno);
					// String tel = "";
					// if (lwk.size() > 0)
					// tel = Code.getFieldVal(lwk.get(0), "TEL", "");
					String tel = "123456789";
					jo.put("mobile", tel);

					jo.put("send_amount", Code.getFieldVal(acpy, "ACPY", ""));
					String paidamount = "";
					if (Code.getFieldVal(acpy, "INBKCFM", "N").equals("Y"))
						paidamount = Code.getFieldVal(acpy, "ACPY", "");
					else
						paidamount = "0";
					jo.put("paid_amount", paidamount);
					String gdstatus = "1";
					if (!Code.getFieldVal(acpy, "INBKCFM", "N").equals("Y"))
						gdstatus = "2";
					jo.put("goods_status", gdstatus);

					Map<String, String> item = sumdata.get(idcdno);
					String attend = "";
					String salary = "";
					String wkds = "";
					if (item != null) {
						attend = item.get("ATTEND");
						attend = String.valueOf((int) Math.ceil(Float.parseFloat(attend)));
						salary = item.get("SALARY");
						wkds = item.get("WKDS");
						wkds = String.valueOf((int) Math.ceil(Float.parseFloat(wkds)));
					}
					jo.put("attendance_month_num", wkds);
					jo.put("attendance_total_num", attend);
					if (salary == null) {
						salary = "0";
					}
					jo.put("total_salary", salary);

					String opid = data.getNextval("OPID");
					data.insertOplist(apcd, bscd, opid, "newuploadsalary", "[" + jo.toJSONString() + "]");
				}
			}
		}
	}

	private static void uploadbank(JSONObject bkrec, Data data) {

		String apcd = Code.getFieldVal(bkrec, "APCD", "");
		String bscd = Code.getFieldVal(bkrec, "BSCD", "");
		List<Map<String, Object>> lbs = data.qryBsinfo(apcd, bscd);

		if (lbs.size() > 0) {
			String projectnum = Code.getFieldVal(lbs.get(0), "AJCODE", "");
			String projectname = Code.getFieldVal(lbs.get(0), "BSDS", "");

			JSONObject jo = new JSONObject();
			jo.put("project_num", projectnum);
			jo.put("project_id", bscd);
			jo.put("project_name", projectname);
			jo.put("bank_transation_id", Code.getFieldVal(bkrec, "RECORDID", ""));
			jo.put("bank_account", Code.getFieldVal(bkrec, "ACCOUNT", ""));
			jo.put("date_time", Code.getFieldVal(bkrec, "RECORDTIME", ""));
			jo.put("account_store", Code.getFieldVal(bkrec, "STORENUM", ""));
			jo.put("account_pay", Code.getFieldVal(bkrec, "PAYNUM", ""));

			String opid = data.getNextval("OPID");
			data.insertOplist(apcd, bscd, opid, "newuploadbank", "[" + jo.toJSONString() + "]");
		}
	}

	public static String bankrecord(String userid, String applydata, Data data) {

		try {
			JSONArray ja = JSON.parseArray(applydata);

			for (int i = 0; i < ja.size(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				data.delBankrecord(Code.getFieldVal(jo, "RECORDID", ""));
				data.insertBankrecord(userid, jo);
				uploadbank(jo, data);
			}
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Code.resultError("1111", "导入银行流水有误" + e.getMessage());
		}

	}

	public static String getYftoken(Data data) {

		String appid = "CD6B613ABAE54D81971955D00B04E370";
		JSONObject jo = new JSONObject();

		List<Map<String, Object>> l = data.qryYftoken(appid);
		if (l.size() > 0) {

			jo.put("appid", appid);
			jo.put("token", Code.getFieldVal(l.get(0), "TOKEN", ""));

		}

		return jo.toJSONString();
	}

	public static void refreshYftoken(Data data) {
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody body = RequestBody.create(mediaType,
				"appId=CD6B613ABAE54D81971955D00B04E370" + "&appKey=1EF34EE105714763823789ED391717FC"
						+ "&timestamp=1541650703000&sign=fb54dab31972fef4bd7228b515ff5874");
		Request request = new Request.Builder()
				.url("http://gs-api.uface.uni-ubi.com/v1/CD6B613ABAE54D81971955D00B04E370/auth").post(body)
				.addHeader("Content-Type", "application/x-www-form-urlencoded").addHeader("Cache-Control", "no-cache")
				.addHeader("Postman-Token", "9e99036c-287e-425e-b6b1-b1630258ab7b").build();

		try {
			Response response = client.newCall(request).execute();
			String result = response.body().string();
			JSONObject jo = JSONObject.parseObject(result);
			if (jo.getBoolean("success")) {
				String appid = "CD6B613ABAE54D81971955D00B04E370";
				String token = jo.getString("data");

				data.delYftoken(appid);
				data.insertYftoken(appid, token);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String insertyfdevice(String userid, String applydata, Data data) {

		try {

			Yfdevice dev = JSONObject.parseObject(applydata, Yfdevice.class);
			data.insertYfdevice(dev);
			return Code.resultSuccess();

		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", e.getMessage());
		}

	}

	public static String updateyfdevice(String userid, String applydata, Data data) {
		try {

			Yfdevice dev = JSONObject.parseObject(applydata, Yfdevice.class);
			data.updateYfdevice(dev);
			return Code.resultSuccess();

		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", e.getMessage());
		}
	}

	public static String delyfdevice(String userid, String applydata, Data data) {
		try {
			data.delYfdevice(applydata);
			return Code.resultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return Code.resultError("1111", e.getMessage());
		}
	}

}
