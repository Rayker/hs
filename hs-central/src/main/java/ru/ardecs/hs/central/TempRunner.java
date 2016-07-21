package ru.ardecs.hs.central;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.net.URL;

public class TempRunner {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		Document document = getXmlDocument();

		Validator validator = createValidator();

		validate(document, validator);
	}

	private static boolean validate(Document document, Validator validator) throws IOException {
		try {
			validator.validate(new DOMSource(document));
		} catch (SAXException e) {
			throw new RuntimeException(e);
//			return false;
		}
		return true;
	}

	private static Document getXmlDocument() throws ParserConfigurationException, SAXException, IOException {
		URL xml = TempRunner.class.getResource("/statistic/instance.xml");

		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return parser.parse(xml.openStream());
	}

	private static Validator createValidator() throws IOException, SAXException {
		URL xsd = TempRunner.class.getResource("/statistic.xsd");
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		Source schemaFile = new StreamSource(xsd.openStream());
		Schema schema = factory.newSchema(schemaFile);

		return schema.newValidator();
	}
}
