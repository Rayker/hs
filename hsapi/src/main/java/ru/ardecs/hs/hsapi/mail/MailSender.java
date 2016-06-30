package ru.ardecs.hs.hsapi.mail;

//import freemarker.template.TemplateException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MailSender
{
//	private static final Logger logger =
//			LoggerFactory.getLogger(MailSender.class);
//	private static final String EMAIL_TICKET_TEMPLATE = "mail/ticket.ftl";
//
//	@Autowired
//	private TemplateGenerator templateGenerator;
//
//	@Autowired
//	private ReservedTimeRepository reservedTimeRepository;
//
//	@Value("${mail.from.username}")
//	private String username;
//
//	@Value("${mail.from.password}")
//	private String password;
//
//	@Autowired
//	private Session session;
//
//	public void send(String addressTo, Long reservedTimeId) {
//		logger.debug("Start of email sending");
//		try {
//			Message message = createMessage(addressTo, reservedTimeId);
//			Transport.send(message);
//			logger.debug("Email was successfully sent");
//		} catch (MessagingException | IOException | TemplateException e) {
//			logger.error("Message processing error", e);
//		}
//	}
//
//	private Message createMessage(String addressTo, Long reservedTimeId) throws MessagingException, IOException, TemplateException {
//		Message message = new MimeMessage(session);
//		message.setFrom(new InternetAddress(username));
//		message.setRecipients(Message.RecipientType.TO,
//				InternetAddress.parse(addressTo));
//		message.setSubject("Ticket " + reservedTimeId);
//		message.setContent(generateHtml(reservedTimeId), "text/html; charset=utf-8");
//		return message;
//	}
//
//	private String generateHtml(Long reservedTimeId) throws IOException, TemplateException {
//		TicketModel model = new TicketModel(reservedTimeRepository.findOne(reservedTimeId));
//		return templateGenerator.generateHtml(model, EMAIL_TICKET_TEMPLATE);
//	}
}