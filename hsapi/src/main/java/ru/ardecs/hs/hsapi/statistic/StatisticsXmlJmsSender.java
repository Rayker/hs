package ru.ardecs.hs.hsapi.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.xml.transform.StringResult;
import ru.ardecs.hs.hscommon.signing.KeyLoader;
import ru.ardecs.hs.hscommon.signing.Signer;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class StatisticsXmlJmsSender implements StatisticsSender {
	private static final Logger logger = LoggerFactory.getLogger(StatisticsXmlJmsSender.class);

	@Autowired
	private Signer signer;

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@Autowired
	private Jaxb2Marshaller marshaller;

	@Value("${application.jms.xml-destination}")
	private String destination;

	private Signature signature;

	public StatisticsXmlJmsSender() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException {
		PrivateKey privateKey = new KeyLoader().loadKeyPair("hscommon/src/main/resources", "DSA").getPrivate();
		signature = Signature.getInstance("DSAwithSHA1");
		signature.initSign(privateKey);
	}

	@Override
	public void sendCityStatisticRequest(SendCityStatisticRequest cityStatistic) {
		logger.debug("sendCityStatisticRequest(): convert to xml");
		StringResult stringResult = new StringResult();

		try {
			marshaller.marshal(cityStatistic, stringResult);
		} catch (XmlMappingException e) {
			logger.error("Marshaling SendCityStatisticRequest error", e);
			return;
		}
		String xml = stringResult.toString();

		logger.debug("sendCityStatisticRequest(): signing");
		byte[] realSignature;
		try {
			signature.update(xml.getBytes(StandardCharsets.UTF_8));
			realSignature = signature.sign();
		} catch (SignatureException e) {
			logger.error("Signature exception", e);
			return;
		}

		Map<String, Object> headers = new HashMap<>();
		headers.put("signature", Base64.getEncoder().encodeToString(realSignature));

		logger.debug("sendCityStatisticRequest(): sending message");
		jmsMessagingTemplate.convertAndSend(destination, xml, headers);

		logger.debug("sendCityStatisticRequest(): success");
	}
}
