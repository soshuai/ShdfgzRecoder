package cn.cherish.shdfgzrecoder.okhttp.utils;

import java.io.File;

/**
 * 数据下载回调接口
 * 
 */
public interface HttpDownloadListener {

    public void onCancel(File file);

    public void onStart(File file);

    public void onDownLoading(long downloaded, long total);

    public void onFailure(Exception e);

    public void onSuccess(File file);
}
