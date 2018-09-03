package com.choucheng.dongzhibot;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.bjkj.library.utils.SPUtils;
import com.bumptech.glide.request.target.ViewTarget;
import com.vondear.rxtool.RxTool;

/**
 * Created by liyou on 2018/6/4.
 */

public class DongZhiApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Context context = DongZhiApplication.getContext();
        SPUtils.init(this);
        RxTool.init(this);
        ViewTarget.setTagId(R.id.tag_glide);
    }

    public static Context getContext() {
        return instance;
    }
    private static DongZhiApplication instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
}
