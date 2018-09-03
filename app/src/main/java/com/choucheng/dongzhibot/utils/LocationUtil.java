package com.choucheng.dongzhibot.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.choucheng.dongzhibot.DongZhiApplication;
import com.choucheng.dongzhibot.activity.MainActivity;
import com.vondear.rxtool.view.RxToast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocationUtil {
    private double lng;
    private double lat;
    private String lat_tv;
    private String lng_tv;
    private int icon;
    private int number;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    public void location() {
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置超时时间
        mLocationOption.setHttpTimeOut(20000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //初始化定位
        mLocationClient = new AMapLocationClient(DongZhiApplication.getContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        mLocationClient.startLocation();

    }


    private LocationListeners theListener;

    public LocationUtil(LocationListeners theListener, int num) {
        this.theListener = theListener;
        this.number = num;
    }

    public void send() {
        theListener.send(lat_tv, lng_tv);
    }

    //异步获取定位结果
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                if (amapLocation.getErrorCode() == 0) {
                    //解析定位结果
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    amapLocation.getCountry();//国家信息
                    amapLocation.getProvince();//省信息
                    amapLocation.getCity();//城市信息
                    amapLocation.getDistrict();//城区信息
                    amapLocation.getStreet();//街道信息
                    amapLocation.getStreetNum();//街道门牌号信息
                    amapLocation.getCityCode();//城市编码
                    amapLocation.getAdCode();//地区编码
                    amapLocation.getAoiName();//获取当前定位点的AOI信息
                    //获取定位时间
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    df.format(date);
                    lat_tv = String.valueOf(amapLocation.getLatitude());
                    lng_tv = String.valueOf(amapLocation.getLongitude());
                    send();
                    if (number==1){
                        RxToast.normal(amapLocation.getCity());
                    }

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };
}