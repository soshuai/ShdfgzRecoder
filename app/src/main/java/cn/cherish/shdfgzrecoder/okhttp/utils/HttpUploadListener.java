package cn.cherish.shdfgzrecoder.okhttp.utils;

/**
 * 数据上传回调接口
 * 
 */
public interface HttpUploadListener {

    public void onStart();

    public void onUpLoading(long uploaded, long total);

    public void onFailure(Exception e);

    public void onSuccess(String ret);
}
