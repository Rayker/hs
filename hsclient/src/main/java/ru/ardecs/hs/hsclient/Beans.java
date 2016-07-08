package ru.ardecs.hs.hsclient;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

@Component
public class Beans {
	@Bean
	public CloseableHttpClient httpClient() {
		return HttpClients.createDefault();
	}

	@Bean
	public Session getMailSession(@Qualifier("app.props") Properties properties,
	                              @Value("${mail.from.username}") String username,
	                              @Value("${mail.from.password}") String password) {
		return Session.getInstance(properties,
				new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
	}

	@Bean(name = "app.props")
	public Properties getApplicationProperties() throws IOException {
		ClassPathResource resource = new ClassPathResource("application.properties");
		Properties properties = new Properties();
		properties.load(resource.getInputStream());
		return properties;
	}

	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/ws/*");
	}

	@Bean(name = "cityStatistic")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema cityStatistic) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("CityStatisticPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://localhost:8080/wsdl/cityStatistic.wsdl");
		wsdl11Definition.setSchema(cityStatistic);
		return wsdl11Definition;
	}

	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
//		factory.setTrustedPackages(Arrays.asList("ru.ardecs.hs.hscommon.soap.generated"));
		factory.setTrustAllPackages(true);
		return factory;
	}

//	@Bean
//	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
//		DefaultJmsListenerContainerFactory factory =
//				new DefaultJmsListenerContainerFactory();
//		factory.setConnectionFactory(connectionFactory());
//		factory.setDestinationResolver(destinationResolver());
//		factory.setConcurrency("3-10");
//		return factory;
//	}

//	@Bean
//	public XsdSchema countriesSchema() {
//		return new SimpleXsdSchema(new ClassPathResource("statistic.xsd"));
//	}
}
