package com.app.http.builders;

import com.app.http.HttpGet;
import com.app.http.InterfaceHttpResult;

/**
 * Created by MaShiZhao on 2017/1/6
 */
public class HttpGetBuilder extends BaseBuilder
{
    private HttpGet httpGet;

    public HttpGetBuilder()
    {
        httpGet = new HttpGet();
    }

    @Override
    public void buildTag(Object object)
    {
        httpGet.setObjectTag(object);
    }

    @Override
    public void buildHttpResult(InterfaceHttpResult interfaceHttpResult)
    {
        httpGet.setmHttpResult(interfaceHttpResult);
    }

    @Override
    public void buildClassName(Class<?> className)
    {
        httpGet.setClassObj(className);
    }

    @Override
    public void buildParams(String... params)
    {
        httpGet.setParams(params);
    }
}
