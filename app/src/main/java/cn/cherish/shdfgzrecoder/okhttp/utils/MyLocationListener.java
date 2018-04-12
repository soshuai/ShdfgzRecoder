package cn.cherish.shdfgzrecoder.okhttp.utils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class MyLocationListener implements BDLocationListener {

    public static MyLocation cacheLocation = null;

    @Override
    public void onReceiveLocation(BDLocation loc) {
        // TODO Auto-generated method stub
        if (loc != null
                && (loc.getLocType() == BDLocation.TypeGpsLocation || loc.getLocType() == BDLocation.TypeCacheLocation
                        || loc.getLocType() == BDLocation.TypeNetWorkLocation || loc.getLocType() == BDLocation.TypeOffLineLocation)) {
            cacheLocation = new MyLocation(loc.getLatitude(), loc.getLongitude());
        }
    }
}
