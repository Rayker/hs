package ru.ardecs.hs.city.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.xml.transform.StringResult;
import ru.ardecs.hs.common.soap.generated.SendCityStatisticRequest;

import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

@Component
public class StatisticsXmlJmsSender implements StatisticsSender {
	private static final Logger logger = LoggerFactory.getLogger(StatisticsXmlJmsSender.class);

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@Autowired
	private Jaxb2Marshaller marshaller;

	@Value("${application.jms.xml-destination}")
	private String destination;

	@Autowired
	private Signature signature;

	@Override
	public void sendCityStatisticRequest(SendCityStatisticRequest cityStatistic) {
		byte[] realSignature;
		String xml;
		try {
			logger.debug("sendCityStatisticRequest(): convert to xml");
			StringResult stringResult = new StringResult();
			marshaller.marshal(cityStatistic, stringResult);
			xml = stringResult.toString();

			logger.debug("sendCityStatisticRequest(): signing");
			signature.update(xml.getBytes(StandardCharsets.UTF_8));
			realSignature = signature.sign();
		} catch (XmlMappingException e) {
			logger.error("Marshaling SendCityStatisticRequest error", e);
			return;
		} catch (SignatureException e) {
			logger.error("Signature exception", e);
			return;
		}

		Map<String, Object> headers = Collections.singletonMap(
				"signature",
				Base64.getEncoder().encodeToString(realSignature));

		logger.debug("sendCityStatisticRequest(): sending message");
		jmsMessagingTemplate.convertAndSend(destination, xml, headers);

		logger.debug("sendCityStatisticRequest(): success");
	}
}
