package com.bjkj.library.okhttp;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import android.support.annotation.NonNull;
import com.bjkj.library.utils.LogUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by liyou on 2018/3/28.
 */

public class OkHttpUtils {
    public static final MediaType TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpUtils okHttpUtils;
    private static OkHttpClient okHttpClient; // OKHttp网络请求
    public static Handler mHandler;
    /**
     * 单例模式  获取NetRequest实例
     *
     * @return netRequest
     */
    public OkHttpUtils(){

        OkHttpClient.Builder builder=new OkHttpClient().newBuilder();
        builder.connectTimeout(5*60, TimeUnit.SECONDS);
        builder.readTimeout(5*60,TimeUnit.SECONDS);
        builder.writeTimeout(5*60,TimeUnit.SECONDS);
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.d("message = [" + message + "]");
            }
        });
        loggingInterceptor.setLevel(level);
        builder.addInterceptor(loggingInterceptor);
        okHttpClient = builder.build();
        mHandler = new Handler(Looper.getMainLooper());
    }
    private static OkHttpUtils getInstance() {

        if (okHttpUtils == null) {
            okHttpUtils = new OkHttpUtils();
        }
        return okHttpUtils;
    }

    public static void getRequestMethod(String url,final HttpCallBack<String> callBack){

        Request request = new Request.Builder().url(url).build();
        addCallBack(callBack,request);

    }

    public static void postJson(String url, String requestBodyJson, final HttpCallBack<String> callBack) {
        RequestBody body = RequestBody.create(TYPE_JSON, requestBodyJson);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        addCallBack(callBack, request);
    }

    public static void postForm(String url, Map<String,String> params, final HttpCallBack<String> callBack) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Set<String> keySet = params.keySet();
        for(String key:keySet) {
            String value = params.get(key);
            formBodyBuilder.add(key,value);
        }
        FormBody formBody = formBodyBuilder.build();
        Request request = new Request
                .Builder()
                .post(formBody)
                .url(url)
                .build();
        addCallBack(callBack, request);
    }

    public static void postMultiFrom(String url, MultipartBody formBody, final HttpCallBack<String> callBack){
        Request request = new Request
                .Builder()
                .post(formBody)
                .url(url)
                .build();
        addCallBack(callBack, request);
    }
    public static void addCallBack(final  HttpCallBack<String> callBack, Request request) {
        isNull();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.fail(e.getMessage());
                    }
                });

            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String json = response.body().string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                callBack.success(json);
                            }catch (Exception ex){
                                callBack.fail(ex.getMessage());
                            }

                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.fail(response.message() );
                        }
                    });
                }
            }
        });
    }

    /**
     * @param url 下载连接
     * @param saveDir 储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public static void download(final String url, final String saveDir, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(url));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess();
                } catch (Exception e) {
                    LogUtils.i(e.getMessage());
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        LogUtils.i(e.getMessage());
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        LogUtils.i(e.getMessage());
                    }
                }
            }
        });
    }
    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    @NonNull
    private static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private static String  isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * 打印请求信息
     *
     * @param message
     */
    public static void showMessage(String message) {
        LogUtils.d("OkHttpUtils"+" "+message);
    }
    public static void isNull(){
        if (okHttpClient==null){
            getInstance();
        }
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }

}
