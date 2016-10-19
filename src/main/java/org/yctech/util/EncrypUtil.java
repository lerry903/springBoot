package org.yctech.util;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncrypUtil {

	private static final String ENCRYP_PASSWORD = "1q2w3e4r5t";
	
	public static String encoder(String content) {
		try {
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			generator.init(new SecureRandom(ENCRYP_PASSWORD.getBytes()));
			Key key = generator.generateKey();
			generator = null;
			return getEncString(content, key);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String decoder(String content) {
		try {
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			generator.init(new SecureRandom(ENCRYP_PASSWORD.getBytes()));
			Key key = generator.generateKey();
			generator = null;
			return getDesString(content, key);
		} catch (Exception e) {
			return null;
		}
	}

	private static String getEncString(String strMing, Key key) {
		byte[] byteMi = null;
		byte[] byteMing = null;
		String strMi = "";
		BASE64Encoder base64en = new BASE64Encoder();
		try {
			byteMing = strMing.getBytes("UTF8");
			byteMi = getEncCode(byteMing, key);
			strMi = base64en.encode(byteMi);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			base64en = null;
			byteMing = null;
			byteMi = null;
		}
		return strMi;
	}

	private static String getDesString(String strMi, Key key) {
		BASE64Decoder base64De = new BASE64Decoder();
		byte[] byteMing = null;
		byte[] byteMi = null;
		String strMing = "";
		try {
			byteMi = base64De.decodeBuffer(strMi);
			byteMing = getDesCode(byteMi, key);
			strMing = new String(byteMing, "UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			base64De = null;
			byteMing = null;
			byteMi = null;
		}
		return strMing;
	}

	private static byte[] getEncCode(byte[] byteS, Key key) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	private static byte[] getDesCode(byte[] byteD, Key key) {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	public static void main(String[] args) {
		String str = "admin";
		
		String enc = EncrypUtil.encoder(str);
		
		System.out.println(enc);
		System.out.println(enc.replaceAll("\\+", "%2B"));
		System.out.println(EncrypUtil.decoder(enc));
	}

}
