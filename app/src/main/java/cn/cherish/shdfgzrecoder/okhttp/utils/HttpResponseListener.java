package cn.cherish.shdfgzrecoder.okhttp.utils;


import cn.cherish.shdfgzrecoder.utils.LogUtils;

/**
 * 网络请求回调接口
 */
public interface HttpResponseListener {

    public void onSuccess(String responseBody, int statusCode);

    public void onFailure(Exception e);

    public void onTimeOut();

    public class DefaultHttpResponseListener implements HttpResponseListener {

        private static final String TAG = DefaultHttpResponseListener.class.getSimpleName();

        public static final DefaultHttpResponseListener instance = new DefaultHttpResponseListener();

        private DefaultHttpResponseListener() {
            super();
        }

        @Override
        public void onSuccess(String responseBody, int statusCode) {
            LogUtils.d(TAG, "http success! code=" + statusCode);
        }

        @Override
        public void onFailure(Exception e) {
            LogUtils.e(TAG, e);
        }

        @Override
        public void onTimeOut() {
            LogUtils.e(TAG, "http timeout!");
        }

    }

}
