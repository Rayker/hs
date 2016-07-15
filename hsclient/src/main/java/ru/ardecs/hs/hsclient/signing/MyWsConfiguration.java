package ru.ardecs.hs.hsclient.signing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;

import java.util.List;

@Configuration
@EnableWs
public class MyWsConfiguration extends WsConfigurerAdapter {
	@Autowired
	private Wss4jSecurityInterceptor interceptor;

	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) {
		interceptors.add(interceptor);
	}

}