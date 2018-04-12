package cn.cherish.shdfgzrecoder.okhttp;

import android.content.Context;
import android.text.TextUtils;

import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import cn.cherish.shdfgzrecoder.AppContext;
import cn.cherish.shdfgzrecoder.okhttp.entity.BaseApiDataEntity;
import cn.cherish.shdfgzrecoder.okhttp.entity.BaseApiEntity;
import cn.cherish.shdfgzrecoder.okhttp.entity.BaseApiListEntity;
import cn.cherish.shdfgzrecoder.okhttp.entity.BaseEntity;
import cn.cherish.shdfgzrecoder.okhttp.utils.BaseHttpRequstHandler;
import cn.cherish.shdfgzrecoder.okhttp.utils.HttpHandler;
import cn.cherish.shdfgzrecoder.okhttp.utils.HttpResponseListener;
import cn.cherish.shdfgzrecoder.okhttp.utils.HttpsHandler;
import cn.cherish.shdfgzrecoder.okhttp.utils.JsonUtils;
import cn.cherish.shdfgzrecoder.utils.LogUtils;

/**
 * API交互基类：提供各种原始方法; 业务api集合类需继承自该类
 */
public abstract class BaseApi {

    public static final boolean IS_CONNECT_DEBUG_SERVER  = false;

    private static final String TAG                      = BaseApi.class.getSimpleName();

    protected static String hostUrl                  = "";
    protected static String httpsHostUrl             = "";


    public final static String ONLINE_API_HOSTURL       = "http://139.224.17.133/shgbit/";
    private final static String DEBUG_API_HOSTURL        = "http://139.196.182.17/shgbit/";

    private final static String HTTPS_ONLINE_API_HOSTURL = "https://121.40.63.64:442/";
    private final static String HTTPS_DEBUG_API_HOSTURL  = "https://27.115.24.102:8182/";

    // private final static String DEBUG_API_HOSTURL =
    // "http://192.168.128.14:8080/hourw/";

    public static String getAesEncryptParams(Map<String, String> orgPars) throws AppException {
        return getAesEncryptParams(orgPars, "utf8");
    }

    /**
     * param orgPars param formatCode(编码格式)
     */
    public static String getAesEncryptParams(Map<String, String> orgPars, String formatCode) throws AppException {
        Iterator<Map.Entry<String, String>> it = orgPars.entrySet().iterator();
        StringBuffer sb = new StringBuffer();
        while (it.hasNext()) {
            Map.Entry<String, String> me = it.next();
            sb.append(me.getKey()).append("=").append(me.getValue()).append("&");
        }
//        sb.append("token=").append(AppContext.getInstance().getToken());
//        try {
//            LogUtils.d(TAG, AppContext.getInstance().getLoginContext().getAesEncrypt() + "");
//            return AppContext.getInstance().getLoginContext().getAesEncrypt()
//                    .encrytorAsString(sb.toString(), formatCode);
//        } catch (InvalidKeyException e) {
//            throw AppException.encrypt(e);
//        } catch (IllegalBlockSizeException e) {
//            throw AppException.encrypt(e);
//        } catch (BadPaddingException e) {
//            throw AppException.encrypt(e);
//        }
        return null;
    }

    public static void initialize(Context context) {
        HttpHandler.initialize(context);
        HttpsHandler.initialize(context);
        hostUrl = IS_CONNECT_DEBUG_SERVER ? DEBUG_API_HOSTURL : ONLINE_API_HOSTURL;
        httpsHostUrl = IS_CONNECT_DEBUG_SERVER ? HTTPS_DEBUG_API_HOSTURL : HTTPS_ONLINE_API_HOSTURL;
    }

    /**
     * http get数据请求
     * 
     * @see #get(Context, Class, String, HashMap, HttpResponseListener)
     */
    protected <T extends BaseApiEntity> T get(Context context, Class<T> entityClass, String urlPath) {
        return get(context, entityClass, urlPath, null, null);
    }

    /**
     * http get数据请求
     * 
     * @see #requestBase(Context, String, String, HashMap, HttpResponseListener, boolean)
     */
    protected <T extends BaseApiEntity> T get(Context context, Class<T> entityClass, String urlPath,
                                              HashMap<String, String> map, HttpResponseListener handle) {
        return requestBase(context, "get", entityClass, urlPath, map, handle, false);
    }

    /**
     * http post数据请求
     * 
     * @see #requestBase(Context, String, String, HashMap, HttpResponseListener, boolean)
     */
    protected <T extends BaseApiEntity> T post(Context context, Class<T> entityClass, String urlPath,
                                               HashMap<String, String> map, HttpResponseListener handle) {
        return requestBase(context, "post", entityClass, urlPath, map, handle, false);
    }

    /**
     * https get数据请求
     * 
     * @see #requestBase(Context, String, String, HashMap, HttpResponseListener, boolean)
     */
    protected <T extends BaseApiEntity> T getByHttps(Context context, Class<T> entityClass, String urlPath,
                                                     HashMap<String, String> map, HttpResponseListener handle) {
        return requestBase(context, "get", entityClass, urlPath, map, handle, true);
    }

    protected <T extends BaseApiEntity> T httpsGet(Context context, Class<T> entityClass, String urlPath,
                                                   HashMap<String, String> map, HttpResponseListener handle) throws AppException {
        T result = requestBase(context, "get", entityClass, urlPath, map, handle, true);
        result.checkServerResult();
        return result;
    }

    protected <T extends BaseApiEntity> T httpsGet(Context context, Class<T> entityClass, String urlPath,
                                                   HashMap<String, String> map) throws AppException {
        return httpsGet(context, entityClass, urlPath, map, null);
    }

    protected <T extends BaseApiEntity> T getByHttps(Context context, Class<T> entityClass, String urlPath,
                                                     HashMap<String, String> map) {
        return getByHttps(context, entityClass, urlPath, map, null);
    }

    /**
     * https post数据请求
     * 
     * @see #requestBase(Context, String, String, HashMap, HttpResponseListener, boolean)
     */
    protected <T extends BaseApiEntity> T postByHttps(Context context, Class<T> entityClass, String urlPath,
                                                      HashMap<String, String> map, HttpResponseListener handle) {
        return requestBase(context, "post", entityClass, urlPath, map, handle, true);
    }

    protected <T extends BaseApiEntity> T httpsPost(Context context, Class<T> entityClass, String urlPath,
                                                    HashMap<String, String> map, HttpResponseListener handle) throws AppException {
        T result = requestBase(context, "post", entityClass, urlPath, map, handle, true);
        result.checkServerResult();
        return result;
    }

    protected <T extends BaseApiEntity> T httpsPost(Context context, Class<T> entityClass, String urlPath,
                                                    HashMap<String, String> map) throws AppException {
        return httpsPost(context, entityClass, urlPath, map, null);
    }

    protected <T extends BaseApiEntity> T postByHttps(Context context, Class<T> entityClass, String urlPath,
                                                      HashMap<String, String> map) {
        return postByHttps(context, entityClass, urlPath, map, null);
    }

    /**
     * 数据请求并解析
     * 
     * @see #requestBase(Context, String, String, HashMap, HttpResponseListener, boolean)
     */
    protected <T extends BaseApiEntity> T requestBase(Context context, String method, Class<T> entityClass,
                                                      String urlPath, HashMap<String, String> params, HttpResponseListener handle, boolean isHttpsRequest) {
        // 异常处理
        try {
            entityClass.getDeclaredConstructor(Integer.class, String.class);
        } catch (SecurityException e2) {
            LogUtils.v(TAG, e2.getMessage());
        } catch (NoSuchMethodException e2) {
            String error = String.format("%s实体类必须包含%s(Integer, String)带参构造函数（用于处理api错误信息等）",
                    entityClass.getSimpleName(), entityClass.getSimpleName());
            throw new RuntimeException(error);
        }

        T entity = null;
        try {
            String result = requestBase(context, method, urlPath, params, handle, isHttpsRequest);
            if (TextUtils.isEmpty(result)) {
                entity = entityClass.getDeclaredConstructor(Integer.class, String.class).newInstance(
                        BaseApiEntity.ERROR_CODE_NET, "网络异常");
            } else {
                entity = JsonUtils.parseObject(result, entityClass);
                if (null == entity) {
                    entity = entityClass.getDeclaredConstructor(Integer.class, String.class).newInstance(
                            BaseApiEntity.ERROR_CODE_JSON, "json格式不正确");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                entity = entityClass.getDeclaredConstructor(Integer.class, String.class).newInstance(
                        BaseApiEntity.ERROR_CODE_UNKOWN, "未知异常");
            } catch (Exception e1) {
            }
        }

        return entity;
    }

    /**
     * 数据请求
     * 
     * @param context
     *            上下文
     * @param method
     *            get/post
     * @param urlPath
     *            api路径，如：/api/get_task
     * @param params
     *            接口参数
     * @param handle
     *            接口回调
     * @param isHttpsRequest
     *            是否为https请求
     * 
     * @return 数据字符串
     */
    protected String requestBase(Context context, String method, String urlPath, HashMap<String, String> params,
                                 HttpResponseListener handle, boolean isHttpsRequest) {
        String result = "";
        if (!urlPath.startsWith("http"))
            urlPath = hostUrl + urlPath;

        params = addCommonParams(params);
        if (isHttpsRequest) {
            LogUtils.i(TAG, "https url : " + urlPath + " / " + method);
            LogUtils.i(TAG, "https params : " + params.toString());
        } else {
            LogUtils.i(TAG, "http url : " + urlPath + " / " + method);
            LogUtils.i(TAG, "http params : " + params.toString());
        }
        BaseHttpRequstHandler requestHandler;
        try {
            requestHandler = isHttpsRequest ? HttpsHandler.initialize(context) : HttpHandler.initialize(context);
            if (handle == null) {
                handle = HttpResponseListener.DefaultHttpResponseListener.instance;
            }

            if ("get".equalsIgnoreCase(method)) {
                result = requestHandler.getMethod(urlPath, null, params, handle);
            } else if ("post".equalsIgnoreCase(method)) {
                result = requestHandler.postMethod(urlPath, null, null, params, handle);
            } else {
                LogUtils.e("TAG", "Unsupport method : " + method);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e);
        }
        if (isHttpsRequest) {
            LogUtils.i(TAG, "https result : " + result);
        } else {
            LogUtils.i(TAG, "http result : " + result);
        }
        return result;
    }

    private HashMap<String, String> addCommonParams(HashMap<String, String> params) {
        if (null == params)
            params = new HashMap<String, String>();
        // TODO 添加公共管api参数，校验等
        params.put("_dc", UUID.randomUUID().getMostSignificantBits() + "");
        return params;
    }

    /**
     * 列表数据请求: get/http
     * 
     * @throws AppException
     * 
     * @see ApiBase#requestApiList(Context, String, String, Class, HashMap, HttpResponseListener, boolean)
     */
    protected <T extends BaseEntity> BaseApiListEntity<T> requestApiList(Context context, String urlPath,
                                                                         Class<T> entityClass, HashMap<String, String> params) throws AppException {
        return requestApiList(context, "get", urlPath, entityClass, params, null, false);
    }
    
    protected <T extends BaseEntity> BaseApiListEntity<T> requestApiList(Context context, String method, String urlPath,
                                                                         Class<T> entityClass, HashMap<String, String> params) throws AppException {
        return requestApiList(context, method, urlPath, entityClass, params, null, false);
    }

    protected <T extends BaseEntity> BaseApiListEntity<T> listByHttps(Context context, String urlPath,
                                                                      Class<T> entityClass, HashMap<String, String> params) throws AppException {
        return requestApiList(context, "get", urlPath, entityClass, params, null, true);
    }

    /**
     * 列表数据请求
     * 
     * @throws AppException
     * 
     * @see ApiBase#requestTemplate(Context, String, String, Class, Class, HashMap, HttpResponseListener, boolean)
     */
    @SuppressWarnings("unchecked")
    protected <T extends BaseEntity> BaseApiListEntity<T> requestApiList(Context context, String method,
                                                                         String urlPath, Class<T> entityClass, HashMap<String, String> params, HttpResponseListener handle,
                                                                         boolean isHttpsRequest) throws AppException {
        Object obj = requestTemplate(context, method, urlPath, BaseApiListEntity.class, entityClass, params, handle,
                isHttpsRequest);
        if (null != obj)
            return (BaseApiListEntity<T>) obj;
        return null;
    }

    /**
     * data数据请求: get/http
     * 
     * @throws AppException
     * 
     * @see ApiBase#requestApiData(Context, String, String, Class, HashMap, HttpResponseListener, boolean)
     */
    protected <T extends BaseEntity> BaseApiDataEntity<T> requestApiData(Context context, String urlPath,
                                                                         Class<T> entityClass, HashMap<String, String> params) throws AppException {
        return requestApiData(context, "get", urlPath, entityClass, params, null, false);
    }

    /**
     * data数据请求
     * 
     * @throws AppException
     * 
     * @see ApiBase#requestTemplate(Context, String, String, Class, Class, HashMap, HttpResponseListener, boolean)
     */
    @SuppressWarnings("unchecked")
    protected <T extends BaseEntity> BaseApiDataEntity<T> requestApiData(Context context, String method,
                                                                         String urlPath, Class<T> entityClass, HashMap<String, String> params, HttpResponseListener handle,
                                                                         boolean isHttpsRequest) throws AppException {
        Object obj = requestTemplate(context, method, urlPath, BaseApiDataEntity.class, entityClass, params, handle,
                isHttpsRequest);
        if (null != obj)
            return (BaseApiDataEntity<T>) obj;
        return null;
    }

    /**
     * 请求模版数据
     * 
     * @param outerClass
     *            外层对象class
     * @param templateClass
     *            模版对象class
     * @see ApiBase#requestBase(Context, String, String, HashMap, HttpResponseListener, boolean)
     * 
     * @return OuterClass&lt;TemplateClass&gt;
     * @throws AppException
     */
    protected <O extends BaseApiEntity, T extends BaseEntity> Object requestTemplate(Context context, String method,
                                                                                     String urlPath, Class<O> outerClass, Class<T> templateClass, HashMap<String, String> params,
                                                                                     HttpResponseListener handle, boolean isHttpsRequest) throws AppException {
        // 异常处理
        try {
            outerClass.getDeclaredConstructor(Integer.class, String.class);
        } catch (SecurityException e2) {
            LogUtils.v(TAG, e2.getMessage());
        } catch (NoSuchMethodException e2) {
            String error = String.format("%s实体类必须要实现带参构造函数（用于处理api错误信息等）", outerClass.getSimpleName());
            throw new RuntimeException(error);
        }

        BaseApiEntity entity = null;
        try {
            String result = requestBase(context, method, urlPath, params, handle, isHttpsRequest);
            if (TextUtils.isEmpty(result)) {
                entity = outerClass.getDeclaredConstructor(Integer.class, String.class).newInstance(
                        BaseApiEntity.ERROR_CODE_NET, "网络异常");
            } else {
                entity = (BaseApiEntity) JsonUtils.parseTemplateObject(result, outerClass, templateClass);
                if (null == entity) {
                    entity = outerClass.getDeclaredConstructor(Integer.class, String.class).newInstance(
                            BaseApiEntity.ERROR_CODE_JSON, "json格式不正确");
                }
            }
        } catch (Exception e) {
            try {
                entity = outerClass.getDeclaredConstructor(Integer.class, String.class).newInstance(
                        BaseApiEntity.ERROR_CODE_UNKOWN, "未知异常");
            } catch (Exception e1) {
            }
        }
        entity.checkServerResult();
        return entity;
    }

}
