package cn.cherish.shdfgzrecoder.okhttp.utils;

import java.io.File;

public class NullDownloadProgressListener implements HttpDownloadListener {

    @Override
    public void onStart(File file) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDownLoading(long downloaded, long total) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onFailure(Exception e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSuccess(File file) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onCancel(File file) {
        // TODO Auto-generated method stub
    }

}
