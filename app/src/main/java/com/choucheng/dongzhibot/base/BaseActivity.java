package com.choucheng.dongzhibot.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import butterknife.ButterKnife;
import com.bjkj.library.utils.StatusBarUtil;
import com.choucheng.dongzhibot.R;

/**
 * Created by liyou on 2018/3/28.
 */

public abstract class BaseActivity extends FragmentActivity {
    public Activity mActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(getLayoutId());
        mActivity=this;
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
        setStatusBar();
    }
    public abstract int getLayoutId();
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.main_blue));
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    protected void showProgressDialog(int msg) {

    }

    public void showToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }


    public abstract void  initView();
    public  void  initData(){}
    public  void  initListener(){}
}
