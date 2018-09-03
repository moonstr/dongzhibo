package com.choucheng.dongzhibot.modle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.bjkj.library.okhttp.HttpCallBack;
import com.bjkj.library.okhttp.OkHttpUtils;
import com.bjkj.library.utils.LogUtils;
import com.bjkj.library.utils.SPUtils;
import com.choucheng.dongzhibot.activity.ErrorListActivity;
import com.choucheng.dongzhibot.activity.LoginActivity;
import com.choucheng.dongzhibot.bean.BaseBean;
import com.choucheng.dongzhibot.bean.DeviceInfoBean;
import com.choucheng.dongzhibot.bean.ErrorListBean;
import com.choucheng.dongzhibot.bean.HistoryListBean;
import com.choucheng.dongzhibot.bean.InstallOrderBean;
import com.choucheng.dongzhibot.bean.InstallOrderInfoBean;
import com.choucheng.dongzhibot.bean.LoginBean;
import com.choucheng.dongzhibot.bean.MainBean;
import com.choucheng.dongzhibot.bean.MerchantInfoBean;
import com.choucheng.dongzhibot.bean.MerchantInfoSeeBean;
import com.choucheng.dongzhibot.bean.MerchantNum;
import com.choucheng.dongzhibot.bean.ProtectBeanOrderInfo;
import com.choucheng.dongzhibot.bean.ProtectOrderBean;
import com.choucheng.dongzhibot.bean.UploadBean;
import com.choucheng.dongzhibot.bean.UserInfoBean;
import com.choucheng.dongzhibot.utils.Constants;
import com.google.gson.Gson;
import com.vondear.rxtool.RxImageTool;
import com.vondear.rxtool.view.RxToast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by admin on 2018/6/14.
 */

public class DongZhiModle {

    public static String baseUrl = "http://dzb2.zgcom.cn";

    /**
     * 登陆
     *
     * @param userName
     * @param password
     * @param callBack
     */
    public static void login(String userName, String password, final HttpCallBack callBack) {
        String url = baseUrl + "/index.php?s=/api/login/login";
        Map<String, String> map = new HashMap<>();
        map.put("number", userName);
        map.put("password", password);
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                LoginBean bean = gson.fromJson(s, LoginBean.class);
                SPUtils.put(Constants.KEY_UCODE, bean.data.ucode);
                Log.e("123456789", "success: ucode手动======" + bean.data.ucode);
                callBack.success("");
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail("");
            }
        });
    }

    public static void sendLatLng(String lat, String lng, final HttpCallBack callBack) {
        String url = baseUrl + "/index.php?s=/api/login/position";
        Map<String, String> map = new HashMap<>();
        map.put("number", (String) SPUtils.get(Constants.LOGIN_USER, ""));
        map.put("lat", lat);
        map.put("lng", lng);
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                callBack.success("");
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                RxToast.normal(errorStr);
                callBack.fail("");
            }
        });
    }

    /**
     * 自动登陆
     *
     * @param context
     * @param callBack
     */
    public static void autoLogin(final Context context, final HttpCallBack callBack) {
        String url = baseUrl + "/index.php?s=/api/login/login";
        Map<String, String> map = new HashMap<>();
        map.put("number", (String) SPUtils.get(Constants.LOGIN_USER, ""));
        map.put("password", (String) SPUtils.get(Constants.LOGIN_PASSWORD, ""));

        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                LoginBean bean = gson.fromJson(s, LoginBean.class);
                Log.e("123456789", "success: ucode自动======" + bean.data.ucode);
                SPUtils.put(Constants.KEY_UCODE, bean.data.ucode);
                callBack.success("");
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail("");
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
    }

    /**
     * 首页
     *
     * @param callBack
     */
    public static void main(final HttpCallBack<MainBean.MainData> callBack) {
        String url = baseUrl + "/index.php?s=/api/userinfo/my_lists";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                MainBean bean = gson.fromJson(s, MainBean.class);
                callBack.success(bean.data);
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail("");
            }
        });
    }

    /**
     * 获取用户信息
     *
     * @param callBack
     */
    public static void getUserInfo(final HttpCallBack<UserInfoBean.UserInfo> callBack) {
        String url = baseUrl + "/index.php?s=/api/user_info/my_info";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                UserInfoBean bean = gson.fromJson(s, UserInfoBean.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success(bean.data);
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }

    /**
     * 更新用户信息
     *
     * @param userName
     * @param phone
     * @param callBack
     */
    public static void updateUserInfo(String userName, String phone, final HttpCallBack<String> callBack) {
        String url = baseUrl + "/index.php?s=/api/user_info/edit_info";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        map.put("uname", userName);
        map.put("phone", phone);
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                BaseBean bean = gson.fromJson(s, BaseBean.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success("");
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }

    /**
     * 修改密码
     *
     * @param password
     * @param callBack
     */
    public static void updatePassword(String password, final HttpCallBack<String> callBack) {
        String url = baseUrl + "/index.php?s=/api/user_info/edit_info";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        map.put("password", password);
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                BaseBean bean = gson.fromJson(s, BaseBean.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success("");
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }

    /**
     * 添加商户信息
     *
     * @param isCommit
     * @param name
     * @param address
     * @param tel
     * @param port
     * @param reg_name
     * @param reg_no
     * @param reg_address
     * @param link_name
     * @param link_phone
     * @param style
     * @param buss_type
     * @param assign_no
     * @param buss_forward
     * @param mark
     * @param top_photo
     * @param head_photo
     * @param inside_photo
     * @param port_photo
     * @param legal_photo
     * @param bank_photo
     * @param other_photo
     * @param callBack
     */
    public static void addMerchantInfo(boolean isCommit, String id, String name, String address, String tel, String port, String reg_name, String reg_no,
                                       String reg_address, String link_name, String link_phone, String style, String buss_type,
                                       String assign_no, String buss_forward, String mark, ArrayList<String> top_photo, ArrayList<String> head_photo,
                                       ArrayList<String> inside_photo, ArrayList<String> port_photo, ArrayList<String> legal_photo,
                                       ArrayList<String> bank_photo, ArrayList<String> other_photo, final HttpCallBack<String> callBack) {
        String url = baseUrl + "/index.php?s=/api/merchant/add";
        if (isCommit) {
            url = baseUrl + "/index.php?s=/api/merchant/add";
        } else {
            url = baseUrl + "/index.php?s=/api/merchant/edit";
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        if (!TextUtils.isEmpty(id)) {
            builder.addFormDataPart("id", id);//修改用户信息有id
        }
        builder.addFormDataPart("name", name);//商户名称
        builder.addFormDataPart("address", address);//营业地址
        builder.addFormDataPart("tel", tel);//联系电话
        builder.addFormDataPart("port", port);//证件号码
        builder.addFormDataPart("reg_name", reg_name);//注册名称
        builder.addFormDataPart("reg_no", reg_no);//注册编号
        builder.addFormDataPart("reg_address", reg_address);//注册地址
        builder.addFormDataPart("link_name", link_name);//网点联系人
        builder.addFormDataPart("link_phone", link_phone);//联系电话
        builder.addFormDataPart("buss_cate", style);//行业类别
        builder.addFormDataPart("buss_type", buss_type);//1零售2批发
        builder.addFormDataPart("assign_no", assign_no);//协议编号
        builder.addFormDataPart("buss_forward", buss_forward);//1意向商户2储备商户
        builder.addFormDataPart("mark", mark);//信息备注

        for (int i = 0; i < top_photo.size(); i++) {

            if (!TextUtils.isEmpty(top_photo.get(i))) {
                File file = new File(top_photo.get(i));
                builder.addFormDataPart("top_photo", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }

        }
        for (int i = 0; i < head_photo.size(); i++) {
            if (!TextUtils.isEmpty(head_photo.get(i))) {
                File file = new File(head_photo.get(i));
                builder.addFormDataPart("head_photo", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }

        }
        for (int i = 0; i < inside_photo.size(); i++) {

            if (!TextUtils.isEmpty(inside_photo.get(i))) {
                File file = new File(inside_photo.get(i));
                builder.addFormDataPart("inside_photo", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }

        }
        for (int i = 0; i < port_photo.size(); i++) {

            if (!TextUtils.isEmpty(port_photo.get(i))) {
                File file = new File(port_photo.get(i));
                builder.addFormDataPart("port_photo", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }

        }
        for (int i = 0; i < legal_photo.size(); i++) {

            if (!TextUtils.isEmpty(legal_photo.get(i))) {
                File file = new File(legal_photo.get(i));
                builder.addFormDataPart("legal_photo", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }

        }
        for (int i = 0; i < bank_photo.size(); i++) {

            if (!TextUtils.isEmpty(bank_photo.get(i))) {
                File file = new File(bank_photo.get(i));
                builder.addFormDataPart("bank_photo", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }

        }
        for (int i = 0; i < other_photo.size(); i++) {

            if (!TextUtils.isEmpty(other_photo.get(i))) {
                File file = new File(other_photo.get(i));
                builder.addFormDataPart("other_photo", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }

        }

        OkHttpUtils.postMultiFrom(url, builder.build(), new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                callBack.success(s);
            }

            @Override
            public void fail(String errorStr) {
                callBack.fail(errorStr);
            }
        });

    }


    /**
     * 修改商户信息
     *
     * @param isCommit
     * @param name
     * @param address
     * @param tel
     * @param port
     * @param reg_name
     * @param reg_no
     * @param reg_address
     * @param link_name
     * @param link_phone
     * @param style
     * @param buss_type
     * @param assign_no
     * @param buss_forward
     * @param mark
     * @param top_photo
     * @param head_photo
     * @param inside_photo
     * @param port_photo
     * @param legal_photo
     * @param bank_photo
     * @param other_photo
     * @param callBack
     */
    public static void addMerchantInfo(boolean isCommit, String name, String id, String address, String tel, String port, String reg_name, String reg_no,
                                       String reg_address, String link_name, String link_phone, String style, String buss_type,
                                       String assign_no, String buss_forward, String mark, String top_photo, String head_photo,
                                       String inside_photo, String port_photo, String legal_photo,
                                       String bank_photo, String other_photo, final HttpCallBack<String> callBack) {
        String url = baseUrl + "/index.php?s=/api/merchant/add";

        if (isCommit) {
            url = baseUrl + "/index.php?s=/api/merchant/add";//
        } else {
            url = baseUrl + "/index.php?s=/api/merchant/edit";
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        if (!TextUtils.isEmpty(id)) {
            builder.addFormDataPart("id", id);//修改用户信息有id
        }
        builder.addFormDataPart("name", name);//商户名称
        builder.addFormDataPart("address", address);//营业地址
//        builder.addFormDataPart("tel", tel);//联系电话
        builder.addFormDataPart("port", port);//证件号码
        builder.addFormDataPart("reg_name", reg_name);//注册名称
        builder.addFormDataPart("reg_no", reg_no);//注册编号
        builder.addFormDataPart("reg_address", reg_address);//注册地址
        builder.addFormDataPart("link_name", link_name);//网点联系人
        builder.addFormDataPart("link_phone", link_phone);//联系电话
        builder.addFormDataPart("buss_cate", style);//行业类别
        builder.addFormDataPart("buss_type", buss_type);//1零售2批发
        builder.addFormDataPart("assign_no", assign_no);//协议编号
        builder.addFormDataPart("buss_forward", buss_forward);//1意向商户2储备商户
        builder.addFormDataPart("mark", mark);//信息备注
        builder.addFormDataPart("top_photo", top_photo);//
        builder.addFormDataPart("head_photo", head_photo);//
        builder.addFormDataPart("inside_photo", inside_photo);//
        builder.addFormDataPart("port_photo", port_photo);//
        builder.addFormDataPart("legal_photo", legal_photo);//
        builder.addFormDataPart("bank_photo", bank_photo);//
        builder.addFormDataPart("other_photo", other_photo);//

        OkHttpUtils.postMultiFrom(url, builder.build(), new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                callBack.success(s);
            }

            @Override
            public void fail(String errorStr) {
                callBack.fail(errorStr);
            }
        });

    }

    /**
     * 查看商户信息
     *
     * @param callBack
     */
    public static void seeMerchantInfo(final HttpCallBack<ArrayList<MerchantInfoSeeBean.MerchantInfoSeeData>> callBack) {
        String url = baseUrl + "/index.php?s=/api/merchant/info";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                MerchantInfoSeeBean bean = gson.fromJson(s, MerchantInfoSeeBean.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success(bean.data);
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                Log.e("MerchantSpreadFragment", "fail: ====" + errorStr);
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }

    public static void updateMerchantInfo(String id, final HttpCallBack<MerchantInfoBean.MerchantInfoData> callBack) {
        String url = baseUrl + "/index.php?s=/api/merchant/merchant_info";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        map.put("id", id);
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                MerchantInfoBean bean = gson.fromJson(s, MerchantInfoBean.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success(bean.data);
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }

    public static void testUpLoad(ArrayList<String> files, final HttpCallBack<ArrayList<UploadBean.UploadData>> httpCallBack) {
        String url = baseUrl + "/index.php?s=/api/fileupload/image";
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        for (int i = 0; i < files.size(); i++) {
            if (!TextUtils.isEmpty(files.get(i))) {
                File file = new File(files.get(i));
//                Bitmap src = BitmapFactory.decodeFile(files.get(i));
//                Bitmap bitmap = RxImageTool.compressByQuality(src, 1024 * 200L, false);
//                File outputFile = new File(Constants.APP_ROOT_STORAGE_DIR + file.getName());
//                try {
//                    if (!outputFile.exists()) {
//                        outputFile.getParentFile().mkdirs();
//                        //outputFile.createNewFile();
//                    } else {
//                        outputFile.delete();
//                    }
//                    FileOutputStream out = new FileOutputStream(outputFile);
//
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                } catch (Exception e) {
//                }
                //outputFile
                builder.addFormDataPart("image[]", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            }
        }


        OkHttpUtils.postMultiFrom(url, builder.build(), new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                UploadBean bean = gson.fromJson(s, UploadBean.class);
                String code = bean.status.code;
                if ("0".equals(code)) {
                    httpCallBack.success(bean.data);
                    RxToast.success("上传图片成功");
                } else {

                }
            }

            @Override
            public void fail(String errorStr) {
                RxToast.error("上传图片失败" + errorStr);
                Message message = new Message();
                message.what = 4;
                new ErrorListActivity().handler.sendMessage(message);
            }
        });
    }


    /**
     * 装机工单列表
     *
     * @param callBack
     */
    public static void installOrder(final HttpCallBack<ArrayList<InstallOrderBean.InstallOrder.InstallOrderItem>> callBack) {
        String url = baseUrl + "/index.php?s=/api/Zhuangji/my_lists";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                InstallOrderBean bean = gson.fromJson(s, InstallOrderBean.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success(bean.data.zhuangji_lists);
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }

    /**
     * 查看装机信息
     *
     * @param callBack
     */
    public static void installOrderInfo(String merchant_id, String device_id, final HttpCallBack<InstallOrderInfoBean.InstallOrderInfoData> callBack) {
        String url = baseUrl + "/index.php?s=/api/Zhuangji/info";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        map.put("merchant_id", merchant_id);
        map.put("device_id", device_id);
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                InstallOrderInfoBean bean = gson.fromJson(s, InstallOrderInfoBean.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success(bean.data);
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }


    /**
     * 装机工单接受
     *
     * @param callBack
     */
    public static void installOrderAccept(String merchant_id, String device_id, String isOver, final HttpCallBack<String> callBack) {
        String url = baseUrl + "/index.php?s=/api/Zhuangji/accept";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        map.put("merchant_id", merchant_id);
        map.put("device_id", device_id);
        map.put("is_over", isOver);//0未接受1已接受2拒绝接受3装机中4装机完成
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                BaseBean bean = gson.fromJson(s, BaseBean.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success("");
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }

    /**
     * 提交装机审核
     *
     * @param callBack
     */
    public static void commitReview(String merchant_id, ArrayList<String> fils, HttpCallBack<String> callBack) {
        String url = baseUrl + "/index.php?s=/api/Zhuangji/report";
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        builder.addFormDataPart("merchant_id", merchant_id);
        for (int i = 0; i < fils.size(); i++) {
            String path = fils.get(i);
            if (!TextUtils.isEmpty(path)) {
                File file = new File(fils.get(i));
                builder.addFormDataPart("photos", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            }
        }


        OkHttpUtils.postMultiFrom(url, builder.build(), new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                LogUtils.i(s + "   上传图片成功");
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr + "   上传图片失败");
            }
        });
    }

    /**
     * 提交装机审核2
     *
     * @param callBack
     */
    public static void commitReview(String merchant_id, String files, final HttpCallBack<String> callBack) {
        String url = baseUrl + "/index.php?s=/api/Zhuangji/report";
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        builder.addFormDataPart("merchant_id", merchant_id);
        builder.addFormDataPart("photos", files);

        OkHttpUtils.postMultiFrom(url, builder.build(), new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                LogUtils.i(s + "   申请成功");
                callBack.success(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr + "   申请失败");
            }
        });
    }


    //        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
    //        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
    //            @Override
    //            public void success(String s) {
    //                Gson gson = new Gson();
    //                InstallOrderBean bean = gson.fromJson(s, InstallOrderBean.class);
    //                if ("0".equals(bean.status.code)) {
    //                    callBack.success(bean.data.zhuangji_lists);
    //                } else {
    //                    callBack.fail(bean.status.msg);

    /**
     * 维护工单列表
     *
     * @param callBack
     */
    public static void protectOrder(final HttpCallBack<ArrayList<ProtectOrderBean.ProtectOrder.ProtectOrderItem>> callBack) {
        String url = baseUrl + "/index.php?s=/api/Yunweilists/my_lists";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                ProtectOrderBean bean = gson.fromJson(s, ProtectOrderBean.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success(bean.data.yunwei_lists);
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                Log.e("123456789", "fail: 3");
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }

    /**
     * 维护工单接受
     *
     * @param callBack
     */
    public static void protectOrderAccept(String device_id, String yunwei_over, final HttpCallBack<String> callBack) {
        String url = baseUrl + "/index.php?s=/api/Yunweilists/accept";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        map.put("device_id", device_id);
        map.put("yunwei_over", yunwei_over);//0未接受1已接受2拒绝接受3装机中4装机完成
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                BaseBean bean = gson.fromJson(s, BaseBean.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success("");
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }

    /**
     * 查看维护工单信息
     *
     * @param callBack
     */
    public static void protectOrderInfo(String xunjianid, String d_id, final HttpCallBack<ProtectBeanOrderInfo.ProtectBeanOrderInfoData> callBack) {
        String url = baseUrl + "/index.php?s=/api/Yunweilists/yunwei_info";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        map.put("xunjianid", xunjianid);
        map.put("d_id", d_id);
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                ProtectBeanOrderInfo bean = gson.fromJson(s, ProtectBeanOrderInfo.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success(bean.data);
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }


    /**
     * 提交维护工单信息
     *
     * @param callBack
     */
    public static void commitProtectInfo(String odd_number, String solve_mark, String files, final HttpCallBack<String> callBack) {
        String url = baseUrl + "/index.php?s=/api/Yunweiadd/start";
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        builder.addFormDataPart("odd_number", odd_number);
        builder.addFormDataPart("solve_mark", solve_mark);
        builder.addFormDataPart("photos", files);
//        for (int i =0;i<fils.size();i++){
//            String path = fils.get(i);
//            if (!TextUtils.isEmpty(path)){
//                File file = new File(fils.get(i));
//                builder.addFormDataPart("photos", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
//            }
//        }

        OkHttpUtils.postMultiFrom(url, builder.build(), new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                LogUtils.i(s + "   上传图片成功");
                callBack.success(s);
            }

            @Override
            public void fail(String errorStr) {

                LogUtils.i(errorStr + "   上传图片失败");
            }
        });
    }


    /**
     * 设备信息
     *
     * @param callBack
     */
    public static void deviceInfo(String pos_no, final HttpCallBack<DeviceInfoBean.DeviceInfoData> callBack) {
        String url = baseUrl + "/index.php?s=/api/Xunjian/my_lists";
        //index.php?s=/api/Xunjian/my_lists
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        map.put("pos_no", pos_no);
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                DeviceInfoBean bean = gson.fromJson(s, DeviceInfoBean.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success(bean.data);
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }


    public static void deviceNum(final HttpCallBack<MerchantNum.MerchantNumData> callBack) {
        String url = baseUrl + "/api/xunjian/xunjian_num";
        //index.php?s=/api/Xunjian/my_lists
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                MerchantNum bean = gson.fromJson(s, MerchantNum.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success(bean.data);
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }


    /**
     * 故障列表
     *
     * @param callBack
     */
    public static void errorList(String device_id, final HttpCallBack<ArrayList<ErrorListBean.ErrorListItem>> callBack) {
        String url = baseUrl + "/index.php?s=/api/Xunjian/question";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        map.put("device_id", device_id);
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                ErrorListBean bean = gson.fromJson(s, ErrorListBean.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success(bean.data);
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }


    /**
     * 提交故障信息
     *
     * @param callBack
     */
    public static void addError(String device_id, String mark_id, String question, String photos, final HttpCallBack<String> callBack) {
        String url = baseUrl + "/index.php?s=/api/Xunjian/add_que";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        map.put("device_id", device_id);
        map.put("mark_id", mark_id);
        map.put("question", question);
        map.put("photos", photos);
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                BaseBean bean = gson.fromJson(s, BaseBean.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success("");
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }


    /**
     * app更新
     *
     * @param callBack
     */
    public static void update(String device_id, final HttpCallBack<ArrayList<ErrorListBean.ErrorListItem>> callBack) {
        String url = baseUrl + "/index.php?s=/api/appversion/app_new";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        map.put("device_id", device_id);
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                ErrorListBean bean = gson.fromJson(s, ErrorListBean.class);
                if ("0".equals(bean.status.code)) {
                    callBack.success(bean.data);
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }

    /**
     * 历史任务
     */
    public static void historyTask(String type, String page, final HttpCallBack<HistoryListBean.HistoryListItem> callBack) {

        String url = baseUrl + "/index.php?s=/api/userInfo/my_mission";
        Map<String, String> map = new HashMap<>();
        map.put("ucode", (String) SPUtils.get(Constants.KEY_UCODE, ""));
        map.put("type", type);
        map.put("page", page);
        OkHttpUtils.postForm(url, map, new HttpCallBack<String>() {
            @Override
            public void success(String s) {
                Gson gson = new Gson();
                HistoryListBean bean = gson.fromJson(s, HistoryListBean.class);

                if ("0".equals(bean.status.code)) {
                    callBack.success(bean.data);
                } else {
                    callBack.fail(bean.status.msg);
                }
                LogUtils.i(s);
            }

            @Override
            public void fail(String errorStr) {
                LogUtils.i(errorStr);
                callBack.fail(errorStr);
            }
        });
    }
}
