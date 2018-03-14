package cn.cherish.shdfgzrecoder.listener;

import java.io.File;
import java.util.zip.ZipFile;

/**
 * 数据下载回调接口
 * 
 */
public interface UnzipListener {

    public void onStart(ZipFile file);

    public void onUnziping(String entryName, int unzipCount, int entryCount);

    public void onFailure(Exception e);

    public void onSuccess(File file);
}
