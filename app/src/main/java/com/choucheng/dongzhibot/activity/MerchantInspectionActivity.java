package com.choucheng.dongzhibot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bjkj.library.okhttp.HttpCallBack;
import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.base.BaseActivity;
import com.choucheng.dongzhibot.bean.DeviceInfoBean;
import com.choucheng.dongzhibot.bean.MerchantNum;
import com.choucheng.dongzhibot.modle.DongZhiModle;
import com.choucheng.dongzhibot.view.TitleView;
import com.vondear.rxtool.view.RxToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2018/7/2.
 * <p>
 * 商户巡检，开始巡检
 */

public class MerchantInspectionActivity extends BaseActivity {
    @Bind(R.id.title)
    TitleView title;
    @Bind(R.id.code)
    EditText code;
    @Bind(R.id.confirm)
    TextView confirm;
    @Bind(R.id.tv_num_time)
    TextView tvNumTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_inspect;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        super.initListener();
        getNum();
        title.setmRightImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, ActivityScanerCode.class));
            }
        });
    }

    @OnClick(R.id.confirm)
    public void onViewClicked() {
        String device_id = code.getText().toString().trim();
        if (TextUtils.isEmpty(device_id)) {
            RxToast.normal("请输入POS编号或终端序列号");
            return;
        }
        isExist(device_id);

    }

    String num = "";

    private void getNum() {
        DongZhiModle.deviceNum(new HttpCallBack<MerchantNum.MerchantNumData>() {
            @Override
            public void success(MerchantNum.MerchantNumData numData) {
                if (numData == null) {
                    RxToast.normal("未获取到数据");
                } else {
                    num = numData.count;
                    tvNumTime.setText(num + "");
                }

            }

            @Override
            public void fail(String errorStr) {
//                Log.e("123456789", "fail: " + errorStr);
                RxToast.error(errorStr);
            }
        });
    }

    //检测改编号是否存在
    private void isExist(String device_id) {

        DongZhiModle.deviceInfo(device_id, new HttpCallBack<DeviceInfoBean.DeviceInfoData>() {
            @Override
            public void success(DeviceInfoBean.DeviceInfoData deviceInfoData) {
                if (deviceInfoData == null) {
                    RxToast.normal("未获取到数据");
                }
                Intent intent = new Intent(mActivity, DeviceInfoActivity.class);
                intent.putExtra("pos_no", code.getText().toString().trim());
                startActivityForResult(intent, 1);
            }

            @Override
            public void fail(String errorStr) {
//                Log.e("123456789", "fail: " + errorStr);
                RxToast.normal(errorStr);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
