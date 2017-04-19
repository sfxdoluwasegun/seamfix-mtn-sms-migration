/**
 * 
 */
package com.sf.vas.mtnsms.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.sf.vas.mtnsms.exception.SmsRuntimeException;

/**
 * @author dawuzi
 *
 */
public class SecurityUtil {

	private static MessageDigest md5MessageDigest;
	private static MessageDigest sha1MessageDigest;
	
	static {
		try {
			md5MessageDigest = MessageDigest.getInstance("MD5");
			sha1MessageDigest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new SmsRuntimeException("Error getting message digests");
		}
	}

	/**
	 * This method is based on the document
	 * @param s
	 * @return
	 */
	public final static String md5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		
		byte[] strTemp = s.getBytes();
		
		md5MessageDigest.update(strTemp);
		
		byte[] md = md5MessageDigest.digest();
		int j = md.length;
		char str[] = new char[j * 2];
		int k = 0;
		
		for (int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}	

	public static byte[] encryptSHA1(String source) {
		sha1MessageDigest.update(source.getBytes());
		return sha1MessageDigest.digest();
	}	
	public static String encryptS1B(String srcPwd) {
		return Base64.getEncoder().encodeToString((encryptSHA1(srcPwd)));
	}
	
	public static void main(String[] args) {

		System.out.println(SecurityUtil.md5("Hi"));
		System.out.println(SecurityUtil.md5("Hi"));
		System.out.println(SecurityUtil.md5("Hi"));
		System.out.println(SecurityUtil.md5("Hi"));
		System.out.println();
		System.out.println(new String(SecurityUtil.encryptSHA1("Hi")));
		System.out.println(new String(SecurityUtil.encryptSHA1("Hi")));
		System.out.println(new String(SecurityUtil.encryptSHA1("Hi")));
		System.out.println(new String(SecurityUtil.encryptSHA1("Hi")));
		
		
		System.out.println("done");
	}
}
