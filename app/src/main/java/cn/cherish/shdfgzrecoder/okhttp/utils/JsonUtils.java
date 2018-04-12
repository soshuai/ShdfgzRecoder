package cn.cherish.shdfgzrecoder.okhttp.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;

import cn.cherish.shdfgzrecoder.utils.LogUtils;

/**
 * json解析工具类
 */
public class JsonUtils {
    public static final String TAG = JsonUtils.class.getSimpleName();

    private JsonUtils() {
    }

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        // 忽略json中多余的字段
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将json字符串解析为目标对象
     * 
     * @param jsonStr
     *            json字符串
     * @param entityClass
     *            对象class
     * 
     * @return 实体类
     */
    public static <T> T parseObject(String jsonStr, Class<T> entityClass) {
        T result = null;
        try {
            // 当json中包含中文时，无法解析时可先转成utf-8的byte后再转
            result = mapper.readValue(jsonStr.getBytes("utf-8"), entityClass);
        } catch (Exception e) {
            LogUtils.e(TAG, e);
        }
        return result;
    }

    /**
     * 将json字符串解析为 "O&lt;T&gt;" 结构的模版数据
     * 
     * @param jsonStr
     *            json字符串
     * @param outerClass
     *            模版外层class
     * @param templateClass
     *            模版class
     * 
     * @return 实体类
     */
    public static Object parseTemplateObject(String jsonStr, Class<?> outerClass, Class<?> templateClass) {
        Object result = null;
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(outerClass, templateClass);
            result = mapper.readValue(jsonStr.getBytes("utf-8"), javaType);
        } catch (Exception e) {
            LogUtils.e(TAG, e);
        }
        return result;
    }

    /**
     * 将json字符串解析为列表数据
     * 
     * @param jsonStr
     *            json字符串
     * @param entityClass
     *            列表内部实体class
     * 
     * @return 列表实体
     */
    public static <T> ArrayList<T> parseList(String jsonStr, Class<T> entityClass) {
        ArrayList<T> result = null;

        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, entityClass);
            result = mapper.readValue(jsonStr.getBytes("utf8"), javaType);
        } catch (Exception e) {
            LogUtils.e(TAG, e);
        }

        return result;
    }

    /**
     * 将json字符串解析为map
     * 
     * @param jsonStr
     *            json字符串
     * @param keyClass
     *            map的key类型
     * @param valueClass
     *            map的value类型
     * 
     * @return HashMap对戏那个
     */
    public static <K, V> HashMap<K, V> parseMap(String jsonStr, Class<K> keyClass, Class<V> valueClass) {
        HashMap<K, V> result = null;

        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(HashMap.class, keyClass, valueClass);
            result = mapper.readValue(jsonStr.getBytes("utf-8"), javaType);
        } catch (Exception e) {
            LogUtils.e(TAG, e);
        }

        return result;
    }

    /**
     * 将数据对象转换为json字符串
     * 
     * @param obj
     *            数据实体
     * 
     * @return json字符串
     */
    public static String toJSONString(Object obj) {
        String result = null;

        try {
            result = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LogUtils.e(TAG, e);
        }

        return result;
    }

}
