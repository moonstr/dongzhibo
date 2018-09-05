package com.choucheng.dongzhibot.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjkj.library.okhttp.HttpCallBack;
import com.bjkj.library.view.MyGridView;
import com.bumptech.glide.Glide;
import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.adapter.CommonAdapter;
import com.choucheng.dongzhibot.adapter.CommonViewHolder;
import com.choucheng.dongzhibot.base.BaseActivity;
import com.choucheng.dongzhibot.bean.ProtectBeanOrderInfo;
import com.choucheng.dongzhibot.bean.UploadBean;
import com.choucheng.dongzhibot.modle.DongZhiModle;
import com.choucheng.dongzhibot.utils.ShowPictureDialog;
import com.gcssloop.widget.RCRelativeLayout;
import com.vondear.rxtool.view.RxToast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by admin on 2018/7/17.
 * 维护工单信息
 */

public class ProtectOrderInfoActivity extends BaseActivity implements View.OnLongClickListener {
    @Bind(R.id.code)
    TextView code;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.user)
    TextView user;
    @Bind(R.id.phone)
    TextView phone;

    @Bind(R.id.des)
    TextView des;
    @Bind(R.id.protect_des)
    EditText protectDes;
    @Bind(R.id.photo)
    MyGridView mPhoto;
    @Bind(R.id.commit)
    TextView commit;
    @Bind(R.id.de_code)
    TextView deCode;
    @Bind(R.id.pos_num)
    TextView posNum;
    @Bind(R.id.de_type)
    TextView deType;
    @Bind(R.id.rcrl2)
    RCRelativeLayout rcrl2;
    @Bind(R.id.protect_rl1)
    RelativeLayout protectRl1;
    @Bind(R.id.protect_rl2)
    RelativeLayout protectRl2;
    @Bind(R.id.protect_rl3)
    RelativeLayout protectRl3;
    @Bind(R.id.protect_rl4)
    RelativeLayout protectRl4;
    @Bind(R.id.protect_rl5)
    RelativeLayout protectRl5;
    @Bind(R.id.protect_rl6)
    RelativeLayout protectRl6;
    @Bind(R.id.protect_rl7)
    RelativeLayout protectRl7;
    @Bind(R.id.protect_rl8)
    RelativeLayout protectRl8;

    private String xunjianid = "";
    private String d_id = "";
    private String id = "";
    private String odd_number = "";
    private CommonAdapter adapter;
    ArrayList<String> photos = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_protect_order_info;
    }

    @Override
    public void initView() {

    }

    private boolean isOK;

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        xunjianid = intent.getStringExtra("xunjianid");
        id = intent.getStringExtra("id");
        d_id = intent.getStringExtra("d_id");
        isOK = intent.getBooleanExtra("isOk", false);
        if (isOK) {
            mPhoto.setEnabled(false);
            rcrl2.setVisibility(View.GONE);
            commit.setEnabled(false);
        }
        photos.add("");
        adapter = new CommonAdapter<String>(ProtectOrderInfoActivity.this, photos, R.layout.item_merchant_image) {
            @Override
            protected void convertView(int position, View item, String s) {
                if (position == photos.size() - 1) {
                    Glide.with(ProtectOrderInfoActivity.this).asBitmap().load(R.mipmap.add_image).into(((ImageView) CommonViewHolder.get(item, R.id.image)));
                }
            }
        };
        mPhoto.setAdapter(adapter);
        getData();
    }

    @Override
    public void initListener() {
        super.initListener();
        protectRl1.setOnLongClickListener(this);
        protectRl2.setOnLongClickListener(this);
        protectRl3.setOnLongClickListener(this);
        protectRl4.setOnLongClickListener(this);
        protectRl5.setOnLongClickListener(this);
        protectRl6.setOnLongClickListener(this);
        protectRl7.setOnLongClickListener(this);
        protectRl8.setOnLongClickListener(this);
        mPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == photos.size() - 1) {
                    openGallery(new ArrayList<String>(), 5, true, 1);
                } else {
                    new ShowPictureDialog().showPicture(ProtectOrderInfoActivity.this, photos.get(i));
                }
            }
        });
    }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                photos = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                photos.add("");
                if (photos.size() > 0) {
                    isOkTo = false;
                }

                mPhoto.setAdapter(new CommonAdapter<String>(ProtectOrderInfoActivity.this, photos, R.layout.item_merchant_image) {
                    @Override
                    protected void convertView(int position, View item, String s) {
                        if (position == photos.size() - 1) {
                            Glide.with(ProtectOrderInfoActivity.this).asBitmap().load(R.mipmap.add_image).into(((ImageView) CommonViewHolder.get(item, R.id.image)));
                        } else {
                            Glide.with(ProtectOrderInfoActivity.this).asBitmap().load(s).into(((ImageView) CommonViewHolder.get(item, R.id.image)));

                        }
                    }
                });

                adapter.notifyDataSetChanged();

//                for (int i=0;i<photos.size();i++){
//                    if (!TextUtils.isEmpty(photos.get(i))){
                DongZhiModle.testUpLoad(photos, new HttpCallBack<ArrayList<UploadBean.UploadData>>() {
                    @Override
                    public void success(ArrayList<UploadBean.UploadData> uploadData) {
//                                urls.add(uploadData.path);
                        for (int i = 0; i < uploadData.size(); i++) {
                            urls.add(uploadData.get(i).path);
                        }

                        if (0 != urls.size() && urls.size() == (photos.size() - 1)) {
                            Message msg = new Message();
                            msg.what = 2;
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("isOk", true);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void fail(String errorStr) {

                    }
                });
//                    }
            }
//            }
        }
    }

    private void getData() {

        DongZhiModle.protectOrderInfo(xunjianid, d_id, new HttpCallBack<ProtectBeanOrderInfo.ProtectBeanOrderInfoData>() {
            @Override
            public void success(ProtectBeanOrderInfo.ProtectBeanOrderInfoData protectBeanOrderInfoData) {
                code.setText(protectBeanOrderInfoData.merchant_no);//商户编码
                name.setText(protectBeanOrderInfoData.name);//网点名称
                address.setText(protectBeanOrderInfoData.address);//网点地址
                user.setText(protectBeanOrderInfoData.link_name);//联系人
                phone.setText(protectBeanOrderInfoData.link_phone);//联系电话
                deCode.setText(protectBeanOrderInfoData.pos_no);//终端编号
                posNum.setText(protectBeanOrderInfoData.iem);//pos序列号
                deType.setText(protectBeanOrderInfoData.device_type);//终端类型
                des.setText(protectBeanOrderInfoData.service_mark);
                odd_number = protectBeanOrderInfoData.odd_number;
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }

            @Override
            public void fail(String errorStr) {
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);
            }
        });

    }

    private boolean isOkTo = true;

    @OnClick(R.id.commit)
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.commit:
                if (isOkTo) {
                    String strs = "";
                    for (int i = 0; i < urls.size(); i++) {
                        if (i != (urls.size() - 1)) {
                            strs += urls.get(i) + ",";
                        } else {
                            strs += urls.get(i);
                        }

                    }

                    String txt = "";
                    if (null != protectDes.getText().toString().trim()) {
                        txt = protectDes.getText().toString().trim();
                    }

                    // http://dzb2.zgcom.cn/Public/common/file/image/2018-08-07/8715b6961138830f5b69611389b38.jpg
                    //http://dzb2.zgcom.cn/Public/common/file/image/2018-08-07/3585b69613c041185b69613c05925.jpg
                    // http://dzb2.zgcom.cn/Public/common/file/image/2018-08-07/3975b696165c9e925b696165cb6e7.jpg
                    DongZhiModle.commitProtectInfo(id, odd_number, txt, strs, new HttpCallBack<String>() {
                        @Override
                        public void success(String o) {
                            RxToast.normal("提交审核成功");
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void fail(String errorStr) {
                            RxToast.normal("提交审核成功");
                        }
                    });
                } else {
                    RxToast.normal("图片上传中请稍等");
                }

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
                case 2:
                    isOkTo = msg.getData().getBoolean("isOk");
                    break;
                case 3:
                case 4:
                    isOkToCopy = true;
                    break;
            }
        }
    };
    private boolean isOkToCopy = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    public boolean onLongClick(View v) {
        ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (isOkToCopy) {
            switch (v.getId()) {
                case R.id.protect_rl1:
                    mClipboardManager.setText(code.getText().toString());
                    RxToast.normal("商户编码复制成功！");
                    break;
                case R.id.protect_rl2:
                    mClipboardManager.setText(name.getText().toString());
                    RxToast.normal("网点名称复制成功！");
                    break;
                case R.id.protect_rl3:
                    mClipboardManager.setText(address.getText().toString());
                    RxToast.normal("维护地址复制成功！");
                    break;
                case R.id.protect_rl4:
                    mClipboardManager.setText(user.getText().toString());
                    RxToast.normal("联系人姓名复制成功！");
                    break;
                case R.id.protect_rl5:
                    mClipboardManager.setText(phone.getText().toString());
                    RxToast.normal("联系电话复制成功！");
                    break;
                case R.id.protect_rl6:
                    mClipboardManager.setText(deCode.getText().toString());
                    RxToast.normal("终端编号复制成功！");
                    break;
                case R.id.protect_rl7:
                    mClipboardManager.setText(posNum.getText().toString());
                    RxToast.normal("POS序列号复制成功！");
                    break;
                case R.id.protect_rl8:
                    mClipboardManager.setText(deType.getText().toString());
                    RxToast.normal("终端类型复制成功！");
                    break;
            }
        }
        return false;
    }
}
