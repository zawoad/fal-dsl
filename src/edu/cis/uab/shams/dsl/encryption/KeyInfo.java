package edu.cis.uab.shams.dsl.encryption;

import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyInfo {

	private static KeyInfo keyInfo = null;
	PrivateKey privateKey = null;
	PrivateKey witnessPrivateKey = null;
	PublicKey witnessPublicKey = null;
	PublicKey userPublicKey = null;
	PublicKey publicKey = null;

	private KeyInfo() {
		String userPublicKeyFile = "/Users/shams/Dropbox/MacEclipes/LocationServer/User_1_public.key";
		String witnessPublicKeyFile = "/Users/shams/Dropbox/MacEclipes/LocationServer/Witness_1_public.key";
		String witnessPrivateKeyFile = "/Users/shams/Dropbox/MacEclipes/LocationServer/Witness_1_private.key";
		String privateKeyFile = "/Users/shams/Dropbox/MacEclipes/LocationServer/private.key";
		String publicKeyFile = "/Users/shams/Dropbox/MacEclipes/LocationServer/public.key";

		try {
			witnessPublicKey = HandleKey.readPublicKeyFromFile(witnessPublicKeyFile);
			userPublicKey = HandleKey.readPublicKeyFromFile(userPublicKeyFile);
			publicKey = HandleKey.readPublicKeyFromFile(publicKeyFile);

			privateKey = HandleKey.readPrivateKeyFromFile(privateKeyFile);
			witnessPrivateKey = HandleKey.readPrivateKeyFromFile(witnessPrivateKeyFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static KeyInfo getInstance() {
		if (keyInfo != null)
			return keyInfo;
		else {
			keyInfo = new KeyInfo();
			return keyInfo;
		}
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public PublicKey getWitnessPublicKey() {
		return witnessPublicKey;
	}

	public PublicKey getUserPublicKey() {
		return userPublicKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}
	public PrivateKey getWitnessPrivateKey() {
		return witnessPrivateKey;
	}
	
}
