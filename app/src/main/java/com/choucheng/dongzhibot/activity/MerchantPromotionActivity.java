package com.choucheng.dongzhibot.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.base.BaseActivity;

/**
 * Created by liyou on 2018/6/5.
 */

public class MerchantPromotionActivity extends BaseActivity {
    @Bind(R.id.collect)
    TextView mCollect;
    @Bind(R.id.see)
    TextView mSee;

    @Override
    public int getLayoutId() {
        return R.layout.activity_merchan_promotion;
    }

    @Override
    public void initView() {

    }


    @OnClick({R.id.collect, R.id.see})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.collect:
                Intent intent = new Intent(MerchantPromotionActivity.this, MerchantInfoCollectActivity.class);
                intent.putExtra("isAdd", true);
                startActivity(intent);
                break;
            case R.id.see:
                startActivity(new Intent(MerchantPromotionActivity.this, MerchantInfoSeeActivity.class));
                break;
        }
    }
}
