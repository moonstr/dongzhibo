package com.choucheng.dongzhibot.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

    }

    private boolean isEnter = true;
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
            public void bindData(final RecyclerViewHolder holder, final int position, final ProtectOrderBean.ProtectOrder.ProtectOrderItem item) {
                if (null != item.merchant_info) {
                    holder.setText(R.id.name, "商户名称：" + item.merchant_info.name);
                    holder.setText(R.id.address, "地址：" + item.merchant_info.address);
                } else {
                    holder.setText(R.id.name, "商户名称：");
                    holder.setText(R.id.address, "地址：");
                }
                final TextView status = (TextView) holder.itemView.findViewById(R.id.status);
                RCRelativeLayout status_bg = (RCRelativeLayout) holder.itemView.findViewById(R.id.status_bg);
                if ("0".equals(item.yunwei_over) && "0".equals(item.yunwei_status)) {
                    holder.setText(R.id.status, "待受理");//xunjianid='4240', merchant_id='15768', yunwei_id='', id='650', yunwei_status='0', yunwei_over='0',
                    holder.setTextColor(R.id.status, R.color.main_green);
                    status_bg.setStrokeColor(mActivity.getResources().getColor(R.color.main_green));
                    status.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            status.setEnabled(false);
                            dialog(item.merchant_info.address, item.d_id, item.id);

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
                Log.e("123456789", "onClick: " + datas.get(i).toString());
                Log.e("123456789", "onItemClick: ================" + i);
                if (datas.get(i).yunwei_over.equals("1")) {

                    Intent intent = new Intent(mActivity, ProtectOrderInfoActivity.class);
                    intent.putExtra("id", datas.get(i).id);
                    intent.putExtra("d_id", datas.get(i).d_id);
                    intent.putExtra("xunjianid", datas.get(i).xunjianid);
                    intent.putExtra("isOk", false);
                    startActivityForResult(intent, 1);
                } else if (datas.get(i).yunwei_over.equals("2")) {
                    RxToast.normal("您已拒绝无法查看工单");
                } else if (datas.get(i).yunwei_over.equals("0") && datas.get(i).yunwei_status.equals("0")) {
//                    isCancel(datas.get(i).d_id, status);
                    dialog(datas.get(i).merchant_info.address, datas.get(i).d_id, datas.get(i).id);
                }

            }
        });
        getData();
    }

    private void dialog(String content, final String d_id, final String id) {
        final Dialog dialog = new Dialog(this, R.style.selectPhotoDialog);
        dialog.setContentView(R.layout.dialog_accept_order);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        dialog.show();
        TextView mAccept = dialog.findViewById(R.id.accept);
        final TextView mRefuse = dialog.findViewById(R.id.refuse);
        TextView mContent = dialog.findViewById(R.id.content);
        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isAccept(d_id, id);
//                datas.get(i).yunwei_over = "1";
                dialog.dismiss();
            }
        });
        mRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCancel(d_id, id);
                dialog.dismiss();

            }
        });
        mContent.setText(content);
    }

    //同意
    private void isAccept(String d_id, String id) {
        DongZhiModle.protectOrderAccept(d_id, "1", id, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Toast.makeText(ProtectOrderActivity.this, "接受成功", Toast.LENGTH_SHORT).show();
                getData();
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);

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
                getData();
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
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
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);

            }

            @Override
            public void fail(String errorStr) {
                getData();
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapterRV.notifyDataSetChanged();
                    break;

                case 2:
                    isEnter = true;
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
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
