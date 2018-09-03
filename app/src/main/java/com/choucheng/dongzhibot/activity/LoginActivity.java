package com.choucheng.dongzhibot.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.bjkj.library.okhttp.HttpCallBack;
import com.bjkj.library.utils.SPUtils;
import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.base.BaseActivity;
import com.choucheng.dongzhibot.modle.DongZhiModle;
import com.choucheng.dongzhibot.utils.Constants;
import com.choucheng.dongzhibot.utils.LocationListeners;
import com.choucheng.dongzhibot.utils.LocationUtil;
import com.vondear.rxtool.view.RxToast;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2018/7/19.
 */

public class LoginActivity extends BaseActivity {
    @Bind(R.id.user)
    EditText mUser;
    @Bind(R.id.password)
    EditText mPasswword;
    @Bind(R.id.login)
    TextView mLogin;
    String user;
    String password;
    private String nowLat;
    private String nowLng;
//    private LocationUtils locationUtils;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

    }


    @OnClick(R.id.login)
    public void onViewClicked() {
        final String user = mUser.getText().toString().trim();
        final String password = mPasswword.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            RxToast.normal("请输入账号");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            RxToast.normal("请输入密码");
            return;
        }

        DongZhiModle.login(user, password, new HttpCallBack() {
            @Override
            public void success(Object o) {
                SPUtils.put(Constants.LOGIN_USER, user);
                SPUtils.put(Constants.LOGIN_PASSWORD, password);
                RxToast.normal("登陆成功");
                finish();
            }

            @Override
            public void fail(String errorStr) {
                RxToast.normal("登陆失败");
            }
        });
    }

}
