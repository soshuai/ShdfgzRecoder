package cn.cherish.shdfgzrecoder.okhttp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;

import cn.cherish.shdfgzrecoder.R;
import cn.cherish.shdfgzrecoder.okhttp.entity.BaseApiEntity;
import cn.cherish.shdfgzrecoder.okhttp.utils.SdkUtils;

/**
 * 应用程序异常类：用于捕获异常和提示错误信息
 */
public class AppException extends Exception implements UncaughtExceptionHandler {

    private static final long               serialVersionUID          = -6115768533590462076L;

    private final static boolean            Debug                     = true;                 // 是否保存错误日志

    /** 定义异常类型 */
    public final static byte                TYPE_NETWORK              = 0x01;
    public final static byte                TYPE_SOCKET               = 0x02;
    public final static byte                TYPE_HTTP_CODE            = 0x03;
    public final static byte                TYPE_HTTP_ERROR           = 0x04;
    public final static byte                TYPE_XML                  = 0x05;
    public final static byte                TYPE_IO                   = 0x06;
    public final static byte                TYPE_RUN                  = 0x07;
    public final static byte                TYPE_JSON                 = 0x08;
    public final static byte                TYPE_ENCRYPT              = 0x09;
    public final static byte                TYPE_SERVER               = 0x010;
    public final static byte                TYPE_AUDIO_FILE_NOT_FOUND = 0x011;

    private byte                            type;
    private int                             code;
    private boolean                         processed;
    private static Toast toast;

    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private AppException() {
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    private AppException(byte type, int code, Exception excp) {
        super(excp);
        this.type = type;
        this.code = code;
        if (Debug && excp != null) {
            this.saveErrorLog(excp);
        }
    }

    public int getCode() {
        return this.code;
    }

    public int getType() {
        return this.type;
    }

    /**
     * 提示友好的错误信息
     */
    public void makeToast(Activity context) {
        /*
         * if (toast == null) { toast = Toast.makeText(context, getDescp(context), Toast.LENGTH_LONG); //toast.setGravity(Gravity.TOP, 0, 0); } else
         * toast.setText(getDescp(context)); toast.show();
         */
        /*
         * if (supertoast == null) { supertoast = new SuperToast(context); supertoast.setGravity(Gravity.TOP, 0, 0); supertoast.setAnimations(Animations.FADE);
         * supertoast.setDuration(SuperToast.Duration.SHORT); } supertoast.setText(getDescp(context)); if (!supertoast.isShowing()) { supertoast.show(); }
         */
        // CroutonUtils.showError(context, getDescp(context));
        String errorMsg = getDescp(context);
        if (!TextUtils.isEmpty(errorMsg)) {
////            CroutonUtils.showError(context, errorMsg);
        }
    }

    /**
     * 获取异常信息
     * 
     * @param ctx
     * 
     * @return
     */
    public String getDescp(Context ctx) {
        try {
            switch (this.getType()) {

            case TYPE_HTTP_CODE:
                String err = ctx.getString(R.string.exception_error_http_status_code, this.getCode());
                return err;

            case TYPE_HTTP_ERROR:
                return ctx.getResources().getText(R.string.exception_error_http).toString();

            case TYPE_SOCKET:
                return ctx.getResources().getText(R.string.exception_error_socket).toString();

            case TYPE_NETWORK:
                return ctx.getResources().getText(R.string.exception_error_network_not_connected).toString();

            case TYPE_XML:
                return ctx.getResources().getText(R.string.exception_error_xml_parser_failed).toString();

            case TYPE_JSON:
                return ctx.getResources().getText(R.string.exception_error_json_parser_failed).toString();

            case TYPE_IO:
                return ctx.getResources().getText(R.string.exception_error_io).toString();

            case TYPE_AUDIO_FILE_NOT_FOUND:
                return ctx.getResources().getText(R.string.exception_error_audio_file_not_found).toString();

            case TYPE_SERVER:
                if (code == BaseApiEntity.ERROR_CODE_ICODE_ERROR || code == BaseApiEntity.ERROR_CODE_SESSTION_TIMOUT) {
                    return null;
                }
                String descp = BaseApiEntity.ERROR_DESCS.get(code);
                if (descp == null)
                    descp = "unknown code " + code;
                return descp;

            case TYPE_RUN:
                return this.getMessage().substring(this.getMessage().indexOf(":") + 1);
            }
            return this.getMessage().substring(this.getMessage().indexOf(":") + 1);
        } catch (Exception e) {
            return "系统错误";
        }
    }

    /**
     * 保存异常日志
     * 
     * @param excp
     */
    public void saveErrorLog(Exception excp) {
        String errorlog = "errorlog.txt";
        String savePath = "";
        String logFilePath = "";
        FileWriter fw = null;
        PrintWriter pw = null;
        try {
            // 判断是否挂载了SD卡
            String storageState = Environment.getExternalStorageState();
            if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/joyEnglish/Log/";
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                logFilePath = savePath + errorlog;
            }
            // 没有挂载SD卡，无法写文件
            if (logFilePath == "") {
                return;
            }
            File logFile = new File(logFilePath);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            fw = new FileWriter(logFile, true);
            pw = new PrintWriter(fw);
            pw.println("--------------------");
            pw.println(DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())));
            pw.println("--------------------");
            excp.printStackTrace(pw);
            pw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                }
            }
        }

    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public static AppException http(int code) {
        return new AppException(TYPE_HTTP_CODE, code, null);
    }

    public static AppException http(Exception e) {
        return new AppException(TYPE_HTTP_ERROR, 0, e);
    }

    public static AppException socket(Exception e) {
        return new AppException(TYPE_SOCKET, 0, e);
    }

    public static AppException io(Exception e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            return new AppException(TYPE_NETWORK, 0, e);
        } else if (e instanceof IOException) {
            return new AppException(TYPE_IO, 0, e);
        }
        return run(e);
    }

    public static AppException audioFileNotFound(Exception e) {
        return new AppException(TYPE_AUDIO_FILE_NOT_FOUND, 0, e);
    }

    public static AppException xml(Exception e) {
        return new AppException(TYPE_XML, 0, e);
    }

    public static AppException json(Exception e) {
        return new AppException(TYPE_JSON, 0, e);
    }

    public static AppException encrypt(Exception e) {
        return new AppException(TYPE_ENCRYPT, 0, e);
    }

    public static AppException network(Exception e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            return new AppException(TYPE_NETWORK, 0, e);
        } else if (e instanceof SocketException) {
            return socket(e);
        }
        return http(e);
    }

    public static AppException run(Exception e) {
        return new AppException(TYPE_RUN, 0, e);
    }

    public static AppException server(int code) {
        return new AppException(TYPE_SERVER, code, null);
    }

    /**
     * 获取APP异常崩溃处理对象
     *
     * 
     * @return
     */
    public static AppException getAppExceptionHandler() {
        return new AppException();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }

    }

    /**
     * 自定义异常处理:收集错误信息&发送错误报告
     * 
     * @param ex
     * 
     * @return true:处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
//        if (ex == null) {
//            return false;
//        }
//
//        final Context context = AppManager.getAppManager().currentActivity();
//
//        if (context == null) {
//            return false;
//        }
//
//        final String crashReport = getCrashReport(context, ex);
// //       StatisHelper.reportError(context, crashReport);
        return true;
    }

    /**
     * 获取APP崩溃异常报告
     * 
     * @param ex
     * 
     * @return
     */
    private String getCrashReport(Context context, Throwable ex) {
        PackageInfo pinfo = SdkUtils.getPackageInfo(context);
        StringBuffer exceptionStr = new StringBuffer();
        exceptionStr.append("Version: " + pinfo.versionName + "(" + pinfo.versionCode + ")\n");
        exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.MODEL + ")\n");
        exceptionStr.append("Exception: " + ex.getMessage() + "\n");
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            exceptionStr.append(elements[i].toString() + "\n");
        }
        return exceptionStr.toString();
    }
}
