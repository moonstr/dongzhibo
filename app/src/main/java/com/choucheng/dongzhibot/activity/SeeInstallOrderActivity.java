package com.choucheng.dongzhibot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bjkj.library.okhttp.HttpCallBack;
import com.bjkj.library.view.MyGridView;
import com.bumptech.glide.Glide;
import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.adapter.CommonAdapter;
import com.choucheng.dongzhibot.adapter.CommonViewHolder;
import com.choucheng.dongzhibot.base.BaseActivity;
import com.choucheng.dongzhibot.bean.InstallOrderInfoBean;
import com.choucheng.dongzhibot.bean.UploadBean;
import com.choucheng.dongzhibot.modle.DongZhiModle;
import com.choucheng.dongzhibot.utils.ShowPictureDialog;
import com.gcssloop.widget.RCRelativeLayout;
import com.vondear.rxtool.view.RxToast;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by admin on 2018/6/14.
 */

public class SeeInstallOrderActivity extends BaseActivity {
    @Bind(R.id.code)
    TextView mCode;
    @Bind(R.id.name)
    TextView mName;
    @Bind(R.id.address)
    TextView mAddress;
    @Bind(R.id.user)
    TextView mUser;
    @Bind(R.id.phone)
    TextView mPhone;
    @Bind(R.id.device_code)
    TextView mDeviceCode;
    @Bind(R.id.device_style)
    TextView mDeviceStyle;
    @Bind(R.id.bank)
    TextView mBank;
    @Bind(R.id.note)
    TextView mNote;
    @Bind(R.id.photo)
    MyGridView mPhoto;
    @Bind(R.id.commit)
    TextView mCommit;
    String merchant_id;
    String device_id;
    public InstallOrderInfoBean.InstallOrderInfoData data;
    @Bind(R.id.rcrL)
    RCRelativeLayout rcrL;
    private CommonAdapter adapter;
    ArrayList<String> photos = new ArrayList<>();

    ArrayList<String> urls = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_see_install_order;
    }

    @Override
    public void initView() {

    }

    private boolean isOk;

    @Override
    public void initData() {
        super.initData();
        merchant_id = getIntent().getStringExtra("merchant_id");
        device_id = getIntent().getStringExtra("device_id");
        isOk = getIntent().getBooleanExtra("isOk", false);
        if (isOk) {
            mPhoto.setEnabled(false);
            mCommit.setEnabled(false);
            rcrL.setVisibility(View.GONE);
        }
        DongZhiModle.installOrderInfo(merchant_id, device_id, new HttpCallBack<InstallOrderInfoBean.InstallOrderInfoData>() {
            @Override
            public void success(InstallOrderInfoBean.InstallOrderInfoData installOrderInfoData) {
                data = installOrderInfoData;
                mCode.setText(installOrderInfoData.merchant_no);
                mName.setText(installOrderInfoData.name);
                mAddress.setText(installOrderInfoData.address);
                mUser.setText(installOrderInfoData.link_name);
                mPhone.setText(installOrderInfoData.link_phone);
                mDeviceCode.setText(installOrderInfoData.pos_no);
                mDeviceStyle.setText(installOrderInfoData.iem);
                if (installOrderInfoData.bank.equals("")) {
                    mBank.setVisibility(View.GONE);
                }
                mBank.setText(installOrderInfoData.device_type);
                mNote.setText(installOrderInfoData.mark);
            }

            @Override
            public void fail(String errorStr) {

            }
        });
        photos.add("");
        adapter = new CommonAdapter<String>(SeeInstallOrderActivity.this, photos, R.layout.item_merchant_image) {
            @Override
            protected void convertView(int position, View item, String s) {
                if (position == photos.size() - 1) {
                    Glide.with(SeeInstallOrderActivity.this).asBitmap().load(R.mipmap.add_image).into(((ImageView) CommonViewHolder.get(item, R.id.image)));
                } else {
                    Glide.with(SeeInstallOrderActivity.this).asBitmap().load(s).into(((ImageView) CommonViewHolder.get(item, R.id.image)));

                }
            }
        };
        mPhoto.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        mPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == photos.size() - 1) {
                    openGallery(photos, 5, true, 1);
                } else {
                    new ShowPictureDialog().showPicture(SeeInstallOrderActivity.this, photos.get(i));
                }
            }
        });
    }

    int photosSize = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                photos = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                removeNull(photos);
                photos.add("");
                upload(photos);
                photosSize = photos.size() - 1;
                mPhoto.setAdapter(new CommonAdapter<String>(SeeInstallOrderActivity.this, photos, R.layout.item_merchant_image) {
                    @Override
                    protected void convertView(int position, View item, String s) {
                        if (position == photos.size() - 1) {
                            Glide.with(SeeInstallOrderActivity.this).asBitmap().load(R.mipmap.add_image).into(((ImageView) CommonViewHolder.get(item, R.id.image)));

                        } else {
                            Glide.with(SeeInstallOrderActivity.this).asBitmap().load(s).into(((ImageView) CommonViewHolder.get(item, R.id.image)));
                        }

                    }
                });
            }
        }
    }

    private void removeNull(ArrayList<String> list) {

        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            // index and number
            String str = it.next();
            if (TextUtils.isEmpty(str)) {
                it.remove();
            }
        }
    }

    private void upload(ArrayList<String> list) {

//        for (int i=0;i<list.size();i++){
//            if (!TextUtils.isEmpty(list.get(i))){
        DongZhiModle.testUpLoad(list, new HttpCallBack<ArrayList<UploadBean.UploadData>>() {
            @Override
            public void success(ArrayList<UploadBean.UploadData> uploadData) {
                for (int i = 0; i < uploadData.size(); i++) {
                    urls.add(uploadData.get(i).path);
                }
            }

            @Override
            public void fail(String errorStr) {

            }
        });
//            }
//        }

    }

    @OnClick({R.id.commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.commit:
//                DialogUtil.showTipDialog(mActivity,"提示","工单已成功提交，请耐心等待审核","确定");
//                if (photos.size()==0||photos.size()<6){
//                    Toast.makeText(mActivity,"请选择5张图片",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (urls.size() < photosSize) {
                    Toast.makeText(mActivity, "请等待图片上传完成", Toast.LENGTH_SHORT).show();
                    return;
                }
                String strs = "";
                for (int i = 0; i < urls.size(); i++) {
                    if (i != (urls.size() - 1)) {
                        strs += urls.get(i) + ",";
                    } else {
                        strs += urls.get(i);
                    }

                }
                Log.e("123456789", "onViewClicked: " + merchant_id + "==" + strs);
                DongZhiModle.commitReview(merchant_id, strs, new HttpCallBack<String>() {
                    @Override
                    public void success(String s) {
                        RxToast.normal("提交审核成功");
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void fail(String errorStr) {

                    }
                });
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setResult(RESULT_OK);
                    finish();
                    break;
//                case 2:
//                    isOkTo = msg.getData().getBoolean("isOk");
//                    break;
            }
        }
    };

    private void openGallery(ArrayList<String> datas, int number, boolean isMulti, int requestCode) {
        MultiImageSelector selector = MultiImageSelector.create(mActivity);
        selector.showCamera(true);//是否显示相机
        selector.count(number);//显示照片个数

        if (isMulti) {
            selector.multi();
        } else {
            selector.single();
        }
        selector.origin(datas);
        selector.start(mActivity, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
