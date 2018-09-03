package com.choucheng.dongzhibot.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;

/**
 * Created by liyou on 2018/3/29.
 */

public abstract class BaseFragment extends Fragment {
    public BaseActivity mActivity;
    public View mRootView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=(BaseActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView =  inflater.inflate(getContentViewLayoutId(), container, false);
        ButterKnife.bind(mRootView);
        initView();
        initData();
        initListener();
        return mRootView;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(mRootView);
    }

    public abstract int getContentViewLayoutId();
    public abstract void initView();
    public  <T extends View> T findViewById(int id){
        return mRootView.findViewById(id);
    }
    public void initData(){}
    public void initListener(){}
    public void refreshData(){}
    protected void showProgressDialog(int msg) {
        showLoadingDialog(getString(msg));
    }
    protected void showLoadingDialog(String msg){
        //mActivity.showLoadingDialog(msg);
    }

    public void hideLoadingDialog(){
        //mActivity.hideLoadingDialog();
    }
}
