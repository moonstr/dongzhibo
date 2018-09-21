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

import com.choucheng.dongzhibot.adapter.BaseRecyclerAdapter;

import com.choucheng.dongzhibot.adapter.RecyclerViewHolder;

import com.choucheng.dongzhibot.bean.HistoryListBean;
import com.choucheng.dongzhibot.bean.MerchantInfoSeeBean;
import com.choucheng.dongzhibot.bean.PageBean;
import com.choucheng.dongzhibot.modle.DongZhiModle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.logging.LogRecord;

import butterknife.Bind;

/**
 * Created by admin on 2018/7/21.
 */

public class MerchantSpreadFragment extends Fragment {
    private static final String TAG = "MerchantSpreadFragment";
    TextView totle;
    RecyclerView recycler;
    RecyclerView rv_merchant_page;
    private List<HistoryListBean.HistoryListItem.HistoryList> datas = new ArrayList<>();
    private BaseRecyclerAdapter adapter;
    private BaseRecyclerAdapter pageAdapter;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private List<PageBean> pageList = new ArrayList<>();


//R.layout.fragment_merchant_spread;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_merchant_spread, container, false);
        recycler = view.findViewById(R.id.recycler);
        rv_merchant_page = view.findViewById(R.id.rv_merchant_page);
        totle = view.findViewById(R.id.totle);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getData("1", "1");//type=1 商户推广数据
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int totalcount = Integer.parseInt(msg.getData().getString("totalcount"));
                    Log.e(TAG, "handleMessage: totalcount====" + totalcount);
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
                    }

                    setData();
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
        rv_merchant_page.setLayoutManager(linearLayoutManager);
        rv_merchant_page.setAdapter(pageAdapter);
        ((BaseRecyclerAdapter) pageAdapter).setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                getData("1", pageList.get(pos).getPage());
                for (int i = 0; i < pageList.size(); i++) {
                    pageList.get(i).setChecked(false);
                }
                pageList.get(pos).setChecked(true);
                pageAdapter.notifyDataSetChanged();
            }
        });

        adapter = new BaseRecyclerAdapter<HistoryListBean.HistoryListItem.HistoryList>(getContext(), datas) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_merchant_info;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, final HistoryListBean.HistoryListItem.HistoryList item) {
                holder.setText(R.id.name, item.name);
                holder.setVisibility(R.id.v_item_gray, 1);
                final Date date = new Date();
                date.setTime(Long.parseLong(item.add_time) * 1000);
                holder.setText(R.id.time, sdf.format(date));
                if ("0".equals(item.is_set)) {//merchantInfoSeeData.is_set
                    holder.setText(R.id.is_set, "查看信息");
                    holder.setText(R.id.status, "未装机");
                    holder.setClickListener(R.id.is_set, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), MerchantInfoCollectActivity.class);
                            intent.putExtra("id", item.id);
                            intent.putExtra("isOk", true);
                            intent.putExtra("isAdd", false);
                            startActivityForResult(intent, 0);
                        }
                    });
                } else {
                    holder.setText(R.id.is_set, "查看信息");
                    holder.setText(R.id.status, "已装机");
                    holder.setClickListener(R.id.is_set, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), MerchantInfoCollectActivity.class);
                            intent.putExtra("id", item.id);
                            intent.putExtra("isOk", true);
                            intent.putExtra("isAdd", false);
                            startActivityForResult(intent, 0);
                        }
                    });
                }


            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }

    private void getData(final String type, final String page) {

        DongZhiModle.historyTask(type, page, new HttpCallBack<HistoryListBean.HistoryListItem>() {
            @Override
            public void success(HistoryListBean.HistoryListItem merchantInfoSeeData) {
                if (null != merchantInfoSeeData) {
                    datas.clear();
                    datas.addAll(merchantInfoSeeData.list);
                    Log.e(TAG, "handleMessage: merchantInfoSeeData.list.size====" + merchantInfoSeeData.list.size());
                    Message msg = new Message();
                    msg.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("totalcount", merchantInfoSeeData.paging.totalcount);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
//                    setData();
                }
            }

            @Override
            public void fail(String errorStr) {
                Log.e(TAG, "fail: errorStr====" + errorStr);
//                getData(type, page);
            }
        });

    }

}
