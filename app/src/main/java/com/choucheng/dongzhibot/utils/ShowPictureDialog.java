package com.choucheng.dongzhibot.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.activity.ProtectOrderInfoActivity;
import com.choucheng.dongzhibot.adapter.CommonViewHolder;

public class ShowPictureDialog {
    public void showPicture(Context context, String picturePath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_show_picture);
        ImageView imageView = dialog.getWindow().findViewById(R.id.iv_show_picture);
        Glide
                .with(context)
                .asBitmap()
                .load(picturePath)
                .into(((ImageView) CommonViewHolder
                        .get(imageView, R.id.iv_show_picture)));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
