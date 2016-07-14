package ru.ardecs.hs.hsclient.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.xml.transform.StringSource;
import ru.ardecs.hs.hscommon.signing.KeyLoader;
import ru.ardecs.hs.hscommon.signing.Signer;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class XmlStatisticReceiver {
	private static Logger logger = LoggerFactory.getLogger(XmlStatisticReceiver.class);

	@Autowired
	private Signer signer;

	@Autowired
	private StatisticsRepositoryWrapper repositoryWrapper;

	@Autowired
	private Jaxb2Marshaller marshaller;

	private Signature signature;

	@PostConstruct
	private void init() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException {
		PublicKey publicKey = new KeyLoader().loadKeyPair("hscommon/src/main/resources", "DSA").getPublic();
		signature = Signature.getInstance("DSAwithSHA1");
		signature.initVerify(publicKey);
	}

	@JmsListener(destination = "${application.jms.xml-destination}")
	public void receiveMessage(String xml, @Header("signature") String realSignature) {
		logger.debug("sendSpecialityStatistic(): parse xml");
		SendCityStatisticRequest request;
		try {
			request = (SendCityStatisticRequest) marshaller.unmarshal(new StringSource(xml));
		} catch (XmlMappingException e) {
			logger.error("Unmarshaling SendCityStatisticRequest error", e);
			return;
		}

		logger.debug("sendSpecialityStatistic(): signature verification");
		try {
			signature.verify(Base64.getDecoder().decode(realSignature));
		} catch (SignatureException e) {
			logger.error("Signature verification error", e);
			return;
		}

		logger.debug("sendSpecialityStatistic(): date = {}, cityId = {}", request.getDate(), request.getCityId());
		repositoryWrapper.save(request);

		logger.debug("sendSpecialityStatistic(): statistic is successfully saved");
	}

}
