package com.choucheng.dongzhibot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bjkj.library.okhttp.HttpCallBack;
import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.activity.MerchantInfoCollectActivity;
import com.choucheng.dongzhibot.activity.SeeInstallOrderActivity;
import com.choucheng.dongzhibot.adapter.BaseRecyclerAdapter;
import com.choucheng.dongzhibot.adapter.RecyclerViewHolder;
import com.choucheng.dongzhibot.base.BaseFragment;
import com.choucheng.dongzhibot.bean.HistoryListBean;
import com.choucheng.dongzhibot.bean.InstallOrderBean;
import com.choucheng.dongzhibot.bean.MerchantInfoSeeBean;
import com.choucheng.dongzhibot.bean.PageBean;
import com.choucheng.dongzhibot.modle.DongZhiModle;
import com.vondear.rxtool.view.RxToast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/7/21.
 */

public class InstallOrderFragment extends Fragment {
    TextView totle;
    RecyclerView recycler;
    RecyclerView rv_install_page;
    private BaseRecyclerAdapter adapter;
    private BaseRecyclerAdapter pageAdapter;
    private List<HistoryListBean.HistoryListItem.HistoryList> data = new ArrayList();
    private List<PageBean> pageList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_install_order, container, false);
        recycler = view.findViewById(R.id.recycler);
        rv_install_page = view.findViewById(R.id.rv_install_page);
        totle = view.findViewById(R.id.totle);
        recycler.setEnabled(false);
        totle.setEnabled(false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setData();
        getData("3", "1");
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 3:
                    int totalcount = Integer.parseInt(msg.getData().getString("totalcount"));
                    if (pageList.size() == 0) {
                        if (totalcount == 0) {//没有数据
                            pageList.add(new PageBean("1", true));

                        } else {
                            int allPage = totalcount / 10;
                            if (totalcount % 10 == 0) {
                                for (int i = 0; i < allPage; i++) {
                                    if (i == 0) {
                                        pageList.add(new PageBean(String.valueOf(i + 1), true));
                                    } else {
                                        pageList.add(new PageBean(String.valueOf(i + 1), false));
                                    }

                                }
                            } else {
                                for (int i = 0; i < allPage + 1; i++) {
                                    if (i == 0) {
                                        pageList.add(new PageBean(String.valueOf(i + 1), true));
                                    } else {
                                        pageList.add(new PageBean(String.valueOf(i + 1), false));
                                    }
                                }
                            }
                        }
                        pageAdapter.notifyDataSetChanged();
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void setData() {
        pageAdapter = new BaseRecyclerAdapter<PageBean>(getContext(), pageList) {
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_install_page.setLayoutManager(linearLayoutManager);
        rv_install_page.setAdapter(pageAdapter);
        ((BaseRecyclerAdapter) pageAdapter).setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                getData("3", pageList.get(pos).getPage());
                for (int i = 0; i < pageList.size(); i++) {
                    pageList.get(i).setChecked(false);
                }
                pageList.get(pos).setChecked(true);
                pageAdapter.notifyDataSetChanged();
            }
        });


        adapter = new BaseRecyclerAdapter<HistoryListBean.HistoryListItem.HistoryList>(getActivity(), data) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_install_order;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, final HistoryListBean.HistoryListItem.HistoryList item) {
                holder.setText(R.id.name, "商户名称：" + item.name);
                holder.setText(R.id.address, "地址：" + item.address);
                holder.setVisibility(R.id.v_item_gray, 1);
                //"2".equals(installOrderItems.get(i).is_over
                if (item.status.equals("2")) {
                    holder.setText(R.id.status, "已审核");
                } else if (item.is_over.equals("2")) {
                    holder.setText(R.id.status, "已拒绝");
                }
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
        ((BaseRecyclerAdapter) adapter).setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                if (data.get(pos).is_over.equals("2")) {
                    RxToast.normal("您已拒绝无法查看工单");
                    return;
                }
                Intent intent = new Intent(getActivity(), SeeInstallOrderActivity.class);
                intent.putExtra("merchant_id", data.get(pos).merchant_id);
                intent.putExtra("device_id", data.get(pos).device_id);
                intent.putExtra("isOk", true);
                startActivity(intent);
            }
        });
    }

    private void getData(final String type, final String page) {

        DongZhiModle.historyTask(type, page, new HttpCallBack<HistoryListBean.HistoryListItem>() {
            @Override
            public void success(HistoryListBean.HistoryListItem historyListData) {
                if (null != historyListData) {
                    data.clear();
                    data.addAll(historyListData.list);
                    Message msg = new Message();
                    msg.what = 3;
                    Bundle bundle = new Bundle();
                    bundle.putString("totalcount", historyListData.paging.totalcount);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
//                    setData();
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void fail(String errorStr) {
                Log.e("123456789", "fail: " + errorStr);
            }
        });

    }


}
