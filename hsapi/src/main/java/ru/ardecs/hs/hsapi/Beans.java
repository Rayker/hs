package ru.ardecs.hs.hsapi;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

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
}
