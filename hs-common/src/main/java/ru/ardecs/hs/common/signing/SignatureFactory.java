package ru.ardecs.hs.common.signing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.*;
import java.security.cert.Certificate;

@Component
public class SignatureFactory {
	private static final Logger logger = LoggerFactory.getLogger(SignatureFactory.class);

	private static final String KEY_FACTORY_ALGORITHM = "DSA";
	private static final String SIGNATURE_ALGORITHM = "DSA";

	@Autowired
	private KeyStore trustStore;

	public Signature createVerificationSignature(long cityId) {
		String cityIdAlias = String.valueOf(cityId);
		return createVerificationSignature(cityIdAlias);
	}

	public Signature createVerificationSignature(String alias) {
		Certificate certificate;
		try {
			certificate = trustStore.getCertificate(alias);
			if (certificate == null) {
				certificate = trustStore.getCertificate("public");
			}
		} catch (KeyStoreException e) {
			logger.error("Certificate loading error");
			throw new RuntimeException(e);
		}
		return createVerificationSignature(certificate);
	}

	public Signature createVerificationSignature(java.security.cert.Certificate certificate) {
		try {
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(certificate);
			return signature;
		} catch (NoSuchAlgorithmException e) {
			logger.error("createVerificationSignature error: no such algorithm: {}", SIGNATURE_ALGORITHM, e);
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			logger.error("createVerificationSignature error", e);
			throw new RuntimeException(e);
		}
	}

}
