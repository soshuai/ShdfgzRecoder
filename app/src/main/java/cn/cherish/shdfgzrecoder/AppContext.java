package cn.cherish.shdfgzrecoder;

import android.app.Application;
import android.content.Context;

/**
 * Created by kris on 16/1/28.
 */
public class AppContext extends Application {

    private static Context APPLICATION_CONTEXT;
    private static int cameraState;
    private static boolean backCamera;
    private static AppContext       mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp=this;
//        Intent serviceIntent = new Intent(this, CommandService.class);
//        this.startService(serviceIntent);
    }

    public static AppContext getInstance() {
        return mApp;
    }
    public static int getCameraState() {
        return cameraState;
    }

    public static void setCameraState(int cameraState) {
        AppContext.cameraState = cameraState;
    }

    public static boolean getBackCamera() {
        return backCamera;
    }

    public static void setBackCamera(boolean backCamera) {
        AppContext.backCamera = backCamera;
    }
}
