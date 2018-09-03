package com.choucheng.dongzhibot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bjkj.library.okhttp.HttpCallBack;
import com.bjkj.library.view.MyGridView;
import com.bumptech.glide.Glide;
import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.adapter.CommonAdapter;
import com.choucheng.dongzhibot.adapter.CommonViewHolder;
import com.choucheng.dongzhibot.base.BaseActivity;
import com.choucheng.dongzhibot.bean.ErrorListBean;
import com.choucheng.dongzhibot.bean.UploadBean;
import com.choucheng.dongzhibot.modle.DongZhiModle;
import com.vondear.rxtool.view.RxToast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by admin on 2018/7/3.
 */

public class ErrorListActivity extends BaseActivity {
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.question)
    EditText mQuestion;
    @Bind(R.id.photo)
    MyGridView mPhoto;
    @Bind(R.id.commit)
    TextView commit;
    CommonAdapter listAdapter;
    ArrayList<ErrorListBean.ErrorListItem> datas = new ArrayList<>();
    @Bind(R.id.photo_number)
    TextView photoNumber;
    private CommonAdapter photoAdapter;
    String device_id;

    ArrayList<String> photos = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();
    ArrayList<String> isEx = new ArrayList<>();
    String question = "";
    String mark_id = 0 + "";
    int num = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_error_list;
    }

    @Override
    public void initView() {
        //不自动打开输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        device_id = getIntent().getStringExtra("device_id");
        listAdapter = new CommonAdapter<ErrorListBean.ErrorListItem>(this, datas, R.layout.item_error_list) {

            @Override
            protected void convertView(final int position, View view, final ErrorListBean.ErrorListItem item) {
                TextView content = (TextView) CommonViewHolder.get(view, R.id.content);
                final TextView correct = (TextView) CommonViewHolder.get(view, R.id.correct);
                final TextView error = (TextView) CommonViewHolder.get(view, R.id.error);
                content.setText(item.mark);
                error.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        num = 2;
                        datas.get(position).setChecked(true);
                        error.setBackgroundResource(R.drawable.error_yellow);
                        error.setTextColor(mActivity.getResources().getColor(R.color.main_yellow));
                        correct.setBackground(mActivity.getResources().getDrawable(R.drawable.error_grey));
                        correct.setTextColor(mActivity.getResources().getColor(R.color.grey));
                        if (isEx.size() == 0) {
                            isEx.add(item.id);
                        } else {
                            if (isEx.contains(item.id)) {
                                return;
                            } else {
                                isEx.add(item.id);
                            }
                        }
                        listAdapter.notifyDataSetChanged();
                    }
                });
                correct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        num = 2;
                        datas.get(position).setChecked(false);
                        correct.setBackground(mActivity.getResources().getDrawable(R.drawable.error_green));
                        correct.setTextColor(mActivity.getResources().getColor(R.color.main_green));
                        error.setBackground(mActivity.getResources().getDrawable(R.drawable.error_grey));
                        error.setTextColor(mActivity.getResources().getColor(R.color.grey));
                        if (isEx.size() != 0) {
                            if (isEx.contains(item.id)) {
                                for (int i = 0; i < isEx.size(); i++) {
                                    if (isEx.get(i).equals(item.id)) {
                                        isEx.remove(i);
                                    }
                                }
                            }
                        }
                        listAdapter.notifyDataSetChanged();
                    }
                });


                correct.setText("正常");
                error.setText("异常");
                if (num == 1) {
                    if (item.status.equals("1")) {
                        datas.get(position).setChecked(false);
                    } else {
                        datas.get(position).setChecked(true);
                    }
                }


                if (datas.get(position).isChecked()) {
                    error.setBackground(mActivity.getResources().getDrawable(R.drawable.error_yellow));
                    error.setTextColor(mActivity.getResources().getColor(R.color.main_yellow));
                    correct.setBackground(mActivity.getResources().getDrawable(R.drawable.error_grey));
                    correct.setTextColor(mActivity.getResources().getColor(R.color.grey));
                } else {
                    correct.setBackground(mActivity.getResources().getDrawable(R.drawable.error_green));
                    correct.setTextColor(mActivity.getResources().getColor(R.color.main_green));
                    error.setBackground(mActivity.getResources().getDrawable(R.drawable.error_grey));
                    error.setTextColor(mActivity.getResources().getColor(R.color.grey));
                }


            }
        };
        listView.setAdapter(listAdapter);

        photos.add("");
        photoAdapter = new CommonAdapter<String>(ErrorListActivity.this, photos, R.layout.item_merchant_image) {
            @Override
            protected void convertView(int position, View item, String s) {
                if (position == photos.size() - 1) {
                    Glide.with(ErrorListActivity.this).asBitmap().load(R.mipmap.add_image).into(((ImageView) CommonViewHolder.get(item, R.id.image)));
                }
            }
        };
        mPhoto.setAdapter(photoAdapter);
    }

    @Override
    public void initData() {
        super.initData();
        DongZhiModle.errorList(device_id, new HttpCallBack<ArrayList<ErrorListBean.ErrorListItem>>() {
            @Override
            public void success(ArrayList<ErrorListBean.ErrorListItem> errorListItems) {
                datas.addAll(errorListItems);
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void fail(String errorStr) {

            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();

        mPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == photos.size() - 1) {
                    openGallery(new ArrayList<String>(), 5, true, 1);
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

    String strs = "";

    @OnClick(R.id.commit)
    public void onViewClicked() {
        for (int i = 0; i < urls.size(); i++) {
            if (i != (urls.size() - 1)) {
                strs += urls.get(i) + ",";
            } else {
                strs += urls.get(i);
            }

        }
        question = mQuestion.getText().toString().trim();
//        mark_id += ("," + item.id);
        if (isEx.size() > 0) {
            for (int i = 0; i < isEx.size(); i++) {
                mark_id = mark_id + ("," + isEx.get(i));
            }
        }
//        updatePic();
        if (isOk) {
            upDateAll(strs);
        } else {
            RxToast.normal("图片上传中，请等一下再提交");
        }

    }

    private void upDateAll(String strs) {
        DongZhiModle.addError(device_id, mark_id, question, strs, new HttpCallBack<String>() {
            @Override
            public void success(String o) {
                RxToast.normal("提交故障成功");
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);

            }

            @Override
            public void fail(String errorStr) {
                RxToast.normal(errorStr);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
//                还需上传5张照片

                photos = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                photos.add("");
                mPhoto.setAdapter(new CommonAdapter<String>(ErrorListActivity.this, photos, R.layout.item_merchant_image) {
                    @Override
                    protected void convertView(int position, View item, String s) {
                        if (position == photos.size() - 1) {
                            Glide.with(ErrorListActivity.this).asBitmap().load(R.mipmap.add_image).into(((ImageView) CommonViewHolder.get(item, R.id.image)));
                        } else {
                            Glide.with(ErrorListActivity.this).asBitmap().load(s).into(((ImageView) CommonViewHolder.get(item, R.id.image)));

                        }
                    }
                });
//                if ((5 - photos.size() + 1) < 0) {
//                    photoNumber.setVisibility(View.GONE);
//                } else {
//                    photoNumber.setVisibility(View.VISIBLE);
////                    photoNumber.setText("还需上传" + (5 - photos.size() + 1) + "张照片");
//                }

                photoAdapter.notifyDataSetChanged();
                isOk = false;
                DongZhiModle.testUpLoad(photos, new HttpCallBack<ArrayList<UploadBean.UploadData>>() {
                    @Override
                    public void success(ArrayList<UploadBean.UploadData> uploadData) {
//                                urls.add(uploadData.path);
                        for (int i = 0; i < uploadData.size(); i++) {
                            urls.add(uploadData.get(i).path);
                        }
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void fail(String errorStr) {
                        Message msg = new Message();
                        msg.what = 3;
                        handler.sendMessage(msg);
                        RxToast.normal(errorStr + "1");
                    }
                });

            }
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    isOk = true;
                    break;
                case 2:
                    setResult(RESULT_OK);
                    Intent intent = new Intent(ErrorListActivity.this, MerchantInspectionActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 3:
                case 4:
                    isOk = true;
//                    RxToast.error("上传失败");
                    break;

            }
        }
    };

    boolean isOk = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
