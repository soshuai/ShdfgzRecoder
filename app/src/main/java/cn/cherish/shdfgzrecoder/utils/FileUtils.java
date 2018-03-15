package cn.cherish.shdfgzrecoder.utils;

import android.os.Environment;

import java.io.File;

public class FileUtils {
    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath();
    public static final String NAME = "AAABBB";

    public static String getAppPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(SD_PATH);
        sb.append(File.separator);
        sb.append(NAME);
        sb.append(File.separator);
        return sb.toString();
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                String[] filePaths = file.list();
                for (String path : filePaths) {
                    deleteFile(filePath + File.separator + path);
                }
                file.delete();
            }
        }
    }

    public static String getPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }else{
            return getAppPath();
        }
        return sdDir.toString();
    }


}
