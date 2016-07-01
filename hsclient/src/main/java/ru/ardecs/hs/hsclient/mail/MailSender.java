package ru.ardecs.hs.hsclient.mail;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hsclient.api.ApiProvider;
import ru.ardecs.hs.hscommon.TemplateGenerator;
import ru.ardecs.hs.hscommon.models.TicketModel;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class MailSender
{
//	private static final Logger logger =
//			LoggerFactory.getLogger(MailSender.class);
	private static final String EMAIL_TICKET_TEMPLATE = "mail/ticket.ftl";

	@Autowired
	private TemplateGenerator templateGenerator;

	@Autowired
	private ApiProvider apiProvider;

	@Value("${mail.from.username}")
	private String username;

	@Value("${mail.from.password}")
	private String password;

	@Autowired
	private Session session;

	public void send(Long cityId, String addressTo, Long reservedTimeId) {
//		logger.debug("Start of email sending");
		try {
			Message message = createMessage(cityId, addressTo, reservedTimeId);
			Transport.send(message);
//			logger.debug("Email was successfully sent");
		} catch (MessagingException | IOException | TemplateException | URISyntaxException e) {
			throw new RuntimeException(e);
//			logger.error("Message processing error", e);
		}
	}

	private Message createMessage(Long cityId, String addressTo, Long reservedTimeId) throws IOException, TemplateException, MessagingException, URISyntaxException {
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(username));
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(addressTo));
		message.setSubject("Ticket " + reservedTimeId);
		message.setContent(generateHtml(cityId, reservedTimeId), "text/html; charset=utf-8");
		return message;
	}

	private String generateHtml(Long cityId, Long reservedTimeId) throws IOException, TemplateException, URISyntaxException {
		TicketModel model = apiProvider.getApiWrapper(cityId).getTicketModel(reservedTimeId);
		return templateGenerator.generateHtml(model, EMAIL_TICKET_TEMPLATE);
	}
}