package com.taobao.web.listener;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Create by zhangpe0312@qq.com on 2018/2/27.
 */
public class HttpSession implements HttpSessionListener {

     private static Logger logger = Logger.getLogger(HttpSession.class);

    //每次session创建的时候生成
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        logger.info("session创建 id: " + httpSessionEvent.getSession().getId());
    }

    //每次session过时的时候执行
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        logger.info("session销毁 id: " + httpSessionEvent.getSession().getId());
    }
}
