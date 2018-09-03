package com.choucheng.dongzhibot.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.choucheng.dongzhibot.R;

/**
 * Created by asus on 2017/10/24.
 */

public class TitleView extends RelativeLayout{
    private ImageView mLeft;
    private TextView mCenter;
    private ImageView mRightImage;
    private TextView mRightText,mLeftText;
    private View mBottomLine;
    private View titleLayout;
    private String title;
    private boolean showLeft;
    private int textSize;
    private int textColor;
    private int bgColor;
    private String rightText,leftText;
    private int rightTextColor,leftTextColor;
    private Drawable rightImage,leftImage;
    private Drawable rightImageOfRightTextView;
    private boolean isShowBottomLine;
    private OnClickListener listener;
    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        LayoutInflater.from(context).inflate(R.layout.view_title,this);
        View.inflate(context,R.layout.view_title,this);
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.viewTitle);
        title = ta.getString(R.styleable.viewTitle_mTitle);
        showLeft = ta.getBoolean(R.styleable.viewTitle_mShowLeft,true);
        textSize = ta.getInteger(R.styleable.viewTitle_mTitleSize,20);
        textColor = ta.getColor(R.styleable.viewTitle_mTitleColor,getResources().getColor(R.color.white));
        rightTextColor = ta.getColor(R.styleable.viewTitle_mRightTextColor,getResources().getColor(R.color.main_blue));
        leftTextColor = ta.getColor(R.styleable.viewTitle_mLeftTextColor,getResources().getColor(R.color.main_blue));
        leftImage = ta.getDrawable(R.styleable.viewTitle_mTitleLeftImage);
        bgColor = ta.getColor(R.styleable.viewTitle_mTitleLayoutBgColor,getResources().getColor(R.color.main_blue));
        leftText = ta.getString(R.styleable.viewTitle_mLeftText);
        rightText = ta.getString(R.styleable.viewTitle_mRightText);
        rightImage = ta.getDrawable(R.styleable.viewTitle_mRightImage);
        rightImageOfRightTextView = ta.getDrawable(R.styleable.viewTitle_mRightImageOfRightTextView);
        isShowBottomLine =  ta.getBoolean(R.styleable.viewTitle_mShowBottomLine,true);
        initView(context);
        ta.recycle();
    }


    private void initView(final Context context){
        titleLayout = findViewById(R.id.titleLayout);
        mLeft = findViewById(R.id.left);
        mCenter = findViewById(R.id.center);
        mRightImage = findViewById(R.id.rightImage);
        mRightText = findViewById(R.id.rightText);
        mLeftText = findViewById(R.id.leftText);
        mBottomLine = findViewById(R.id.bottom_line);
        mCenter.setText(title);
        mCenter.setTextSize(textSize);
        mCenter.setTextColor(textColor);
        mLeft.setVisibility(showLeft?VISIBLE:GONE);
        titleLayout.setBackgroundColor(bgColor);
        setLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
            }
        });
        mBottomLine.setVisibility(isShowBottomLine?VISIBLE:GONE);
        if(leftImage != null){
            mLeft.setImageDrawable(leftImage);
        }
        if (!TextUtils.isEmpty(rightText)){
            mRightText.setText(rightText);
            mRightText.setTextColor(rightTextColor);
            mRightText.setVisibility(VISIBLE);
        }
        if(rightImageOfRightTextView != null) {
            mRightText.setCompoundDrawablesRelativeWithIntrinsicBounds(rightImageOfRightTextView,null,null,null);
        }
        if (rightImage!=null){
            mRightImage.setImageDrawable(rightImage);
            mRightImage.setVisibility(VISIBLE);
        }
        if(!TextUtils.isEmpty(leftText)){
            mLeftText.setText(leftText);
            mLeftText.setTextColor(leftTextColor);
            mLeftText.setVisibility(VISIBLE);
            setLeftTextClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity)context).finish();
                }
            });

        }
    }
    public void setLeftResource(int resId){
        mLeft.setImageResource(resId);
    }
    public void setCenterText(String str){
        mCenter.setText(str);
    }
    public void setCenterText(int str){
        mCenter.setText(str);
    }
    public void setRightText(String str){
        if (mRightText!=null){
            mRightText.setText(str);
            mRightText.setVisibility(VISIBLE);
        }


    }
    public void setRightImage(int resId){
        rightImageOfRightTextView = getContext().getResources().getDrawable(resId);
        if(rightImageOfRightTextView != null) {
            mRightText.setCompoundDrawablesRelativeWithIntrinsicBounds(rightImageOfRightTextView,null,null,null);
        }
    }
    public void setRightImageResource(int resId){
        if (mRightText!=null) {
            mRightImage.setImageResource(resId);
            mRightImage.setVisibility(VISIBLE);
        }
    }
    public TextView getRightTextView(){
        return mRightText;
    }
    public void setLeftClickListener(OnClickListener listener){
            this.listener = listener;
            mLeft.setOnClickListener(listener);
    }

    public void setLeftTextClickListener(OnClickListener listener){
        this.listener = listener;
        mLeftText.setOnClickListener(listener);
    }
    public void setmRightImageClickListener(OnClickListener listener){
        mRightImage.setOnClickListener(listener);
    }
    public void setmRightTextClickListener(OnClickListener listener){
        mRightText.setOnClickListener(listener);
    }

    public void setBgColor(int bgColor){
        this.bgColor = bgColor;
        titleLayout.setBackgroundColor(bgColor);
    }

    public void setShowLeft(boolean isVisible){
        mLeft.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void setShowRightText(boolean isVisible){
        mRightText.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void setShowRightImg(boolean isVisible){
        mRightImage.setVisibility(isVisible ? VISIBLE : GONE);
        mRightImage.setClickable(isVisible);
    }
}
