package com.taobao.web.listener;

import org.apache.log4j.Logger;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * Create by zhangpe0312@qq.com on 2018/2/27.
 */
public class ServletRequest implements ServletRequestListener {

    private static Logger logger = Logger.getLogger(ServletRequest.class);

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        logger.info("有人请求路径 " + servletRequestEvent.getServletRequest().getLocalAddr());
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        logger.info("有人退出 " + servletRequestEvent.getServletRequest().getLocalAddr());
    }
}
