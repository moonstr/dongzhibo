package com.choucheng.dongzhibot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;//集合类，layout里包含的View,以View的id作为key，value是view对象
    private Context mContext;//上下文对象

    public RecyclerViewHolder(View itemView, Context mContext) {
        super(itemView);
        this.mContext = mContext;
        mViews = new SparseArray<View>();
    }

    private <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getView(int viewId) {
        return findViewById(viewId);
    }

    public TextView getTextView(int viewId) {
        return (TextView) getView(viewId);
    }

    public Button getButton(int viewId) {
        return (Button) getView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return (ImageView) getView(viewId);
    }

    public ImageButton getImageButton(int viewId) {
        return (ImageButton) getView(viewId);
    }

    public EditText getEditText(int viewId) {
        return (EditText) getView(viewId);
    }

    public RecyclerViewHolder setText(int viewId, String value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }

    public RecyclerViewHolder setTextColor(int viewId, int value) {
        TextView view = findViewById(viewId);
        view.setTextColor(value);
        return this;
    }

    public RecyclerViewHolder setBackground(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }


//    public RecyclerViewHolder setGlide(int viewId, String img) {
//        ImageView view = findViewById(viewId);
//        Glide.with(mContext)
//                .load(img)//图片地址
//                .asBitmap()//显示gif静态图片  .asGif();//显示gif动态图片
//                .centerCrop()//动态转换
//                .placeholder(R.mipmap.default_header)//加载失败
//                .into(view);//图片展示位置
//        return this;
//    }


    public RecyclerViewHolder setClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
        return this;
    }



    public RecyclerViewHolder setVisibility(int viewId, int isShow) {
        View view = findViewById(viewId);
        switch (isShow) {
            case 1:
                view.setVisibility(View.VISIBLE);
                break;
            case 2:
                view.setVisibility(View.INVISIBLE);
                break;
            case 3:
                view.setVisibility(View.GONE);
                break;
        }
        return this;
    }

}

