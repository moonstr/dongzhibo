package com.choucheng.dongzhibot.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kelina on 2017/8/23.
 */

public class PermissionUtil {

    public static String[] getStoragePermission(){
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
    }

    public static String[] getCameraPermission(){
        return new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
    }

    public static String[] getContactPermission(){
        return new String[]{
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE
        };
    }

    public static String[] getSmsPermission(){
        return new String[]{
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_SMS
        };
    }

    /**
     * 获取定位的权限
     */
    public static String[] getLocationPermission() {
        return new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
        };

    }

    public static String[] getBluetoothPermission(){
        return new String[]{
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
    }
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasPermission(Context context, String permission) {
        if (isVersionM()
                && context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasPermission(Context context, String[]... permissions) {
        if (isVersionM()) {
            for (String[] permissionGroup : permissions) {
                for (String permission : permissionGroup) {
                    if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }

                }
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasLowPermission(Context context, String[]... permissions) {
        for (String[] permissionGroup : permissions) {
            for (String permission : permissionGroup) {
                if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }

            }
        }
        return true;
    }

    public static void requestPermission(Activity activity, int requestCode, String permission) {
        requestPermission(activity, requestCode, new String[]{permission});
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermission(Activity activity, int requestCode, String[]... permissions) {
        if (isVersionM()) {
            int count = 0;
            List<String> allPermission = new ArrayList<>();
            for (String[] permissionGroup : permissions) {
                count += permissionGroup.length;
            }
            for (String[] permissionGroup : permissions) {
                for (String permission : permissionGroup) {
                    if (!hasPermission(activity, permission)) {
                        allPermission.add(permission);
                    }
                }
            }
            if(allPermission.size() > 0) {
                String[] permisionArray = new String[allPermission.size()];
                for (int i = 0; i < allPermission.size(); i++) {
                    permisionArray[i] = allPermission.get(i);
                }
                activity.requestPermissions(permisionArray, requestCode);
            }
        }
    }


    public static boolean grantedPermission(int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    public static boolean isVersionM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }

    /**
     * 是否打开GPS
     *
     * @return
     */
    public static boolean isOpenGPS(final Context context) {
        if (context == null) {
            return false;
        }

        LocationManager locationManager = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

}
