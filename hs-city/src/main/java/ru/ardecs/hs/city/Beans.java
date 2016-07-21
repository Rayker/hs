package ru.ardecs.hs.city;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import ru.ardecs.hs.city.bl.ScheduleFactory;
import ru.ardecs.hs.city.cache.MemoryCacheManager;
import ru.ardecs.hs.city.statistic.StatisticsSoapSender;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;


@Component
public class Beans {
	@Bean
	public StatisticsSoapSender statisticsSoapSender(Jaxb2Marshaller marshaller, org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor interceptor) {
		StatisticsSoapSender sender = new StatisticsSoapSender();
		sender.setInterceptors(new ClientInterceptor[]{interceptor});
		sender.setDefaultUri("http://localhost:8080/ws");
		sender.setMarshaller(marshaller);
		sender.setUnmarshaller(marshaller);
		return sender;
	}

	@Bean
	public DatatypeFactory datatypeFactory() throws DatatypeConfigurationException {
		return DatatypeFactory.newInstance();
	}

	@Bean
	public JmsMessagingTemplate jmsMessagingTemplate(JmsTemplate jmsTemplate) {
		return new JmsMessagingTemplate(jmsTemplate);
	}

	@Bean
	public ScheduleFactory scheduleFactory(@Value("${application.schedule.visitInMinutes}") int visitInMinutes,
	                                       @Value("${application.schedule.timeFormatPattern}") String timeFormatPattern) {
		int visitItMilliseconds = visitInMinutes * 60 * 1000;
		return new ScheduleFactory(visitItMilliseconds, timeFormatPattern);
	}

	@Bean
	public MemoryCacheManager memoryCacheManager(@Value("${application.cache.expireTimeInMinutes}") int expireTimeInMinutes) {
		return new MemoryCacheManager(expireTimeInMinutes);
	}

	@Bean
	public KeyStore keyStore() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(Beans.class.getResourceAsStream("keystore.jks"), "testpass".toCharArray());
		return keyStore;
	}

	@Bean
	public Signature signature(KeyStore keyStore) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException, KeyStoreException, CertificateException, UnrecoverableKeyException {
		Key key = keyStore.getKey("soap", "testpass".toCharArray());
		PrivateKey privateKey = (PrivateKey) key;
		Signature signature = Signature.getInstance("DSAwithSHA1");
		signature.initSign(privateKey);
		return signature;
	}

	@Bean
	public org.springframework.ws.soap.security.wss4j.support.CryptoFactoryBean serverCrypto(
			@Value("${application.security.keystore.location}") String location,
			@Value("${application.security.keystore.password}") String password
	) throws IOException {
		org.springframework.ws.soap.security.wss4j.support.CryptoFactoryBean factory = new org.springframework.ws.soap.security.wss4j.support.CryptoFactoryBean();
		factory.setKeyStorePassword(password);
		factory.setKeyStoreLocation(new ClassPathResource(location));
		return factory;
	}

	@Bean
	public org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor wss4jSecurityInterceptor(
			@Value("${application.security.keystore.alias}") String alias,
			@Value("${application.security.keystore.password}") String password,
			org.apache.ws.security.components.crypto.Crypto crypto) {
		org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor interceptor =
				new org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor();
		interceptor.setSecurementActions("Signature");
		interceptor.setSecurementUsername(alias);
		interceptor.setSecurementPassword(password);
		interceptor.setSecurementSignatureCrypto(crypto);
		return interceptor;
	}

	// TODO: 7/15/16 update wss4j security
//	@Bean
//	public Wss4jSecurityInterceptor wss4jSecurityInterceptor(Crypto crypto) {
//		Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();
//		interceptor.setSecurementActions("Signature");
//		interceptor.setSecurementUsername("soap");
//		interceptor.setSecurementPassword("testpass");
//		interceptor.setSecurementSignatureCrypto(crypto);
//		return interceptor;
//	}
//
//	@Bean
//	public CryptoFactoryBean serverCryptoFactoryBean() throws IOException {
//		CryptoFactoryBean factory = new CryptoFactoryBean();
//		factory.setKeyStorePassword("testpass");
//		factory.setKeyStoreLocation(new ClassPathResource("keystore.jks"));
//		return factory;
//	}
}
