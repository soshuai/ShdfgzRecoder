package cn.cherish.shdfgzrecoder.okhttp.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import cn.cherish.shdfgzrecoder.utils.LogUtils;
import cn.cherish.shdfgzrecoder.utils.StringUtils;

/**
 * 加密/解密工具类
 */
public class EncryptUtil {

    private EncryptUtil() {
    }

    /**
     * DES算法，加密
     * 
     * @param data
     *            待加密字符串
     * @param key
     *            加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws InvalidAlgorithmParameterException
     * @throws Exception
     */
    public static String desEncode(String key, String data) {
        if (data == null)
            return null;
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            byte[] bytes = cipher.doFinal(data.getBytes());
            return byte2hex(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * DES算法，解密
     * 
     * @param data
     *            待解密字符串
     * @param key
     *            解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception
     *             异常
     */
    public static String desDecode(String key, String data) {
        if (data == null)
            return null;
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return new String(cipher.doFinal(hex2byte(data.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * 获取3Des加密cbc填充的密文
     * 
     * @param key
     *            密钥
     * @param encodeStr
     *            待加密的字符串
     * @return 密文
     */
    public static byte[] des3EncodeCBC(String key, String encodeStr) {
        try {
            byte[] data = encodeStr.getBytes("UTF-8");
            Key deskey = null;
            DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
            deskey = keyfactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            IvParameterSpec ips = new IvParameterSpec(new byte[8]);
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            return cipher.doFinal(data);

        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 获取3Des加密ecb填充的密文
     * 
     * @param key
     *            密钥
     * @param encodeStr
     *            待加密的字符串
     * @return 密文
     */
    public static byte[] des3EncodeEcb(String key, String encodeStr) {
        try {
            byte[] data = encodeStr.getBytes("UTF-8");
            Key deskey = null;
            DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
            deskey = keyfactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            return cipher.doFinal(data);

        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 获取经过Base64处理过的3Des加密ecb填充的密文
     * 
     * @param key
     *            密钥
     * @param encodeStr
     *            待加密的字符串
     * @return 密文
     */
    public static String des3EncodeEcbWithBase64(String key, String encodeStr) {
        byte[] data = des3EncodeEcb(key, encodeStr);
        if (null == data || 0 == data.length)
            return "";
        return filter(Base64.encodeToString(data, Base64.DEFAULT));
    }

    /**
     * 去掉加密字符串换行符
     * 
     * @param str
     * @return
     */
    private static String filter(String str) {
        String output = "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            int asc = str.charAt(i);
            if (asc != 10 && asc != 13) {
                sb.append(str.subSequence(i, i + 1));
            }
        }
        output = new String(sb);
        return output;
    }

    /**
     * 获取3DEC加密ECB填充的明文
     * 
     * @param key
     *            密钥
     * @param data
     *            3DES加密的密文
     * @return 明文
     */
    public static String des3DecodeCBC(String key, byte[] data) {
        Key deskey = null;
        DESedeKeySpec spec;
        try {
            spec = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
            deskey = keyfactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            IvParameterSpec ips = new IvParameterSpec(new byte[8]);
            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
            cipher.init(Cipher.DECRYPT_MODE, deskey);

            byte[] bOut = cipher.doFinal(data);

            return new String(bOut, "UTF-8");
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取3DEC加密ECB填充的明文
     * 
     * @param key
     *            密钥
     * @param data
     *            3DES加密的密文
     * @return 明文
     */
    public static String des3DecodeEcb(String key, byte[] data) {
        Key deskey = null;
        DESedeKeySpec spec;
        try {
            spec = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
            deskey = keyfactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, deskey);

            byte[] bOut = cipher.doFinal(data);

            return new String(bOut, "UTF-8");
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取3DEC加密ECb填充的明文
     * 
     * @param key
     *            密钥
     * @param base64Str
     *            经过Base64处理过的3DEC加密ecb填充的密文
     * @return 明文
     */
    public static String des3DecodeEcbWithBase64(String key, String base64Str) {
        return des3DecodeEcb(key, Base64.decode(base64Str, Base64.DEFAULT));
    }

    private static char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * MD5加密字符串
     * 
     * @param str
     * @return
     */
    public static String Md5(String str) {
        if (str != null && !str.trim().equals("")) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < md5Byte.length; i++) {
                    sb.append(HEX[(int) (md5Byte[i] & 0xf0) >>> 4]);
                    sb.append(HEX[(int) (md5Byte[i] & 0x0f)]);
                }
                str = sb.toString();
            } catch (NoSuchAlgorithmException e) {

            } catch (Exception e) {

            }
        }
        return str;
    }

    /**
     * MD5加密文件
     * 
     * @param file
     * @return
     */
    public static String Md5File(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(byteBuffer);
            return bufferToHex(messagedigest.digest());
        } catch (NoSuchAlgorithmException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (null != in) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                }
            }
        }
        return "";
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = HEX[(bt & 0xf0) >> 4];
        char c1 = HEX[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    /**
     * SHA1加密字符串
     * 
     * @param s
     * @return
     */
    public static String SHA1(String s) {
        if (s != null && !s.trim().equals("")) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-1");
                digest.update(s.getBytes("UTF8"));
                byte messageDigest[] = digest.digest();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < messageDigest.length; i++) {
                    sb.append(HEX[(int) (messageDigest[i] & 0xf0) >>> 4]);
                    sb.append(HEX[(int) (messageDigest[i] & 0x0f)]);
                }
                s = sb.toString();
            } catch (Exception e) {

            }
        }
        return s;
    }

    /**
     * 加密关键文本<br>
     * 为避免关键key泄漏，应用中所有使用到的密钥等重要信息，需经过该方法加密
     * 
     * @param key
     *            关键文本
     * @return 加密后的关键文本byte数组
     */
    public static byte[] keyEncode(String key) {
        if (TextUtils.isEmpty(key))
            return null;
        byte[] base64Bytes = Base64.encode(key.getBytes(), Base64.DEFAULT);
        int len = base64Bytes.length;
        if (0 == len)
            return null;
        int middleIndex = len / 2 - 1;
        boolean isEven = 0 == len % 2;
        byte[] resultBytes = new byte[len + 1];
        int middleValue = (int) System.currentTimeMillis() % 125 + 3;
        if (isEven) {
            if (0 == middleValue % 2)
                middleValue--;
            resultBytes[0] = (byte) middleValue;
            resultBytes[1] = base64Bytes[middleIndex];
            resultBytes[len] = base64Bytes[middleIndex + 1];
            int startIndex = 2;
            for (int i = 0; i < len; i++) {
                if (i != middleIndex && i != (middleIndex + 1)) {
                    resultBytes[startIndex] = base64Bytes[i];
                    startIndex++;
                }
            }
        } else {
            if (0 != middleValue % 2)
                middleValue--;
            resultBytes[0] = (byte) middleValue;
            resultBytes[1] = base64Bytes[middleIndex];
            int startIndex = 2;
            for (int i = 0; i < len; i++) {
                if (i != middleIndex) {
                    resultBytes[startIndex] = base64Bytes[i];
                    startIndex++;
                }
            }
        }
        LogUtils.d("EncryptUtil", key + " --- " + StringUtils.toString(resultBytes));
        return resultBytes;
    }

    /**
     * 关键字解码
     * 
     * @param encodeKeyBytes
     *            经过{@link this#keyEncode(String)}处理过的byte数组
     * @return 关键字
     */
    public static String keyDecode(byte[] encodeKeyBytes) {
        if (null == encodeKeyBytes || 1 >= encodeKeyBytes.length)
            return "";
        int len = encodeKeyBytes.length - 1;
        byte[] resultBytes = new byte[len];
        int middleIndex = len / 2 - 1;
        boolean isEven = 0 == encodeKeyBytes[0] % 2;
        System.arraycopy(encodeKeyBytes, 1, resultBytes, 0, len);
        if (isEven) {
            byte middleValue = resultBytes[0];
            for (int i = 0; i < middleIndex; i++) {
                resultBytes[i] = resultBytes[i + 1];
            }
            resultBytes[middleIndex] = middleValue;
        } else {
            byte leftMiddleValue = resultBytes[0];
            for (int i = 0; i < middleIndex; i++) {
                resultBytes[i] = resultBytes[i + 1];
            }
            resultBytes[middleIndex] = leftMiddleValue;

            byte rightMiddleValue = resultBytes[len - 1];
            for (int i = len - 1; i > middleIndex; i--) {
                resultBytes[i] = resultBytes[i - 1];
            }
            resultBytes[middleIndex + 1] = rightMiddleValue;
        }
        return new String(Base64.decode(resultBytes, Base64.DEFAULT));
    }

    /**
     * 二行制转字符串
     * 
     * @param b
     * @return
     */
    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase(Locale.getDefault());
    }

    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }
    
    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[] hexStr2ByteArr(String strIn) 互为可逆的转换过程
     * 
     * @param arrB
     *            需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception
     *             本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArr2HexStr(byte[] arrB) {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16).toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB) 互为可逆的转换过程
     * 
     * @param strIn
     *            需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception
     *             本方法不处理任何异常，所有异常全部抛出
     * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>
     */
    public static byte[] hexStr2ByteArr(String strIn) {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }
}
