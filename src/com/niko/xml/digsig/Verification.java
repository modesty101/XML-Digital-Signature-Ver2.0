package com.niko.xml.digsig;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public class Verification {
	
	
	/**
	 * 변조된 XML(전자서명)을 검증합니다.
	 */
	public static void testSignedTamperedXMLDoc() {
		String signedXmlFilePath = "DefaceSignedTT.xml";
		String publicKeyFilePath = "keys" + File.separator + "public.key";
		//String publicKeyFilePath = "publickey";
		try {
			boolean validFlag = GenDigSigXml.isDigitalSignatureValid(signedXmlFilePath, publicKeyFilePath);
			System.out.println("Validity of XML Digital Signature : " + validFlag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 테스트 - 서명된 XML 문서가 옳은지 검사합니다.
	 */
	public static void testSignedXML() {
		String signedXmlPath = "SignedAngel.xml";
		String publicKeyPath = "keys" + File.separator + "public.key";
		// String publicKeyPath = "publickey";

		try {
			boolean validFlag = GenDigSigXml.isDigitalSignatureValid(signedXmlPath, publicKeyPath);
			System.out.println("Validity of XML Digital Siganture : " + validFlag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			byte[] publicKeyEncoded = Files.readAllBytes(Paths.get("keypair","publickey"));
			byte[] digitalSignature = Files.readAllBytes(Paths.get("keypair","signature"));

			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyEncoded);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initVerify(publicKey);

			byte[] bytes = Files.readAllBytes(Paths.get(""));
			signature.update(bytes);

			boolean verified = signature.verify(digitalSignature);
			if (verified) {
				System.out.println("Data verified.");
			} else {
				System.out.println("Cannot verify data.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		testSignedXML();
		testSignedTamperedXMLDoc();
	}

}
