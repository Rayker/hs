package ru.ardecs.hs.hsapi.mail;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hsapi.TemplateGenerator;
import ru.ardecs.hs.hsapi.bl.TicketModel;
import ru.ardecs.hs.hsdb.repositories.ReservedTimeRepository;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Component
public class MailSender
{
	@Autowired
	private TemplateGenerator templateGenerator;

	@Autowired
	private ReservedTimeRepository reservedTimeRepository;

	@Value("${mail.from.username}")
	private String username;

	@Value("${mail.from.password}")
	private String password;

	@Autowired
	private Session session;

	public void send(String addressTo, Long reservedTimeId) throws IOException, TemplateException {
		try {
			Message message = createMessage(addressTo, reservedTimeId);
			Transport.send(message);
			System.out.println("Done");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private Message createMessage(String addressTo, Long reservedTimeId) throws MessagingException, IOException, TemplateException {
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(username));
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(addressTo));
		message.setSubject("Ticket " + reservedTimeId);
		message.setContent(generateHtml(reservedTimeId), "text/html; charset=utf-8");
		return message;
	}

	private String generateHtml(Long reservedTimeId) throws IOException, TemplateException {
		TicketModel model = new TicketModel(reservedTimeRepository.findOne(reservedTimeId));
		return templateGenerator.generateHtml(model, "mail/ticket.ftl");
	}
}