package ru.ardecs.hs.common;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

@Component
public class TemplateGenerator {
	@Autowired
	@Qualifier("freemarker.configuration")
	private Configuration cfg;

	public String generateHtml(Object model, String name) throws IOException, TemplateException {
		Template template = cfg.getTemplate(name);
		try (Writer output = new StringWriter()) {
			template.process(model, output);
			return output.toString();
		}
	}
}
