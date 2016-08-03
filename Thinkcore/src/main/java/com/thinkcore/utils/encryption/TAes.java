package com.thinkcore.utils.encryption;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.thinkcore.utils.TAndroidVersionUtils;
import com.thinkcore.utils.TDigitalUtils;

public class TAes {

	public static String encrypt(String key, String content) throws Exception {
		byte[] rawKey = getRawKey(key.getBytes());
		byte[] result = encrypt(rawKey, content.getBytes());
		return TDigitalUtils.toHex(result);
	}

	public static String decrypt(String key, String encrypted) throws Exception {
		byte[] rawKey = getRawKey(key.getBytes());
		byte[] enc = TDigitalUtils.toByte(encrypted);
		byte[] result = decrypt(rawKey, enc);
		return new String(result);
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		// SHA1PRNG 强随机种子算法
		SecureRandom sr = null;
		if (TAndroidVersionUtils.hasJellyBeanMr1()) {
			sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
		} else {
			sr = SecureRandom.getInstance("SHA1PRNG");
		}
		sr.setSeed(seed);
		kgen.init(256, sr); // 256 bits or 128 bits,192bits
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	private static byte[] encrypt(byte[] key, byte[] content) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(content);
		return encrypted;
	}

	private static byte[] decrypt(byte[] key, byte[] encrypted)
			throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}
}
