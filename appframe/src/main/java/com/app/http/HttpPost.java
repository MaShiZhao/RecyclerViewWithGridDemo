package com.app.http;

import android.content.Context;
import android.text.TextUtils;

import com.app.utils.BaseConstants;
import com.app.utils.BaseSharedPreferencesUtil;
import com.app.utils.Decrypt_Utils;
import com.app.utils.LogWriter;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * utils 里面有其他形式的httpPost
 */
public class HttpPost
{
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private InterfaceHttpResult mHttpResult;
    private Object objectTag;
    private Platform mPlatform;
    private Class<?> classObj;
    private String postUrl;
    private Map<String, String> postMap;
    private String[] params;
    private int requestCode;
    private Map<String, String> postImages;
    private BaseSharedPreferencesUtil sharedPreferencesUtil;


    /**
     * httpPost 构造
     *
     * @param listener  服务器 回调接口
     * @param className 返回结果 强转对象
     * @param params    请求参数 第一位是url
     */
    public HttpPost(Context context,Object objectTag, InterfaceHttpResult listener, Class<?> className, Map<String, String> postImages, String... params)
    {
        this.objectTag = objectTag;
        this.mHttpResult = listener;
        this.mPlatform = Platform.getInstance();
        this.classObj = className;
        this.params = params;
        this.postImages = postImages;
        sharedPreferencesUtil = new BaseSharedPreferencesUtil(context);
//        onInit(params);
    }

    public HttpPost(Context context,Object objectTag, InterfaceHttpResult listener, Class<?> className, String... params)
    {
        this.objectTag = objectTag;
        this.mHttpResult = listener;
        this.mPlatform = Platform.getInstance();
        this.classObj = className;
        this.params = params;
        this.postImages = null;
        sharedPreferencesUtil = new BaseSharedPreferencesUtil(context);
//        onInit(params);
    }

    public void excute()
    {
        excute(0);
    }

    /**
     * 添加请求code
     *
     * @param requestCode 请求码默认是0
     */
    public void excute(int requestCode)
    {
        this.requestCode = requestCode;
        if (params == null || params.length == 0)
        {
            LogWriter.e("params is null or params length is 0");
            setHttpResultString("");
            return;
        }
        resetPostMap(params);
        try
        {
            okHttpRequest();
        } catch (IOException e)
        {
            LogWriter.e("HttpGet okHttpRequest IOException " + e.toString());
            e.printStackTrace();
        }
    }

    //获取请求的url 和 params参数
    private void resetPostMap(String[] params)
    {
        postMap = new HashMap<>();
        String url = params[0];
        String[] keys = url.split(";");
        if (keys.length > params.length)
        {
            setHttpResultString("httpPost params is missing ");
            return;
        }

        if (keys.length < params.length)
        {
            setHttpResultString("httpPost url  is missing");
            return;
        }
        postUrl = keys[0];
        LogWriter.d("postUrl = " + postUrl);
        //params 第一位是url  后面的是value
        //url 包含请求地址和请求需要的key
        for (int i = 1; i < params.length; i++)
        {
            postMap.put(keys[i], params[i]);
            LogWriter.d("httpPost params key = " + params[i] + " value = " + params[i]);
        }
        LogWriter.d("httpPost mid  = " +sharedPreferencesUtil.getUid());
        LogWriter.d("httpPost oauth_token  = " +sharedPreferencesUtil.getToken());
        LogWriter.d("httpPost oauth_token_secret  = " +sharedPreferencesUtil.getTokenSecret());
        LogWriter.d("httpPost deviceId  = " +sharedPreferencesUtil.getDevicedId());

        postMap.put("mid", sharedPreferencesUtil.getUid());
        postMap.put("oauth_token", sharedPreferencesUtil.getToken());
        postMap.put("oauth_token_secret", sharedPreferencesUtil.getTokenSecret());
        postMap.put("deviceId", sharedPreferencesUtil.getDevicedId());
    }

    /**
     * 调用ok post 请求返回结果
     *
     * @throws IOException
     */
    private void okHttpRequest() throws IOException
    {
        int timeoutConnection = 60;
        // OkHttpClient 初始化配置
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(timeoutConnection, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(timeoutConnection, TimeUnit.SECONDS);
        // TODO: 2016/12/7 需要cookieJar ？
//        client.newBuilder().cookieJar(new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL));

        MultipartBody.Builder multipartEntityBuilder = new MultipartBody.Builder();
        multipartEntityBuilder.setType(MultipartBody.FORM);

        //添加params
        addParams(multipartEntityBuilder);

        if (postImages != null)
        {
            Iterator<Map.Entry<String, String>> strings = postImages.entrySet().iterator();
            while (strings.hasNext())
            {
                Map.Entry<String, String> entry = strings.next();
                String key = entry.getKey();
                String value = entry.getValue();
                File file = new File(value);
                multipartEntityBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            }
        }


        //获取requestBody
        RequestBody body = multipartEntityBuilder.build();
        //设置请求url和body 获取request
        Request.Builder builde = new Request.Builder();

        if (this.objectTag != null)
        {
            builde.tag(this.objectTag);
        }
        Request request = builde
                .url(postUrl)
                .post(body)
                .build();

        //Call 回去返回结果
        Call call = client.newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                final String s = "call " + e.toString();
                mPlatform.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setHttpResultString(s);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                if (call.isCanceled())
                {
                    LogWriter.e("call is canceled");
                } else
                {
                    sendSuccessResultCallback(response);
                }
                if (HttpPost.this.objectTag != null)
                {
                    BaseActivityLifecycleCallbacks.cancelCall(HttpPost.this.objectTag.getClass(), call);
                }
            }
        });
        if (this.objectTag != null)
        {
            BaseActivityLifecycleCallbacks.putCall(this.objectTag.getClass(), call);
        }

    }

    private void addParams(MultipartBody.Builder builder)
    {
        if (postMap != null)
        {
            Iterator<Map.Entry<String, String>> strings = postMap.entrySet().iterator();
            while (strings.hasNext())
            {
                Map.Entry<String, String> entry = strings.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                builder.addFormDataPart(key, value + "");
            }
        }
    }

    /**
     * 设置回调 返回给主线程
     *
     * @param response response
     */
    private String resultString;

    private void sendSuccessResultCallback(final Response response)
    {
        if (mHttpResult == null)
        {
            LogWriter.e("HttpResult should not be null");
            return;
        }
        try
        {
            resultString = response.body().string();
            mPlatform.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    setHttpResultString(resultString);
//                    setHttpResult(resultString);
                }
            });

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 设置回调内容
     */
    public void setHttpResultString(String resultString)
    {
        if (TextUtils.isEmpty(resultString))
        {
            resultString = "接收数据错误!";
            setHttpResult( resultString);
        } else {
            try {
                String jsonStrDes = Decrypt_Utils.decode(BaseConstants.HTTP_KEY, resultString);
                JSONObject jsonO = new JSONObject(jsonStrDes);
                LogWriter.d(" resultString : "+postUrl + jsonStrDes);
                int code = 0;
                if (jsonO.has("code")) {
                    code = jsonO.getInt("code");
                }
                String msg = "";
                if (jsonO.has("msg") && jsonO.getString("msg") != null) {
                    msg = jsonO.getString("msg");
                }
                Object bean = null;
                if (code == BaseConstants.HTTP_CODE_1000 && classObj != null) {
                    bean = new Gson().fromJson(jsonO.toString(), classObj);
                }

                mHttpResult.getCallback(requestCode, code, msg, bean);
            } catch (Exception e) {
                LogWriter.e("mHttpResult result call back response.body().string " + e.toString());
                e.printStackTrace();
            }
        }


    }

    private void setHttpResult(String resultString)
    {
        LogWriter.d(params[0] + " resultJson : " + resultString);
        LogWriter.e(" resultString : " + resultString);
        Object bean = new Gson().fromJson(resultString, classObj);
//        mHttpResult.getCallback(requestCode,0,bean.toString(),bean);
    }


}
