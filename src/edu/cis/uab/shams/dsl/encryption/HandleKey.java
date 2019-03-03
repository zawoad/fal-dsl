package edu.cis.uab.shams.dsl.encryption;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class HandleKey {
	static String IV = "AAAAAAAAAAAAAAAA";
	public HandleKey() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KeyPair kp = kpg.genKeyPair();
			Key publicKey = kp.getPublic();
			Key privateKey = kp.getPrivate();

			KeyFactory fact = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(),
					RSAPublicKeySpec.class);
			RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(),
					RSAPrivateKeySpec.class);

			saveToFile("User_1_public.key", pub.getModulus(), pub.getPublicExponent());
			saveToFile("User_1_private.key", priv.getModulus(),
					priv.getPrivateExponent());
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void saveToFile(String fileName, BigInteger mod, BigInteger exp)
			throws IOException {
		ObjectOutputStream oout = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(fileName)));
		try {
			oout.writeObject(mod);
			oout.writeObject(exp);
		} catch (Exception e) {
			throw new IOException("Unexpected error", e);
		} finally {
			oout.close();
		}
	}

	public static String readAESKey(String aesKeyFileName)
	{
		try{
//			BufferedReader is = new BufferedReader(new FileReader(new File(aesKeyFileName)));
//			StringBuffer sbf = new StringBuffer();
//			while(true)
//			{
//				String line = is.readLine();
//				if(line == null || line.isEmpty())
//					break;
//				sbf.append(line);
//			}
//			return sbf.toString();
			
			BufferedInputStream bfi = new BufferedInputStream(new FileInputStream(new File(aesKeyFileName)));
			byte[] buf = new byte[16];
			bfi.read(buf);
			return new String(buf);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return "";
		}
	}
	public static PublicKey readPublicKeyFromFile(String publicKeyFileName)
			throws IOException {
		InputStream in = new FileInputStream(new File(publicKeyFileName));
		ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(
				in));
		try {
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PublicKey pubKey = fact.generatePublic(keySpec);
			return pubKey;
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			oin.close();
		}
	}
	
	public static PrivateKey readPrivateKeyFromFile(String privateKeyFileName)
			throws IOException {
		InputStream in = new FileInputStream(new File(privateKeyFileName));
		ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(
				in));
		try {
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = fact.generatePrivate(keySpec);
			return privateKey;
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			oin.close();
		}
	}

	public static byte[] rsaEncrypt(byte[] data, PublicKey pubKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] cipherData = cipher.doFinal(data);
			return cipherData;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	public static byte[] rsaDecrypt(byte[] cipherData, PrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] plainData = cipher.doFinal(cipherData);
			return plainData;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static byte[] rsaEncryptLongMessage(byte[] plainText, PublicKey pubKey)
	{
		List<byte[]> cipherByteList = new ArrayList<byte[]>();
		int maxSize = 245;
		int n = plainText.length/maxSize;
		int totalBytes = 0;
		int totalCyperSize = 0;
		for(int i=0; i< n; i++)
		{
			byte[] subPlain = getSubArray(plainText, i*maxSize, maxSize);
			byte[] subCipher = HandleKey.rsaEncrypt(subPlain, pubKey);
			cipherByteList.add(subCipher);
			totalBytes+=maxSize;
			totalCyperSize+= subCipher.length;
		}
		byte[] subPlain = getSubArray(plainText, totalBytes, (plainText.length - totalBytes));
		byte[] subCipher = HandleKey.rsaEncrypt(subPlain, pubKey);
		totalCyperSize+= subCipher.length;
		cipherByteList.add(subCipher);
		
		byte[] result = new byte[totalCyperSize];
		int offset=0;
		for(byte[] cb : cipherByteList)
		{
			int i=0;
			for(; i< cb.length; i++)
			{
				result[offset+i] = cb[i];
			}
			offset += i;
		}
		return result;
	}
	
	public static String rsaDecryptLongMessage(byte[] cipherText, PrivateKey priKey)
	{
		StringBuilder sb = new StringBuilder();
		int maxSize = 256;
		int n = cipherText.length/maxSize;
		for(int i=0; i< n; i++)
		{
			byte[] subCipher = getSubArray(cipherText, i*maxSize, maxSize);
			byte[] subPlain = HandleKey.rsaDecrypt(subCipher, priKey);
			sb.append(new String(subPlain));
		}
		return sb.toString();
	}
	private static byte[] getSubArray(byte[] array, int offset, int size) {
		byte[] subArray = new byte[size];
		for (int i = 0; (i < size) && (i + offset < array.length); i++) {
			subArray[i] = array[offset + i];
		}
		return subArray;
	}
	
	public static byte[] rsaSignMessage(byte[] data, PrivateKey prvKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, prvKey);
			byte[] cipherData = cipher.doFinal(data);
			return cipherData;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	public static byte[] rsaDecryptSignature(byte[] cipherData, PublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] plainData = cipher.doFinal(cipherData);
			return plainData;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static String aesEncrypt(String plainText, String encryptionKey) throws Exception {
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
	    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
	    cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
	    return new String(cipher.doFinal(plainText.getBytes("UTF-8")));
	  }
	 
	  public static String aesDecrypt(byte[] cipherText, String encryptionKey) throws Exception{
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
	    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
	    cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
	    return new String(cipher.doFinal(cipherText),"UTF-8");
	  }
	  
	public static String getHash(String message, String algorithm) {
		MessageDigest md;
		StringBuffer sb = new StringBuffer();
		try {
			md = MessageDigest.getInstance(algorithm);
			md.update(message.getBytes());
			byte byteData[] = md.digest();

			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			//System.out.println("Hex format : " + sb.toString());

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
}
