package ru.ardecs.hs.hscommon;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Component
public class CommonBeans {
	@Bean(name = "freemarker.configuration")
	public Configuration getTemplateConfiguration(@Value("${application.ftlBaseFolder}") String baseDir) {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setClassForTemplateLoading(getClass(), baseDir);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
//		    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
		cfg.setAutoFlush(true);
		return cfg;
	}

	@Bean
	public XsdSchema cityStatisticSchema() {
		return new SimpleXsdSchema(new ClassPathResource("statistic.xsd"));
	}

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("ru.ardecs.hs.hscommon.soap.generated");
		return marshaller;
	}

	@Bean
	public KeyPairGenerator keyPairGenerator() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
		keyPairGenerator.initialize(512);
		return keyPairGenerator;
	}
}
