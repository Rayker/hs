package ru.ardecs.hs.hsclient.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.xml.transform.StringSource;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;

@Component
public class XmlStatisticReceiver {
	private static Logger logger = LoggerFactory.getLogger(XmlStatisticReceiver.class);

	@Autowired
	private StatisticsRepositoryWrapper repositoryWrapper;

	@Autowired
	private Jaxb2Marshaller marshaller;

	@JmsListener(destination = "${application.jms.xml-destination}")
	public void receiveMessage(String xml) {
		logger.debug("sendSpecialityStatistic(): parse xml");
		SendCityStatisticRequest request;
		try {
			request = (SendCityStatisticRequest) marshaller.unmarshal(new StringSource(xml));
		} catch (XmlMappingException e) {
			logger.error("Unmarshaling SendCityStatisticRequest error", e);
			return;
		}
		logger.debug("sendSpecialityStatistic(): date = {}, cityId = {}", request.getDate(), request.getCityId());
		repositoryWrapper.save(request);

		logger.debug("sendSpecialityStatistic(): statistic is successfully saved");
	}

}
