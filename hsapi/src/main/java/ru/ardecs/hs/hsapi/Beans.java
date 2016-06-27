package ru.ardecs.hs.hsapi;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class Beans {
	private Configuration cfg;

	@Bean
	public Configuration getTemplateConfiguration() {
		if (cfg == null) {
			cfg = new Configuration(Configuration.VERSION_2_3_23);
			cfg.setClassForTemplateLoading(getClass(), "/ftl/");
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
//		    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
			cfg.setAutoFlush(true);
		}
		return cfg;
	}

	@Bean(name = "app.props")
	public Properties getApplicationProperties() throws IOException {
		ClassPathResource resource = new ClassPathResource("application.properties");
		Properties properties = new Properties();
		properties.load(resource.getInputStream());
		return properties;
	}
}
