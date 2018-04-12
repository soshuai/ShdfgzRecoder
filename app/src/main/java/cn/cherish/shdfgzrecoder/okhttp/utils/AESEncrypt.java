package cn.cherish.shdfgzrecoder.okhttp.utils;

import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESEncrypt {
	private static final String TAG = AESEncrypt.class.getName();

	/** 加密工具 */
	private Cipher encryptCipher = null;

	/** 解密工具 */
	private Cipher decryptCipher = null;

	public AESEncrypt(String keyvalue) {
		SecretKeySpec key = new SecretKeySpec(keyvalue.getBytes(), "AES");
		// 生成Cipher对象,指定其支持的DES算法
		try {
			encryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			encryptCipher.init(Cipher.ENCRYPT_MODE, key);

			decryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			decryptCipher.init(Cipher.DECRYPT_MODE, key);
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, e.getLocalizedMessage());
		} catch (NoSuchPaddingException e) {
			Log.e("", e.getLocalizedMessage());
		} catch (InvalidKeyException e) {
			Log.e("", e.getLocalizedMessage());
		}
	}

	/**
	 * 对字符串加密
	 * 
	 * @param str
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public byte[] encrytor(String str, String charsetName)
			throws InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException {
		byte[] src = null;
		try {
			src = str.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getLocalizedMessage());
		}
		return encryptCipher.doFinal(src);
	}

	/**
	 * @Title: encrytorAsString
	 * @Description: 对字符串加密，并转换成ASC字符�?
	 * @author MACK
	 * @date 2015-1-11 下午12:26:39
	 * 
	 * @param str
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws
	 */
	public String encrytorAsString(String str, String charsetName)
			throws InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException {
		byte[] srcBytes = encrytor(str, charsetName);
		return EncryptUtil.byteArr2HexStr(srcBytes);
	}

	/**
	 * @Title: encrytorAsString
	 * @Description: 对字符串加密，并转换成ASC字符�?
	 * @author MACK
	 * @date 2015-1-11 下午12:26:39
	 * 
	 * @param str
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws
	 */
	public String encrytorAsString(byte[] srcBytes) throws InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
		byte[] destBytes = encryptCipher.doFinal(srcBytes);
		return EncryptUtil.byteArr2HexStr(destBytes);
	}

	/**
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @Title: decryptorFromString
	 * @Description: 对字符串解密
	 * @author MACK
	 * @date 2015-1-13 上午12:35:17
	 * 
	 * @param str
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws
	 */
	public String decryptorFromString(String str, String charsetName)
			throws InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, IOException {
		byte[] resultBytes = EncryptUtil.hexStr2ByteArr(str);
		byte[] jsonBytes = decryptCipher.doFinal(resultBytes);

		return new String(jsonBytes, charsetName);
	}
}
