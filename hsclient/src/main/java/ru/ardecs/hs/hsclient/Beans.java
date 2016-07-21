package ru.ardecs.hs.hsclient;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.ws.security.components.crypto.Crypto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.security.support.KeyStoreFactoryBean;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j.support.CryptoFactoryBean;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.XsdSchema;
import ru.ardecs.hs.hsclient.db.entities.CityApi;
import ru.ardecs.hs.hsclient.db.repositories.CityApiRepository;
import ru.ardecs.hs.hsclient.signing.SignatureProviderImpl;
import ru.ardecs.hs.common.signing.SignatureFactory;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.security.KeyStore;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
	public ActiveMQConnectionFactory activeMQConnectionFactory(
			@Value("${spring.activemq.broker-url}") String brokerUrl,
			@Value("${spring.activemq.user}") String user,
			@Value("${spring.activemq.password}") String password) {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(user, password, brokerUrl);
//		factory.setTrustedPackages(Arrays.asList("ru.ardecs.hs.common.soap.generated"));
		factory.setTrustAllPackages(true);
		return factory;
	}

	@Bean
	public KeyStoreFactoryBean trustStoreFactoryBean(
			@Value("${application.security.truststore.password}") String password,
			@Value("${application.security.truststore.location}") String location
	) {
		KeyStoreFactoryBean keyStoreFactoryBean = new KeyStoreFactoryBean();
		keyStoreFactoryBean.setLocation(new ClassPathResource(location));
		keyStoreFactoryBean.setPassword(password);
		keyStoreFactoryBean.setType(KeyStore.getDefaultType()); // TODO: 7/16/16 change?
		return keyStoreFactoryBean;
	}

	@Bean
	public SignatureProviderImpl signatureProvider(CityApiRepository cityApiRepository, SignatureFactory signatureFactory) throws CertificateException {
		Map<Long, Signature> signatures = StreamSupport
				.stream(cityApiRepository.findAll().spliterator(), false)
				.collect(Collectors.toConcurrentMap(
						CityApi::getId,
						c -> signatureFactory.createVerificationSignature(c.getId())));
		return new SignatureProviderImpl(signatures);
	}

	@Bean
	public CryptoFactoryBean serverCrypto(
			@Value("${application.security.truststore.password}") String password,
			@Value("${application.security.truststore.location}") String location
	) throws IOException {
		CryptoFactoryBean factory = new CryptoFactoryBean();
		factory.setKeyStorePassword(password);
		factory.setKeyStoreLocation(new ClassPathResource(location));
		return factory;
	}

	@Bean
	public Wss4jSecurityInterceptor wss4jSecurityInterceptor(Crypto crypto) {
		Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();
		interceptor.setValidationActions("Signature");
		interceptor.setValidationSignatureCrypto(crypto);
		return interceptor;
	}
}
