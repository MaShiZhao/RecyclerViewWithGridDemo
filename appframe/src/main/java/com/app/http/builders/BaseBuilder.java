package com.app.http.builders;

import com.app.http.InterfaceHttpResult;

/**
 * Created by MaShiZhao on 2017/1/6
 */
public abstract class BaseBuilder
{
    public abstract void buildTag(Object object);
    public abstract void buildHttpResult(InterfaceHttpResult interfaceHttpResult);
    public abstract void buildClassName(Class<?> className);
    public abstract void buildParams(String... params);
//    public abstract HttpGet create();
}
