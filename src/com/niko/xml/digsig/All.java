package com.niko.xml.digsig;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Signature;

public class All {
	/* IS_CHUNKED의 논리값 참일 경우, 76자씩 개행 문자(&#13;)와 함께 인코딩된다. */
	private static final boolean IS_CHUNKED = false;
	
	public static void main(String[] args) throws Exception {
		try {
			String keyPath = "keys";
			String privateKeyPath = "keys" + File.separator + "private.key";
			String publicKeyPath = "keys" + File.separator + "public.key";
			Nrypto util = new Nrypto();
			util.storeKeyPair(keyPath);

			// Get an instance of Signature object and initialize it.
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(util.storedPrivateKey(privateKeyPath));
			// signature.initSign(privateKey);

			// Supply the data to be signed to the Signature object
			// using the update() method and generate the digital
			// signature.
			byte[] bytes = Files.readAllBytes(Paths.get("angel.jpg"));
			signature.update(bytes);
			byte[] digitalSignature = signature.sign();

			Files.write(Paths.get("verify","signature"), digitalSignature);
			Files.write(Paths.get("verify","publickey"), util.storedPublicKey(publicKeyPath).getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Base64IO.encodeFile("angel.jpg", "enc_angel.jpg", IS_CHUNKED);
		
		NewXmlDoc.extractEncodingValue("enc_angel.jpg", "Angel.xml");
		
		Base64IO.decodeFile("enc_angel.jpg", "dec_angel.jpg");
		
		
		String keyPairPath = "keys";
		Nrypto util = new Nrypto();

		if (util.areKeysPresent()) {
			System.out.println("Keys are already existed..!!");
		} else {
			System.out.println("Private & Public Keys generating...");
			System.out.println("===================================");
			System.out.println("Complete.");
			util.storeKeyPair(keyPairPath);
		}
		
		String oriXmlPath = "Angel.xml";
		String destSignedXmlPath = "SignedAngel.xml";
		String privateKeyPath = "keys" + File.separator + "private.key";
		String publicKeyPath = "keys" + File.separator + "public.key";
		GenDigSigXml.generateXMLDigitalSignature(oriXmlPath, destSignedXmlPath, privateKeyPath, publicKeyPath);
		
	}
}
