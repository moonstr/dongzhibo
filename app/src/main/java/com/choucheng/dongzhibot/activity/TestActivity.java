package com.choucheng.dongzhibot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bjkj.library.okhttp.HttpCallBack;
import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.base.BaseActivity;
import com.choucheng.dongzhibot.modle.DongZhiModle;
import com.vondear.rxtool.view.RxToast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by admin on 2018/6/26.
 */

public class TestActivity extends BaseActivity {
    public static final int REQUEST_HEAD = 0;
    @Bind(R.id.update)
    Button update;
    @Bind(R.id.test)
    Button test;
    @Bind(R.id.account)
    EditText account;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.login)
    Button login;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView() {

    }

    ArrayList<String> heads = new ArrayList<>();

    @OnClick({R.id.update, R.id.test, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update:
                openGallery(heads, 1, false, REQUEST_HEAD);
                break;
            case R.id.test:
                break;

            case R.id.login:
                DongZhiModle.login(account.getText().toString().trim(), password.getText().toString().trim(), new HttpCallBack() {
                    @Override
                    public void success(Object o) {
                        RxToast.success("登陆成功");
                        finish();
                    }

                    @Override
                    public void fail(String errorStr) {
                        RxToast.success("登陆失败");
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_HEAD) {
            heads = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
//            ivHead.setVisibility(View.VISIBLE);
//            GlideUtils.loadImageView(mActivity, heads.get(0), ivHead);
            DongZhiModle.testUpLoad(heads, new HttpCallBack() {
                @Override
                public void success(Object o) {

                }

                @Override
                public void fail(String errorStr) {

                }
            });
        }
    }

    private void openGallery(ArrayList<String> datas, int number, boolean isMulti, int requestCode) {
        MultiImageSelector selector = MultiImageSelector.create(TestActivity.this);
        selector.showCamera(true);//是否显示相机
        selector.count(number);//显示照片个数

        if (isMulti) {
            selector.multi();
        } else {
            selector.single();
        }
        selector.origin(datas);
        selector.start(TestActivity.this, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login)
    public void onViewClicked() {
    }
}
