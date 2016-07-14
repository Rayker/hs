package ru.ardecs.hs.hsapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hsapi.bl.ScheduleFactory;
import ru.ardecs.hs.hsapi.cache.MemoryCacheManager;
import ru.ardecs.hs.hsapi.statistic.StatisticsSoapSender;
import ru.ardecs.hs.hscommon.signing.KeyLoader;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;

@Component
public class Beans {
	@Bean
	public StatisticsSoapSender statisticsSoapSender(Jaxb2Marshaller marshaller) {
		StatisticsSoapSender sender = new StatisticsSoapSender();
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
	public Signature signature() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException {
		// TODO: 7/14/16 fix key loading
		PrivateKey privateKey = new KeyLoader().loadKeyPair("hscommon/src/main/resources", "DSA").getPrivate();
		Signature signature = Signature.getInstance("DSAwithSHA1");
		signature.initSign(privateKey);
		return signature;
	}
}
