package cn.cherish.shdfgzrecoder.utils;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class SpfUtils {

    private static final String KEY_SHAREDPREFERENCES_NAME = "vidyosample";

    public static void keepPreference(Context context, String key, String value) {
        // Context.MODE_PRIVATE代表在data/data下创建的首选项文件是私有的
        getSharedPreferences(context).edit().putString(key, value).commit();// 记得获取edit对象后一定要commit提交
    }

    /**
     * 当返回空字符串则不存在
     * 
     * @param key
     * @return
     */
    public static String getPreference(Context context, String key) {
        // Context.MODE_PRIVATE代表在data/data下创建的首选项文件是私有的
        return getSharedPreferences(context).getString(key, "");
    }

    /**
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        return getSharedPreferences(context).getString(key, "");
    }

    /**
     * @param key
     * @return
     */
    public static boolean saveString(Context context, String key, String value) {
        return getSharedPreferences(context).edit().putString(key, value).commit();
    }

    /**
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        return getSharedPreferences(context).getInt(key, 0);
    }

    /**
     * @param key
     * @return
     */
    public static boolean saveInt(Context context, String key, int value) {
        return getSharedPreferences(context).edit().putInt(key, value).commit();
    }

    /**
     * 用户是否已登陆
     * 
     * @param context
     * @return
     */
    public static boolean isUserLogin(Context context) {
        return getSharedPreferences(context).getBoolean("is_login", false);
    }

    /**
     * 更新用户登陆状态
     * 
     * @param context
     * @param isLogin
     */
    public static void updateUserLoginStatu(Context context, boolean isLogin) {
        getSharedPreferences(context).edit().putBoolean("is_login", isLogin).commit();
    }
    
    public static void deleteCityListVertion(Context context) {
        // TODO Auto-generated method stub
        SpfUtils.getSharedPreferences(context).edit().putString("cityListVertion", "").commit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(KEY_SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void deleteAutoLogin(Context context) {
        // TODO Auto-generated method stub
      //  SpfUtil.getSharedPreferences(context).edit().putString("loginCode", "").commit();
        SpfUtils.getSharedPreferences(context).edit().putString("password", "").commit();
        SpfUtils.getSharedPreferences(context).edit().putString("isSave", "").commit();
    }
}
