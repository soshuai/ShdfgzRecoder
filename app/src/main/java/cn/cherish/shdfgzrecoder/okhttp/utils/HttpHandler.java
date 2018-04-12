package cn.cherish.shdfgzrecoder.okhttp.utils;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import cn.cherish.shdfgzrecoder.okhttp.AppConfig;
import cn.cherish.shdfgzrecoder.okhttp.utils.HttpRequest.HttpRequestException;

import cn.cherish.shdfgzrecoder.utils.LogUtils;

/**
 * http工具类<br>
 * 提供get、post方法使用及上传下载文件方法（提供自定义handle处理下载进度等）
 * 
 * @author zj_chongzi
 */
public class HttpHandler extends BaseHttpRequstHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    private static HttpHandler  mInstance;

    public synchronized static HttpHandler initialize(Context context) {
        if (null == mInstance)
            mInstance = new HttpHandler(context);
        return mInstance;
    }

    private HttpHandler(Context context) {

    }

    /**
     * 下载文件，保存在内部存储空间中context.getFilesDir(): /data/data/com.yourpackage/files
     * 
     * @param url
     *            下载文件的url
     * @param fileName
     *            保存的文件名
     * @param context
     * @return
     */
    public File download(String url, String fileName, Context context) {
        return download(url, fileName, context, null);
    }

    /**
     * 下载文件，保存在内部存储空间中context.getFilesDir(): /data/data/com.yourpackage/files
     * 
     * @param url
     *            下载文件的url
     * @param fileName
     *            保存的文件名
     * @param context
     * @param handle
     *            自定义回调
     * @return
     */
    public File download(String url, String fileName, Context context, HttpDownloadOptions options) {
        return download(url, new File(AppConfig.PATH_DOWNLOAD_CACHE, fileName), options);
    }

    /**
     * 下载文件
     * 
     * @param url
     *            下载文件的url
     * @param file
     *            指定本地文件
     * @return
     */
    public File download(String url, File file) {
        return download(url, file, null);
    }

    private long totalWritten = 0;

    /**
     * 下载文件
     * 
     * @param url
     *            下载文件的url
     * @param file
     *            指定本地文件
     * @param handle
     *            自定义回调
     * @return
     */
    public File download(String url, File file, final HttpDownloadOptions defOptions) {
        LogUtils.d(TAG, "download url : " + url);
        setConnectionFactory();
        
        HttpRequest request = HttpRequest.get(url);
        final HttpDownloadOptions options = (defOptions == null ? new HttpDownloadOptions() : defOptions);
        FileOutputStream outputstream = null;
        try {
            options.getProgressListener().onStart(file);

            int responseCode = request.code();
            LogUtils.d(TAG, "response code : " + responseCode);
            if (200 == responseCode) {
                totalWritten = 0;
                final int length = request.getConnection().getContentLength();// 获取文件大小

                outputstream = new FileOutputStream(file) {
                    @Override
                    public void write(byte[] buffer, int byteOffset, int byteCount) throws IOException {
                        super.write(buffer, byteOffset, byteCount);
                        totalWritten += byteCount;
                        options.getProgressListener().onDownLoading(totalWritten, length);
                        if (options.getCancellationSignal().isCancelled()) {
                            throw new IOException("user cancelled");
                        }
                    }
                };
                request.receive(outputstream);

                options.getProgressListener().onSuccess(file);
            } else {
                options.getProgressListener().onFailure(new Exception("request code : " + responseCode));
            }
        } catch (HttpRequestException exception) {
            file = null;
            options.getProgressListener().onFailure(exception.getCause());
        } catch (IOException e) {
            LogUtils.e(TAG, e);
            file = null;
            if (!options.getCancellationSignal().isCancelled()) {
                options.getProgressListener().onFailure(e);
            }
        } finally {
            if (outputstream != null) {
                try {
                    outputstream.close();
                } catch (IOException e) {
                    LogUtils.e(TAG, e);
                }
            }
            if (options.getCancellationSignal().isCancelled()) {
                options.getProgressListener().onCancel(file);
            }
        }
        return file;
    }

    /**
     * @see #upload(String, String, File, Map, Map, HttpUploadListener)
     */
    public String upload(String url, String fileKey, File file) {
        return upload(url, fileKey, file, null, null, null);
    }

    /**
     * @see #upload(String, String, File, Map, Map, HttpUploadListener)
     */
    public String upload(String url, String fileKey, File file, HttpUploadListener uploadHandle) {
        return upload(url, fileKey, file, null, null, uploadHandle);
    }

    /**
     * 上传文件
     * 
     * @param url
     *            上传地址
     * @param fileKey
     *            上传文件的key
     * @param file
     *            上传文件
     * @param header
     *            请求头
     * @param form
     *            表单数据
     * @param uploadHandle
     *            自定义回调
     * @return 上传结果
     */
    public String upload(String url, String fileKey, File file, Map<String, String> header, Map<String, String> form,
                         final HttpUploadListener uploadHandle) {
        String ret = null;

        HttpRequest request = HttpRequest.post(url);

        try {
            // 设置header
            if (header != null && header.size() > 0) {
                for (Entry<String, String> temp : header.entrySet()) {
                    request.header(temp);
                }
            }

            if (uploadHandle != null) {
                uploadHandle.onStart();
                request.progress(new HttpRequest.UploadProgress() {
                    @Override
                    public void onUpload(long uploaded, long total) {
                        uploadHandle.onUpLoading(uploaded, total);
                    }
                });
            }

            request.part(fileKey, file.getName(), file);
            if (form != null && form.size() > 0) {
                for (String key : form.keySet()) {
                    String value = form.get(key);
                    request.part(key, null == value ? "" : value);
                }
            }

            if (request.ok()) {
                ret = request.body();
                if (uploadHandle != null) {
                    uploadHandle.onSuccess(ret);
                }
            }
        } catch (HttpRequestException exception) {
            exception.printStackTrace();
            file = null;
            if (uploadHandle != null) {
                uploadHandle.onFailure(exception.getCause());
            }

        }
        return ret;
    }
}
