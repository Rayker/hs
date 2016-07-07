package ru.ardecs.hs.hscommon;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

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
}
