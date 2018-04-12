package cn.cherish.shdfgzrecoder.okhttp.utils;

import android.content.Context;
import android.os.Environment;


import java.io.File;

import cn.cherish.shdfgzrecoder.AppContext;
import cn.cherish.shdfgzrecoder.okhttp.utils.MyLocation;

public class CaseRecordUtils {

    public static final String BASE_DIR        = Environment.getExternalStorageDirectory().getAbsolutePath()
                                                       + "/vidyosample/";

    public static final String IMAGE_DIR       = BASE_DIR + "imgs/";
    public static final String AUDIO_DIR       = BASE_DIR + "audios/";
    public static final String VIDEO_DIR       = BASE_DIR + "videos/";
    public static final String UPLOAD_DIR      = BASE_DIR + "upRecorder/";
    public static final String DOWNLOAD_DIR    = BASE_DIR + "download/";
    public static final String AUTO_UPLOAD_DIR = BASE_DIR + "autoupload/";
    public static final String WATER_MARK_DIR  = BASE_DIR + "watermark/";

    public static final int    LOAD_SUCCESS    = 0;
    public static final int    LOAD_FAILED     = 1;
    public static final int    LOAD_CANCELLED  = 2;

    public static final int    timer_interval  = 10000;

    static {
        makeDirectory(IMAGE_DIR);
        makeDirectory(AUDIO_DIR);
        makeDirectory(VIDEO_DIR);
        makeDirectory(UPLOAD_DIR);
        makeDirectory(AUTO_UPLOAD_DIR);
        makeDirectory(DOWNLOAD_DIR);
        makeDirectory(WATER_MARK_DIR);
    }

    private static final void makeDirectory(String directory) {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

//    public static final String getImagePath(int recordId) {
//        return IMAGE_DIR + makeFileId(recordId) + ".jpg";
//    }
//
//    public static final String getAudioPath(int recordId) {
//        return AUDIO_DIR + makeFileId(recordId) + ".mp3";
//    }
//
//    public static final String getVideoPath(int recordId) {
//        return VIDEO_DIR + makeFileId(recordId) + ".mp4";
//    }

//    public static final String getRecordPath(CaseRecordEntity entity) {
//        // TODO Auto-generated method stub
//        if (entity != null) {
//            switch (entity.getRecordType()) {
//            case 1:
//                return getImagePath(entity.getId());
//            case 2:
//                return getAudioPath(entity.getId());
//            case 3:
//                return getVideoPath(entity.getId());
//            case 4:
//                return String.valueOf(entity.getId());
//            }
//        }
//        return null;
//    }

//    public static final String getClickRecordPath(CaseRecordEntity entity) {
//        // TODO Auto-generated method stub
//        if (entity != null) {
//            switch (entity.getRecordType()) {
//            case 1:
//                return getWatermarkImagePath(entity.getId());
//            case 2:
//                return getAudioPath(entity.getId());
//            case 3:
//                return getVideoPath(entity.getId());
//            case 4:
//                return String.valueOf(entity.getId());
//            }
//        }
//        return null;
//    }

//    private static String getWatermarkImagePath(int recordId) {
//        // TODO Auto-generated method stub
//        return WATER_MARK_DIR + makeFileId(recordId) + ".jpg";
//    }

    public static final MyLocation getCurrentLocation(Context context) {
        return MyLocationListener.cacheLocation;
    }
//
//    public static String makeFileId(int recordId) {
//        // TODO Auto-generated method stub
//        return recordId + "_" + AppContext.getLoginUserInfo().getId();
//    }

//    public static File getDownloadFile(CaseRecordEntity entity) {
//        // TODO Auto-generated method stub
//        if (entity != null) {
//            switch (entity.getRecordType()) {
//            case 1:
//                return new File(DOWNLOAD_DIR, entity.getId() + ".jpg");
//            case 2:
//                return new File(DOWNLOAD_DIR, entity.getId() + ".mp3");
//            case 3:
//                return new File(DOWNLOAD_DIR, entity.getId() + ".mp4");
//            }
//        }
//        return null;
//    }
}
