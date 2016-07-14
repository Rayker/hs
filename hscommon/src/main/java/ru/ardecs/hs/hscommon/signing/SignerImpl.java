package ru.ardecs.hs.hscommon.signing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.xml.transform.StringResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.Iterator;

@Component
public class SignerImpl implements Signer {
	private static final Logger logger = LoggerFactory.getLogger(SignerImpl.class);

	private KeyPair kp;

	private static PrivateKey privateKey;
	private static PublicKey publicKey;

	public SignerImpl() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
		kp = new KeyLoader().loadKeyPair("hscommon/src/main/resources", "DSA");
	}
	public static void main(String[] args) throws Exception {
		StringBuilder builder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(SignerImpl.class.getResourceAsStream("test.xml")))) {
			while (br.ready()) {
				builder.append(br.readLine());
				builder.append("\n");
			}
		}
		SignerImpl signer = new SignerImpl();
		String xml = signer.sign(builder.toString());
		logger.debug(xml);
		signer.validate(xml);
	}

	@Override
	public String sign(String xml) throws Exception {
//		keyPairGenerator = new CommonBeans().keyPairGenerator();
//		KeyPair keyPair = keyPairGenerator.genKeyPair();
//
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		dbf.setNamespaceAware(true);
//		DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
//		Document document = documentBuilder.parse(SignerImpl.class.getResourceAsStream("test.xml"));
//
//		DOMSignContext dsc = new DOMSignContext(keyPair.getPrivate(), document.getDocumentElement());
//
//		XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM");
//		Reference ref = xmlSignatureFactory.newReference("", xmlSignatureFactory.newDigestMethod(DigestMethod.SHA1, null));
//
//		CanonicalizationMethod canonicalizationMethod = xmlSignatureFactory.newCanonicalizationMethod(
//				CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS,
//				(C14NMethodParameterSpec) null);
//		SignatureMethod signatureMethod = xmlSignatureFactory.newSignatureMethod(SignatureMethod.DSA_SHA1, null);
//
//
//
//		SignedInfo signedInfo = xmlSignatureFactory.newSignedInfo(canonicalizationMethod, signatureMethod, Collections.singletonList(ref));
//		KeyInfoFactory keyInfoFactory = xmlSignatureFactory.getKeyInfoFactory();
//		KeyValue keyValue = keyInfoFactory.newKeyValue(keyPair.getPublic());
//		KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(keyValue));
//
//		XMLSignature xmlSignature = xmlSignatureFactory.newXMLSignature(signedInfo, keyInfo);
//
//		xmlSignature.sign(dsc);
//
//		TransformerFactory tf = TransformerFactory.newInstance();
//		Transformer trans = tf.newTransformer();
//		trans.transform(new DOMSource(document), new StreamResult(System.out));

		// First, create a DOM XMLSignatureFactory that will be used to
		// generate the XMLSignature and marshal it to DOM.
		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

		// Create a Reference to an external URI that will be digested
		// using the SHA1 digest algorithm
//		Reference ref = fac.newReference("http://www.w3.org/TR/xml-stylesheet",             fac.newDigestMethod(DigestMethod.SHA1, null));
//		Reference ref = fac.newReference("", fac.newDigestMethod(DigestMethod.SHA1, null));

		Reference ref = fac.newReference
				("", fac.newDigestMethod(DigestMethod.SHA1, null),
						Collections.singletonList
								(fac.newTransform
										(Transform.ENVELOPED, (TransformParameterSpec) null)),
						null, null);

		// Create the SignedInfo
		SignedInfo si = fac.newSignedInfo(
				fac.newCanonicalizationMethod
						(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS,
								(C14NMethodParameterSpec) null),
				fac.newSignatureMethod(SignatureMethod.DSA_SHA1, null),
				Collections.singletonList(ref));

		// Create a DSA KeyPair
//		KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
//		kpg.initialize(512);
//		kp = kpg.generateKeyPair();



		// Create a KeyValue containing the DSA PublicKey that was generated
		KeyInfoFactory kif = fac.getKeyInfoFactory();
		KeyValue kv = kif.newKeyValue(kp.getPublic());

		// Create a KeyInfo and add the KeyValue to it
		KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv));

		// Create the XMLSignature (but don't sign it yet)
		XMLSignature signature = fac.newXMLSignature(si, ki);

		// Create the Document that will hold the resulting XMLSignature
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true); // must be set
//		Document doc = dbf.newDocumentBuilder().newDocument();
		Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

		// Create a DOMSignContext and set the signing Key to the DSA
		// PrivateKey and specify where the XMLSignature should be inserted
		// in the target document (in this case, the document root)
		DOMSignContext signContext = new DOMSignContext(kp.getPrivate(), doc.getDocumentElement());

		// Marshal, generate (and sign) the detached XMLSignature. The DOM
		// Document will contain the XML Signature if this method returns
		// successfully.
		signature.sign(signContext);

		StringResult stringResult = new StringResult();

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), stringResult);
		return stringResult.toString();
	}

	@Override
	public boolean validate(String xml) throws Exception {
		// Instantiate the document to be validated
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc =
				dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

		// Find Signature element
		NodeList nl =
				doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
		if (nl.getLength() == 0) {
			throw new Exception("Cannot find Signature element");
		}

		// Create a DOM XMLSignatureFactory that will be used to unmarshal the
		// document containing the XMLSignature
		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

		// Create a DOMValidateContext and specify a KeyValue KeySelector
		// and document context
		DOMValidateContext valContext = new DOMValidateContext
				(kp.getPublic(), nl.item(0));

		// unmarshal the XMLSignature
		XMLSignature signature = fac.unmarshalXMLSignature(valContext);

		// Validate the XMLSignature (generated above)
		boolean coreValidity = signature.validate(valContext);

		// Check core validation status
		if (!coreValidity) {
			System.err.println("Signature failed core validation");
			boolean sv = signature.getSignatureValue().validate(valContext);
			logger.debug("signature validation status: " + sv);
			// check the validation status of each Reference
			Iterator i = signature.getSignedInfo().getReferences().iterator();
			for (int j=0; i.hasNext(); j++) {
				boolean refValid =
						((Reference) i.next()).validate(valContext);
				logger.debug("ref["+j+"] validity status: " + refValid);
			}
		} else {
			logger.debug("Signature passed core validation");
		}
		return coreValidity;
	}


}
