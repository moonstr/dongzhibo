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
import com.choucheng.dongzhibot.activity.ProtectOrderInfoActivity;
import com.choucheng.dongzhibot.activity.SeeInstallOrderActivity;
import com.choucheng.dongzhibot.adapter.BaseRecyclerAdapter;
import com.choucheng.dongzhibot.adapter.RecyclerViewHolder;
import com.choucheng.dongzhibot.base.BaseFragment;
import com.choucheng.dongzhibot.bean.HistoryListBean;
import com.choucheng.dongzhibot.bean.InstallOrderBean;
import com.choucheng.dongzhibot.bean.PageBean;
import com.choucheng.dongzhibot.bean.ProtectOrderBean;
import com.choucheng.dongzhibot.modle.DongZhiModle;
import com.vondear.rxtool.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by admin on 2018/7/21.
 */

public class ProtectOrderFragment extends Fragment {
    TextView totle;
    RecyclerView recycler1;
    RecyclerView rv_protect_page;
    private BaseRecyclerAdapter adapter;
    private BaseRecyclerAdapter pageAdapter;
    private List<HistoryListBean.HistoryListItem.HistoryList> data = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_protect_order, container, false);
        recycler1 = view.findViewById(R.id.recycler);
        rv_protect_page = view.findViewById(R.id.rv_protect_page);
        totle = view.findViewById(R.id.totle);
        recycler1.setEnabled(false);
        totle.setEnabled(false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setData();
        getData("2", "1");
    }

    private void getData(final String type, final String page) {

        DongZhiModle.historyTask(type, page, new HttpCallBack<HistoryListBean.HistoryListItem>() {
            @Override
            public void success(HistoryListBean.HistoryListItem historyListData) {
                if (null != historyListData) {
                    data.clear();
                    data.addAll(historyListData.list);
                    Message msg = new Message();
                    msg.what = 2;
                    Bundle bundle = new Bundle();
                    bundle.putString("totalcount", historyListData.paging.totalcount);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
//                    setData();
                }

            }

            @Override
            public void fail(String errorStr) {
                Log.e("123456789", "fail: " + errorStr);
            }
        });

    }

    private List<PageBean> pageList = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
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
        rv_protect_page.setLayoutManager(linearLayoutManager);
        rv_protect_page.setAdapter(pageAdapter);
        ((BaseRecyclerAdapter) pageAdapter).setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                getData("2", pageList.get(pos).getPage());

                for (int i = 0; i < pageList.size(); i++) {
                    pageList.get(i).setChecked(false);
                }
                Log.e("123456789", "onItemClick: " + pos);
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
//                v_item_gray
                holder.setVisibility(R.id.v_item_gray, 1);
                //!("2").equals(installOrderItems.get(i).yunwei_over)
                if (item.status.equals("2")) {
                    holder.setText(R.id.status, "已审核");
                } else if (item.is_over.equals("2")) {
                    holder.setText(R.id.status, "已拒绝");
                }
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler1.setLayoutManager(layoutManager);
        recycler1.setAdapter(adapter);
        ((BaseRecyclerAdapter) adapter).setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                if (data.get(pos).is_over.equals("2")) {
                    RxToast.normal("您已拒绝无法查看工单");
                    return;
                }
                Intent intent = new Intent(getActivity(), ProtectOrderInfoActivity.class);
                intent.putExtra("d_id", data.get(pos).d_id);
                intent.putExtra("xunjianid", data.get(pos).xunjianid);
                intent.putExtra("isOk", true);
                startActivity(intent);
            }
        });
    }

}
