package com.choucheng.dongzhibot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bjkj.library.okhttp.HttpCallBack;
import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.adapter.CommonAdapter;
import com.choucheng.dongzhibot.adapter.CommonViewHolder;
import com.choucheng.dongzhibot.base.BaseActivity;
import com.choucheng.dongzhibot.bean.MerchantInfoSeeBean;
import com.choucheng.dongzhibot.modle.DongZhiModle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2018/6/12.
 */

public class MerchantInfoSeeActivity extends BaseActivity {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Bind(R.id.all)
    TextView all;
    @Bind(R.id.listView)
    ListView listView;
    public ArrayList<MerchantInfoSeeBean.MerchantInfoSeeData> datas = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_merchant_info_see;
    }

    @Override
    public void initView() {
        all.setVisibility(View.GONE);

    }

    @Override
    public void initData() {
        super.initData();
//        MerchantInfoSeeBean.MerchantInfoSeeData bean1 = new MerchantInfoSeeBean().new MerchantInfoSeeData();
//        bean1.name="1111";
//        bean1.add_time="2018-8-1";
//        bean1.is_set="0";
//
//        MerchantInfoSeeBean.MerchantInfoSeeData bean2 = new MerchantInfoSeeBean().new MerchantInfoSeeData();
//        bean2.name="2222";
//        bean2.add_time="2018-8-2";
//        bean2.is_set="1";
//
//        MerchantInfoSeeBean.MerchantInfoSeeData bean3 = new MerchantInfoSeeBean().new MerchantInfoSeeData();
//        bean3.name="333";
//        bean3.add_time="2018-8-3";
//        bean3.is_set="2";
//        datas.add(bean1);
//        datas.add(bean2);
//        datas.add(bean3);
        DongZhiModle.seeMerchantInfo(new HttpCallBack<ArrayList<MerchantInfoSeeBean.MerchantInfoSeeData>>() {
            @Override
            public void success(ArrayList<MerchantInfoSeeBean.MerchantInfoSeeData> merchantInfoSeeData) {
                datas = merchantInfoSeeData;
                ArrayList<MerchantInfoSeeBean.MerchantInfoSeeData> list = new ArrayList<>();
                for (int i = 0; i < datas.size(); i++) {
                    if (!datas.get(i).status.equals("2")) {
                        list.add(datas.get(i));
                    }
                }
                listView.setAdapter(new CommonAdapter<MerchantInfoSeeBean.MerchantInfoSeeData>(mActivity, list, R.layout.item_merchant_info) {
                    @Override
                    protected void convertView(final int position, View item, MerchantInfoSeeBean.MerchantInfoSeeData merchantInfoSeeData) {
                        TextView name = (TextView) CommonViewHolder.get(item, R.id.name);
                        TextView time = (TextView) CommonViewHolder.get(item, R.id.time);
                        TextView is_set_tv = (TextView) CommonViewHolder.get(item, R.id.is_set);
                        TextView status = (TextView) CommonViewHolder.get(item, R.id.status);
                        name.setText(merchantInfoSeeData.name);
                        Log.e("MerchantSpre", "convertView: " + item.getId());
                        final Date date = new Date();
                        date.setTime(Long.parseLong(merchantInfoSeeData.add_time) * 1000);
                        time.setText(sdf.format(date));
                        boolean isOk = false;
                        switch (merchantInfoSeeData.status) {
                            case "0"://未审核
                                status.setText("待审核");
                                isOk = false;
                                break;
                            case "1"://审核中
                                status.setText("审核中");
                                isOk = false;
                                break;
                            case "3"://审核未过
                                status.setText("审核失败");
                                isOk = false;
                                break;
                            case "2"://审核通过 
                                isOk = true;

                                break;

                        }
                        is_set_tv.setText("修改信息");
                        is_set_tv.setVisibility(View.VISIBLE);
                        is_set_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MerchantInfoSeeActivity.this, MerchantInfoCollectActivity.class);
                                intent.putExtra("id", datas.get(position).id);
                                intent.putExtra("isOk", false);
                                intent.putExtra("isAdd", false);
                                startActivityForResult(intent, 0);
                            }
                        });
                    }
                });
            }

            @Override
            public void fail(String errorStr) {

            }
        });

    }

    @OnClick(R.id.all)
    public void onViewClicked() {
        Toast.makeText(getApplicationContext(), "点击了查看所有商户信息", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
