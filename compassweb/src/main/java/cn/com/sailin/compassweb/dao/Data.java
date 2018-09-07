package cn.com.sailin.compassweb.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

}
