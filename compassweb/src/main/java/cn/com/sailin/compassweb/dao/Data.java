package cn.com.sailin.compassweb.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import cn.com.sailin.compassweb.model.DataCtnTkDriver;
import cn.com.sailin.compassweb.model.DataCtntk;
import cn.com.sailin.compassweb.model.DataCtntkReleaseDetail;
import cn.com.sailin.compassweb.publiccode.Code;
import oracle.jdbc.OracleTypes;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository
public class Data {

	@Autowired
	private JdbcTemplate jdbc;

	public String getPid() {
		String sql = "select processid.nextval as pid from dual";
		try {
			List<Map<String, Object>> list = null;
			list = jdbc.queryForList(sql);
			return list.get(0).get("pid").toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public boolean insertLog(String day, String pid, String pname, String logtext) {
		String sql = "insert into glog" + day + "(processid,usercode,systime,programname,logtext)" + " values(" + pid
				+ ",'ITD',sysdate,'" + pname + "','" + logtext + "')";
		try {
			jdbc.execute(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateLog(String day, String pid, String result) {
		String sql = "update glog" + day + " set result='" + result + "', retime=sysdate" + " where processid=" + pid;
		try {
			jdbc.execute(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String readResultinfo(String pid) {
		String sql = "select resultinfo from pg_resultinfo where pid=?";
		List<Map<String, Object>> lr = jdbc.queryForList(sql, new Object[] { pid });
		if (lr.size() > 0) {
			return Code.getFieldVal(lr.get(0), "resultinfo", "");
		} else {
			return "";
		}
	}

	public void insertResultinfo(String pid, String resultinfo) {
		String sql = "insert into pg_resultinfo(pid,resultinfo)" + " values(?,?)";
		jdbc.update(sql, new Object[] { pid, resultinfo });
	}

	public String readProcessid() {
		String sql = "select processid.nextval as proid from dual";
		List<Map<String, Object>> lr = jdbc.queryForList(sql);
		if (lr.size() > 0) {
			return Code.getFieldVal(lr.get(0), "proid", "");
		} else {
			return "";
		}
	}

	public List<Map<String, Object>> qryctntkbyid(String pid) {
		String sql = "select * from pg_ctntk where pid=?";
		return jdbc.queryForList(sql, new Object[] { pid });
	}

	public List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = null;
		String sql = "select * from glog5";
		list = jdbc.queryForList(sql);
		return list;
	}

	public List<Map<String, Object>> qrySgdtcfm(String blno, String cntr) {
		String sql = "select dgrq,vessel,voyage,lcpt,ctowner,blno,ctszty,cntr,freeday,remark,backpos,jcmd,ccmd,to_char(cmftime,'YYYY-MM-DD HH24:MI:SS') as cmftime,cmfuser,yxr,uncode from ygt_sgdtcfm where blno=? and cntr=?";
		return jdbc.queryForList(sql, new Object[] { blno, cntr });
	}

	public void insertctnck(DataCtntk tk) {
		String sql = "insert into pg_ctntk(pid,jobid,opercode,plannumber,"
				+ "vesselnamee,vesselvoyage,vesseluncode,ctnoperatorcode,blno,transferport,destinationport,"
				+ "tkaddress,barcode,eirno,ctnsizetype,tkvaliditybegin,tkvalidity,remarks,"
				+ "subscribterm,subscribflag,reeferflag,hazardflag,odflag,damageflag,rsv1,rsv2,rsv3,rsv4,rsv5,rsv6,rsv7,rsv8,rsv9,rsv10)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbc.update(sql, new Object[] { tk.getID(), tk.getJobId(), tk.getOperCode(), tk.getPlanNumber(),
				tk.getVesselNameE(), tk.getVesselVoyage(), tk.getVesselUnCode(), tk.getCtnOperatorCode(), tk.getBlNo(),
				tk.getTransferPort(), tk.getDestinationPort(), tk.getTkAddress(), tk.getBarCode(), tk.getEirNo(),
				tk.getCtnSizeType(), tk.getTkValidityBegin(), tk.getTkValidity(), tk.getRemarks(), tk.getSubscribTerm(),
				tk.getSubscribFlag(), tk.getReeferFlag(), tk.getHazardFlag(), tk.getOdFlag(), tk.getDamagerFlag(),
				tk.getRSV1(), tk.getRSV2(), tk.getRSV3(), tk.getRSV4(), tk.getRSV5(), tk.getRSV6(), tk.getRSV7(),
				tk.getRSV8(), tk.getRSV9(), tk.getRSV10() });
	}

	public void insertReleaseDetail(String pid, DataCtntkReleaseDetail rd) {
		String sql = "insert into pg_ctntk_releasedetail(pid,ctnsizetype,releasenum,rsv1,rsv2,rsv3,rsv4,rsv5)"
				+ " values(?,?,?,?,?,?,?,?)";
		jdbc.update(sql, new Object[] { pid, rd.getCtnSizeType(), rd.getReleaseNum(), rd.getRsv1(), rd.getRsv2(),
				rd.getRsv3(), rd.getRsv4(), rd.getRsv5() });
	}

	public void insertTkDriver(String pid, DataCtnTkDriver tk) {
		String sql = "insert into pg_ctntk_tkdriver(pid,tksdtime,tkedtime,carjobnumber,cartrucklicense,drivername,drivertel,rsv1,rsv2,rsv3,rsv4,rsv5)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbc.update(sql,
				new Object[] { pid, tk.getTkSDTime(), tk.getTkEDTime(), tk.getCarJobnumber(), tk.getCarTrucklicense(),
						tk.getDriverName(), tk.getDriverTel(), tk.getRsv1(), tk.getRsv2(), tk.getRsv3(), tk.getRsv4(),
						tk.getRsv5() });
	}

	public Map<String, String> storedProcedure(final String msghead, final String usercode, final String cntr,
			final String msgin) {
		final HashMap<String, String> rm = new HashMap<String, String>();
		jdbc.execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "call p_toplsql(?,?,?,?,?,?,?)";
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, msghead);
				cs.setString(2, usercode);
				cs.setString(3, cntr);
				cs.setString(4, msgin);
				cs.registerOutParameter(5, OracleTypes.VARCHAR);
				cs.registerOutParameter(6, OracleTypes.VARCHAR);
				cs.registerOutParameter(7, OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.execute();
				rm.put("flag", cs.getString(5));
				rm.put("errmsg", cs.getString(6));
				rm.put("msgout", cs.getString(7));
				return rm;
			}

		});
		return rm;
	}

}
