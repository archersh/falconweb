package cn.com.sailin.falconweb.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface WebserviceInterface {

	@WebMethod
	public String invoke(
			@WebParam(name = "userid") String userid, 
			@WebParam(name = "timestamp") String timestamp,
			@WebParam(name = "token") String token, 
			@WebParam(name = "method") String method,
			@WebParam(name = "applydata") String applydata);
	
	@WebMethod
	public String webinvoke(
			@WebParam(name = "userid") String userid, 
			@WebParam(name = "timestamp") String timestamp,
			@WebParam(name = "token") String token, 
			@WebParam(name = "method") String method,
			@WebParam(name = "applydata") String applydata);
	
	@WebMethod
	public String postbytes(
			@WebParam(name="userid") String userid,
			@WebParam(name="timestamp") String timestamp,
			@WebParam(name="token") String token,
			@WebParam(name="applydata")String applydata,
			@WebParam(name="bytes") byte[] bytes);
}
