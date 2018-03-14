package cn.cherish.shdfgzrecoder.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包
 */
@SuppressLint("SimpleDateFormat")
public abstract class StringUtils {

    private final static SimpleDateFormat dateFormater  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");

    private static final Pattern mobileP       = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
    private static final Pattern nameP         = Pattern.compile("^[a-zA-Z\u4e00-\u9fa5]+$");
    private static final Pattern emailP        = Pattern
                                                                .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    public static boolean isMobile(String mobile) {
        return validate(mobileP, mobile);
    }

    public static boolean isName(String name) {
        return validate(nameP, name);
    }

    public static boolean isEmail(String email) {
        return validate(emailP, email);
    }

    private static boolean validate(Pattern pat, String value) {
        if (!TextUtils.isEmpty(value)) {
            Matcher m = pat.matcher(value);
            return m.matches();
        }
        return false;
    }

    public static String getDate(String fomater) {
        return new SimpleDateFormat(fomater).format(new Date(System.currentTimeMillis()));
    }

    /**
     * 将字符串转为日期类型
     * 
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 判断给定字符串时间是否为今日
     * 
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.format(today);
            String timeDate = dateFormater2.format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 返回long类型的今天的日期
     * 
     * @return
     */
    public static long getToday() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater2.format(cal.getTime());
        curDate = curDate.replace("-", "");
        return Long.valueOf(curDate);
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     * 
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 类似于apache的 isNotBlank
     * 
     * @param input
     * @return
     */
    public static boolean isNotEmpty(String input) {
        return !isEmpty(input);
    }

    /**
     * 字符串转整数
     * 
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     * 
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     * 
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.valueOf(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     * 
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.valueOf(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 字符串转小数
     * 
     * @param b
     * @return 转换异常返回 false
     */
    public static float toFloat(String b) {
        try {
            return Float.valueOf(b);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 将一个InputStream流转换成字符串
     * 
     * @param is
     * @return
     */
    public static String toConvertString(InputStream is) {
        StringBuffer res = new StringBuffer();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader read = new BufferedReader(isr);
        try {
            String line;
            line = read.readLine();
            while (line != null) {
                res.append(line);
                line = read.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != isr) {
                    isr.close();
                    isr.close();
                }
                if (null != read) {
                    read.close();
                    read = null;
                }
                if (null != is) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
            }
        }
        return res.toString();
    }

    public static String toString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (byte b : bytes) {
            if (!isFirst)
                sb.append(",");
            else
                isFirst = false;
            sb.append(String.valueOf(b));
        }
        return sb.toString();
    }

    public static String toString(int[] ints) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (int i : ints) {
            if (!isFirst)
                sb.append(",");
            else
                isFirst = false;
            sb.append(String.valueOf(i));
        }
        toString(new byte[] { 12, 23 });
        return sb.toString();
    }

    /****
     * 全角字符转半角
     * ***/
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /****
     * 半角字符转全角
     * ***/
    public static String toSBC(String input) {
        // 半角转全角：
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127 && c[i] > 32)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    public static byte[] enc_unicode_to_utf8(String str) {
        return enc_unicode_to_utf8(Integer.parseInt(str, 16));
    }

    public static byte[] enc_unicode_to_utf8(long unic) {
        byte[] utf8_charArray = null;
        if (unic <= 0x0000007F) {
            utf8_charArray = new byte[1];
            // * U-00000000 - U-0000007F: 0xxxxxxx
            utf8_charArray[0] = (byte) (unic & 0x7F);
        } else if (unic >= 0x00000080 && unic <= 0x000007FF) {
            utf8_charArray = new byte[2];
            // * U-00000080 - U-000007FF: 110xxxxx 10xxxxxx
            utf8_charArray[1] = (byte) ((unic & 0x3F) | 0x80);
            utf8_charArray[0] = (byte) (((unic >> 6) & 0x1F) | 0xC0);
        } else if (unic >= 0x00000800 && unic <= 0x0000FFFF) {
            utf8_charArray = new byte[3];
            // * U-00000800 - U-0000FFFF: 1110xxxx 10xxxxxx 10xxxxxx
            utf8_charArray[2] = (byte) ((unic & 0x3F) | 0x80);
            utf8_charArray[1] = (byte) (((unic >> 6) & 0x3F) | 0x80);
            utf8_charArray[0] = (byte) (((unic >> 12) & 0x0F) | 0xE0);
        } else if (unic >= 0x00010000 && unic <= 0x001FFFFF) {
            utf8_charArray = new byte[4];
            // * U-00010000 - U-001FFFFF: 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
            utf8_charArray[3] = (byte) ((unic & 0x3F) | 0x80);
            utf8_charArray[2] = (byte) (((unic >> 6) & 0x3F) | 0x80);
            utf8_charArray[1] = (byte) (((unic >> 12) & 0x3F) | 0x80);
            utf8_charArray[0] = (byte) (((unic >> 18) & 0x07) | 0xF0);
        } else if (unic >= 0x00200000 && unic <= 0x03FFFFFF) {
            utf8_charArray = new byte[5];
            // * U-00200000 - U-03FFFFFF: 111110xx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
            utf8_charArray[4] = (byte) ((unic & 0x3F) | 0x80);
            utf8_charArray[3] = (byte) (((unic >> 6) & 0x3F) | 0x80);
            utf8_charArray[2] = (byte) (((unic >> 12) & 0x3F) | 0x80);
            utf8_charArray[1] = (byte) (((unic >> 18) & 0x3F) | 0x80);
            utf8_charArray[0] = (byte) (((unic >> 24) & 0x03) | 0xF8);
        } else if (unic >= 0x04000000 && unic <= 0x7FFFFFFF) {
            utf8_charArray = new byte[6];
            // * U-04000000 - U-7FFFFFFF: 1111110x 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
            utf8_charArray[5] = (byte) ((unic & 0x3F) | 0x80);
            utf8_charArray[4] = (byte) (((unic >> 6) & 0x3F) | 0x80);
            utf8_charArray[3] = (byte) (((unic >> 12) & 0x3F) | 0x80);
            utf8_charArray[2] = (byte) (((unic >> 18) & 0x3F) | 0x80);
            utf8_charArray[1] = (byte) (((unic >> 24) & 0x3F) | 0x80);
            utf8_charArray[0] = (byte) (((unic >> 30) & 0x01) | 0xFC);
        }
        return utf8_charArray;
    }

    public static String join(String[] fileNames, String sep) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fileNames.length - 1; i++) {
            sb.append(fileNames[i]);
            sb.append(sep);
        }
        sb.append(fileNames[fileNames.length - 1]);
        return sb.toString();
    }

    public static String join(List<String> names, String sep) {
        // TODO Auto-generated method stub
        return join(names.toArray(new String[names.size()]), sep);
    }

}
