package com.app.utils;

import com.app.http.Platform;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public  abstract class HttpPost<T> {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//    private InterfaceHttpResult<T> mHttpResult;
    private Platform mPlatform;
//    private Class<?> classObj;
    private String postUrl;
    private Map<String, Object> postMap;
    private String[] params;
    private int requestCode;

    Type mType;

    /**
     * httpPost 构造
     *
     * @param params      请求参数 第一位是url
     */
    public HttpPost(/*InterfaceHttpResult listener, Class<?> className, */String... params) {
//        this.mHttpResult = listener;
        this.mPlatform = Platform.getInstance();
//        this.classObj = className;
        this.params = params;
//        onInit(params);
        Class<?> cls=getClass();
        Type superclass = cls.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        mType = parameterized.getActualTypeArguments()[0];
    }

//    public void excute() {
//        excute(0);
//    }

    /**
     *
     */
    public void excute(/*int requestCode*/) {
//        this.requestCode = requestCode;
        if (params == null || params.length == 0) {
            LogWriter.e("params is null or params length is 0");
//            setHttpResultString("");
            return;
        }
        resetPostMap(params);
        try {
            okHttpRequest();
        } catch (IOException e) {
            LogWriter.e("HttpGet okHttpRequest IOException " + e.toString());
            e.printStackTrace();
        }
    }

    //获取请求的url 和 params参数
    private void resetPostMap(String[] params) {
        postMap = new HashMap<>();
        String url = params[0];
        String[] keys = url.split(";");
        if (keys.length > params.length) {
            LogWriter.d("httpPost params is missing ");
//            setHttpResultString("");
             throw new RuntimeException("httpPost params is missing ");
//            return;
        }

        if (keys.length < params.length) {
            LogWriter.d("httpPost url  is missing ");
//            setHttpResultString("");
            throw new RuntimeException("httpPost url  is missing ");
//            return;
        }
        postUrl = keys[0];
        LogWriter.d("post url = " + postUrl);
        //params 第一位是url  后面的是value
        //url 包含请求地址和请求需要的key
        for (int i = 1; i < params.length; i++) {
            postMap.put(keys[i], params[i]);
            LogWriter.d("httpPost params key = " + params[i] + " value = " + params[i]);
        }
    }

    /**
     * 调用ok post 请求返回结果
     *
     * @throws IOException
     */
    private void okHttpRequest() throws IOException {
        int timeoutConnection = 60;
        // OkHttpClient 初始化配置
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(timeoutConnection, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(timeoutConnection, TimeUnit.SECONDS);
        // TODO: 2016/12/7 需要cookieJar ？
//        client.newBuilder().cookieJar(new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL));
        //添加params
        FormBody.Builder builder = new FormBody.Builder();
        addParams(builder);
        //获取requestBody
        RequestBody body = builder.build();
        //设置请求url和body 获取request
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        //Call 回去返回结果
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogWriter.d("call onFailure : " + e.toString());
                sendFailureResultCallback(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (call.isCanceled()) {
                    LogWriter.e("call is canceled");
                } else {
                    T bean = new Gson().fromJson(response.body().string(), mType);
                    sendSuccessResultCallback(bean);
                }

            }
        });
    }

    private void addParams(FormBody.Builder builder) {
        if (postMap != null) {
            Iterator<Map.Entry<String, Object>> strings = postMap.entrySet().iterator();
            while (strings.hasNext()) {
                Map.Entry<String, Object> entry = strings.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                builder.add(key, value + "");
            }
        }
    }
    public abstract void sendFailureResultCallback(String error);
    public abstract void sendSuccessResultCallback(T bean);

//    /**
//     * 设置回调 返回给主线程
//     *
//     * @param response response
//     */
//    private String resultString;
//
//    private void sendSuccessResultCallback(final Response response) {
//        if (mHttpResult == null) {
//            LogWriter.e("HttpResult should not be null");
//            return;
//        }
//        try {
//            resultString = response.body().string();
//            mPlatform.execute(new Runnable() {
//                @Override
//                public void run() {
//                    setHttpResultString(resultString);
//                }
//            });
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

//    /**
//     * 设置回调内容
//     */
//    public void setHttpResultString(String resultString) {
//        if (resultString == null) {
//            resultString = "";
//        }
//        LogWriter.d("resultJson : " + resultString);
//        T bean = new Gson().fromJson(resultString, mType);
//        mHttpResult.getCallback(requestCode, bean);
//    }


}
