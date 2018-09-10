package cn.com.sailin.compassrest.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;

import cn.com.sailin.compassrest.model.Outready;
import cn.com.sailin.compassrest.model.Paydetails;
import cn.com.sailin.compassrest.model.Planoutcn;
import cn.com.sailin.compassrest.model.Upphotolist;
import cn.com.sailin.compassrest.publiccode.Code;
import oracle.jdbc.internal.OracleTypes;

@Repository
public class Data {

	@Autowired
	private JdbcTemplate jdbc;

	public JdbcTemplate getJdbc() {
		return jdbc;
	}

	public List<Map<String, Object>> qryPlanoutcn(String gcpguid) {
		String sql = "select * from sf_planoutcn where gcpguid=?";
		return jdbc.queryForList(sql, new Object[] { gcpguid });
	}

	public void delPlanoutcn(String gcpguid) {
		String sql = "delete from sf_planoutcn where gcpguid=?";
		jdbc.update(sql, new Object[] { gcpguid });
	}

	public void insertPlanoutcn(Planoutcn cntr) {
		String sql = "insert into sf_planoutcn(gcpguid,cycode,rvn,truck,truck2,driver,drivertel,"
				+ "pickuptype,cntype,surchargebarcode,shipping,bn,eir,vsl,voy,dzfxh,remark,inva,inresult)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Y','')";
		jdbc.update(sql,
				new Object[] { cntr.getGcpguid(), cntr.getCycode(), cntr.getRvn(), cntr.getTruck(), cntr.getTruck2(),
						cntr.getDriver(), cntr.getDrivertel(), cntr.getPickuptype(), cntr.getCntype(),
						cntr.getSurchargebarcode(), cntr.getShipping(), cntr.getBn(), cntr.getEir(), cntr.getVsl(),
						cntr.getVoy(), cntr.getDzfxh(), cntr.getRemark() });
	}

	public int updatePlanoutcninresult(String inresult) {
		String sql = "update sf_planoutcn set inva='N',inresult=?";
		return jdbc.update(sql, new Object[] { inresult });
	}

	public void delUpphotolist(String gcpguid) {
		String sql = "delete from sf_upphotolist where gcpguid=?";
		jdbc.update(sql, new Object[] { gcpguid });
	}

	public void insertUpphotolist(Upphotolist pt) {
		String sql = "insert into sf_upphotolist(gcpguid,photoguid,photourl,photoslturl,uploaddate)"
				+ " values(?,?,?,?,?)";
		jdbc.update(sql, new Object[] { pt.getGcpguid(), pt.getPhotourl(), pt.getPhotoslturl(), pt.getUploaddate() });
	}

	public void delPaydetails(String gcpguid) {
		String sql = "delete from sf_paydetails where gcpguid=?";
		jdbc.update(sql, new Object[] { gcpguid });
	}

	public void insertPaydetails(Paydetails py) {
		String sql = "insert into sf_paydetails(gcpguid,paydetailsguid,shno,payno,chargename,amount,paymodel,paydate,payopenid)"
				+ " values(?,?,?,?,?,?,?,?,?)";
		jdbc.update(sql, new Object[] { py.getGcpguid(), py.getPaydetailsguid(), py.getShno(), py.getPayno(),
				py.getChargename(), py.getAmount(), py.getPaymodel(), py.getPaydate(), py.getPayopenid() });

	}

	public void delOutready(String gcpguid) {
		String sql = "delete from sf_outready where gcpguid=?";
		jdbc.update(sql, new Object[] { gcpguid });
	}

	public void insertOutread(Outready or) {
		String sql = "insert into sf_outready(gcpguid,rvn,cycode) values(?,?,?)";
		jdbc.update(sql, new Object[] { or.getGcpguid(), or.getRvn(), or.getCycode() });
	}

	public List<Map<String, Object>> qryUserinfo(String userid) {
		String sql = "select * from ac_userinfo where trim(userid)=?";
		return jdbc.queryForList(sql, new Object[] { userid });
	}

	public List<Map<String, Object>> qryRp_repairitemcodebycodetype(String codetype) {
		String sql = "select * from rp_repairitemcode where codetype=?";
		return jdbc.queryForList(sql, new Object[] { codetype });
	}

	public List<Map<String, Object>> qryRp_repairitempurpose() {
		String sql = "select purposecode,purposename from rp_repairitempurpose";
		return jdbc.queryForList(sql);
	}
	
	public List<Map<String,Object>> qryCtnsizetype(){
		String sql = "select * from ctn_size_type order by ctnsizetype";
		return jdbc.queryForList(sql);
	}

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
		String sqlresult = "";
		if (result.length() > 1000)
			sqlresult = result.substring(0, 999);
		else
			sqlresult = result;
		String sql = "update glog" + day + " set result='" + sqlresult + "', retime=sysdate" + " where processid="
				+ pid;
		try {
			jdbc.execute(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getRepairfee(String lineCode, String repairCode, String placeCode, String repairPos,
			String repairSize, String workHoure, String szty) {
		String sql = "select f_getrepairfee(?,?,?,?,?,?,?) as result from dual";
		List<Map<String, Object>> list = jdbc.queryForList(sql,
				new Object[] { lineCode, repairCode, placeCode, repairPos, repairSize, workHoure, szty });
		return Code.getFieldVal(list.get(0), "result", "");
	}

	public String getRepairid(String cntrid) {
		String sql = "select f_getrepairid(?) as repairid from dual";
		List<Map<String, Object>> list = jdbc.queryForList(sql,new Object[] {cntrid});
		return Code.getFieldVal(list.get(0), "repairid", "");
	}

	public JSONObject toPlsql(final String msghead, final String usercode, final String cntr, final String msgin) {
		final JSONObject obj = new JSONObject();
		jdbc.execute(new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection conn) throws SQLException {
				String storedProc = "{call p_toplsql(?,?,?,?,?,?,?)}";
				CallableStatement cs = conn.prepareCall(storedProc);
				cs.setString(1, msghead);
				cs.setString(2, usercode);
				cs.setString(3, cntr);
				cs.setString(4, msgin);
				cs.registerOutParameter(5, OracleTypes.VARCHAR);
				cs.registerOutParameter(6, OracleTypes.VARCHAR);
				cs.registerOutParameter(7, OracleTypes.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<Object>() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.execute();
				obj.put("flag", cs.getString(5));
				obj.put("errmsg", cs.getString(6));
				obj.put("outmsg", cs.getString(7));
				return obj;
			}
		});
		return obj;
	}

	public void insertUploadfile(String repairId, String fileName) {
		String sql = "insert into rp_uploadfile(repairid,filename,uptime) values(?,?,to_char(sysdate,'YYYYMMDDHH24MISS'))";
		jdbc.update(sql, new Object[] { repairId, fileName });
	}
	
	public void deleteUploadfile(String repairId,String fileName) {
		String sql="delete from rp_uploadfile where repairid=? and filename=?";
		jdbc.update(sql,new Object[] {repairId,fileName});
	}

}
