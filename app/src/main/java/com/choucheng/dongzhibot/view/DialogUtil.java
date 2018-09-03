package com.choucheng.dongzhibot.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.choucheng.dongzhibot.R;

/**
 * Created by asus on 2017/11/15.
 */

public class DialogUtil {
    /*拍照*/


    public static Dialog showUserInfoDialog(Context context, final UserEditClick listener) {
        final Dialog dialog = new Dialog(context, R.style.selectPhotoDialog);
        dialog.setContentView(R.layout.dialog_userinfo);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        dialog.show();
        final EditText name = dialog.findViewById(R.id.name);
        final EditText phone = dialog.findViewById(R.id.phone);
        TextView confirm = dialog.findViewById(R.id.confirm);
        TextView cancel = dialog.findViewById(R.id.cancel);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.userData(name.getText().toString().trim(), phone.getText().toString().trim());
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static Dialog showPassword(Context context, final UserEditClick listener) {
        final Dialog dialog = new Dialog(context, R.style.selectPhotoDialog);
        dialog.setContentView(R.layout.dialog_password);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        dialog.show();
        final EditText password1 = dialog.findViewById(R.id.password1);
        final EditText password2 = dialog.findViewById(R.id.password2);
        TextView confirm = dialog.findViewById(R.id.confirm);
        TextView cancel = dialog.findViewById(R.id.cancel);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.userData(password1.getText().toString().trim(), password2.getText().toString().trim());
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static void showLastestDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.selectPhotoDialog);
        dialog.setContentView(R.layout.dialog_lastest_verson);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        dialog.show();
        TextView button = dialog.findViewById(R.id.button);
        TextView title = dialog.findViewById(R.id.title);
        TextView new_verson_text = dialog.findViewById(R.id.new_verson_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public static void showTipDialog(Context context, String title, String content, String confirm) {
        final Dialog dialog = new Dialog(context, R.style.selectPhotoDialog);
        dialog.setContentView(R.layout.dialog_tip);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        dialog.show();
        TextView mButton = dialog.findViewById(R.id.button);
        TextView mTitle = dialog.findViewById(R.id.title);
        TextView mContent = dialog.findViewById(R.id.content);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mTitle.setText(title);
        mContent.setText(content);
    }

    public static Dialog showOrderDialog(Context context, String content, final View.OnClickListener acceptListener, final View.OnClickListener refuseListener) {
        final Dialog dialog = new Dialog(context, R.style.selectPhotoDialog);
        dialog.setContentView(R.layout.dialog_accept_order);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        dialog.show();
        TextView mAccept = dialog.findViewById(R.id.accept);
        TextView mRefuse = dialog.findViewById(R.id.refuse);
        TextView mContent = dialog.findViewById(R.id.content);
        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                acceptListener.onClick(view);
            }
        });
        mRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                refuseListener.onClick(view);
            }
        });
        mContent.setText(content);
        return dialog;
    }

    public void getGongDanDialog(Context context, String content, final View.OnClickListener acceptListener, final View.OnClickListener refuseListener) {
        final Dialog dialog = new Dialog(context, R.style.selectPhotoDialog);
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
                acceptListener.onClick(view);

                dialog.dismiss();


            }
        });
        mRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refuseListener.onClick(view);

                dialog.dismiss();

            }
        });
        mContent.setText(content);
    }

    public interface UserEditClick {
        void userData(String data1, String data2);
    }
}
