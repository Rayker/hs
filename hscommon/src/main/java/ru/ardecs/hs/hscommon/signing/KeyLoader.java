package ru.ardecs.hs.hscommon.signing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyLoader {

	public static void main(String args[]) {
		KeyLoader keyLoader = new KeyLoader();
		try {
			String path = "hscommon/src/main/resources";

			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");

			keyGen.initialize(512);
			KeyPair generatedKeyPair = keyGen.genKeyPair();

			System.out.println("Generated Key Pair");
			keyLoader.dumpKeyPair(generatedKeyPair);
			keyLoader.SaveKeyPair(path, generatedKeyPair);

			KeyPair loadedKeyPair = keyLoader.loadKeyPair(path, "DSA");
			System.out.println("Loaded Key Pair");
			keyLoader.dumpKeyPair(loadedKeyPair);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private void dumpKeyPair(KeyPair keyPair) {
		PublicKey pub = keyPair.getPublic();
		System.out.println("Public Key: " + getHexString(pub.getEncoded()));

		PrivateKey priv = keyPair.getPrivate();
		System.out.println("Private Key: " + getHexString(priv.getEncoded()));
	}

	private String getHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	public void SaveKeyPair(String path, KeyPair keyPair) throws IOException {
		storKey(path, keyPair.getPublic(), "/public.key");
		storKey(path, keyPair.getPrivate(), "/private.key");
	}

	private void storKey(String path, Key key, String name) throws IOException {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
				key.getEncoded());
		FileOutputStream fos = new FileOutputStream(path + name);
		fos.write(x509EncodedKeySpec.getEncoded());
		fos.close();
	}

	public KeyPair loadKeyPair(String path, String algorithm)
			throws IOException, NoSuchAlgorithmException,
			InvalidKeySpecException {
		// Read Public Key.

		// Generate KeyPair.
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(readKey(path, "/public.key")));
		PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(readKey(path, "/private.key")));
		return new KeyPair(publicKey, privateKey);
	}

	private byte[] readKey(String path, String name) throws IOException {
		File filePublicKey = new File(path + name);
		FileInputStream fis = new FileInputStream(path + name);
		byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
		fis.read(encodedPublicKey);
		fis.close();
		return encodedPublicKey;
	}
}
