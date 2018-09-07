package cn.com.sailin.compassweb.config;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws22.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.com.sailin.compassweb.service.WebserviceImpl;
import cn.com.sailin.compassweb.service.WebserviceInterface;

@SuppressWarnings("deprecation")
@Configuration
public class WebserviceConfig {
	
	@Bean
    public ServletRegistrationBean dispatcherServlet() {
        return new ServletRegistrationBean(new CXFServlet(),"/*");
    }
    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }
    @Bean
    public WebserviceInterface ws() {
        return new WebserviceImpl();
    }
	@Bean
    public Endpoint endpoint() {
		EndpointImpl endpoint = new EndpointImpl(springBus(),ws());
        endpoint.publish("/ws");
        return endpoint;
    }

}
