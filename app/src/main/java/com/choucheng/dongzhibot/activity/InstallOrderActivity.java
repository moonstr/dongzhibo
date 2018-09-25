package com.choucheng.dongzhibot.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bjkj.library.okhttp.HttpCallBack;
import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.adapter.BaseRecyclerAdapter;
import com.choucheng.dongzhibot.adapter.CommonAdapter;
import com.choucheng.dongzhibot.adapter.CommonViewHolder;
import com.choucheng.dongzhibot.adapter.RecyclerViewHolder;
import com.choucheng.dongzhibot.base.BaseActivity;
import com.choucheng.dongzhibot.bean.InstallOrderBean;
import com.choucheng.dongzhibot.bean.PageBean;
import com.choucheng.dongzhibot.modle.DongZhiModle;
import com.choucheng.dongzhibot.view.DialogUtil;
import com.choucheng.dongzhibot.view.RCRelativeLayout;
import com.vondear.rxtool.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/6/13.
 * 装机工单
 */

public class InstallOrderActivity extends BaseActivity {
    @Bind(R.id.listView)
    ListView listView;
    ArrayList<InstallOrderBean.InstallOrder.InstallOrderItem> datas = new ArrayList();
    CommonAdapter adapter;
    @Bind(R.id.rv_install_page)
    RecyclerView rvInstallPage;
    private boolean isUser = false;
    private boolean isEnter = false;
    private BaseRecyclerAdapter baseRecyclerAdapter;
    private List<PageBean> pageList = new ArrayList<>();
    private int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_install_order;
    }

    @Override
    public void initView() {
        baseRecyclerAdapter = new BaseRecyclerAdapter<PageBean>(this, pageList) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_page_tv;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, PageBean item) {
                holder.setText(R.id.tv_item_page, item.getPage());
                if (item.isChecked()) {
                    holder.setBackground(R.id.tv_item_page, R.color.colorAccent);
                } else {
                    holder.setBackground(R.id.tv_item_page, R.color.white);
                }
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvInstallPage.setLayoutManager(layoutManager);
        rvInstallPage.setAdapter(baseRecyclerAdapter);
        ((BaseRecyclerAdapter) baseRecyclerAdapter).setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                for (int i = 0; i < pageList.size(); i++) {
                    pageList.get(i).setChecked(false);
                }
                pageList.get(pos).setChecked(true);
                page = pos + 1;
                getData();
                baseRecyclerAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        adapter = new CommonAdapter<InstallOrderBean.InstallOrder.InstallOrderItem>(mActivity, datas, R.layout.item_install_order) {
            @Override
            protected void convertView(int position, View item, final InstallOrderBean.InstallOrder.InstallOrderItem installOrderItem) {
                TextView name = (TextView) CommonViewHolder.get(item, R.id.name);
                TextView address = (TextView) CommonViewHolder.get(item, R.id.address);
                final TextView status = (TextView) CommonViewHolder.get(item, R.id.status);
                RCRelativeLayout status_bg = (RCRelativeLayout) CommonViewHolder.get(item, R.id.status_bg);
                name.setText("商户名称：" + installOrderItem.name);
                address.setText("地址：" + installOrderItem.address);

//                is_over 0未接受1已接受2拒绝接受3装机中4装机完成
                if (isUser == false) {
                    if (0 == (Integer.parseInt(installOrderItem.is_over))) {
                        status.setText("待受理");
                        status.setTextColor(mActivity.getResources().getColor(R.color.main_green));
                        status_bg.setStrokeColor(mActivity.getResources().getColor(R.color.main_green));
                        status_bg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isEnter) {
                                    return;
                                }
                                if (isUser) {
                                    return;
                                }
                                Dialog dialog = DialogUtil.showOrderDialog(mActivity, installOrderItem.name, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                    dialog.dismiss();
                                        isAccept(installOrderItem.merchant_id, installOrderItem.device_id, status);
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        isCancel(installOrderItem.merchant_id, installOrderItem.device_id, status);
                                    }
                                });
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } else {//is_over ==1
                        if ("0".equals(installOrderItem.status)) {
                            status.setText("受理中");
                            status.setTextColor(mActivity.getResources().getColor(R.color.main_yellow));
                            status_bg.setStrokeColor(mActivity.getResources().getColor(R.color.main_yellow));
                        } else if ("1".equals(installOrderItem.status)) {
                            status.setText("审核中");
                            status.setTextColor(mActivity.getResources().getColor(R.color.main_red));
                            status_bg.setStrokeColor(mActivity.getResources().getColor(R.color.main_red));
                        } else if ("2".equals(installOrderItem.status)) {
                            status.setText("已审核");
                            status.setTextColor(mActivity.getResources().getColor(R.color.main_blue));
                            status_bg.setStrokeColor(mActivity.getResources().getColor(R.color.main_blue));
                        } else if ("3".equals(installOrderItem.status)) {
                            status.setText("未通过");
                            status.setTextColor(mActivity.getResources().getColor(R.color.main_purple));
                            status_bg.setStrokeColor(mActivity.getResources().getColor(R.color.main_purple));
                        }
                        if ("2".equals(installOrderItem.is_over)) {
                            status.setText("已拒绝");
                        }
                    }
                } else {
//                    RxToast.normal("您已拒绝无法查看工单");
                }

            }
        };

        listView.setAdapter(adapter);
        getData();
    }

    //同意
    private void isAccept(String merchant_id, String device_id, final TextView status) {
        DongZhiModle.installOrderAccept(merchant_id, device_id, "1", new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Toast.makeText(InstallOrderActivity.this, "接受成功", Toast.LENGTH_SHORT).show();
                status.setText("受理中");
                isEnter = true;
                getData();
            }

            @Override
            public void fail(String errorStr) {
                Toast.makeText(InstallOrderActivity.this, "接受失败", Toast.LENGTH_SHORT).show();
                getData();
            }
        });
    }

    //拒绝
    private void isCancel(String merchant_id, String device_id, final TextView status) {
        DongZhiModle.installOrderAccept(merchant_id, device_id, "2", new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Toast.makeText(InstallOrderActivity.this, "拒绝成功", Toast.LENGTH_SHORT).show();
                status.setText("已拒绝");
                isUser = true;
                getData();
            }

            @Override
            public void fail(String errorStr) {
                Toast.makeText(InstallOrderActivity.this, "拒绝失败", Toast.LENGTH_SHORT).show();
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

    private int allPage = 0;
    private InstallOrderBean.InstallOrder.InstallOrderPaging paging = null;

    private void getData() {
        DongZhiModle.installOrder(String.valueOf(page), new HttpCallBack<InstallOrderBean.InstallOrder>() {
            @Override
            public void success(InstallOrderBean.InstallOrder installOrderItems) {
                if (installOrderItems != null) {
                    datas.clear();
                    if (paging == null) {
                        paging = installOrderItems.paging;
                        int allNum = Integer.parseInt(paging.totalcount);
                        int nowPage = Integer.parseInt(paging.page);
                        if (allNum != 0) {
                            if (allNum % 10 == 0) {
                                allPage = allNum / 10;
                            } else {
                                allPage = allNum / 10 + 1;
                            }
                            for (int i = 0; i < allPage; i++) {
                                String thisPage = String.valueOf(i + 1);
                                pageList.add(new PageBean(thisPage, false));
                            }
                            if (pageList.size() != 0) {
                                pageList.get(0).setChecked(true);
                                baseRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                        // public String page;//页码
                        //            public String totalcount;//总数
                        //            public String numberofpage;//每页数量
                    }
                    List<InstallOrderBean.InstallOrder.InstallOrderItem> list = new ArrayList<>();
                    list = installOrderItems.zhuangji_lists;
                    for (int i = 0; i < list.size(); i++) {
                        if (!list.get(i).status.equals("2") && !"2".equals(list.get(i).is_over)) {
                            datas.add(list.get(i));
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void fail(String errorStr) {
                getData();
            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (datas.get(i).is_over.equals("1")) {
                    Intent intent = new Intent(mActivity, SeeInstallOrderActivity.class);
                    intent.putExtra("isOk", false);
                    intent.putExtra("merchant_id", datas.get(i).merchant_id);
                    intent.putExtra("device_id", datas.get(i).device_id);
                    startActivityForResult(intent, 1);
                } else if (datas.get(i).is_over.equals("2")) {
                    RxToast.normal("您已拒绝无法查看工单");
                } else {
                    final TextView status = (TextView) CommonViewHolder.get(view, R.id.status);
//                    isCancel(datas.get(i).merchant_id, datas.get(i).device_id, status);
                    Dialog dialog = DialogUtil.showOrderDialog(mActivity, datas.get(i).name, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                                    dialog.dismiss();
                            isAccept(datas.get(i).merchant_id, datas.get(i).device_id, status);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isCancel(datas.get(i).merchant_id, datas.get(i).device_id, status);
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
