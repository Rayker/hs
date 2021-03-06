package ru.ardecs.hs.central.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.xml.transform.StringSource;
import ru.ardecs.hs.central.signing.SignatureProvider;
import ru.ardecs.hs.common.soap.generated.SendCityStatisticRequest;

import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Component
public class XmlStatisticReceiver {
	private static Logger logger = LoggerFactory.getLogger(XmlStatisticReceiver.class);

	@Autowired
	private StatisticsService statisticsService;

	@Autowired
	private Jaxb2Marshaller marshaller;

	@Autowired
	private SignatureProvider signatureProvider;

	@JmsListener(destination = "${application.jms.xml-destination}")
	public void receiveMessage(String xml, @Header("signature") String encodedSignature) {
		SendCityStatisticRequest request;
		boolean verified;
		try {
			logger.debug("sendSpecialityStatistic(): parse xml");
			request = (SendCityStatisticRequest) marshaller.unmarshal(new StringSource(xml));

			logger.debug("sendSpecialityStatistic(): signature verification");
			Signature signature = signatureProvider.getSignature(request.getCityId().longValueExact());
			signature.update(xml.getBytes(StandardCharsets.UTF_8));
			verified = signature.verify(Base64.getDecoder().decode(encodedSignature));
		} catch (XmlMappingException e) {
			logger.error("Unmarshaling SendCityStatisticRequest error", e);
			return;
		} catch (SignatureException e) {
			logger.error("Signature verification error", e);
			return;
		}

		if (!verified) {
			logger.info("Verification failed");
			return;
		}

		logger.debug("sendSpecialityStatistic(): date = {}, cityId = {}", request.getDate(), request.getCityId());

		CompletableFuture.runAsync(() -> statisticsService.save(request));

		logger.debug("sendSpecialityStatistic(): statistic is successfully saved");
	}

}
