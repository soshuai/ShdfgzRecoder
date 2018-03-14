package cn.cherish.shdfgzrecoder.utils;

import android.view.InputDevice;
import android.widget.Toast;

import cn.cherish.shdfgzrecoder.AppContext;


public final class BluetoothController {

    public static final int A_BTN   = 1;
    public static final int IOS_BTN = 2;
    public static final int X_BTN   = 3;
    public static final int TRI_BTN = 4;

    public static final boolean checkDevice(InputDevice id) {
        if (id != null) {
            Toast.makeText(AppContext.getInstance(), id.getName(), Toast.LENGTH_SHORT).show();
            String deviceName = id.getName().toLowerCase();
            return deviceName.contains("bt") || deviceName.startsWith("xgame") || deviceName.contains("bluetooth");
        }
        return false;
    }

    public static final int getControllerKey(int keyCode) {
        // Toast.makeText(AppContext.getInstance(), keyCode + "", Toast.LENGTH_SHORT).show();
        if (keyCode == 97) {// || keyCode == 66) {
            // a 按键
            return A_BTN;
        } else if (keyCode == 99) {// || keyCode == 67) {
            // ios按键
            return IOS_BTN;
        } else if (keyCode == 100) {// || keyCode == 62) {
            return TRI_BTN;
        } else if (keyCode == 96) {// || keyCode == 23) {
            return X_BTN;
        }
        return 0;
    }
}
