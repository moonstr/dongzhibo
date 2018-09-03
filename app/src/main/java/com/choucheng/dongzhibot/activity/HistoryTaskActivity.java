package com.choucheng.dongzhibot.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.base.BaseActivity;
import com.choucheng.dongzhibot.fragment.InstallOrderFragment;
import com.choucheng.dongzhibot.fragment.MerchantSpreadFragment;
import com.choucheng.dongzhibot.fragment.ProtectOrderFragment;
import com.choucheng.dongzhibot.view.TitleView;
import com.othershe.calendarview.weiget.CalendarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/7/20.
 */

public class HistoryTaskActivity extends BaseActivity {
    @Bind(R.id.title)
    TitleView title;
    @Bind(R.id.tab)
    TabLayout tab;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    MyPagerAdapter adapter;
    private String[] tabs = new String[]{"装机工单","维护工单","商户推广",};
    ArrayList<Fragment> lists = new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.activity_history_task;
    }

    @Override
    public void initView() {
      final TextView rightText =  title.getRightTextView();
        rightText.setText("筛选");
        rightText.setVisibility(View.VISIBLE);
        rightText.setTextColor(Color.WHITE);
        rightText.setTextSize(16);
        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view =View.inflate(HistoryTaskActivity.this,R.layout.popwindow_calendar,null);
                PopupWindow window=new PopupWindow(view,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
                CalendarView start = (CalendarView) view.findViewById(R.id.start);

                CalendarView end = (CalendarView) view.findViewById(R.id.end);
//日历init，年月日之间用点号隔开
                start.setStartEndDate("2010.7", "2018.12")
                        .setInitDate("2017.11")
                        .setSingleDate("2017.12.12")
                        .init();

                end.setStartEndDate("2010.7", "2018.12")
                        .setInitDate("2017.11")
                        .setSingleDate("2017.12.12")
                        .init();
                window.setOutsideTouchable(true);
                window.setBackgroundDrawable(new BitmapDrawable());
                window.showAsDropDown(rightText);
            }
        });


    }

    @Override
    public void initData() {
        super.initData();

        InstallOrderFragment installOrderFragment = new InstallOrderFragment();
        ProtectOrderFragment protectOrderFragment = new ProtectOrderFragment();
        MerchantSpreadFragment merchantSpreadFragment = new MerchantSpreadFragment();
        lists.add(installOrderFragment);
        lists.add(protectOrderFragment);
        lists.add(merchantSpreadFragment);
        adapter = new MyPagerAdapter(getSupportFragmentManager(),lists);
        viewPager.setAdapter(adapter);

        tab.setupWithViewPager(viewPager);
//        adapter.setFragments(lists);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {


        private List<Fragment> mFragmentList;

//        public void setFragments(ArrayList<Fragment> fragments) {
//            mFragmentList = fragments;
//            notifyDataSetChanged();
//        }

        public MyPagerAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {
            super(fm);
            mFragmentList = fragments;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = mFragmentList.get(position);

            return fragment;
        }

        @Override
        public int getCount() {

            return mFragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }


}
