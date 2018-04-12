package cn.cherish.shdfgzrecoder.okhttp.utils;

/**
 * 数据下载回调接口
 * 
 */
public class HttpDownloadOptions {

    private HttpDownloadListener       progressListener;
    private DownloadCancellationSignal cancellationSignal;

    public HttpDownloadOptions() {
        this(null, null);
    }

    public HttpDownloadOptions(HttpDownloadListener progressListener, DownloadCancellationSignal cancellationSignal) {
        super();
        this.progressListener = progressListener;
        this.cancellationSignal = cancellationSignal;

        if (this.progressListener == null) {
            this.progressListener = new NullDownloadProgressListener();
        }

        if (this.cancellationSignal == null) {
            this.cancellationSignal = new NullCancellationSignal();
        }
    }

    public HttpDownloadListener getProgressListener() {
        return progressListener;
    }

    public DownloadCancellationSignal getCancellationSignal() {
        return cancellationSignal;
    }

}
