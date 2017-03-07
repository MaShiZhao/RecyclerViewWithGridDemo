package com.app.frame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;

import com.app.http.BaseActivityLifecycleCallbacks;

import java.util.LinkedList;

/**
 * Created by MaShiZhao on 2016/12/12
 */

public class FrameApplication extends Application
{
    //错误处理器
    private static ErrorHandler crashHandler = null;
    //FrameApplication实例
    private static FrameApplication instance;
    //保存Activity的列表
    private static LinkedList<Activity> mList = new LinkedList<Activity>();


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onCreate()
    {
        super.onCreate();
        //注册activity的生命周期 （添加 删除 activity 网络请求的终止）
        FrameApplication.this.registerActivityLifecycleCallbacks(new BaseActivityLifecycleCallbacks());
        FrameApplication.instance = this;
        FrameApplication.crashHandler = ErrorHandler.getInstance();
        FrameApplication.crashHandler.setToErrorHandler();
//        welcome处理
//        REQUEST_HEADER.put(BaseConstants.APP_VERSION, Utils.getVersionName(this));

    }

    /**
     * 添加activity
     *
     * @param act
     */
    public static void addToList(final Activity act)
    {
        FrameApplication.mList.add(act);
    }



    public static synchronized FrameApplication getInstance()
    {
        return FrameApplication.instance;
    }

}
