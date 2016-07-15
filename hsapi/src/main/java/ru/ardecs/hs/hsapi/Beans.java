package ru.ardecs.hs.hsapi;

import org.apache.wss4j.common.crypto.Crypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.support.CryptoFactoryBean;
import ru.ardecs.hs.hsapi.bl.ScheduleFactory;
import ru.ardecs.hs.hsapi.cache.MemoryCacheManager;
import ru.ardecs.hs.hsapi.statistic.StatisticsSoapSender;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;



@Component
@ImportResource({"classpath:spring-ws-context.xml"})
public class Beans {
	@Bean
	public StatisticsSoapSender statisticsSoapSender(Jaxb2Marshaller marshaller, Wss4jSecurityInterceptor interceptor) {
		StatisticsSoapSender sender = new StatisticsSoapSender();
		sender.setInterceptors(new Wss4jSecurityInterceptor[]{interceptor});
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

//	@Bean
//	public Crypto crypto() throws Exception {
//		CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
//		cryptoFactoryBean.setKeyStoreLocation(new ClassPathResource("keystore.jks"));
//		cryptoFactoryBean.setKeyStorePassword("testpass");
//		return cryptoFactoryBean.getObject();
//	}
//
//	@Bean
//	public Wss4jSecurityInterceptor wsClientSecurityInterceptor(Crypto crypto) {
//		Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();
//		interceptor.setSecurementActions("Signature");
//		interceptor.setSecurementUsername("soap");
//		interceptor.setSecurementPassword("testpass");
//		interceptor.setSecurementSignatureAlgorithm("DSAwithSHA1");
//		interceptor.setSecurementSignatureCrypto(crypto);
////		interceptor.setSecurementEncryptionCrypto(crypto);
////		interceptor.setSecurementEncryptionParts();
//		return interceptor;
//	}
//
//	@Bean
//	public WebServiceTemplate webServiceTemplate(Wss4jSecurityInterceptor wss4jSecurityInterceptor) {
//		WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
//		webServiceTemplate.setInterceptors(new Wss4jSecurityInterceptor[]{wss4jSecurityInterceptor});
//		return webServiceTemplate;
//	}
}
