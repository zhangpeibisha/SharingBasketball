package com.taobao.web.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Create by zhangpe0312@qq.com on 2018/2/27.
 */
public class HttpSession implements HttpSessionListener {


    //每次session创建的时候生成
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    //每次session过时的时候执行
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
