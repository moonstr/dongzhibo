package com.choucheng.dongzhibot.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.bjkj.library.okhttp.HttpCallBack;
import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.adapter.BaseRecyclerAdapter;
import com.choucheng.dongzhibot.adapter.CommonAdapter;
import com.choucheng.dongzhibot.adapter.CommonViewHolder;
import com.choucheng.dongzhibot.adapter.RecyclerViewHolder;
import com.choucheng.dongzhibot.base.BaseActivity;
import com.choucheng.dongzhibot.bean.ProtectOrderBean;
import com.choucheng.dongzhibot.modle.DongZhiModle;
import com.choucheng.dongzhibot.view.DialogUtil;
import com.choucheng.dongzhibot.view.RCRelativeLayout;
import com.vondear.rxtool.view.RxToast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/6/13.
 * 维护工单列表
 */

public class ProtectOrderActivity extends BaseActivity {
    //    @Bind(R.id.listView)
//    ListView listView;
    ArrayList<ProtectOrderBean.ProtectOrder.ProtectOrderItem> datas = new ArrayList();
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    //    CommonAdapter adapter;
    private BaseRecyclerAdapter adapterRV;

    @Override
    public int getLayoutId() {
        return R.layout.activity_protect_order;
    }

    @Override
    public void initView() {
        getData();
    }

    private boolean isEnter = false;
    private boolean isEnter2 = false;
    private boolean isClose = false;//控制监听是否生效

    @Override
    public void initData() {
        super.initData();
        adapterRV = new BaseRecyclerAdapter<ProtectOrderBean.ProtectOrder.ProtectOrderItem>(this, datas) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_install_order;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, final int position, final ProtectOrderBean.ProtectOrder.ProtectOrderItem item) {
                if (null != item.merchant_info) {
                    holder.setText(R.id.name, "商户名称：" + item.merchant_info.name);
                    holder.setText(R.id.address, "地址：" + item.merchant_info.address);
                } else {
                    holder.setText(R.id.name, "商户名称：");
                    holder.setText(R.id.address, "地址：");
                }
                final TextView status = (TextView) holder.itemView.findViewById(R.id.status);
                RCRelativeLayout status_bg = (RCRelativeLayout) holder.itemView.findViewById(R.id.status_bg);
                if ("0".equals(item.yunwei_over)) {
                    holder.setText(R.id.status, "待受理");
                    holder.setTextColor(R.id.status, R.color.main_green);
                    status_bg.setStrokeColor(mActivity.getResources().getColor(R.color.main_green));
                    holder.setClickListener(R.id.status, new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Log.e("123456789", "bindData: ================" + position);
                            new DialogUtil().getGongDanDialog(mActivity, item.merchant_info.address, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    isAccept(item.d_id, item.id);
                                    getData();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    isCancel(item.d_id, item.id);
                                    getData();
                                }
                            });
                            adapterRV.notifyDataSetChanged();
                        }
                    });
                } else if ("0".equals(item.yunwei_status)) {
                    status.setText("受理中");
                    status.setTextColor(mActivity.getResources().getColor(R.color.main_yellow));
                    status_bg.setStrokeColor(mActivity.getResources().getColor(R.color.main_yellow));
                } else if ("3".equals(item.yunwei_status)) {
                    status.setText("未通过");
                    status.setTextColor(mActivity.getResources().getColor(R.color.main_red));
                    status_bg.setStrokeColor(mActivity.getResources().getColor(R.color.main_red));
                } else if ("1".equals(item.yunwei_status)) {
                    status.setText("审核中");
                    status.setTextColor(mActivity.getResources().getColor(R.color.main_blue));
                    status_bg.setStrokeColor(mActivity.getResources().getColor(R.color.main_blue));
                } else if ("2".equals(item.yunwei_status)) {
                    status.setText("装机完成");
                    status.setTextColor(mActivity.getResources().getColor(R.color.main_purple));
                    status_bg.setStrokeColor(mActivity.getResources().getColor(R.color.main_purple));
                } else if ("2".equals(item.yunwei_over)) {
                    status.setText("已拒绝");
                }
            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterRV);
        ((BaseRecyclerAdapter) adapterRV).setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, final int i) {
                Log.e("123456789", "onItemClick: ================" + i);
                if (datas.get(i).yunwei_over.equals("1")) {

                    Intent intent = new Intent(mActivity, ProtectOrderInfoActivity.class);

                    intent.putExtra("d_id", datas.get(i).d_id);
                    intent.putExtra("xunjianid", datas.get(i).xunjianid);
                    intent.putExtra("isOk", false);
                    startActivityForResult(intent, 1);
                } else if (datas.get(i).yunwei_over.equals("2")) {
                    RxToast.normal("您已拒绝无法查看工单");
                } else {
//                    isCancel(datas.get(i).d_id, status);
                    new DialogUtil().getGongDanDialog(mActivity, datas.get(i).merchant_info.address, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isAccept(datas.get(i).d_id, datas.get(i).id);
                            getData();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isCancel(datas.get(i).d_id, datas.get(i).id);
                            getData();
                        }
                    });
                    adapterRV.notifyDataSetChanged();
                }

            }
        });
    }

    //同意
    private void isAccept(String d_id, String id) {
        DongZhiModle.protectOrderAccept(d_id, "1", id, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Toast.makeText(ProtectOrderActivity.this, "接受成功", Toast.LENGTH_SHORT).show();
                isClose = true;
            }

            @Override
            public void fail(String errorStr) {
                Toast.makeText(ProtectOrderActivity.this, "接受失败", Toast.LENGTH_SHORT).show();
                isClose = true;
            }
        });
    }

    //拒绝
    private void isCancel(String d_id, String id) {
        DongZhiModle.protectOrderAccept(d_id, "2", id, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Toast.makeText(ProtectOrderActivity.this, "拒绝成功", Toast.LENGTH_SHORT).show();
                isClose = true;
            }

            @Override
            public void fail(String errorStr) {
                Toast.makeText(ProtectOrderActivity.this, "拒绝失败", Toast.LENGTH_SHORT).show();
                isClose = true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void getData() {
        DongZhiModle.protectOrder(new HttpCallBack<ArrayList<ProtectOrderBean.ProtectOrder.ProtectOrderItem>>() {
            @Override
            public void success(ArrayList<ProtectOrderBean.ProtectOrder.ProtectOrderItem> installOrderItems) {
                datas.clear();
                for (int i = 0; i < installOrderItems.size(); i++) {
                    if (!installOrderItems.get(i).yunwei_status.equals("2") && !("2").equals(installOrderItems.get(i).yunwei_over)) {
                        datas.add(installOrderItems.get(i));
                    }
                }
                adapterRV.notifyDataSetChanged();
            }

            @Override
            public void fail(String errorStr) {
                getData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            getData();
        }

    }

    @Override
    public void initListener() {
        super.initListener();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
