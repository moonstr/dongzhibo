package com.choucheng.dongzhibot.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjkj.library.okhttp.HttpCallBack;
import com.bjkj.library.utils.GlideUtils;
import com.bumptech.glide.Glide;
import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.adapter.CommonAdapter;
import com.choucheng.dongzhibot.adapter.CommonViewHolder;
import com.choucheng.dongzhibot.base.BaseActivity;
import com.choucheng.dongzhibot.bean.ImagePic;
import com.choucheng.dongzhibot.bean.MerchantInfoBean;
import com.choucheng.dongzhibot.bean.UploadBean;
import com.choucheng.dongzhibot.modle.DongZhiModle;
import com.choucheng.dongzhibot.utils.PermissionUtil;
import com.choucheng.dongzhibot.utils.ShowPictureDialog;
import com.choucheng.dongzhibot.view.DialogUtil;
import com.vondear.rxtool.view.RxToast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by liyou on 2018/6/5.
 */

public class MerchantInfoCollectActivity extends BaseActivity {

    public static final int REQUEST_HEAD = 0;
    public static final int REQUEST_FRONT = 1;
    public static final int REQUEST_INNER = 2;
    public static final int REQUEST_BUISSNESS = 3;
    public static final int REQUEST_LEGAL = 4;
    public static final int REQUEST_BANK = 5;
    public static final int REQUEST_OTHERS = 6;
    @Bind(R.id.name)
    EditText mName;
    @Bind(R.id.address)
    EditText mAddress;
    @Bind(R.id.legal_phone)
    EditText mLegalPhone;
    @Bind(R.id.document_number)
    EditText mDocumentNumber;
    @Bind(R.id.regist_name)
    EditText mRegistName;
    @Bind(R.id.regist_number)
    EditText mRegistNumber;
    @Bind(R.id.regist_address)
    EditText mRegistAddress;
    @Bind(R.id.contacts)
    EditText mContacts;
    @Bind(R.id.contacts_phone)
    EditText mContactsPhone;

    @Bind(R.id.retail)
    RadioButton mRetail;
    @Bind(R.id.wholesale)
    RadioButton mWholesale;
    @Bind(R.id.type)
    RadioGroup mType;
    @Bind(R.id.style)
    EditText mStyle;
    @Bind(R.id.protocol_number)
    EditText mProtocolNumber;
    @Bind(R.id.rb_trend)
    RadioButton mRbTrend;
    @Bind(R.id.rb_reserve)
    RadioButton mRbReserve;
    @Bind(R.id.ll_trend)
    RadioGroup mLlTrend;
    @Bind(R.id.note)
    EditText mNote;

    @Bind(R.id.iv_head)
    ImageView ivHead;
    @Bind(R.id.add_head)
    ImageView addHead;
    @Bind(R.id.iv_front)
    ImageView ivFront;
    @Bind(R.id.add_front)
    ImageView addFront;
    @Bind(R.id.iv_inner)
    ImageView ivInner;

    @Bind(R.id.add_inner)
    ImageView addInner;

    @Bind(R.id.iv_business)
    ImageView ivBusiness;

    @Bind(R.id.add_business)
    ImageView addBusiness;

    @Bind(R.id.legal)
    GridView mLegal;
    @Bind(R.id.bank)
    GridView mBank;
    @Bind(R.id.others)
    GridView mOthers;
    @Bind(R.id.save)
    TextView mSave;
    @Bind(R.id.commit)
    TextView mCommit;
    ArrayList<String> others = new ArrayList<>();
    ArrayList<String> heads = new ArrayList<>();
    ArrayList<String> fronts = new ArrayList<>();
    ArrayList<String> inners = new ArrayList<>();
    ArrayList<String> businesss = new ArrayList<>();
    ArrayList<String> legals = new ArrayList<>();
    ArrayList<String> banks = new ArrayList<>();
    String type = "1";
    String trend = "1";
    String id;
    String headUrl = "";
    String frontUrl = "";
    String innerUrl = "";
    String businesUrl = "";
    String legalUrl = "";
    String bankUrl = "";
    String otherUrl = "";
    public MerchantInfoBean.MerchantInfoData merchantInfo;
    boolean isOk = false;
    @Bind(R.id.merchant_ll)
    LinearLayout merchantLl;
    @Bind(R.id.merchant_ok_ll)
    LinearLayout merchantOkLl;
    @Bind(R.id.merchant_ll_1)
    LinearLayout merchantLl1;
    @Bind(R.id.merchant_ll_2)
    LinearLayout merchantLl2;
    @Bind(R.id.merchant_ll_3)
    LinearLayout merchantLl3;
    @Bind(R.id.merchant_ll_4)
    LinearLayout merchantLl4;
    @Bind(R.id.merchant_ll_5)
    LinearLayout merchantLl5;
    @Bind(R.id.merchant_ll_6)
    LinearLayout merchantLl6;
    private RelativeLayout rl;

    private List<ImagePic> imagePic;
    private boolean isUpdateOk_1 = true;
    private boolean isUpdateOk_2 = true;
    private boolean isUpdateOk_3 = true;
    private boolean isUpdateOk_4 = true;
    private boolean isUpdateOk_5 = true;
    private boolean isUpdateOk_6 = true;
    private boolean isUpdateOk_7 = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_merchant_info;
    }

    @Override
    public void initView() {
        rl = findViewById(R.id.merchant_rl);
        imagePic = new ArrayList<>();
        imagePic.add(new ImagePic("heads", 0));
        imagePic.add(new ImagePic("fronts", 0));
        imagePic.add(new ImagePic("inners", 0));
        imagePic.add(new ImagePic("businesss", 0));
        imagePic.add(new ImagePic("legals", 0));
        imagePic.add(new ImagePic("banks", 0));
        imagePic.add(new ImagePic("others", 0));
    }

    boolean isAdd = false;

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        isOk = intent.getBooleanExtra("isOk", false);
        isAdd = intent.getBooleanExtra("isAdd", false);
        if (isOk) {
            //mName mAddress mLegalPhone  mDocumentNumber mRegistName mRegistNumber mRegistAddress mContacts mContactsPhone
            //mStyle mProtocolNumber mNote
            mName.setKeyListener(null);
            mAddress.setKeyListener(null);
            mLegalPhone.setKeyListener(null);
            mDocumentNumber.setKeyListener(null);
            mRegistName.setKeyListener(null);
            mRegistNumber.setKeyListener(null);
            mRegistAddress.setKeyListener(null);
            mContacts.setKeyListener(null);
            mContactsPhone.setKeyListener(null);
            mStyle.setKeyListener(null);
            mProtocolNumber.setKeyListener(null);
            mNote.setKeyListener(null);
            rl.setVisibility(View.VISIBLE);
            rl.setOnClickListener(null);
            addBusiness.setEnabled(false);
            addFront.setEnabled(false);
            addHead.setEnabled(false);
            addInner.setEnabled(false);
            mLegal.setEnabled(false);
            mBank.setEnabled(false);
            mOthers.setEnabled(false);
            mRetail.setEnabled(false);
            mWholesale.setEnabled(false);
            mRbReserve.setEnabled(false);
            mRbTrend.setEnabled(false);
//            merchantLl.setFocusable(false);
            merchantOkLl.setVisibility(View.GONE);
        }
        if (id != null) {
            DongZhiModle.updateMerchantInfo(id, new HttpCallBack<MerchantInfoBean.MerchantInfoData>() {
                @Override
                public void success(MerchantInfoBean.MerchantInfoData merchantInfoData) {
                    merchantInfo = merchantInfoData;
                    if (merchantInfo != null) {
                        mName.setText(merchantInfo.name);
                        mAddress.setText(merchantInfo.address);
                        mLegalPhone.setText(merchantInfo.tel);
                        mDocumentNumber.setText(merchantInfo.port);
                        mRegistName.setText(merchantInfo.reg_name);
                        mRegistNumber.setText(merchantInfo.reg_no);
                        mRegistAddress.setText(merchantInfo.reg_address);
                        mContacts.setText(merchantInfo.link_name);
                        mContactsPhone.setText(merchantInfo.link_phone);
                        if ("1".equals(merchantInfo.buss_type)) {
                            mRetail.setChecked(true);
                        } else if ("2".equals(merchantInfo.buss_type)) {
                            mWholesale.setChecked(true);
                        }
                        mStyle.setText(merchantInfo.buss_cate);
                        mProtocolNumber.setText(merchantInfo.assign_no);
                        if ("1".equals(merchantInfo.buss_type)) {
                            mRbTrend.setChecked(true);
                        } else if ("2".equals(merchantInfo.buss_type)) {
                            mRbReserve.setChecked(true);
                        }
                        if (!TextUtils.isEmpty(merchantInfo.top_photo)) {
                            ivHead.setVisibility(View.VISIBLE);
                            Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(merchantInfo.top_photo).into(ivHead);
                        }

                        if (!TextUtils.isEmpty(merchantInfo.head_photo)) {
                            ivFront.setVisibility(View.VISIBLE);
                            Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(merchantInfo.head_photo).into(ivFront);
                        }

                        if (!TextUtils.isEmpty(merchantInfo.inside_photo)) {
                            ivInner.setVisibility(View.VISIBLE);
                            Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(merchantInfo.inside_photo).into(ivInner);
                        }
                        if (!TextUtils.isEmpty(merchantInfo.port_photo)) {
                            ivBusiness.setVisibility(View.VISIBLE);
                            Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(merchantInfo.port_photo).into(ivBusiness);
                        }


                        final ArrayList<String> legalsTemp = new ArrayList<>();
                        final ArrayList<String> banksTemp = new ArrayList<>();
                        final ArrayList<String> othersTemp = new ArrayList<>();


                        if (!TextUtils.isEmpty(merchantInfo.legal_photo)) {
                            String[] legalsStr;
                            legalsStr = merchantInfo.legal_photo.split(",");
                            for (int i = 0; i < legalsStr.length; i++) {
                                legalsTemp.add(legalsStr[i]);
                            }
                            legalsTemp.add("");
                            mLegal.setAdapter(new CommonAdapter<String>(MerchantInfoCollectActivity.this, legalsTemp, R.layout.item_merchant_image) {
                                @Override
                                protected void convertView(int position, View item, String s) {
                                    if (position == legalsTemp.size() - 1) {
                                        Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(R.mipmap.add_image).into(((ImageView) CommonViewHolder.get(item, R.id.image)));
                                    } else {
                                        Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(legalsTemp.get(position)).into(((ImageView) CommonViewHolder.get(item, R.id.image)));
                                    }
                                }
                            });
                        }

                        if (!TextUtils.isEmpty(merchantInfo.bank_photo)) {
                            String[] banksStr = merchantInfo.bank_photo.split(",");
                            for (int i = 0; i < banksStr.length; i++) {
                                banksTemp.add(banksStr[i]);
                            }
                            banksTemp.add("");
                            mBank.setAdapter(new CommonAdapter<String>(MerchantInfoCollectActivity.this, banksTemp, R.layout.item_merchant_image) {
                                @Override
                                protected void convertView(int position, View item, String s) {
                                    if (position == banksTemp.size() - 1) {
                                        Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(R.mipmap.add_image).into(((ImageView) CommonViewHolder.get(item, R.id.image)));
                                    } else {
                                        Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(banksTemp.get(position)).into(((ImageView) CommonViewHolder.get(item, R.id.image)));

                                    }
                                }
                            });
                        }

                        if (!TextUtils.isEmpty(merchantInfo.other_photo)) {
                            String[] othersStr = merchantInfo.other_photo.split(",");
                            for (int i = 0; i < othersStr.length; i++) {
                                othersTemp.add(othersStr[i]);
                            }
                            othersTemp.add("");
                            mOthers.setAdapter(new CommonAdapter<String>(MerchantInfoCollectActivity.this, othersTemp, R.layout.item_merchant_image) {
                                @Override
                                protected void convertView(int position, View item, String s) {
                                    if (position == legalsTemp.size() - 1) {
                                        Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(R.mipmap.add_image).into(((ImageView) CommonViewHolder.get(item, R.id.image)));
                                    } else {
                                        Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(othersTemp.get(position)).into(((ImageView) CommonViewHolder.get(item, R.id.image)));
                                    }
                                }
                            });
                        }

                    }
                }

                @Override
                public void fail(String errorStr) {

                }
            });
        }
        others.add("");
        mOthers.setAdapter(new CommonAdapter<String>(MerchantInfoCollectActivity.this, others, R.layout.item_merchant_image) {
            @Override
            protected void convertView(int position, View item, String s) {
                if (position == others.size() - 1) {
                    Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(R.mipmap.add_image).into(((ImageView) CommonViewHolder.get(item, R.id.image)));
                }else {
//
                }
            }
        });


        banks.add("");
        mBank.setAdapter(new CommonAdapter<String>(MerchantInfoCollectActivity.this, banks, R.layout.item_merchant_image) {
            @Override
            protected void convertView(int position, View item, String s) {
                if (position == banks.size() - 1) {
                    Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(R.mipmap.add_image).into(((ImageView) CommonViewHolder.get(item, R.id.image)));
                }
            }
        });

        legals.add("");
        mLegal.setAdapter(new CommonAdapter<String>(MerchantInfoCollectActivity.this, legals, R.layout.item_merchant_image) {
            @Override
            protected void convertView(int position, View item, String s) {
                if (position == legals.size() - 1) {
                    Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(R.mipmap.add_image).into(((ImageView) CommonViewHolder.get(item, R.id.image)));
                }
            }
        });
        if (!checkPermission()) {
            PermissionUtil.requestPermission(MerchantInfoCollectActivity.this, 0, PermissionUtil.getCameraPermission());
        }
    }

    public boolean checkPermission() {

        return PermissionUtil.hasPermission(MerchantInfoCollectActivity.this, PermissionUtil.getCameraPermission());
    }

    @Override
    public void initListener() {
        super.initListener();
        mOthers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == others.size() - 1) {
                    openGallery(new ArrayList<String>(), 2, true, REQUEST_OTHERS);
                }else {
                    new ShowPictureDialog().showPicture(MerchantInfoCollectActivity.this, others.get(i));
                }
            }
        });

        mBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == banks.size() - 1) {
                    openGallery(new ArrayList<String>(), 2, true, REQUEST_BANK);
                }else {
                    new ShowPictureDialog().showPicture(MerchantInfoCollectActivity.this, banks.get(i));
                }
            }
        });

        mLegal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == legals.size() - 1) {
                    openGallery(new ArrayList<String>(), 2, true, REQUEST_LEGAL);
                }else {
                    new ShowPictureDialog().showPicture(MerchantInfoCollectActivity.this, legals.get(i));
                }
            }
        });

        mType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.retail) {
                    type = "1";
                } else if (i == R.id.wholesale) {
                    type = "2";
                }
            }
        });

        mLlTrend.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_trend) {
                    trend = "1";
                } else if (i == R.id.rb_reserve) {
                    trend = "2";
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_HEAD) {
                heads = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                ivHead.setVisibility(View.VISIBLE);
                imagePic.set(0, new ImagePic("heads", heads.size() - 1));
                GlideUtils.loadImageView(mActivity, heads.get(0), ivHead);
                isUpdateOk_1 = false;
                DongZhiModle.testUpLoad(heads, new HttpCallBack<ArrayList<UploadBean.UploadData>>() {
                    @Override
                    public void success(ArrayList<UploadBean.UploadData> uploadData) {
//                        headsUrl.clear();
//                        headsUrl.add(uploadData.path);
//                        getUrl(headUrl,uploadData);

                        String tempStr = "";
                        for (int i = 0; i < uploadData.size(); i++) {
                            if (i != (uploadData.size() - 1)) {
                                tempStr += uploadData.get(i).path + ",";
                            } else {
                                tempStr += uploadData.get(i).path;
                            }
                            headUrl = tempStr;
                        }
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void fail(String errorStr) {

                    }
                });
            } else if (requestCode == REQUEST_FRONT) {
                fronts = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                imagePic.set(1, new ImagePic("fronts", fronts.size() - 1));
                ivFront.setVisibility(View.VISIBLE);
                GlideUtils.loadImageView(mActivity, fronts.get(0), ivFront);
                isUpdateOk_2 = false;
                DongZhiModle.testUpLoad(fronts, new HttpCallBack<ArrayList<UploadBean.UploadData>>() {
                    @Override
                    public void success(ArrayList<UploadBean.UploadData> uploadData) {
//                        frontsUrl.clear();
//                        frontsUrl.add(uploadData.path);
//                        getUrl(frontUrl,uploadData);

                        String tempStr = "";
                        for (int i = 0; i < uploadData.size(); i++) {
                            if (i != (uploadData.size() - 1)) {
                                tempStr += uploadData.get(i).path + ",";
                            } else {
                                tempStr += uploadData.get(i).path;
                            }
                            frontUrl = tempStr;
                        }
                        Message msg = new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void fail(String errorStr) {

                    }
                });
            } else if (requestCode == REQUEST_INNER) {
                inners = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                imagePic.set(2, new ImagePic("inners", inners.size() - 1));
                ivInner.setVisibility(View.VISIBLE);
                GlideUtils.loadImageView(mActivity, inners.get(0), ivInner);
                isUpdateOk_3 = false;
                DongZhiModle.testUpLoad(inners, new HttpCallBack<ArrayList<UploadBean.UploadData>>() {
                    @Override
                    public void success(ArrayList<UploadBean.UploadData> uploadData) {
//                        innersUrl.clear();
//                        innersUrl.add(uploadData.);
//                        getUrl(innerUrl,uploadData);

                        String tempStr = "";
                        for (int i = 0; i < uploadData.size(); i++) {
                            if (i != (uploadData.size() - 1)) {
                                tempStr += uploadData.get(i).path + ",";
                            } else {
                                tempStr += uploadData.get(i).path;
                            }
                            innerUrl = tempStr;
                        }
                        Message msg = new Message();
                        msg.what = 3;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void fail(String errorStr) {

                    }
                });
            } else if (requestCode == REQUEST_BUISSNESS) {
                businesss = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                imagePic.set(3, new ImagePic("businesss", businesss.size() - 1));
                ivBusiness.setVisibility(View.VISIBLE);
                GlideUtils.loadImageView(mActivity, businesss.get(0), ivBusiness);
                isUpdateOk_4 = false;
                DongZhiModle.testUpLoad(businesss, new HttpCallBack<ArrayList<UploadBean.UploadData>>() {
                    @Override
                    public void success(ArrayList<UploadBean.UploadData> uploadData) {
//                        businesssUrl.clear();
//                        businesssUrl.add(uploadData.path);
//                        getUrl(businesUrl,uploadData);

                        String tempStr = "";
                        for (int i = 0; i < uploadData.size(); i++) {
                            if (i != (uploadData.size() - 1)) {
                                tempStr += uploadData.get(i).path + ",";
                            } else {
                                tempStr += uploadData.get(i).path;
                            }
                            businesUrl = tempStr;
                        }
                        Message msg = new Message();
                        msg.what = 4;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void fail(String errorStr) {

                    }
                });
            } else if (requestCode == REQUEST_LEGAL) {
                legals.clear();
                legals = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                imagePic.set(4, new ImagePic("legals", legals.size() - 1));
                removeNull(legals);
                legals.add("");
                isUpdateOk_5 = false;
                DongZhiModle.testUpLoad(legals, new HttpCallBack<ArrayList<UploadBean.UploadData>>() {
                    @Override
                    public void success(ArrayList<UploadBean.UploadData> uploadData) {
//                        legalsUrl.clear();
//                        legalsUrl.addAll(uploadData);
//                        getUrl(legalUrl,uploadData);

                        String tempStr = "";
                        for (int i = 0; i < uploadData.size(); i++) {
                            if (i != (uploadData.size() - 1)) {
                                tempStr += uploadData.get(i).path + ",";
                            } else {
                                tempStr += uploadData.get(i).path;
                            }
                            legalUrl = tempStr;
                        }
                        Message msg = new Message();
                        msg.what = 5;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void fail(String errorStr) {

                    }
                });
                mLegal.setAdapter(new CommonAdapter<String>(MerchantInfoCollectActivity.this, legals, R.layout.item_merchant_image) {
                    @Override
                    protected void convertView(int position, View item, String s) {
                        if (position == legals.size() - 1) {
                            Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(R.mipmap.add_image).into(((ImageView) CommonViewHolder.get(item, R.id.image)));

                        } else {
                            Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(s).into(((ImageView) CommonViewHolder.get(item, R.id.image)));

                        }

                    }
                });
                calGridViewWidthAndHeigh(3, mLegal);
            } else if (requestCode == REQUEST_BANK) {
                banks.clear();
                banks = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                imagePic.set(5, new ImagePic("banks", banks.size() - 1));
                removeNull(banks);
                banks.add("");
                isUpdateOk_6 = false;
                DongZhiModle.testUpLoad(banks, new HttpCallBack<ArrayList<UploadBean.UploadData>>() {
                    @Override
                    public void success(ArrayList<UploadBean.UploadData> uploadData) {
//                        getUrl(bankUrl,uploadData);

                        String tempStr = "";
                        for (int i = 0; i < uploadData.size(); i++) {
                            if (i != (uploadData.size() - 1)) {
                                tempStr += uploadData.get(i).path + ",";
                            } else {
                                tempStr += uploadData.get(i).path;
                            }
                            bankUrl = tempStr;
                        }
                        Message msg = new Message();
                        msg.what = 6;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void fail(String errorStr) {

                    }
                });
                mBank.setAdapter(new CommonAdapter<String>(MerchantInfoCollectActivity.this, banks, R.layout.item_merchant_image) {
                    @Override
                    protected void convertView(int position, View item, String s) {
                        if (position == banks.size() - 1) {
                            Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(R.mipmap.add_image).into(((ImageView) CommonViewHolder.get(item, R.id.image)));

                        } else {
                            Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(s).into(((ImageView) CommonViewHolder.get(item, R.id.image)));

                        }

                    }
                });
                calGridViewWidthAndHeigh(3, mBank);
            } else if (requestCode == REQUEST_OTHERS) {
                others.clear();
                isUpdateOk_7 = false;
                others = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                imagePic.set(6, new ImagePic("others", others.size() - 1));
                removeNull(others);
                others.add("");
                DongZhiModle.testUpLoad(others, new HttpCallBack<ArrayList<UploadBean.UploadData>>() {
                    @Override
                    public void success(ArrayList<UploadBean.UploadData> uploadData) {
//                        getUrl(otherUrl,uploadData);


                        String tempStr = "";
                        for (int i = 0; i < uploadData.size(); i++) {
                            if (i != (uploadData.size() - 1)) {
                                tempStr += uploadData.get(i).path + ",";
                            } else {
                                tempStr += uploadData.get(i).path;
                            }
                            otherUrl = tempStr;
                        }
                        Message msg = new Message();
                        msg.what = 7;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void fail(String errorStr) {

                    }
                });
                mOthers.setAdapter(new CommonAdapter<String>(MerchantInfoCollectActivity.this, others, R.layout.item_merchant_image) {
                    @Override
                    protected void convertView(int position, View item, String s) {
                        if (position == others.size() - 1) {
                            Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(R.mipmap.add_image).into(((ImageView) CommonViewHolder.get(item, R.id.image)));

                        } else {
                            Glide.with(MerchantInfoCollectActivity.this).asBitmap().load(s).into(((ImageView) CommonViewHolder.get(item, R.id.image)));

                        }

                    }
                });
                calGridViewWidthAndHeigh(3, mOthers);
            }
        }
    }

    //    private void getUrl(String  str,ArrayList<UploadBean.UploadData> da){
//        String tempStr="";
//        for (int i=0;i<da.size();i++){
//            if (i!=(da.size()-1)){
//                tempStr+=da.get(i)+",";
//            }else {
//                tempStr+=da.get(i);
//            }
//            str=tempStr;
//        }
//    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    isUpdateOk_1 = true;
                    break;
                case 2:
                    isUpdateOk_2 = true;
                    break;
                case 3:
                    isUpdateOk_3 = true;
                    break;
                case 4:
                    isUpdateOk_4 = true;
                    break;
                case 5:
                    isUpdateOk_5 = true;
                    break;
                case 6:
                    isUpdateOk_6 = true;
                    break;
                case 7:
                    isUpdateOk_7 = true;
                    break;
            }
        }
    };

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

    private boolean isOkToCommit = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }

    private void openGallery(ArrayList<String> datas, int number, boolean isMulti, int requestCode) {
        MultiImageSelector selector = MultiImageSelector.create(MerchantInfoCollectActivity.this);
        selector.showCamera(true);//是否显示相机
        selector.count(number);//显示照片个数
        if (isMulti) {
            selector.multi();
        } else {
            selector.single();
        }
        selector.origin(datas);
        selector.start(MerchantInfoCollectActivity.this, requestCode);
    }

    @OnClick({R.id.add_head, R.id.add_front, R.id.add_inner, R.id.add_business, R.id.commit, R.id.save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_head:
                openGallery(heads, 1, false, REQUEST_HEAD);
                break;
            case R.id.add_front:
                openGallery(heads, 1, false, REQUEST_FRONT);
                break;
            case R.id.add_inner:
                openGallery(heads, 1, false, REQUEST_INNER);
                break;

            case R.id.add_business:
                openGallery(businesss, 1, false, REQUEST_BUISSNESS);
                break;
            case R.id.save:
//                if (isOkToCommit) {
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        commitData(false);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(timerTask, 3000);

//                } else {
//                    RxToast.normal("您还有照片正在上传，请稍后");
//                }

//                DialogUtil.showTipDialog(mActivity,"提示","信息保存成功","确定");
                break;
            case R.id.commit:
                if (isUpdateOk_1 && isUpdateOk_2 && isUpdateOk_3 && isUpdateOk_4 && isUpdateOk_5 && isUpdateOk_6 && isUpdateOk_7) {
                    commitData(true);
                } else {
                    RxToast.normal("图片上传中,请稍等");
                }
//                DialogUtil.showTipDialog(mActivity,"提示","工单已成功提交，请耐心等待审核","确定");
                break;

        }
    }


    public void commitData(final boolean isCommit) {
        String name = mName.getText().toString().trim();
        String address = mAddress.getText().toString().trim();
        String leglePhone = mLegalPhone.getText().toString().trim();
        String legleNumber = mDocumentNumber.getText().toString().trim();
        String registName = mRegistName.getText().toString().trim();
        String registNumber = mRegistNumber.getText().toString().trim();
        String registAddress = mRegistAddress.getText().toString().trim();
        String contacts = mContacts.getText().toString().trim();
        String contactsPhone = mContactsPhone.getText().toString().trim();
        String typeStr = type;
        String style = mStyle.getText().toString().trim();
        String protocolNumber = mProtocolNumber.getText().toString().trim();
        String trendStr = trend;
        String note = mNote.getText().toString().trim();
//        if (TextUtils.isEmpty(name)) {
//            showToast("请填写商户名称");
//            return;
//        }
//        if (TextUtils.isEmpty(address)) {
//            showToast("请填写营业地址");
//            return;
//        }
//        if (TextUtils.isEmpty(leglePhone)) {
//            showToast("请填写联系电话");
//            return;
//        }
//        if (TextUtils.isEmpty(legleNumber)) {
//            showToast("请填写法人证件号码");
//            return;
//        }
//        if (TextUtils.isEmpty(registName)) {
//            showToast("请填写注册名称");
//            return;
//        }
//        if (TextUtils.isEmpty(registNumber)) {
//            showToast("请填写注册编号");
//            return;
//        }
//        if (TextUtils.isEmpty(registAddress)) {
//            showToast("请填写注册地址");
//            return;
//        }
//        if (TextUtils.isEmpty(contacts)) {
//            showToast("请填写网点联系人");
//            return;
//        }
//        if (TextUtils.isEmpty(contactsPhone)) {
//            showToast("请填写电话号码");
//            return;
//        }
////        if (TextUtils.isEmpty(name)){
////            showToast("请填写商户名称");
////            return;
////        }
////        if (TextUtils.isEmpty(name)){
////            showToast("请填写商户名称");
////            return;
////        }
//        if (TextUtils.isEmpty(style)) {
//            showToast("请填写行业类别");
//            return;
//        }
//        if (TextUtils.isEmpty(protocolNumber)) {
//            showToast("请填写协议编号");
//            return;
//        }
//        if (TextUtils.isEmpty(note)) {
//            showToast("请填写备注信息");
//            return;
//        }
//        if (heads.size() < 1) {
//            showToast("请上传门头图片");
//            return;
//        }
//        if (fronts.size() < 1) {
//            showToast("请上传前台图片");
//            return;
//        }
//        if (inners.size() < 1) {
//            showToast("请上传店内图片");
//            return;
//        }
//        if (businesss.size() < 1) {
//            showToast("请上传营业执照图片");
//            return;
//        }
//
//        if (legals.size() < 3) {
//            showToast("请上传身份证正反照");
//            return;
//        }
//        if (banks.size() < 3) {
//            showToast("请上传银行卡正反照");
//            return;
//        }
//        if (others.size() < 3) {
//            showToast("请上传2张其他照片");
//            return;
//        }

        DongZhiModle.addMerchantInfo(isAdd, name, id, address, leglePhone, legleNumber, registName, registNumber, registAddress, contacts, contactsPhone, style,
                typeStr, protocolNumber, trendStr, note, headUrl, frontUrl, innerUrl, businesUrl, legalUrl, bankUrl, otherUrl, new HttpCallBack<String>() {
                    @Override
                    public void success(String s) {
//                        if (isCommit) {
//                            DialogUtil.showTipDialog(mActivity, "提示", "工单已成功提交，请耐心等待审核", "确定");
//                        } else {
//                            DialogUtil.showTipDialog(mActivity, "提示", "工单已成功提交，请耐心等待审核", "确定");
////                            DialogUtil.showTipDialog(mActivity, "提示", "信息保存成功", "确定");
//                        }
                        RxToast.normal("工单已成功提交，请耐心等待审核");
                        MerchantInfoCollectActivity.this.finish();
                    }

                    @Override
                    public void fail(String errorStr) {

                    }
                });


    }


    /**
     * 计算GridView宽高
     *
     * @param gridView
     */
    public static void calGridViewWidthAndHeigh(int numColumns, GridView gridView) {

        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高

            if ((i + 1) % numColumns == 0) {
                totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
            }

            if ((i + 1) == len && (i + 1) % numColumns != 0) {
                totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
            }
        }

        totalHeight += 40;

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        gridView.setLayoutParams(params);
    }

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
}
