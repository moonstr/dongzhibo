package com.choucheng.dongzhibot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjkj.library.okhttp.HttpCallBack;
import com.bjkj.library.utils.SPUtils;
import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.base.BaseActivity;
import com.choucheng.dongzhibot.bean.UserInfoBean;
import com.choucheng.dongzhibot.modle.DongZhiModle;
import com.choucheng.dongzhibot.utils.Constants;
import com.choucheng.dongzhibot.view.DialogUtil;
import com.vondear.rxtool.view.RxToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liyou on 2018/6/5.
 */

public class UserInfoActivity extends BaseActivity {
    @Bind(R.id.phone)
    TextView mPhone;
    @Bind(R.id.name)
    TextView mName;
    @Bind(R.id.edit_info)
    TextView mEditInfo;
    @Bind(R.id.edit_password)
    TextView mEditPassword;
    @Bind(R.id.tv_exit)
    TextView tvExit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        super.initData();
        DongZhiModle.getUserInfo(new HttpCallBack<UserInfoBean.UserInfo>() {
            @Override
            public void success(UserInfoBean.UserInfo bean) {
                mName.setText(bean.name);
                mPhone.setText(bean.phone);
            }

            @Override
            public void fail(String errorStr) {

            }
        });
    }

    @OnClick({R.id.edit_info, R.id.edit_password, R.id.tv_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_info:
                DialogUtil.showUserInfoDialog(UserInfoActivity.this, new DialogUtil.UserEditClick() {
                    @Override
                    public void userData(String data1, String data2) {

                        DongZhiModle.updateUserInfo(data1, data2, new HttpCallBack<String>() {
                            @Override
                            public void success(String s) {

                            }

                            @Override
                            public void fail(String errorStr) {

                            }
                        });

                    }
                });
                break;
            case R.id.edit_password:
                DialogUtil.showPassword(UserInfoActivity.this, new DialogUtil.UserEditClick() {
                    @Override
                    public void userData(String data1, String data2) {
                        DongZhiModle.updatePassword(data1, new HttpCallBack<String>() {
                            @Override
                            public void success(String s) {

                            }

                            @Override
                            public void fail(String errorStr) {

                            }
                        });
                    }
                });
                break;
            case R.id.tv_exit:
                SPUtils.remove(Constants.LOGIN_USER);
                SPUtils.remove(Constants.LOGIN_PASSWORD);
                RxToast.normal("您已注销登录");
                setResult(RESULT_OK);
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
