package ru.ardecs.hs.hscommon.signing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Component
public class SignatureFactory {
	private static final Logger logger = LoggerFactory.getLogger(SignatureFactory.class);

	private static final String KEY_FACTORY_ALGORITHM = "DSA";
	private static final String SIGNATURE_ALGORITHM = "DSA";

	private KeyFactory keyFactory;

	@PostConstruct
	private void init() throws NoSuchAlgorithmException {
		keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
	}

	public Signature createVerificationSignature(byte[] storedPublicKey) {
		PublicKey publicKey;
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(storedPublicKey);
			publicKey = keyFactory.generatePublic(keySpec);
		} catch (InvalidKeySpecException e) {
			logger.error("createVerificationSignature error", e);
			throw new RuntimeException(e);
		}
		return createVerificationSignature(publicKey);
	}

	public Signature createVerificationSignature(PublicKey publicKey) {
		try {
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(publicKey);
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
