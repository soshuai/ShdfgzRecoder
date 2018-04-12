package cn.cherish.shdfgzrecoder.okhttp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;

import cn.cherish.shdfgzrecoder.utils.LogUtils;

/**
 * 开发工具类
 * 
 * @author zj_chongzi
 */
public class SdkUtils {
    private SdkUtils() {
    }

    /**
     * 判断是否模拟器。如果返回TRUE，则当前是模拟器
     * 
     * @param context
     *            Context
     * @return boolean
     */
    public static boolean isEmulator(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if (imei == null || imei.equals("000000000000000")) {
            return true;
        }
        return false;
    }

    /**
     * 手机操作系统是否>= level5 2.0
     * 
     * @return
     */
    public static boolean hasAndroid2_0() {
        return Build.VERSION.SDK_INT >= 5;// Build.VERSION_CODES.ECLAIR;
    }

    /**
     * 手机操作系统是否>= level7 2.1
     * 
     * @return
     */
    public static boolean hasAndroid2_1() {
        return Build.VERSION.SDK_INT >= 7;// Build.VERSION_CODES.ECLAIR_MR1
    }

    /**
     * 手机操作系统是否>=Froyo level8 2.2
     * 
     * @return
     */
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= 8;// Build.VERSION_CODES.FROYO;
    }

    /**
     * 手机操作系统是否>=Gingerbread level9 2.3.1
     * 
     * @return
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= 9;// 低版本Build.VERSION_CODES.GINGERBREAD未定义
    }

    /**
     * 手机操作系统是否>=Honeycomb level11 3.0
     * 
     * @return
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= 11;// Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * 手机操作系统是否>=HoneycombMR1 level12 3.1
     * 
     * @return
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= 12;// Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * 手机操作系统是否>=HoneycombMR2 level12 3.2
     * 
     * @return
     */
    public static boolean hasHoneycombMR2() {
        return Build.VERSION.SDK_INT >= 13;// Build.VERSION_CODES.HONEYCOMB_MR2;
    }

    /**
     * 手机操作系统是否>=HoneycombMR1 level14 4.0
     * 
     * @return
     */
    public static boolean hasIceCreamSandwich_4_0() {
        return Build.VERSION.SDK_INT >= 14;// Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * 手机操作系统是否>=HoneycombMR1 level16 4.1
     * 
     * @return
     */
    public static boolean hasJellyBean_4_1() {
        return Build.VERSION.SDK_INT >= 16;// Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * 手机操作系统是否>=HoneycombMR1 level17 4.2
     * 
     * @return
     */
    public static boolean hasJellyBean_4_2() {
        return Build.VERSION.SDK_INT >= 17;// Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * 手机操作系统是否>=HoneycombMR1 level18 4.3
     * 
     * @return
     */
    public static boolean hasJellyBean_4_3() {
        return Build.VERSION.SDK_INT >= 18;// Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * 手机操作系统是否>=HoneycombMR1 level19 4.4
     * 
     * @return
     */
    public static boolean hasKitkat_4_4() {
        return Build.VERSION.SDK_INT >= 19;
    }

    /**
     * 手机操作系统是否>=HoneycombMR1 level21 5.0
     * 
     * @return
     */
    public static boolean hasLollipop_5_0() {
        return Build.VERSION.SDK_INT >= 21;
    }

    /**
     * 获取packageinfo
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), Context.MODE_PRIVATE);
        } catch (NameNotFoundException e) {
        }
        return info;
    }

    /**
     * 返回当前程序versionName
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            versionName = getPackageInfo(context).versionName;
        } catch (Exception e) {
        }
        return versionName;
    }

    /**
     * 返回当前程序versionCode
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = getPackageInfo(context).versionCode;
        } catch (Exception e) {
        }
        return versionCode;
    }

    /**
     * 获取sim卡手机号
     * 
     * @return String phome num 可能为空
     */
    public static String getSIMCardPhoneNumber(Context context) {
        String SIMCardPhoneNumber = null;
        if (null != context) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            SIMCardPhoneNumber = tm.getLine1Number();
        }
        return TextUtils.isEmpty(SIMCardPhoneNumber) ? "" : SIMCardPhoneNumber;
    }

    /**
     * 获取手机卡标识码imsi
     * 
     * @param context
     *            Context
     * @return imsi 没有手机卡是为空
     */
    public static String getImsiId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
    }

    /**
     * 获取手机标识imei
     * 
     * @param context
     *            Context
     * @return imeiid pad为空
     */
    public static String getImeiId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取mac地址
     * 
     * @param context
     *            Context
     * @return mac
     */
    public static String getMac(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取分辨率
     * 
     * @param context
     *            Context
     * @return x*y
     */
    public static String getResolution(Activity context) {
        Display dis = context.getWindowManager().getDefaultDisplay();
        Point p = new Point();
        dis.getSize(p);
        return p.x + "*" + p.y;
    }

    /**
     * 获取androidid <br>
     * 所有的CDMA设备对于 ANDROID_ID 和 TelephonyManager.getDeviceId() 返回相同的值（只要在设置时添加了谷歌账户）<br>
     * 
     * @param context
     *            Context
     * @return androidid
     */
    public static String getAndroidId(Context context) {
        String androidid = "";
        try {
            androidid = android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
        }
        return androidid;
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * App是否安装
     * 
     * @param context
     *            Context
     * @param packageName
     *            package name
     * @return boolean
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            return null != context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * packagename对应的应用当前是否为前台进程
     * 
     * @param context
     *            Context
     * @param packageName
     *            package name
     * @return boolean
     */
    public static boolean isAppForegroundProcess(Context context, String packageName) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).baseActivity;
            return packageName.equals(cn.getPackageName());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取arm内存大小
     * 
     * @since api 16
     */
    @SuppressLint("NewApi")
    public static long getDeviceARMSize(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
    }

    /**
     * 开启StrickMode
     */
    public static void enableStrictMode() {
        if (hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder().detectAll()
                    .penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    /**
     * 关闭StrickMode
     */
    public static void disableStrictMode() {
        if (hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .penaltyLog();
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
        }
    }

    public static boolean checkSdCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getRootDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (null != info)
            return info.getType() == ConnectivityManager.TYPE_WIFI;
        return false;
    }

    public static boolean isWifiAvailable(Context context) {
    	/*
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (null != info && info.isAvailable()) {
            String nettype = getNetworkType(info);
            return ("wifi".equalsIgnoreCase(nettype) || "4g".equalsIgnoreCase(nettype));
        }
        return false;
        */
    	return isWifi(context);
    }

    public static String getNetworkType(NetworkInfo networkInfo) {
        String strNetworkType = "";

        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                // TD-SCDMA networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by 11
                    strNetworkType = "2G";
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by 15
                    strNetworkType = "3G";
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by 13
                    strNetworkType = "4G";
                    break;
                default:
                    if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA")
                            || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                        strNetworkType = "3G";
                    } else {
                        strNetworkType = _strSubTypeName;
                    }

                    break;
                }

                LogUtils.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }

        LogUtils.e("cocos2d-x", "Network Type : " + strNetworkType);

        return strNetworkType;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (null != info)
            return info.isAvailable();
        return false;
    }
}
