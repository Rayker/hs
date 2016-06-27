package ru.ardecs.hs.hsapi.mail;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hsapi.TemplateGenerator;
import ru.ardecs.hs.hsapi.bl.TicketModel;
import ru.ardecs.hs.hsdb.repositories.ReservedTimeRepository;

import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

@Component
public class MailSender
{
	@Autowired
	@Qualifier("app.props")
	private Properties properties;

	@Autowired
	private TemplateGenerator templateGenerator;

	@Autowired
	private ReservedTimeRepository reservedTimeRepository;

	@Value("${mail.from.username}")
	private String username;

	@Value("${mail.from.password}")
	private String password;

	public void send(String addressTo, Long reservedTimeId) throws IOException, TemplateException {
		Session session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(addressTo));
			message.setSubject("Ticket " + reservedTimeId);
			message.setText(generateHtml(reservedTimeId));

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private String generateHtml(Long reservedTimeId) throws IOException, TemplateException {
		TicketModel model = new TicketModel(reservedTimeRepository.findOne(reservedTimeId));
		return templateGenerator.generateHtml(model, "ticket.ftl");
	}
}