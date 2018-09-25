package com.choucheng.dongzhibot.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjkj.library.okhttp.HttpCallBack;
import com.choucheng.dongzhibot.R;
import com.choucheng.dongzhibot.base.BaseActivity;
import com.choucheng.dongzhibot.bean.InstallOrderBean;
import com.choucheng.dongzhibot.bean.MainBean;
import com.choucheng.dongzhibot.bean.ProtectOrderBean;
import com.choucheng.dongzhibot.fragment.HintDialogFragment;
import com.choucheng.dongzhibot.modle.DongZhiModle;
import com.choucheng.dongzhibot.utils.GetIP;
import com.choucheng.dongzhibot.utils.LocationListeners;
import com.choucheng.dongzhibot.utils.LocationUtil;
import com.choucheng.dongzhibot.view.DialogUtil;
import com.vondear.rxtool.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liyou on 2018/6/4.
 */

public class MainActivity extends BaseActivity implements LocationListeners, HintDialogFragment.DialogFragmentCallback {
    private static final int LOCATION_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    //正式版SHA1: 22:2E:81:FB:A2:4C:FD:0D:49:23:C0:7B:D9:35:D6:68:C3:8D:AD:44
    @Bind(R.id.promotion)
    ImageView mPromotion;
    @Bind(R.id.business_order)
    ImageView mBusinessOrder;
    @Bind(R.id.inspection)
    ImageView mInspection;
    @Bind(R.id.maintain_order)
    ImageView mMaintainOrder;
    @Bind(R.id.user_info)
    ImageView mUserInfo;
    @Bind(R.id.sys_update)
    ImageView mSysUpdate;
    @Bind(R.id.history)
    LinearLayout mHistory;
    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.tv_zhuangji_num)
    TextView tvZhuangjiNum;
    @Bind(R.id.rl_zhuangji)
    RelativeLayout rlZhuangji;
    @Bind(R.id.install_order_count)
    TextView installOrderCount;
    @Bind(R.id.tv_weifu_num)
    TextView tvWeifuNum;
    @Bind(R.id.rl_weihu)
    RelativeLayout rlWeihu;
    @Bind(R.id.protect_order_count)
    TextView protectOrderCount;
    private String nowLat;
    private String nowLng;
    private boolean isOk = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

//        checkLocationPermission();
        login();
        checkStoragePermission();
        checkLocationPermission();


    }

    private String getPermissionString(int requestCode) {
        String permission = "";
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE:
                permission = Manifest.permission.ACCESS_FINE_LOCATION;
                break;
            case STORAGE_PERMISSION_CODE:
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                break;
        }
        return permission;
    }

    public static boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    private void requestPermission(int permissioncode) {
        String permission = getPermissionString(permissioncode);
        if (!IsEmptyOrNullString(permission)) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    permission)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                if (permissioncode == LOCATION_PERMISSION_CODE) {
                    android.app.DialogFragment newFragment = HintDialogFragment.newInstance(R.string.location_description_title,
                            R.string.location_description_why_we_need_the_permission,
                            permissioncode);
                    newFragment.show(getFragmentManager(), HintDialogFragment.class.getSimpleName());
                } else if (permissioncode == STORAGE_PERMISSION_CODE) {
                    android.app.DialogFragment newFragment = HintDialogFragment.newInstance(R.string.storage_description_title,
                            R.string.storage_description_why_we_need_the_permission,
                            permissioncode);
                    newFragment.show(getFragmentManager(), HintDialogFragment.class.getSimpleName());
                }


            } else {
                Log.i("MY", "返回false 不需要解释为啥要权限，可能是第一次请求，也可能是勾选了不再询问");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{permission}, permissioncode);
            }
        }
    }

    private void checkLocationPermission() {
        // 检查是否有定位权限
        // 检查权限的方法: ContextCompat.checkSelfPermission()两个参数分别是Context和权限名.
        // 返回PERMISSION_GRANTED是有权限，PERMISSION_DENIED没有权限
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统申请该权限。
            Log.i("MY", "没有权限");
            requestPermission(LOCATION_PERMISSION_CODE);
        } else {
            //已经获得权限，则执行定位请求。
//            Toast.makeText(MainActivity.this, "已获取定位权限", Toast.LENGTH_SHORT).show();
            new LocationUtil(this, 1).location();
            isOk = true;
            if (isOk) {
                handler.postDelayed(runnable, 900000);//第一次循环
            }

        }
    }


    private void sendLat() {
//        Log.e("123456789", "send: " + nowLat + nowLng);
        DongZhiModle.sendLatLng(nowLat, nowLng, new HttpCallBack() {
            @Override
            public void success(Object o) {
                Message msg = new Message();
                msg.what = 5;
                handler.sendMessage(msg);
            }

            @Override
            public void fail(String errorStr) {
                Message msg = new Message();
                msg.what = 6;
                handler.sendMessage(msg);
            }
        });
    }

    private void login() {
        DongZhiModle.autoLogin(mActivity, new HttpCallBack() {
            @Override
            public void success(Object o) {
                DongZhiModle.main(new HttpCallBack<MainBean.MainData>() {
                    @Override
                    public void success(MainBean.MainData mainData) {

                    }

                    @Override
                    public void fail(String errorStr) {

                    }
                });
            }

            @Override
            public void fail(String errorStr) {
//                Message msg = new Message();
//                msg.what = 7;
//                handler.sendMessage(msg);
            }
        });
    }

    private ProtectOrderBean.ProtectOrder.ProtectOrderPaging paging1 = null;

    private void getData() {
        DongZhiModle.protectOrder("1", new HttpCallBack<ProtectOrderBean.ProtectOrder>() {
            @Override
            public void success(ProtectOrderBean.ProtectOrder installOrderItems) {
                int num = 0;
                if (installOrderItems != null && installOrderItems.paging != null) {
                    paging1 = installOrderItems.paging;
                    num = Integer.parseInt(paging1.totalcount);
                }
//                List<ProtectOrderBean.ProtectOrder.ProtectOrderItem> list = new ArrayList<>();
//                list = installOrderItems.yunwei_lists;
//                for (int i = 0; i < list.size(); i++) {
//                    if (!list.get(i).yunwei_status.equals("2") && !("2").equals(list.get(i).yunwei_over)) {
//                        num++;
//                    }
//                }
                Message msg = new Message();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putInt("protectOrder", num);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

            @Override
            public void fail(String errorStr) {

                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
                if (data1 < 5) {
                    getData();
                } else {
                    RxToast.error(errorStr);
                }

            }
        });
    }

    private int data1 = 0;
    private int data2 = 0;
    private InstallOrderBean.InstallOrder.InstallOrderPaging paging2 = null;

    private void getData2() {
        DongZhiModle.installOrder("1", new HttpCallBack<InstallOrderBean.InstallOrder>() {
            @Override
            public void success(InstallOrderBean.InstallOrder installOrderItems) {
                int num = 0;

                if (installOrderItems != null && installOrderItems.paging != null) {
                    paging2 = installOrderItems.paging;
                    num = Integer.parseInt(paging2.totalcount);
                }
                Message msg = new Message();
                msg.what = 2;
                Bundle bundle = new Bundle();
                bundle.putInt("installOrder", num);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

            @Override
            public void fail(String errorStr) {
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);
                if (data2 < 5) {
                    getData2();
                } else {
                    RxToast.error(errorStr);
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            new LocationUtil(MainActivity.this, 2).location();
            handler.postDelayed(this, 900000);//第2~n次循环
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int num1 = msg.getData().getInt("protectOrder");
                    if (num1 > 0) {
                        rlWeihu.setVisibility(View.VISIBLE);
                        tvWeifuNum.setText(num1 + "");
                    }
                    break;
                case 2:
                    int num2 = msg.getData().getInt("installOrder");
                    if (num2 > 0) {
                        rlZhuangji.setVisibility(View.VISIBLE);
                        tvZhuangjiNum.setText(num2 + "");
                    }
                    break;
                case 3:
                    data1++;
                    break;
                case 4:
                    data2++;
                    break;
                case 5:
                case 6:
                    getData2();
                    getData();
                    break;
                case 7:
                    MainActivity.this.finish();
                    break;
            }
        }
    };

    int i = 0;
    int num = 0;
    int number = 0;

    @OnClick({
            R.id.promotion, R.id.business_order, R.id.inspection, R.id.maintain_order, R.id.user_info,
            R.id.sys_update, R.id.history, R.id.title
    })
    public void onViewClicked(View view) {

//        checkLocationPermission();
        if (number > 0) {
            checkStoragePermission();
            checkLocationPermission();
        }
        Log.e("123456789", "onViewClicked: isOk===" + isOk);
        if (!isOk) {
            RxToast.error("未获得定位权限无法进行操作");
            number++;
            return;
        }
        switch (view.getId()) {
            case R.id.promotion:
                startActivityForResult(new Intent(MainActivity.this, MerchantPromotionActivity.class), 1);
                break;
            case R.id.business_order:
                startActivityForResult(new Intent(MainActivity.this, InstallOrderActivity.class), 1);
                break;
            case R.id.inspection:
                startActivityForResult(new Intent(MainActivity.this, MerchantInspectionActivity.class), 1);
                break;
            case R.id.maintain_order://维护工单
                startActivityForResult(new Intent(MainActivity.this, ProtectOrderActivity.class), 1);
                break;
            case R.id.user_info:
                startActivityForResult(new Intent(MainActivity.this, UserInfoActivity.class), 2);
                break;
            case R.id.sys_update:
                DialogUtil.showLastestDialog(MainActivity.this);
                break;
            case R.id.history:
                startActivityForResult(new Intent(MainActivity.this, HistoryTaskActivity.class), 1);
                break;
            case R.id.title:
                i++;
                if (i > 5) {
                    startActivity(new Intent(MainActivity.this, TestActivity.class));
                }
                break;
        }
        if (num == 0) {
//            sendLat();
            num++;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            getData2();
            getData();
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            this.finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        handler.removeCallbacksAndMessages(null);

    }

    @Override
    public void send(String lat, String lng) {
        nowLat = lat;
        nowLng = lng;
        sendLat();
    }


    private void checkStoragePermission() {
        // 检查是否有存储的读写权限
        // 检查权限的方法: ContextCompat.checkSelfPermission()两个参数分别是Context和权限名.
        // 返回PERMISSION_GRANTED是有权限，PERMISSION_DENIED没有权限
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统申请该权限。
            Log.i("MY", "没有权限");
            requestPermission(STORAGE_PERMISSION_CODE);
        } else {
            //同组的权限，只要有一个已经授权，则系统会自动授权同一组的所有权限，比如WRITE_EXTERNAL_STORAGE和READ_EXTERNAL_STORAGE
//            Toast.makeText(MainActivity.this, "已获取存储的读写权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void doPositiveClick(int requestCode) {
        String permission = getPermissionString(requestCode);
        if (!IsEmptyOrNullString(permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{permission},
                        requestCode);
            } else {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "定位权限已获取", Toast.LENGTH_SHORT).show();
                    Log.i("MY", "定位权限已获取");
                    new LocationUtil(this, 1).location();
                    isOk = true;
                    if (isOk) {
                        handler.postDelayed(runnable, 900000);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "定位权限被拒绝", Toast.LENGTH_SHORT).show();
                    Log.i("MY", "定位权限被拒绝");
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        android.app.DialogFragment newFragment = HintDialogFragment.newInstance(R.string.location_description_title,
                                R.string.location_description_why_we_need_the_permission,
                                requestCode);
                        newFragment.show(getFragmentManager(), HintDialogFragment.class.getSimpleName());
                        Log.i("MY", "false 勾选了不再询问，并引导用户去设置中手动设置");

                        return;
                    }
                }
                return;
            }
            case STORAGE_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "存储权限已获取", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "存储权限被拒绝", Toast.LENGTH_SHORT).show();
                    Log.i("MY", "定位权限被拒绝");
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        android.app.DialogFragment newFragment = HintDialogFragment.newInstance(R.string.storage_description_title,
                                R.string.storage_description_why_we_need_the_permission,
                                requestCode);
                        newFragment.show(getFragmentManager(), HintDialogFragment.class.getSimpleName());
                        Log.i("MY", "false 勾选了不再询问，并引导用户去设置中手动设置");
                    }
                    return;
                }
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void doNegativeClick(int requestCode) {

    }
}
