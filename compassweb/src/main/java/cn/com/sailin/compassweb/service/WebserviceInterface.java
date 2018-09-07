package cn.com.sailin.compassweb.service;

import javax.jws.WebService;

@WebService
public interface WebserviceInterface {
	
	public String sayHello(String user);
	
	public String getData();
	
	public String CallService(String method,String applyData);

}
