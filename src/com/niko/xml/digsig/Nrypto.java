package com.niko.xml.digsig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Nrypto {
	/**
	 * ����� ��ȣȭ �˰����� �����Ѵ�.
	 */
	private static final String ALGORITHM = "RSA";

	/**
	 * ����Ű�� ����Ű�� 1024 ����Ʈ�� �����Ѵ�. 
	 * �׸��� ����Ű�� ����Ű�� �����Ѵ�. (Public.key / Private.key)
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	private KeyPair generateKeyPair() {
		KeyPair keyPair = null;
		KeyPairGenerator keyGen;
		try {
			// ALGORITHM(RSA)�� �ش��ϴ� Ű���� ����
			keyGen = KeyPairGenerator.getInstance(ALGORITHM);
			// Ű�� ���̸� 1024 �ʱ�ȭ
			keyGen.initialize(1024);
			// Ű ����
			keyPair = keyGen.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return keyPair;
	}

	/**
	 * ����Ű�� �����Ѵ�.
	 * 
	 * @param filePath , ����Ű�� ���
	 * @return privateKey
	 */
	public PrivateKey storedPrivateKey(String filePath) {
		PrivateKey privateKey = null;
		byte[] keyData = getKey(filePath);
		// ����Ű�� PKCS8 ǥ������ ���ڵ�
		PKCS8EncodedKeySpec encodePrivateKey = new PKCS8EncodedKeySpec(keyData);
		KeyFactory keyFactory = null;

		try {
			// RSA �˰����� �޾ƿ´�
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		try {
			// ����Ű�� �����Ѵ�.
			privateKey = keyFactory.generatePrivate(encodePrivateKey);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return privateKey;
	}

	/**
	 * ����Ű�� �����Ѵ�
	 * 
	 * @param filePath , ����Ű�� ���
	 * @return publicKey
	 */
	public PublicKey storedPublicKey(String filePath) {
		PublicKey publicKey = null;
		byte[] keyData = getKey(filePath);
		KeyFactory keyFactory = null;

		try {
			// RSA �˰����� �޾ƿ´�
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		X509EncodedKeySpec encodePublicKey = new X509EncodedKeySpec(keyData);

		try {
			// ����Ű�� �����Ѵ�.
			publicKey = keyFactory.generatePublic(encodePublicKey);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return publicKey;
	}

	/**
	 * ���� �ȿ� ����Ű�� ����Ű�� �����Ѵ�.
	 * 
	 * @param dirPath
	 */
	public void storeKeyPair(String dirPath) {
		// Ű �� -> ���� �Լ� ȣ��
		KeyPair keyPair = generateKeyPair();
		// Ű �ֿ��� privatekey�� ����
		PrivateKey privateKey = keyPair.getPrivate();
		// Ű �ֿ��� pulbickey�� ����
		PublicKey publicKey = keyPair.getPublic();

		storeKeys(dirPath + File.separator + "public.key", publicKey);
		storeKeys(dirPath + File.separator + "private.key", privateKey);
	}

	/**
	 * ����/���� Ű�� �����Ѵ�.
	 * 
	 * @param filePath , ������ �̸�
	 * @param key
	 */
	private void storeKeys(String filePath, Key key) {
		// ���ڵ��� Ű�� �޾ƿ´�
		byte[] keyBytes = key.getEncoded();
		// ��� ��Ʈ�� �غ�
		OutputStream outputStream = null;
		try {
			// ���ϰ�ο� ���ڵ��� Ű�� �����Ѵ�
			outputStream = new FileOutputStream(filePath);
			outputStream.write(keyBytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// ��Ʈ�� null�̸� ����!
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * �迭 ���� (����Ʈ)���� Ű�� �˻��Ѵ�/��ã�ƿ´�.
	 * 
	 * @param filePath
	 * @return buffer
	 */
	private byte[] getKey(String filePath) {
		File file = new File(filePath);
		// ������ ���̸�ŭ �Ҵ��Ѵ�
		byte[] buffer = new byte[(int) file.length()];
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(file);
			fis.read(buffer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return buffer;
	}

	/**
	 * Ű ���� ���� ���θ� Ȯ���Ѵ�.
	 * 
	 * Before: public boolean areKeysPresent(String filePath)
	 * 
	 * @return flag indicating if the pair of keys were generated.
	 */
	public boolean areKeysPresent() {
		File privateKey = new File("keys" + File.separator + "private.key");
		File publicKey = new File("keys" + File.separator + "public.key");

		if (privateKey.exists() && publicKey.exists()) {
			return true;
		}
		return false;
	}
}
