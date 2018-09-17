package cn.com.sailin.falconweb.model;

public class Uploaddata {
	
	private String user;
	
	private String pass;
	
	private String content;
	
	private String url;
	
	private static String host="http://api.pm361.cn/";
	
	public static Uploaddata buildObject(String method) {
		Uploaddata data=new Uploaddata();
		//工地信息
		if (method.equals("uploadproject")) {
			data.user = "18080420Vg84CN";
			data.pass = "FXEeU5D3ce";
			data.url = host + "ZHGD/interfaceBatch/batch_uploaddw_aj_d_project.do";			
		}
		//上传工人信息
		if (method.equals("uploadhuman")) {
			data.user = "18080420fiQFuL";
			data.pass = "0W0Nr1RQ0o";
			data.url = host + "ZHGD/interfaceBatch/batch_uploaddw_aj_f_human_worker.do";
		}
		//删除工人信息
		if (method.equals("deletehuman")) {
			data.user = "18080420fiQFuL";
			data.pass = "0W0Nr1RQ0o";
			data.url = host + "ZHGD/interfaceBatch/batch_deletedw_aj_f_human_worker.do";
		}
		//上传考勤数据
		if (method.equals("uploadattend")) {
			data.user="18080420cpzN9F";
			data.pass="ZE84k1d0gz";
			data.url = host + "ZHGD/interfaceBatch/batch_uploaddw_aj_f_human_worker_attendance.do";
		}
		//上传经历信息
		if (method.equals("uploadexprience")) {
			data.user = "18080420A9PVqn";
			data.pass="x2WPpv13Nx";
			data.url = host + "ZHGD/interfaceBatch/batch_uploaddw_aj_f_worker_exprience.do";
		}
		//删除经历信息
		if (method.equals("deleteexprience")) {
			data.user = "18080420A9PVqn";
			data.pass="x2WPpv13Nx";
			data.url = host + "ZHGD/interfaceBatch/batch_deletedw_aj_f_worker_exprience.do";
		}
		//上传培训信息
		if (method.equals("uploadtrain")) {
			data.user="18080420A9PVqn";
			data.pass="x2WPpv13Nx";
			data.url=host + "ZHGD/interfaceBatch/batch_uploaddw_aj_f_worker_train.do";
		}
		//删除培训信息
		if (method.equals("deletetrain")) {
			data.user="18080420A9PVqn";
			data.pass="x2WPpv13Nx";
			data.url=host + "ZHGD/interfaceBatch/batch_deletedw_aj_f_worker_train.do";
		}
		//上传薪酬信息
		if (method.equals("uploadsalary")) {
			data.user="180804200GP1Lz";
			data.pass="CHwnNmua2X";
			data.url = host + "ZHGD/interfaceBatch/batch_uploaddw_aj_f_worker_salary.do";
		}
		//上传银行流水
		if (method.equals("uploadbank")) {
			data.user="18080420GAHJ2M";
			data.pass="PBvtNOZwYo";
			data.url = host + "ZHGD/interfaceBatch/batch_uploaddw_aj_f_project_bank_transation.do";
		}
		return data;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
