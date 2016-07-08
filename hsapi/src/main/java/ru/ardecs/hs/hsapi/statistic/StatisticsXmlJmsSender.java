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
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;

@Component
public class StatisticsXmlJmsSender implements StatisticsSender {
	private static final Logger logger = LoggerFactory.getLogger(StatisticsXmlJmsSender.class);

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@Autowired
	private Jaxb2Marshaller marshaller;

	@Value("${application.jms.xml-destination}")
	private String destination;

	@Override
	public void sendCityStatisticRequest(SendCityStatisticRequest cityStatistic) {
		logger.info("sendCityStatisticRequest(): convert to xml");
		StringResult stringResult = new StringResult();

		try {
			marshaller.marshal(cityStatistic, stringResult);
		} catch (XmlMappingException e) {
			logger.error("Marshaling SendCityStatisticRequest error", e);
			return;
		}
		String xml = stringResult.toString();

		logger.info("sendCityStatisticRequest(): sending message");
		jmsMessagingTemplate.convertAndSend(destination, xml);

		logger.info("sendCityStatisticRequest(): success");
	}
}
