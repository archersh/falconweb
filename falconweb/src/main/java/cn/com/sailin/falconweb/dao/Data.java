package cn.com.sailin.falconweb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;

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
import cn.com.sailin.falconweb.model.Cyjl;
import cn.com.sailin.falconweb.model.Pxxx;
import cn.com.sailin.falconweb.model.Spanbl;
import cn.com.sailin.falconweb.model.Statcard;
import cn.com.sailin.falconweb.model.Sybscd;
import cn.com.sailin.falconweb.model.Sycdtb;
import cn.com.sailin.falconweb.model.Wkds;
import cn.com.sailin.falconweb.model.Wker;
import cn.com.sailin.falconweb.model.Wkerattendtime;
import cn.com.sailin.falconweb.model.Yfdevice;
import cn.com.sailin.falconweb.model.Zyjn;
import cn.com.sailin.falconweb.publiccode.Code;

import org.springframework.jdbc.core.JdbcTemplate;

@Repository
public class Data {

	@Autowired
	private JdbcTemplate jdbc;

	public Data() {
	}

	public JdbcTemplate getJdbc() {
		return jdbc;
	}

	public String getPid() {
		String sql = "select _nextval('processid') as pid";
		try {
			List<Map<String, Object>> list = null;
			list = jdbc.queryForList(sql);
			return list.get(0).get("pid").toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getNextval(String seq) {

		String sql = "select _nextval('" + seq + "') as val";
		try {
			List<Map<String, Object>> list = null;
			list = jdbc.queryForList(sql);
			return Code.getFieldVal(list.get(0), "val", "");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public float getCfbl() {
		String sql = "select SYC1TB from SYCDTB" + " where SYCDTB='SYSTCD' and SYIDTB='FXKQBL'";
		try {
			List<Map<String, Object>> list = jdbc.queryForList(sql);
			if (list.size() > 0)
				return Float.valueOf(Code.getFieldVal(list.get(0), "SYC1TB", "0"));
			else
				return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int getKxts() {
		String sql = "select SYC1TB from SYCDTB" + " where SYCDTB='SYSTCD' and SYIDTB='FXKXTS'";
		try {
			List<Map<String, Object>> list = jdbc.queryForList(sql);
			if (list.size() > 0)
				return Integer.valueOf(Code.getFieldVal(list.get(0), "SYC1TB", "0"));
			else
				return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public boolean insertLog(String day, String pid, String pname, String logtext, String userid) {
		String log = null;
		if (logtext.length() > 50000)
			log = logtext.substring(0, 50000 - 1);
		else
			log = logtext;
		log = log.replaceAll("'", "^");
		String sql = "insert into GLOG" + day + "(PROCESSID,USERCODE,SYSTIME,PROGRAMNAME,LOGTEXT)" + " values(" + pid
				+ ",'" + userid + "',now(),'" + pname + "','" + log + "')";
		try {
			jdbc.execute(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateLog(String day, String pid, String result) {
		String res = null;
		if (result.length() > 50000)
			res = result.substring(0, 50000 - 1);
		else
			res = result;
		res = res.replaceAll("'", "^");
		String sql = "update GLOG" + day + " set RESULT='" + res + "', RETIME=now()" + " where PROCESSID=" + pid;
		try {
			jdbc.execute(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getPassword(String userid) {
		String sql = "select USERPASS from AC_USERINFO where USERID=?";
		List<Map<String, Object>> list = jdbc.queryForList(sql, new Object[] { userid });

		if (list.isEmpty()) {
			return "";
		} else {
			return Code.getFieldVal(list.get(0), "USERPASS", "");
		}
	}

	public String getPasswordweb(String userid) {
		String sql = "select USERPASS from WKER where IDCDNO=?";
		List<Map<String, Object>> list = jdbc.queryForList(sql, new Object[] { userid });

		if (list.isEmpty()) {
			return "";
		} else {
			return Code.getFieldVal(list.get(0), "USERPASS", "");
		}
	}

	public List<Map<String, Object>> qryAcpy(String month, String apcd, String bscd, String lccd) {

		String sql = "select * from ACPY" + " where MONTH='" + month + "'" + " and APCD='" + apcd + "'" + " and BSCD='"
				+ bscd + "'" + " and LCCD='" + lccd + "'";

		return jdbc.queryForList(sql);

	}

	public List<Map<String, Object>> qryAcpy(String sqnb) {

		String sql = "select * from ACPY where SQNB=?";

		return jdbc.queryForList(sql, new Object[] { sqnb });

	}

	public List<Map<String, Object>> qryAcpyitem(String sqnb) {

		String sql = "select * from ACPYITEM where SQNB=?";

		return jdbc.queryForList(sql, new Object[] { sqnb });
	}

	public List<Map<String, Object>> qryAcpy(String month, String apcd, String bscd) {
		String sql = "select * from ACPY where MONTH=? and APCD=? and BSCD=?";
		return jdbc.queryForList(sql, new Object[] { month, apcd, bscd });
	}

	public List<Map<String, Object>> qryAcpy(String sqnb, String lccd) {
		String sql = "select * from ACPY where SQNB=" + sqnb + " and LCCD='" + lccd + "'";
		return jdbc.queryForList(sql);
	}

	public void delAcpy(String sqnb) {

		String sql = "delete from ACPY" + " where SQNB=" + sqnb;
		jdbc.execute(sql);
		return;

	}

	public List<Map<String, Object>> qryAcpyinfo(String startdate, String enddate, String idcdno) {
		String sql = "SELECT\r\n" + "	date_format(BKCFMDT,'%Y-%m-%d %H:%i') BKCFMDT,\r\n" + "	ACPY \r\n" + "FROM\r\n"
				+ "	ACPYITEM \r\n" + "WHERE\r\n" + "	bkcfmdt >= str_to_date( ?, '%Y%m%d%H%i%s' ) \r\n"
				+ "	AND bkcfmdt < str_to_date( ?, '%Y%m%d%H%i%s' ) \r\n" + "	AND IFNULL( INBKCFM, 'N' ) = 'Y' \r\n"
				+ "	AND IDCDNO = ?";
		return jdbc.queryForList(sql, new Object[] { startdate, enddate, idcdno });
	}

	public void insertAcpy(Acpy acpy) {

		String sql = "insert into ACPY(MONTH,APCD,BSCD,LCCD,SQNB,SQRM,UPLDUR,UPLDDT,INCCCK,INLCCK,INBKOT)"
				+ " values(?,?,?,?,?,?,?,now(),'N','N','N')";
		jdbc.update(sql, new Object[] { acpy.getMONTH(), acpy.getAPCD(), acpy.getBSCD(), acpy.getLCCD(), acpy.getSQNB(),
				acpy.getSQRM(), acpy.getUPLDUR() });
		return;

	}

	public void delAcpyitem(String sqnb) {

		String sql = "delete from ACPYITEM" + " where SQNB=" + sqnb;
		jdbc.execute(sql);
		return;

	}

	public void insertAcpyitem(Acpyitem item) {

		String sql = "insert into ACPYITEM(MONTH,APCD,BSCD,LCCD,SQNB,IDCDNO,NAME,BKCD,BKAN,ACPY,INTEMP,INONEMON)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbc.update(sql,
				new Object[] { item.getMONTH(), item.getAPCD(), item.getBSCD(), item.getLCCD(), item.getSQNB(),
						item.getIDCDNO(), item.getNAME(), item.getBKCD(), item.getBKAN(), item.getACPY(),
						item.getINTEMP(), item.getINONEMON() });
		return;

	}

	public int updateAcpyccck(String sqnb, String inccck, String userid) {

		String sql = "update ACPY" + " set INCCCK='" + inccck + "'," + " CCCKUR='" + userid + "'," + " CCCKDT=now()"
				+ " where SQNB=" + sqnb;

		return jdbc.update(sql);

	}

	public int updateAcpylcck(String sqnb, String lccd, String inlcck, String userid) {

		String sql = "update ACPY" + " set INLCCK='" + inlcck + "'," + " LCCKUR='" + userid + "'," + " LCCKDT=now()"
				+ " where SQNB=" + sqnb + " and LCCD='" + lccd + "'";

		return jdbc.update(sql);

	}

	public int updateAcpybkot(String sqnb, String lccd, String inbkot, String userid) {

		String sql = "update ACPY" + " set INBKOT='" + inbkot + "'," + " BKOTUR='" + userid + "'," + " BKOTDT=now()"
				+ " where SQNB=" + sqnb + " and LCCD='" + lccd + "'";

		return jdbc.update(sql);

	}

	public int updateAcpybkcfm(String sqnb, String lccd, String inbkcfm, String userid) {

		String sql = "update ACPY set INBKCFM='" + inbkcfm + "', BKCFMUR='" + userid + "', BKCFMDT=now()"
				+ " where SQNB=" + sqnb + " and LCCD='" + lccd + "'";
		return jdbc.update(sql);
	}

	public int updateAcpyitembkcfm(String sqnb, String lccd, String inbkcfm, String bkcfercd, String bkcfer) {

		String sql = "update ACPYITEM set INBKCFM=? , BKCFMDT=now(), BKCFERCD=?, BKCFER=? where SQNB=? and LCCD=?";
		return jdbc.update(sql, new Object[] { inbkcfm, bkcfercd, bkcfer, sqnb, lccd });

	}

	public int updateAcpyitembkcfm(String sqnb, String lccd, String idcdno, String bkan, String state, String errcode,
			String errmsg) {
		String sql = "update ACPYITEM set INBKCFM=?,BKCFMDT=now(),BKCFERCD=?,BKCFER=?"
				+ " where SQNB=? and LCCD=? and IDCDNO=? and BKAN=?";
		return jdbc.update(sql, new Object[] { state, errcode, errmsg, sqnb, lccd, idcdno, bkan });
	}

	public int updateAcpy(String sqnb, String lccd, Bscollinfo bs) {
		String sql = "update ACPY set FFJE=?,FFRS=?,QXRS=?,WKRS=?,KQFFRS=?,CFBL=?,NOCFM=?" + " where SQNB=? and LCCD=?";
		return jdbc.update(sql, new Object[] { bs.getFFJE(), bs.getFFRS(), bs.getQXRS(), bs.getWKRS(), bs.getKQFFRS(),
				bs.getCFBL(), bs.getNOCFM(), sqnb, lccd });
	}

	public void insertBslc(Bs_lc bslc) {

		String sql = "insert into BS_LC(APCD,BSCD,LCCD,CHDT,CHUR,INCC) values(?,?,?,now(),?,?)";
		jdbc.update(sql,
				new Object[] { bslc.getAPCD(), bslc.getBSCD(), bslc.getLCCD(), bslc.getCHUR(), bslc.getINCC() });
		return;

	}

	public void insertBsls(Bs_ls bsls) {

		String sql = "insert into BS_LS(APCD,BSCD,LSCD,CHDT,CHUR) values(?,?,?,now(),?)";
		jdbc.update(sql, new Object[] { bsls.getAPCD(), bsls.getBSCD(), bsls.getLSCD(), bsls.getCHUR() });
		return;

	}

	public void delBslc(String apcd, String bscd) {

		String sql = "delete from BS_LC where APCD=? and BSCD=?";

		jdbc.update(sql, new Object[] { apcd, bscd });
		return;

	}

	public void delBsls(String apcd, String bscd) {

		String sql = "delete from BS_LS where APCD=? and BSCD=?";

		jdbc.update(sql, new Object[] { apcd, bscd });
		return;

	}

	public void delSpanbl(Spanbl bl) {

		String sql = "delete from SPANBL" + " where BKCD='" + bl.getBKCD() + "'" + " and SPAN='" + bl.getSPAN()
				+ "' and MONTH='" + bl.getMONTH() + "'";
		jdbc.execute(sql);
		return;
	}

	public void insertSpanbl(Spanbl bl) {

		String sql = "insert into SPANBL(APCD,BSCD,BKCD,SPAN,SPANBL,CHDT,CHUR,MONTH,SVDT,RQANBL,CHARGEDATE)"
				+ " values(?,?,?,?,?,now(),?,?,str_to_date(?,'%Y%m%d%H%i%s'),?,?)";
		jdbc.update(sql, new Object[] { bl.getAPCD(), bl.getBSCD(), bl.getBKCD(), bl.getSPAN(), bl.getSPANBL(),
				bl.getCHUR(), bl.getMONTH(), bl.getSVDT(), bl.getRQANBL(),bl.getCHARGEDATE() });
		return;
	}

	public void insertSpanbllog(Spanbl bl, String userid) {

		String sql = "insert into SPANBL_LOG(APCD,BSCD,BKCD,SPAN,SPANBL,CHDT,CHUR,MONTH)" + " values('" + bl.getAPCD()
				+ "'," + "'" + bl.getBSCD() + "'," + "'" + bl.getBKCD() + "'," + "'" + bl.getSPAN() + "',"
				+ bl.getSPANBL() + ",now()," + "'" + userid + "','" + bl.getMONTH() + "')";

		jdbc.execute(sql);
		return;

	}

	public void delWkds(String apcd, String bscd, String month) {
		String sql = "delete from WKDS" + " where APCD='" + apcd + "'" + " and BSCD='" + bscd + "'" + " and MONTH='"
				+ month + "'";
		jdbc.execute(sql);
		return;
	}

	public void delWkds(String apcd, String bscd, String month, String idcdno) {
		String sql = "delete from WKDS" + " where APCD='" + apcd + "'" + " and BSCD='" + bscd + "'" + " and MONTH='"
				+ month + "'" + " and IDCDNO='" + idcdno + "'";
		jdbc.execute(sql);
		return;
	}

	public void insertWkds(Wkds wkds) {
		String sql = "insert into WKDS(APCD,BSCD,MONTH,IDCDNO,NAME,POST,WKDS,LCCD,DT,UR,INONEMON)"
				+ " values(?,?,?,?,?,?,?,?,now(),?,?)";
		jdbc.update(sql, new Object[] { wkds.getAPCD(), wkds.getBSCD(), wkds.getMONTH(), wkds.getIDCDNO(),
				wkds.getNAME(), wkds.getPOST(), wkds.getWKDS(), wkds.getLCCD(), wkds.getUR(), wkds.getINONEMON() });
		return;
	}

	public List<Map<String, Object>> qryBsinfobyBkan(String bkcd, String spno) {
		String sql = "select * from SYBSCD" + " where BKCD='" + bkcd + "'" + " and SPAN='" + spno + "'";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qryBsinfo() {
		String sql = "select *  from SYBSCD";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qryBsinfonodt() {
		String sql = "select * from SYBSCD where ifnull(INDT,'N')<>'Y'";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qryBsinfo(String apcd, String bscd) {
		String sql = "select * from SYBSCD" + " where APCD='" + apcd + "'" + " and BSCD='" + bscd + "'";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qryBsinfobydetail(String apcd, String bscd) {
		String sql = "SELECT a.APCD,a.BCNM, a.BSCD, a.BSDS, a.BKCD, a.INDT, b.SYDSTB BKDS,"
				+ "	a.CCCD,	c.SYDSTB CCDS FROM sybscd a"
				+ "	LEFT JOIN ( SELECT syidtb, sydstb FROM sycdtb WHERE sycdtb = 'BKCD' ) b ON a.bkcd = b.syidtb"
				+ "	LEFT JOIN ( SELECT syidtb, sydstb FROM sycdtb WHERE sycdtb = 'LCCD' ) c ON a.cccd = c.syidtb"
				+ " WHERE a.apcd = ? AND a.bscd = ?";
		return jdbc.queryForList(sql, new Object[] { apcd, bscd });
	}

	public List<Map<String, Object>> qryBsinfo(String apcd) {
		String sql = "select * from SYBSCD" + " where APCD='" + apcd + "'";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qryAttendbsinfo(String apcd) {
		if (apcd.equals("HW")) {
			String sql = "select * from attend.sys_branch" + " where nt_state>0" + " and sz_set_id like 'GD_%'";
			return jdbc.queryForList(sql);
		}
		if (apcd.equals("ZK")) {
			String sql = "select * from attend.sys_branch" + " where nt_state>0" + " and sz_set_id like 'GDZ%'";
			return jdbc.queryForList(sql);
		}
		String sql = "select * from attend.sys_branch" + " where nt_state>0";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qryAttendbsinfo(String apcd, String bscd) {
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			String sql = "select * from attend.sys_branch where sz_set_id=? and nt_state>0";
			return jdbc.queryForList(sql, new Object[] { bscd });
		}
		return null;
	}

	public List<Map<String, Object>> qrySvdp() {
		String sql = "select * from SYCDTB where SYCDTB='SVDPCD'";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qryBkcd() {
		String sql = "select * from SYCDTB where SYCDTB='BKCD'";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qryZyjn(String idcdno) {
		String sql = "select * from ZYJN where idcdno=?";
		return jdbc.queryForList(sql, new Object[] { idcdno });
	}

	public List<Map<String, Object>> qryCyjl(String apcd, String bscd, String idcdno) {
		String sql = "select * from CYJL where APCD=? and BSCD=? and IDCDNO=?";
		return jdbc.queryForList(sql, new Object[] { apcd, bscd, idcdno });
	}

	public List<Map<String, Object>> qryPxxx(String apcd, String bscd, String idcdno) {
		String sql = "select * from PXXX where APCD=? and BSCD=? and IDCDNO=?";
		return jdbc.queryForList(sql, new Object[] { apcd, bscd, idcdno });
	}

	public String getSycdds(String syscd, String sysid) {
		String sql = "select SYDSTB from SYCDTB where SYCDTB=? and SYIDTB=?";
		List<Map<String, Object>> list = jdbc.queryForList(sql, new Object[] { syscd, sysid });
		if (list.size() > 0)
			return Code.getFieldVal(list.get(0), "SYDSTB", "");
		else
			return "";
	}

	public List<Map<String, Object>> qryCccd() {
		String sql = "select * from SYCDTB where SYCDTB='LCCD'";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qryLccdbybscd(String apcd, String bscd) {
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			String sql = "select * from BS_LC where apcd=? and bscd=?";
			return jdbc.queryForList(sql, new Object[] { apcd, bscd });
		}
		return null;
	}

	public List<Map<String, Object>> qryBzbycd(String apcd, String bscd, String bzcd) {
		String sql = "select * from SYCDTB where SYCDTB='BZCD' and SYIDTB=? and SYC1TB=? and SYC2TB=?";
		return jdbc.queryForList(sql, new Object[] { bzcd, bscd, apcd });
	}

	public void delAtpyitem(String bkcd, String tasq) {
		String sql = "delete from ATPYITEM" + " where BKCD='" + bkcd + "'" + " and TASQ='" + tasq + "'";
		jdbc.execute(sql);
		return;
	}

	public List<Map<String, Object>> qryAcuser(String userid) {
		String sql = "select * from AC_USERINFO" + " where USERID='" + userid + "'";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qryWkds(String month, String apcd, String bscd) {
		String sql = "select * from WKDS" + " where MONTH='" + month + "'" + " and APCD='" + apcd + "'" + " and BSCD='"
				+ bscd + "'";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qryWker(String apcd, String bscd) {
		String sql = "select * from WKER_BS where APCD='" + apcd + "' and BSCD='" + bscd + "'";
		return jdbc.queryForList(sql);

	}

	public List<Map<String, Object>> qryAttendWker(String apcd, String bscd) {
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			String sql = "SELECT attend.sys_user.sz_card_id,attend.sys_user.sz_name,"
					+ " attend.sys_post.sz_name as post" + " FROM attend.sys_user"
					+ " LEFT JOIN attend.sys_user_branch ON attend.sys_user.ng_id = attend.sys_user_branch.ng_user_id"
					+ " LEFT JOIN attend.sys_post ON attend.sys_user_branch.ng_post_id = attend.sys_post.ng_id"
					+ " WHERE attend.sys_user.ng_id IN"
					+ " (SELECT attend.sys_user_branch.ng_user_id FROM attend.sys_user_branch"
					+ " WHERE ng_branch_id IN ( select distinct e.ng_id" + " from attend.sys_branch a"
					+ " inner join attend.sys_branch b on a.ng_id=b.ng_parent_id or a.ng_id=b.ng_id"
					+ " inner join attend.sys_branch c on b.ng_id=c.ng_parent_id or b.ng_id=c.ng_id"
					+ " inner join attend.sys_branch d on c.ng_id=d.ng_parent_id or c.ng_id=d.ng_id"
					+ " inner join attend.sys_branch e on d.ng_id=e.ng_parent_id or d.ng_id=e.ng_id"
					+ " where a.sz_set_id='" + bscd + "') )" + " AND nt_user_state > 0"
					+ " and not isnull(trim(attend.sys_user.sz_card_id))";
			return jdbc.queryForList(sql);
		}
		return null;
	}

	public List<Map<String, Object>> qryAcpyitem(String month, String apcd, String bscd) {
		String sql = "select * from ACPYITEM where MONTH=? and APCD=? and BSCD=? and ACPY>0";
		return jdbc.queryForList(sql, new Object[] { month, apcd, bscd });
	}

	public List<Map<String, Object>> qryAcpyitem(String month, String apcd, String bscd, String sqnb, String lccd) {
		String sql = "select * from ACPYITEM where MONTH=? and APCD=? and BSCD=? and SQNB=? and LCCD=? and ACPY>0";
		return jdbc.queryForList(sql, new Object[] { month, apcd, bscd, sqnb, lccd });
	}

	public void insertBscollinfo(Bscollinfo bs) {
		String sql = "insert into BSCOLLINFO(MONTH,APCD,BSCD,WKRS,FFRS,FFJE,QXRS,KQFFRS,CFBL,NOCFM,QXRSCFM)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?)";
		jdbc.update(sql, new Object[] { bs.getMONTH(), bs.getAPCD(), bs.getBSCD(), bs.getWKRS(), bs.getFFRS(),
				bs.getFFJE(), bs.getQXRS(), bs.getKQFFRS(), bs.getCFBL(), bs.getNOCFM(), bs.getQXRSCFM() });
		return;
	}

	public void delBscollinfo(String month, String apcd, String bscd) {
		String sql = "delete from BSCOLLINFO" + " where MONTH='" + month + "'" + " and APCD='" + apcd + "'"
				+ " and BSCD='" + bscd + "'";
		jdbc.execute(sql);
		return;
	}

	public void delSybscd(String apcd, String bscd) {
		String sql = "delete from SYBSCD" + " where APCD='" + apcd + "'" + " and BSCD='" + bscd + "'";
		jdbc.execute(sql);
		return;
	}

	public void delAttendbscd(String apcd, String bscd) {
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			// String sql = "update attend.sys_branch set nt_state=-1 where sz_set_id=?";
			String sql = "delete from attend.sys_branch where sz_set_id=?";
			jdbc.update(sql, new Object[] { bscd });
		}
	}

	public void insertSybscd(Sybscd bs) {
		String sql = "insert into SYBSCD(BSID,APCD,BSCD,SVDP,BSDS,BKCD,PYDY,BCNM,CCCD,BSCHUR,BSCHDT,RGDT,RGUR,CKBGDY,"
				+ "AJCODE,TENDERID,PROJECTTYPE,PRINNAME,PRINTEL,BSADDRESS,LICENSEKEY,STARTDATE,ENDDATE,PROJECTCOST,SALARYCOST,INDT,LEASESTARTDATE,LEASEENDDATE,SOFTUSETYPE)"
				+ " values(?,?,?,?,?,?,?,?,?,?,now(),now(),?,?,?,?,?,?,?,?,?,?,?,?,?,'N',?,?,?)";
		jdbc.update(sql,
				new Object[] { bs.getBSID(), bs.getAPCD(), bs.getBSCD(), bs.getSVDP(), bs.getBSDS(), bs.getBKCD(),
						bs.getPYDY(), bs.getBCNM(), bs.getCCCD(), bs.getBSCHUR(), bs.getRGUR(), bs.getCKBGDY(),
						bs.getAJCODE(), bs.getTENDERID(), bs.getPROJECTTYPE(), bs.getPRINNAME(), bs.getPRINTEL(),
						bs.getBSADDRESS(), bs.getLICENSEKEY(), bs.getSTARTDATE(), bs.getENDDATE(), bs.getPROJECTCOST(),
						bs.getSALARYCOST(), bs.getLEASESTARTDATE(), bs.getLEASEENDDATE(),bs.getSOFTUSETYPE() });
		return;
	}

	public void insertAttendbscd(Sybscd bs) {
		String sql = "insert into attend.sys_branch(sz_name,ng_parent_id,sz_set_id,sz_code,nt_order,bt_inherit,nt_state,sz_branch_path,nt_branch_level,ts_create)"
				+ " values(?,2,?,?,10,1,1,?,3,now())";
		jdbc.update(sql, new Object[] { bs.getBSDS(), bs.getBSCD(), bs.getBSCD(), ",1,2," });
	}

	public List<Map<String, Object>> qrySybscd(String apcd, String bscd) {
		String sql = "select * from SYBSCD" + " where APCD='" + apcd + "'" + " and BSCD='" + bscd + "'";
		return jdbc.queryForList(sql);
	}

	public int updateSybscdByBs(Sybscd bs) {
		String sql = "update SYBSCD set SVDP=?,BSDS=?,BKCD=?,PYDY=?,BCNM=?,CCCD=?,BSCHUR=?,BSCHDT=now(),CKBGDY=?,"
				+ "AJCODE=?,TENDERID=?,PROJECTTYPE=?,PRINNAME=?,PRINTEL=?,BSADDRESS=?,LICENSEKEY=?,STARTDATE=?,ENDDATE=?,PROJECTCOST=?,SALARYCOST=?,INDT='N',"
				+ "LEASESTARTDATE=?,LEASEENDDATE=?,SOFTUSETYPE=? where APCD=? and BSCD=?";
		return jdbc.update(sql,
				new Object[] { bs.getSVDP(), bs.getBSDS(), bs.getBKCD(), bs.getPYDY(), bs.getBCNM(), bs.getCCCD(),
						bs.getBSCHUR(), bs.getCKBGDY(), bs.getAJCODE(), bs.getTENDERID(), bs.getPROJECTTYPE(),
						bs.getPRINNAME(), bs.getPRINTEL(), bs.getBSADDRESS(), bs.getLICENSEKEY(), bs.getSTARTDATE(),
						bs.getENDDATE(), bs.getPROJECTCOST(), bs.getSALARYCOST(), bs.getLEASESTARTDATE(),
						bs.getLEASEENDDATE(),bs.getSOFTUSETYPE(), bs.getAPCD(), bs.getBSCD() });
	}

	public int updateAttendbscd(Sybscd bs) {
		String sql = "update attend.sys_branch set sz_name=?,sz_branch_path=? where sz_set_id=?";
		return jdbc.update(sql, new Object[] { bs.getBSDS(), bs.getBSCDPATH(), bs.getBSCD() });
	}

	public int updateSybscdByBank(Sybscd bs) {
		String sql = "update SYBSCD set SPAN=?,SPANRQBL=?,BLWNDY=?,SPANPREBL=? where APCD=? and BSCD=?";
		return jdbc.update(sql, new Object[] { bs.getSPAN(), bs.getSPANRQBL(), bs.getBLWNDY(), bs.getSPANPREBL(),
				bs.getAPCD(), bs.getBSCD() });
	}

	public int updateSybscdByDt(Sybscd bs) {
		String sql = "update SYBSCD set INDT=?,DTDT=now(),DTUR=?,ENDDATE=str_to_date(?,'%Y-%m-%d') where APCD=? and BSCD=?";
		return jdbc.update(sql,
				new Object[] { bs.getINDT(), bs.getDTUR(), bs.getENDDATE(), bs.getAPCD(), bs.getBSCD() });
	}

	public void insertSycdtb(Sycdtb cd) {
		String sql = "insert into SYCDTB(SYCDTB,SYIDTB,SYDSTB,SYC1TB,SYC2TB,SYC3TB,SYC4TB,SYC5TB,SYC6TB,INUSCH,DTTR,URCD)"
				+ " values('" + cd.getSYCDTB() + "'," + "'" + cd.getSYIDTB() + "'," + "'" + cd.getSYDSTB() + "'," + "'"
				+ cd.getSYC1TB() + "'," + "'" + cd.getSYC2TB() + "'," + "'" + cd.getSYC3TB() + "'," + "'"
				+ cd.getSYC4TB() + "'," + "'" + cd.getSYC5TB() + "'," + "'" + cd.getSYC6TB() + "'," + "'"
				+ cd.getINUSCH() + "',now()," + "'" + cd.getURCD() + "')";
		jdbc.execute(sql);
		return;
	}

	public void delSycdtb(String sycd, String syid) {
		String sql = "delete from SYCDTB" + " where SYCDTB='" + sycd + "'" + " and SYIDTB='" + syid + "'";
		jdbc.execute(sql);
		return;
	}

	public List<Map<String, Object>> qrySybscd() {
		String sql = "select * from SYBSCD";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qrySpanbl(String month) {
		String sql = "select APCD,BSCD,BKCD,SPAN,SPANBL,date_format(CHDT,'%Y%m%d%H%i%s') as CHDT from SPANBL"
				+ " where MONTH='" + month + "'";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qrySpanbl(String month, String apcd, String bscd) {
		String sql = "select APCD,BSCD,BKCD,SPAN,SPANBL,date_format(CHDT,'%Y%m%d%H%i%s') as CHDT,"
				+ " CHUR,MONTH,date_format(SVDT,'%Y%m%d%H%i%s') as SVDT,RQANBL from SPANBL" + " where MONTH='" + month
				+ "'" + " and APCD='" + apcd + "'" + " and BSCD='" + bscd + "'";
		return jdbc.queryForList(sql);
	}

	public void insertWkerbs(Wker wker) {
		String sql = "insert into WKER_BS(IDCDNO,NAME,LCCD,APCD,BSCD,RGDT,RGUR,BKCD,BKAN,WKKD,SZ_EMPLOY_ID,INONEMON,LSCD,WORKERTYPE,DAYSALARY,MONTHSALARY,BZ,ISCERT,EMPDATE,INEMP,TIMECARD,ISGROUPER,ISMIGRANT,WKKDSV)"
				+ " values(?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,'1',?,?,?,?)";
		jdbc.update(sql,
				new Object[] { wker.getIDCDNO(), wker.getNAME(), wker.getLCCD(), wker.getAPCD(), wker.getBSCD(),
						wker.getRGUR(), wker.getBKCD(), wker.getBKAN(), wker.getWKKD(), wker.getSZ_EMPLOY_ID(),
						wker.getINONEMON(), wker.getLSCD(), wker.getWORKERTYPE(), wker.getDAYSALARY(),
						wker.getMONTHSALARY(), wker.getBZ(), wker.getISCERT(), wker.getEMPDATE(), wker.getTIMECARD(),
						wker.getINGROUPER(), wker.getINMIGRANT(), wker.getWKKDSV() });
		return;
	}

	public void delWkerbs(String apcd, String bscd, String idcdno) {
		String sql = "delete from WKER_BS where APCD=? and BSCD=? and IDCDNO=?";
		jdbc.update(sql, new Object[] { apcd, bscd, idcdno });
		return;
	}

	public int updateWkerbs(Wker wker) {
		String sql = "update WKER_BS set NAME=?,LCCD=?,RGDT=now(),RGUR=?,BKCD=?,BKAN=?,WKKD=?,INONEMON=?,LSCD=?,WORKERTYPE=?,DAYSALARY=?,MONTHSALARY=?,BZ=?,ISCERT=?,"
				+ "EMPDATE=?,TIMECARD=?,ISGROUPER=?,ISMIGRANT=?,WKKDSV=? where APCD=? and BSCD=? and IDCDNO=?";
		return jdbc.update(sql,
				new Object[] { wker.getNAME(), wker.getLCCD(), wker.getRGUR(), wker.getBKCD(), wker.getBKAN(),
						wker.getWKKD(), wker.getINONEMON(), wker.getLSCD(), wker.getWORKERTYPE(), wker.getDAYSALARY(),
						wker.getMONTHSALARY(), wker.getBZ(), wker.getISCERT(), wker.getEMPDATE(), wker.getTIMECARD(),
						wker.getINGROUPER(), wker.getINMIGRANT(), wker.getWKKDSV(), wker.getAPCD(), wker.getBSCD(),
						wker.getIDCDNO() });
	}

	public List<Map<String, Object>> qryWkerbs(String apcd, String bscd, String idcdno) {
		String sql = "select * from WKER_BS where APCD=? and BSCD=? and IDCDNO=?";
		return jdbc.queryForList(sql, new Object[] { apcd, bscd, idcdno });
	}

	public List<Map<String, Object>> qryWkerbs(String apcd, String bscd) {
		String sql = "select * from WKER_BS where APCD=? and BSCD=?";
		return jdbc.queryForList(sql, new Object[] { apcd, bscd });
	}

	public List<Map<String, Object>> qryWkerbsbyemploy(String apcd, String bscd, String employ_id) {
		String sql = "select * from WKER_BS where APCD=? and BSCD=? and SZ_EMPLOY_ID=?";
		return jdbc.queryForList(sql, new Object[] { apcd, bscd, employ_id });
	}

	public void insertWkerlog(Wker wker) {
		String sql = "insert into WKER_LOG(IDCDNO,NAME,LCCD,APCD,BSCD,RGDT,RGUR,BKCD,BKAN,PXQK,ZYJN,"
				+ "CYJL,INDY,DLDT,DLUR)" + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?)";
		jdbc.update(sql,
				new Object[] { wker.getIDCDNO(), wker.getNAME(), wker.getLCCD(), wker.getAPCD(), wker.getBSCD(),
						wker.getRGDT(), wker.getRGUR(), wker.getBKCD(), wker.getBKAN(), wker.getPXQK(), wker.getZYJN(),
						wker.getCYJL(), wker.getINDY(), wker.getDLUR() });
		return;
	}

	public void insertWker(Wker wker) {
		String sql = "insert into WKER(IDCDNO,NAME,USERPASS,RGDT,RGUR,PXQK,ZYJN,CYJL,INDY,INSEX,BRDT,HOMEADD,ETHNIC,PIC1,PIC2,PIC3,PIC4,PIC5,"
				+ "REGADDRESS,IDSTARTDATE,IDENDDATE,TEL,EDUCATION,INMARRY,INMIN,HOUSEHOLDTYPE,PERSONGUID,PIC3GUID,PIC4GUID,PIC5GUID)"
				+ "values(?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbc.update(sql, new Object[] { wker.getIDCDNO(), wker.getNAME(), wker.getUSERPASS(), wker.getRGUR(),
				wker.getPXQK(), wker.getZYJN(), wker.getCYJL(), wker.getINDY(), wker.getINSEX(), wker.getBRDT(),
				wker.getHOMEADD(), wker.getETHNIC(), wker.getPIC1(), wker.getPIC2(), wker.getPIC3(), wker.getPIC4(),
				wker.getPIC5(), wker.getREGADDRESS(), wker.getIDSTARTDATE(), wker.getIDENDDATE(), wker.getTEL(),
				wker.getEDUCATION(), wker.getINMARRY(), wker.getINMIN(), wker.getHOUSEHOLDTYPE(), wker.getPERSONGUID(),
				wker.getPIC3GUID(), wker.getPIC4GUID(), wker.getPIC5GUID() });
		return;
	}

	public void insertOplist(String apcd, String opid, String type, String content) {
		String sql = "insert into oplist(apcd,opid,type,content)" + "values(?,?,?,?)";
		jdbc.update(sql, new Object[] { apcd, opid, type, content });
	}

	public void insertOplist(String apcd, String bscd, String opid, String type, String content) {
		String sql = "insert into oplist(apcd,bscd,opid,type,content) values(?,?,?,?,?)";
		jdbc.update(sql, new Object[] { apcd, bscd, opid, type, content });
	}

	public void insertOplistsuccess(String apcd, String bscd, String opid, String type, String content) {
		String sql = "insert into oplistsuccess(apcd,bscd,opid,type,content,optime) values(?,?,?,?,?,now())";
		jdbc.update(sql, new Object[] { apcd, bscd, opid, type, content });
	}

	public void delOplist(String opid) {
		String sql = "delete from oplist where opid=?";
		jdbc.update(sql, new Object[] { opid });
	}

	public void insertZyjn(String idcdno, Zyjn jn) {
		String sql = "insert into zyjn(IDCDNO,JNID,JNNAME,REMARK)" + " values(?,?,?,?)";
		jdbc.update(sql, new Object[] { idcdno, jn.getJNID(), jn.getJNNAME(), jn.getREMARK() });
	}

	public void delZyjn(String idcdno) {
		String sql = "delete from zyjn where IDCDNO=?";
		jdbc.update(sql, new Object[] { idcdno });
	}

	public void insertPxxx(String apcd, String bscd, String idcdno, Pxxx px) {
		String sql = "insert into pxxx(IDCDNO,PXID,PXTITLE,APCD,BSCD,STARTDATE,ENDDATE,REMARK)"
				+ " values(?,?,?,?,?,?,?,?)";
		jdbc.update(sql, new Object[] { idcdno, px.getPXID(), px.getPXTITLE(), apcd, bscd, px.getSTARTDATE(),
				px.getENDDATE(), px.getREMARK() });
	}

	public void delPxxx(String apcd, String bscd, String idcdno) {
		String sql = "delete from pxxx where APCD=? and BSCD=? and IDCDNO=?";
		jdbc.update(sql, new Object[] { apcd, bscd, idcdno });
	}

	public void insertCyjl(String apcd, String bscd, String idcdno, Cyjl jl) {
		String sql = "insert into cyjl(IDCDNO,JLID,APCD,BSCD,STARTDATE,ENDDATE,WKKD,REMARK)"
				+ " values(?,?,?,?,?,?,?,?)";
		jdbc.update(sql, new Object[] { idcdno, jl.getJLID(), apcd, bscd, jl.getSTARTDATE(), jl.getENDDATE(),
				jl.getWKKD(), jl.getREMARK() });
	}

	public void delCyjl(String apcd, String bscd, String idcdno) {
		String sql = "delete from cyjl where APCD=? and BSCD=? and IDCDNO=?";
		jdbc.update(sql, new Object[] { apcd, bscd, idcdno });
	}

	public void updateOplistresult(String opid, String result) {
		String sql = "update oplist set result=? where opid=?";
		jdbc.update(sql, new Object[] { result, opid });
	}

	public List<Map<String, Object>> qryOplistbytype(String type) {
		String sql = "select * from oplist where type=? and ifnull(result,'')='' order by opid";
		return jdbc.queryForList(sql, new Object[] { type });
	}

	public List<Map<String, Object>> qryOplistupload() {
		String sql = "select * from oplist where ifnull(result,'')=''"
				+ " and type in ('newuploadproject','newuploadhuman','newdeletehuman','newuploadattend','newuploadexprience','newdeleteexprience','newuploadtrain','newdeletetrain','newuploadsalary','newuploadbank')"
				+ " order by opid";
		return jdbc.queryForList(sql);
	}

	public void insertAttendWker(Wker wker) {
		if (wker.getAPCD().equals("HW") || wker.getAPCD().equals("ZK") || wker.getAPCD().equals("ZQ")
				|| wker.getAPCD().equals("RJ") || wker.getAPCD().equals("YF")) {
			String sql = "insert into attend.sys_user(SZ_USER_NAME,SZ_EMPLOY_ID,SZ_NAME,SZ_CARD_ID,SZ_PASSWORD,NT_USER_STATE,DT_ENTRY,DT_START_WORK,TS_CREATE)"
					+ "values(?,?,?,?,'123456',1,now(),now(),now())";
			jdbc.update(sql,
					new Object[] { wker.getSZ_EMPLOY_ID(), wker.getSZ_EMPLOY_ID(), wker.getNAME(), wker.getIDCDNO() });
			return;
		}
		return;
	}

	public void insertAttendData(String apcd, Statcard dt) {
		String place = "";
		if (dt.getSz_dev_name().indexOf("入") != -1)
			place = "入";
		if (dt.getSz_dev_name().indexOf("出") != -1)
			place = "出";
		String sql = "insert into attend.stat_card(apcd,apid,st_kind,ng_user_id,ng_branch_id,ng_dev_id,ts_card,ts_create,sz_employ_id,sz_dev_name,sz_dev_place,sz_photo_path)"
				+ "values(?,?,?,?,?,?,str_to_date(?, '%Y-%m-%d %H:%i:%s'),now(),?,?,?,?)";
		jdbc.update(sql,
				new Object[] { apcd, dt.getApid(), dt.getSt_kind(), dt.getNg_user_id(), dt.getNg_branch_id(),
						dt.getNg_dev_id(), dt.getTs_card(), dt.getSz_employ_id(), dt.getSz_dev_name(), place,
						dt.getSz_photo_path() });
		return;
	}

	public void delAttendData(String apcd, int apid) {
		String sql = "delete from attend.stat_card where apcd=? and apid=?";
		jdbc.update(sql, new Object[] { apcd, apid });
		return;
	}

	public void insertAttendPost(String apcd, String code, String desc) {
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			String sql = "insert into attend.sys_post(NG_BRANCH_ID,SZ_SET_ID,SZ_CODE,SZ_NAME,NT_SOURCE,NT_JUDGE_ID,NG_CREATOR,TS_CREATE)"
					+ " values(1,?,?,?,0,0,1,now())";
			jdbc.update(sql, new Object[] { code, code, desc });
			return;
		}
		return;
	}

	public int updateWker(Wker wker) {
		String sql = "update WKER set NAME=?,RGDT=now(),RGUR=?,PXQK=?,ZYJN=?,CYJL=?,INDY=?,INSEX=?,BRDT=?,HOMEADD=?,ETHNIC=?,PIC1=?,PIC2=?,PIC3=?,PIC4=?,PIC5=?,"
				+ "REGADDRESS=?,IDSTARTDATE=?,IDENDDATE=?,TEL=?,EDUCATION=?,INMARRY=?,INMIN=?,HOUSEHOLDTYPE=?,PERSONGUID=?,PIC3GUID=?,PIC4GUID=?,PIC5GUID=?"
				+ " where IDCDNO=?";
		return jdbc.update(sql, new Object[] { wker.getNAME(), wker.getRGUR(), wker.getPXQK(), wker.getZYJN(),
				wker.getCYJL(), wker.getINDY(), wker.getINSEX(), wker.getBRDT(), wker.getHOMEADD(), wker.getETHNIC(),
				wker.getPIC1(), wker.getPIC2(), wker.getPIC3(), wker.getPIC4(), wker.getPIC5(), wker.getREGADDRESS(),
				wker.getIDSTARTDATE(), wker.getIDENDDATE(), wker.getTEL(), wker.getEDUCATION(), wker.getINMARRY(),
				wker.getINMIN(), wker.getHOUSEHOLDTYPE(), wker.getPERSONGUID(), wker.getPIC3GUID(), wker.getPIC4GUID(),
				wker.getPIC5GUID(), wker.getIDCDNO() });
	}

	public int wkerleave(String apcd, String bscd, String wkid, String disdate) {
		String sql = "update WKER_BS set INEMP='2',DISDATE=? where APCD=? and BSCD=? and SZ_EMPLOY_ID=?";
		return jdbc.update(sql, new Object[] { disdate, apcd, bscd, wkid });
	}

	public int updateAttendWker(Wker wker) {
		if (wker.getAPCD().equals("HW") || wker.getAPCD().equals("ZK") || wker.getAPCD().equals("ZQ")
				|| wker.getAPCD().equals("RJ") || wker.getAPCD().equals("YF")) {
			String sql = "update attend.sys_user set SZ_NAME=?,SZ_CARD_ID=?,NT_USER_STATE=1" + " where SZ_EMPLOY_ID=?";
			return jdbc.update(sql, new Object[] { wker.getNAME(), wker.getIDCDNO(), wker.getSZ_EMPLOY_ID() });
		}
		return 0;
	}

	public int delAttendWker(String apcd, String wkid) {
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			String sql = "update attend.sys_user set nt_user_state=-1 where ng_id=?";
			return jdbc.update(sql, new Object[] { wkid });
		}
		return 0;
	}

	public List<Map<String, Object>> qryWker(String idcdno) {
		String sql = "select * from WKER where IDCDNO=?";
		return jdbc.queryForList(sql, new Object[] { idcdno });
	}

	public List<Map<String, Object>> qryAttendUser(String apcd, String employid) {
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			String sql = "select * from attend.sys_user where sz_employ_id=? and nt_user_state>0";
			return jdbc.queryForList(sql, new Object[] { employid });
		}
		return null;
	}

	public List<Map<String, Object>> qryAttendUserbyidcdno(String apcd, String idcdno) {
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			String sql = "select * from attend.sys_user where sz_card_id=? and nt_user_state>0";
			return jdbc.queryForList(sql, new Object[] { idcdno });
		}
		return null;
	}
	
	public List<Map<String, Object>> qryAttendUserbywkid(String apcd, String wkid) {
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			String sql = "select * from attend.sys_user where sz_employ_id=? and nt_user_state>0";
			return jdbc.queryForList(sql, new Object[] { wkid });
		}
		return null;
	}

	public List<Map<String, Object>> qryAttendPost(String apcd, String postcd) {
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			String sql = "select * from attend.sys_post where sz_code=?";
			return jdbc.queryForList(sql, new Object[] { postcd });
		}
		return null;
	}

	public List<Map<String, Object>> qryAttendPostbyname(String apcd, String postname) {
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			String sql = "select * from attend.sys_post where sz_name=?";
			return jdbc.queryForList(sql, new Object[] { postname });
		}
		return null;
	}

	public List<Map<String, Object>> qryAttendUserbranch(String apcd, String wkid, String bsid, String postid) {
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			String sql = "select * from attend.sys_user_branch where ng_user_id=? and ng_branch_id=? and ng_post_id=?";
			return jdbc.queryForList(sql, new Object[] { wkid, bsid, postid });
		}
		return null;
	}

	public void insertAttendUserbranch(String apcd, String wkid, String bsid, String postid) {
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			String sql = "insert into attend.sys_user_branch(ng_user_id,ng_branch_id,ng_post_id,bt_is_primary,ts_create)"
					+ " values(?,?,?,1,now())";
			jdbc.update(sql, new Object[] { wkid, bsid, postid });
		}
		return;
	}

	public int updateAttendUserbranch(String apcd, String wkid, String bsid, String postid) {
		// 其实就是改个工种
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			String sql = "update attend.sys_user_branch set ng_post_id=? where ng_user_id=? and ng_branch_id=?";
			return jdbc.update(sql, new Object[] { postid, wkid, bsid });
		}
		return 0;
	}

	public void delAttendUserbranch(String apcd, String wkid, String bsid) {
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			String sql = "delete from attend.sys_user_branch where ng_user_id=? and ng_branch_id=?";
			jdbc.update(sql, new Object[] { wkid, bsid });
		}
		return;
	}

	public int resetPassword(String userid, String newpassword) {
		String sql = "update AC_USERINFO set USERPASS=? where upper(trim(USERID))=?";
		return jdbc.update(sql, new Object[] { newpassword, userid.toUpperCase() });
	}

	public int updatePassword(String userid, String oldpassword, String newpassword) {
		String sql = "update AC_USERINFO set USERPASS=? where upper(trim(USERID))=? and upper(trim(USERPASS))=?";
		return jdbc.update(sql, new Object[] { newpassword, userid.toUpperCase(), oldpassword.toUpperCase() });
	}

	public int updatePasswordworker(String idcdno, String oldpassword, String newpassword) {
		String sql = "update WKER set USERPASS=? where upper(trim(IDCDNO))=? and upper(trim(USERPASS))=?";
		return jdbc.update(sql, new Object[] { newpassword, idcdno.toUpperCase(), oldpassword.toUpperCase() });
	}

	public void insertAcfunctioninfo(Acfunctioninfo fun) {
		String sql = "insert into AC_FUNCTIONINFO(FUNCTIONID,FUNCTIONNAME,FUNCTIONDESC,CONTROLNAME,FORMMODAL,PARENTFUNCTIONID,ISPUBLIC,ACTIONCONTROL,ORDERNUM)"
				+ " values('" + fun.getFUNCTIONID() + "'," + "'" + fun.getFUNCTIONNAME() + "'," + "'"
				+ fun.getFUNCTIONDESC() + "'," + "'" + fun.getCONTROLNAME() + "'," + "'" + fun.getFORMMODAL() + "',"
				+ "'" + fun.getPARENTFUNCTIONID() + "'," + "'" + fun.getISPUBLIC() + "'," + fun.getACTIONCONTROL() + ","
				+ fun.getORDERNUM() + ")";
		jdbc.execute(sql);
		return;
	}

	public void insertAcmenustruct(Acmenustruct menu) {
		String sql = "insert into AC_MENUSTRUCT(MENUID,MENUTEXT,PARENTMENUID,FUNCTIONID,ORDERNUM,CLICKNUM,LASTCLICKUSER,LASTCLICKTIME)"
				+ " values('" + menu.getMENUID() + "'," + "'" + menu.getMENUTEXT() + "'," + "'" + menu.getPARENTMENUID()
				+ "'," + "'" + menu.getFUNCTIONID() + "'," + "'" + menu.getORDERNUM() + "'," + "'" + menu.getCLICKNUM()
				+ "'," + "'" + menu.getLASTCLICKUSER() + "',now())";
		jdbc.execute(sql);
		return;
	}

	public void insertActoolcfg(Actoolcfg cfg) {
		String sql = "insert into AC_TOOLCFG(BUTTONID,BUTTONTEXT,ICONINDEX,ISSHOW,FUNCTIONID,ORDERNUM)" + " values("
				+ cfg.getBUTTONID() + "," + "'" + cfg.getBUTTONTEXT() + "'," + cfg.getICONINDEX() + "," + "'"
				+ cfg.getISSHOW() + "'," + "'" + cfg.getFUNCTIONID() + "'," + cfg.getORDERNUM() + ")";
		jdbc.execute(sql);
		return;
	}

	public void insertAcroleinfo(String roleid, String rolename, String roledesc) {
		String sql = "insert into AC_ROLEINFO(ROLEID,ROLENAME,ROLEDESC)" + " values(" + roleid + ",'" + rolename + "','"
				+ roledesc + "')";
		jdbc.execute(sql);
		return;
	}

	public void delAcroleinfo(String roleid) {
		String sql = "delete from AC_ROLEINFO where ROLEID=" + roleid;
		jdbc.execute(sql);
		return;
	}

	public void delAcrolefunctionbyroleid(String roleid) {
		String sql = "delete from AC_ROLE_FUNCTION where ROLEID=" + roleid;
		jdbc.execute(sql);
		return;
	}

	public void delAcrolefunctionbyfuncid(String funcid) {
		String sql = "delete from AC_ROLE_FUNCTION where FUNCTION='" + funcid + "'";
		jdbc.execute(sql);
		return;
	}

	public void delAcroleuserbyroleid(String roleid) {
		String sql = "delete from AC_ROLE_USER where ROLEID=" + roleid;
		jdbc.execute(sql);
		return;
	}

	public void delAcroleuserbyuserid(String userid) {
		String sql = "delete from AC_ROLE_USER where USERID='" + userid + "'";
		jdbc.execute(sql);
		return;
	}

	public void insertAcrolefunction(String roleid, String funcid) {
		String sql = "insert into AC_ROLE_FUNCTION(ROLEID,FUNCTIONID)" + " values(" + roleid + ",'" + funcid + "')";
		jdbc.execute(sql);
		return;
	}

	public void insertAcuserinfo(Acuserinfo ur) {
		String sql = "insert into AC_USERINFO(USERID,USERNAME,USERPASS,USERTYPE,DEPTID,ISUSE,PASSNOTOVER,PASSOVERDATE,INADMIN,RECORDUSERID,RECORDTIME)"
				+ " values('" + ur.getUSERID() + "'," + "'" + ur.getUSERNAME() + "'," + "'" + ur.getUSERPASS() + "',"
				+ "'" + ur.getUSERTYPE() + "'," + "'" + ur.getDEPTID() + "'," + "'" + ur.getISUSE() + "'," + "'"
				+ ur.getPASSNOTOVER() + "'," + "'" + ur.getPASSOVERDATE() + "'," + "'" + ur.getINADMIN() + "'," + "'"
				+ ur.getRECORDUSERID() + "',now())";
		jdbc.execute(sql);
		return;
	}

	public void insertAcroleuser(String roleid, String userid) {
		String sql = "insert into AC_ROLE_USER(ROLEID,USERID)" + " values(" + roleid + ",'" + userid + "')";
		jdbc.execute(sql);
		return;
	}

	public void delAcuserinfo(String userid) {
		String sql = "delete from AC_USERINFO" + " where USERID='" + userid + "'";
		jdbc.execute(sql);
		return;
	}

	public void delAcmenustruct(String menuid) {
		String sql = "delete from AC_MENUSTRUCT" + " where MENUID=" + menuid;
		jdbc.execute(sql);
		return;
	}

	public void delAcfunctioninfo(String funcid) {
		String sql = "delete from AC_FUNCTIONINFO where FUNCTIONID='" + funcid + "'";
		jdbc.execute(sql);
		return;
	}

	public void clearAcmenustructbyfuncid(String funcid) {
		String sql = "update from AC_MENUSTRUCT set FUNCTIONID=''" + " where FUNCTIONID='" + funcid + "'";
		jdbc.execute(sql);
		return;
	}

	public void clearActoolcfgbyfuncid(String funcid) {
		String sql = "update from AC_TOOLCFG set FUNCTIONID=''" + " where FUNCTIONID='" + funcid + "'";
		jdbc.execute(sql);
		return;
	}

	public void delActoolcfg(String buttonid) {
		String sql = "delete from AC_TOOLCFG where BUTTONID=" + buttonid;
		jdbc.execute(sql);
		return;
	}

	public int updateAcmenustructinfo(String mt, String pmid, String fid, String mid) {
		String sql = "update AC_MENUSTRUCT set MENUTEXT='" + mt + "'," + " PARENTMENUID=" + pmid + "," + " FUNCTIONID='"
				+ fid + "'" + " where MENUID='" + mid + "'";
		return jdbc.update(sql);

	}

	public int updateAcmenustructordernum(String menuid, String ordernum) {
		String sql = "update AC_MENUSTRUCT set ORDERNUM=" + ordernum + " where MENUID=" + menuid;
		return jdbc.update(sql);
	}

	public int updateAcfunctioninfo(Acfunctioninfo func) {
		String sql = "update AC_FUNCTIONINFO set FUNCTIONNAME='" + func.getFUNCTIONNAME() + "'," + " FUNCTIONDESC='"
				+ func.getFUNCTIONDESC() + "'," + " CONTROLNAME='" + func.getCONTROLNAME() + "'," + " FORMMODAL='"
				+ func.getFORMMODAL() + "'," + " PARENTFUNCTIONID='" + func.getPARENTFUNCTIONID() + "'," + " ISPUBLIC='"
				+ func.getISPUBLIC() + "'," + " ACTIONCONTROL=" + func.getACTIONCONTROL() + " where FUNCTIONID='"
				+ func.getFUNCTIONID() + "'";
		return jdbc.update(sql);
	}

	public int updateAcfunctioninfoordernum(String funcid, String ordernum) {
		String sql = "update AC_FUNCTIONINFO set ORDERNUM=" + ordernum + " where FUNCTIONID='" + funcid + "'";
		return jdbc.update(sql);
	}

	public int updateActoolcfginfo(Actoolcfg tool) {
		String sql = "update AC_TOOLCFG set BUTTONTEXT='" + tool.getBUTTONTEXT() + "'," + " ICONINDEX="
				+ tool.getICONINDEX() + "," + " ISSHOW='" + tool.getISSHOW() + "'," + " FUNCTIONID='"
				+ tool.getFUNCTIONID() + "'," + " ORDERNUM=" + tool.getORDERNUM() + " where BUTTONID="
				+ tool.getBUTTONID();
		return jdbc.update(sql);
	}

	public int updateActoolcfgordernum(String buttonid, String ordernum) {
		String sql = "update AC_TOOLCFG set ORDERNUM=" + ordernum + " where BUTTONID=" + buttonid;
		return jdbc.update(sql);
	}

	public int incMenuclicknum(String userid, String funcid, String menutext) {
		String sql = "update AC_MENUSTRUCT set CLICKNUM=ifnull(CLICKNUM,0) + 1,LASTCLICKUSER='" + userid
				+ "',LASTCLICKTIME=now()" + " where FUNCTIONID='" + funcid + "'" + " and MENUTEXT='" + menutext + "'";
		return jdbc.update(sql);
	}

	public int updateAcuserinfo(Acuserinfo user) {
		String sql = "update AC_USERINFO set USERNAME=?, USERTYPE=?, DEPTID=?, ISUSE=?,"
				+ " PASSNOTOVER=?, PASSOVERDATE=?, INADMIN=?, RECORDUSERID=?, RECORDTIME=now()" + " where USERID=?";
		return jdbc.update(sql,
				new Object[] { user.getUSERNAME(), user.getUSERTYPE(), user.getDEPTID(), user.getISUSE(),
						user.getPASSNOTOVER(), user.getPASSOVERDATE(), user.getINADMIN(), user.getRECORDUSERID(),
						user.getUSERID() });
	}

	public List<Map<String, Object>> qryBscollinfobymonth(String month) {
		String sql = "select * from BSCOLLINFO" + " where MONTH='" + month + "'";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qryBscollinfobyapcdbscdmonth(String month, String apcd, String bscd) {
		String sql = "select * from BSCOLLINFO where MONTH=? and APCD=? and BSCD=?";
		return jdbc.queryForList(sql, new Object[] { month, apcd, bscd });
	}

	public List<Map<String, Object>> qryAttendcheckon(String apcd, String bscd, String starttime, String endtime) {
		if (apcd.equals("HW") || apcd.equals("ZK") || apcd.equals("ZQ") || apcd.equals("RJ") || apcd.equals("YF")) {
			String sql = "SELECT DATE_FORMAT( stat_card.ts_card, '%Y%m%d%H%i%s' ) AS ts_card,"
					+ "	sys_user.sz_card_id, sys_user.sz_name,sys_user.sz_employ_id,g.post FROM\r\n"
					+ "	attend.stat_card\r\n"
					+ "	LEFT JOIN attend.sys_user ON stat_card.ng_user_id = sys_user.ng_id\r\n" + "	LEFT JOIN (\r\n"
					+ "SELECT\r\n" + "	e.ng_user_id,\r\n" + "	e.ng_branch_id,\r\n" + "	f.sz_name post \r\n"
					+ "FROM\r\n" + "	attend.sys_user_branch e\r\n"
					+ "	LEFT JOIN attend.sys_post f ON e.ng_post_id = f.ng_id \r\n"
					+ "	) g ON stat_card.ng_user_id = g.ng_user_id \r\n"
					+ "	AND stat_card.ng_branch_id = g.ng_branch_id \r\n" + "WHERE\r\n"
					+ "	stat_card.ts_card >= STR_TO_DATE( ?, '%Y%m%d' ) \r\n"
					+ "	AND stat_card.ts_card < STR_TO_DATE( ?, '%Y%m%d' ) \r\n"
					+ "	AND stat_card.ng_branch_id IN (\r\n" + "SELECT DISTINCT\r\n" + "	e.ng_id \r\n" + "FROM\r\n"
					+ "	attend.sys_branch a\r\n" + "	INNER JOIN attend.sys_branch b ON a.ng_id = b.ng_parent_id \r\n"
					+ "	OR a.ng_id = b.ng_id\r\n" + "	INNER JOIN attend.sys_branch c ON b.ng_id = c.ng_parent_id \r\n"
					+ "	OR b.ng_id = c.ng_id\r\n" + "	INNER JOIN attend.sys_branch d ON c.ng_id = d.ng_parent_id \r\n"
					+ "	OR c.ng_id = d.ng_id\r\n" + "	INNER JOIN attend.sys_branch e ON d.ng_id = e.ng_parent_id \r\n"
					+ "	OR d.ng_id = e.ng_id \r\n" + "WHERE\r\n" + "	a.sz_set_id = ? \r\n" + "	) \r\n"
					+ "ORDER BY\r\n" + "	stat_card.ts_card";
			return jdbc.queryForList(sql, new Object[] { starttime, endtime, bscd });
		}
		return null;
	}

	public void insertAnnc(Annc annc) {

		String sql = "insert into ANNC(ANDATE,ANTT,ANNC,CHUR,SVDP)" + " values(str_to_date(?,'%Y%m%d%H%i%s'),?,?,?,?)";

		jdbc.update(sql,
				new Object[] { annc.getANDATE(), annc.getANTT(), annc.getANNC(), annc.getCHUR(), annc.getSVDP() });

		return;
	}

	public void delAnnc(String anid) {

		String sql = "delete from ANNC where ANID=" + anid;
		jdbc.update(sql);
		return;

	}

	public int updateAnnc(Annc annc) {

		String sql = "update ANNC set ANDATE=str_to_date(?,'%Y%m%d%H%i%s')," + " ANTT=?,ANNC=?,CHUR=?,SVDP=?"
				+ " where ANID=?";
		return jdbc.update(sql, new Object[] { annc.getANDATE(), annc.getANTT(), annc.getANNC(), annc.getCHUR(),
				annc.getSVDP(), annc.getANID() });

	}

	public List<Map<String, Object>> qrySumqx() {
		String sql = "select APCD,BSCD,sum(QXRS) as SUMQXRS,sum(FFRS) as SUMFFRS from bscollinfo group by APCD,BSCD";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qryWorkerinfo(String idcdno) {
		String sql = "SELECT a.IDCDNO,a.NAME,b.SYDSTB AS LCCD,c.BSDS, d.SYDSTB AS BKCD FROM WKER_BS a"
				+ "	LEFT JOIN ( SELECT * FROM SYCDTB WHERE SYCDTB = 'LCCD' ) b ON a.LCCD = b.SYIDTB"
				+ "	LEFT JOIN SYBSCD c ON a.APCD = c.APCD AND a.BSCD = c.BSCD"
				+ "	LEFT JOIN ( SELECT * FROM SYCDTB WHERE SYCDTB = 'BKCD' ) d ON a.BKCD = d.SYIDTB"
				+ " where a.IDCDNO=?";
		return jdbc.queryForList(sql, new Object[] { idcdno });
	}

	public List<Map<String, Object>> qryAnnc() {
		String sql = "select date_format(ANDATE,'%Y-%m-%d %H:%i:%s') ANDATE,ANTT,ANNC,CHUR,SVDP,ANID"
				+ " from ANNC order by ANDATE desc";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qryCheckon(String idcdno, String startdate, String enddate) {
		String sql = "SELECT\r\n" + "	date_format(a.ts_card,'%Y-%m-%d %H:%i') AS CKTM,\r\n"
				+ "	a.sz_branch_name AS BSNM,\r\n" + "	b.sz_name AS DEVNM,\r\n" + "	c.sz_set_id AS BSCD,\r\n"
				+ "	c.sz_name AS BSDS \r\n" + "FROM\r\n" + "	attend.stat_card a\r\n"
				+ "	LEFT JOIN attend.dev_device b ON a.ng_dev_id = b.ng_id\r\n"
				+ "	LEFT JOIN attend.sys_branch c ON a.ng_branch_id = c.ng_id \r\n" + "WHERE\r\n"
				+ "	a.ng_user_id IN ( SELECT d.ng_id FROM attend.sys_user d WHERE d.sz_card_id = ? ) \r\n"
				+ "	AND a.ts_card >= str_to_date( ?, '%Y%m%d' ) \r\n"
				+ "	AND a.ts_card < str_to_date( ?, '%Y%m%d' ) \r\n" + "ORDER BY\r\n" + "	a.ts_card";
		return jdbc.queryForList(sql, new Object[] { idcdno, startdate, enddate });
	}

	public void truncateLog(String day) {
		String sql = "truncate GLOG" + day;
		jdbc.execute(sql);
		return;
	}

	public List<Map<String, Object>> qryBslistbysvdp(String svdp) {
		String sql = "select APCD,BSCD from SYBSCD where SVDP=?";
		return jdbc.queryForList(sql, new Object[] { svdp });
	}

	public List<Map<String, Object>> qryBslistbybkcd(String bkcd) {
		String sql = "select APCD,BSCD from SYBSCD where BKCD=?";
		return jdbc.queryForList(sql, new Object[] { bkcd });
	}
	
	public List<Map<String,Object>> qryFullbslistbybkcd(String bkcd){
		String sql="select APCD,BSCD from SYBSCD where BKCD in (" + getAllbkcdbybkcd(bkcd) + ")";
		return jdbc.queryForList(sql);
	}
	
	public String getAllbkcdbybkcd(String bkcd) {
		String sql="select SYIDTB bkcd from SYCDTB where SYCDTB='BKCD' and SYC3TB='" + bkcd + "'";
		String result="'" + bkcd + "'";
		List<Map<String,Object>> lbs=jdbc.queryForList(sql);
		if (lbs.size()==0) {
			return result;
		}else {
			for (Map<String,Object> mbs:lbs) {
				String bs=getAllbkcdbybkcd(Code.getFieldVal(mbs, "bkcd", ""));
				result=result +  "," + bs;
			}
		}
		return result;
	}

	public List<Map<String, Object>> qryBslistbycccd(String cccd) {
		String sql = "select APCD,BSCD from SYBSCD where CCCD=?";
		return jdbc.queryForList(sql, new Object[] { cccd });
	}

	public List<Map<String, Object>> qryHisBsbl() {
		String sql = "SELECT\r\n" + "	APCD,\r\n" + "	BSCD,\r\n" + "	sum( IF ( SPANBL < RQANBL, 1, 0 ) ) NOBL \r\n"
				+ "FROM\r\n" + "	SPANBL \r\n" + "GROUP BY\r\n" + "	APCD,\r\n" + "	BSCD";
		return jdbc.queryForList(sql);
	}

	public List<Map<String, Object>> qryHisBscoll(String cfbl) {
		String sql = "SELECT\r\n" + "	APCD,\r\n" + "	BSCD,\r\n" + "	sum( QXRS ) QXRS,\r\n"
				+ "	sum( IF ( CFBL > ?, 1, 0 ) ) CFBL \r\n" + "FROM\r\n" + "	BSCOLLINFO \r\n" + "GROUP BY\r\n"
				+ "	APCD,\r\n" + "	BSCD";
		return jdbc.queryForList(sql, new Object[] { cfbl });
	}

	public List<Map<String, Object>> qryBscdbyrgdt(String startdate, String enddate) {
		String sql = "select APCD,BSCD from SYBSCD" + " where RGDT>=str_to_date(?,'%Y%m%d%H%i%s')"
				+ " and RGDT<=str_to_date(?,'%Y%m%d%H%i%s')";
		return jdbc.queryForList(sql, new Object[] { startdate, enddate });
	}

	public int getMaxngid() {
		String sql = "select max(ngid) as maxid from wkerattendtime";
		List<Map<String, Object>> list = jdbc.queryForList(sql);

		if (list.size() > 0) {
			return Integer.parseInt(Code.getFieldVal(list.get(0), "maxid", "0"));
		} else {
			return 0;
		}
	}

	public List<Map<String, Object>> qryAttenddata(int ngid) {
		String sql = "select date_format(a.ts_card,'%Y%m%d%H%i%s') as attendtime,b.sz_set_id as branchid,a.sz_employ_id,a.sz_dev_place,a.apcd,a.ng_id"
				+ " from attend.stat_card a left join attend.sys_branch b on a.ng_branch_id = b.ng_id where a.ng_id>=?";
		return jdbc.queryForList(sql, new Object[] { ngid });
	}

	public List<Map<String, Object>> qryWkerattendtime(String apcd, String bscd, String wkerid) {
		String sql = "select * from wkerattendtime where APCD=? and BSCD=? and SZ_EMPLOY_ID=?";
		return jdbc.queryForList(sql, new Object[] { apcd, bscd, wkerid });
	}

	public int insertWkerattendtime(Wkerattendtime wktm) {
		String sql = "insert into wkerattendtime(APCD,BSCD,IDCDNO,SZ_EMPLOY_ID,WKKD,BZ,INTIME,OUTTIME,NGID)"
				+ " values(?,?,?,?,?,?,?,?,?)";
		return jdbc.update(sql, new Object[] { wktm.getAPCD(), wktm.getBSCD(), wktm.getIDCDNO(), wktm.getSZ_EMPLOY_ID(),
				wktm.getWKKD(), wktm.getBZ(), wktm.getINTIME(), wktm.getOUTTIME(), wktm.getNGID() });
	}

	public int updateWkerattendtime(Wkerattendtime wktm) {
		String sql = "update wkerattendtime set WKKD=?,BZ=?,INTIME=?,OUTTIME=?,NGID=?"
				+ " where APCD=? and BSCD=? and SZ_EMPLOY_ID=?";
		return jdbc.update(sql, new Object[] { wktm.getWKKD(), wktm.getBZ(), wktm.getINTIME(), wktm.getOUTTIME(),
				wktm.getNGID(), wktm.getAPCD(), wktm.getBSCD(), wktm.getSZ_EMPLOY_ID() });
	}

	public List<Map<String, Object>> qryUploadconfig(String apcd, String bscd, String method) {
		String sql = "select * from uploadconfig where APCD=? and BSCD=? and METHOD=?";
		return jdbc.queryForList(sql, new Object[] { apcd, bscd, method });
	}

	public void insertBankrecord(String userid, JSONObject jo) {
		String sql = "insert into bankrecord(APCD,BSCD,RECORDID,BKCD,ACCOUNT,RECORDTIME,STORENUM,PAYNUM,REMARK,DTDT,DTUSER)"
				+ " values(?,?,?,?,?,?,?,?,?,now(),?)";
		jdbc.update(sql, new Object[] { Code.getFieldVal(jo, "APCD", ""), Code.getFieldVal(jo, "BSCD", ""),
				Code.getFieldVal(jo, "RECORDID", ""), Code.getFieldVal(jo, "BKCD", ""),
				Code.getFieldVal(jo, "ACCOUNT", ""), Code.getFieldVal(jo, "RECORDTIME", ""),
				Float.parseFloat(Code.getFieldVal(jo, "STORENUM", "")),
				Float.parseFloat(Code.getFieldVal(jo, "PAYNUM", "")), Code.getFieldVal(jo, "REMARK", ""), userid });
	}

	public int delBankrecord(String recordid) {
		String sql = "delete from bankrecord where RECORDID=?";
		return jdbc.update(sql, new Object[] { recordid });
	}

	public List<Map<String, Object>> qryUploadtotalattend(String sqnb) {
		String sql = "SELECT	sum( a.WKDS ) as TOTAL,	a.IDCDNO FROM wkds a"
				+ " WHERE ( a.APCD, a.BSCD, a.IDCDNO ) IN ( SELECT b.APCD, b.BSCD, b.IDCDNO FROM acpyitem b WHERE b.SQNB = ? )"
				+ " group by a.IDCDNO";
		return jdbc.queryForList(sql, new Object[] { sqnb });
	}

	public List<Map<String, Object>> qryUploadtotalsalary(String sqnb) {
		String sql = "SELECT sum( a.ACPY ) as TOTAL,a.IDCDNO FROM acpyitem a WHERE	INBKCFM = 'Y'"
				+ " and ( a.APCD, a.BSCD, a.IDCDNO ) IN ( SELECT b.APCD, b.BSCD, b.IDCDNO FROM acpyitem b WHERE b.SQNB =? group by a.IDCDNO)"
				+ " group by a.IDCDNO";
		return jdbc.queryForList(sql, new Object[] { sqnb });
	}

	public List<Map<String, Object>> qryUploadwkds(String sqnb) {
		String sql = "SELECT a.IDCDNO, a.WKDS FROM wkds a WHERE	( a.APCD, a.BSCD, a.IDCDNO ) IN ( SELECT b.APCD, b.BSCD, b.IDCDNO FROM acpyitem b WHERE b.SQNB = ? )";
		return jdbc.queryForList(sql, new Object[] { sqnb });
	}

	public List<Map<String, Object>> qryAttenddevfeature(int userngid) {
		String sql = "select * from attend.dev_feature where ng_user_id=?";
		return jdbc.queryForList(sql, new Object[] { userngid });
	}

	public void insertAttenddevfeature(int userngid) {
		String sql = "insert into attend.dev_feature(ng_user_id,sz_dev_type,sz_algorithm_edition,ba_feature,sz_check_type,ts_create)"
				+ " values(?,'C340A','3.1',' ','人脸',now())";
		jdbc.update(sql, new Object[] { userngid });
	}

	public String hasBankcard(String idcdno, String bkan) {
		String sql = "select * from wker_bs where idcdno<>? and bkan=?";
		List<Map<String, Object>> lwk = jdbc.queryForList(sql, new Object[] { idcdno, bkan });
		if (lwk.size() > 0) {
			return "该银行卡已被使用";
		}
		return "Y";
	}

	public void insertYfdevice(Yfdevice dev) {
		String sql = "insert into yfdevice(DEVICEKEY,SN,DEVICENAME,APCD,BSCD,ININOUT,DEVICETYPE,OPUR,OPDT)"
				+ " values(?,?,?,?,?,?,?,?,now())";
		jdbc.update(sql, new Object[] { dev.getDEVICEKEY(), dev.getSN(), dev.getDEVICENAME(), dev.getAPCD(),
				dev.getBSCD(), dev.getININOUT(), dev.getDEVICETYPE(), dev.getOPUR() });
		return;
	}

	public int updateYfdevice(Yfdevice dev) {
		String sql = "update yfdevice set SN=?,DEVICENAME=?,APCD=?,BSCD=?,ININOUT=?,DEVICETYPE=?,OPUR=?,OPDT=now()"
				+ " where DEVICEKEY=?";
		return jdbc.update(sql, new Object[] { dev.getSN(), dev.getDEVICENAME(), dev.getAPCD(), dev.getBSCD(),
				dev.getININOUT(), dev.getDEVICETYPE(), dev.getOPUR(), dev.getDEVICEKEY() });
	}

	public int delYfdevice(String devsn) {
		String sql = "delete from yfdevice where DEVICEKEY=?";
		return jdbc.update(sql, new Object[] { devsn });
	}

	public List<Map<String, Object>> qryWkerbyyf(String guid) {
		String sql = "select * from wker where PERSONGUID=?";
		return jdbc.queryForList(sql, new Object[] { guid });
	}

	public String getIdcdnobyyf(String guid) {
		List<Map<String, Object>> l = qryWkerbyyf(guid);
		if (l.size() > 0) {
			return Code.getFieldVal(l.get(0), "IDCDNO", "");
		} else {
			return "";
		}
	}

	public List<Map<String, Object>> qryDevinfobydevsn(String devsn) {
		String sql = "select * from yfdevice where DEVICEKEY=?";
		return jdbc.queryForList(sql, new Object[] { devsn });
	}

	public String getBscdbyyf(String devsn) {
		List<Map<String, Object>> l = qryDevinfobydevsn(devsn);
		if (l.size() > 0) {
			return Code.getFieldVal(l.get(0), "BSCD", "");
		} else {
			return "";
		}
	}
	
	public List<Map<String,Object>> qryBslc(){
		String sql="select * from bs_lc";
		return jdbc.queryForList(sql);
	}
	
	public void insertYftoken(String appid, String token) {
		String sql = "insert into yftoken(APPID,TOKEN,UPDATETIME) values(?,?,now())";
		jdbc.update(sql, new Object[] { appid, token });
	}

	public void delYftoken(String appid) {
		String sql = "delete from yftoken where APPID=?";
		jdbc.update(sql, new Object[] { appid });
	}
	
	public List<Map<String,Object>> qryYftoken(String appid){
		String sql = "select * from yftoken where APPID=?";
		return jdbc.queryForList(sql,new Object[] {appid});
	}
	
	public int updateWkeruppath(String idcdno,String uppath) {
		String sql="update wker set uppath=? where idcdno=?";
		return jdbc.update(sql,new Object[] {uppath,idcdno});
	}

}
