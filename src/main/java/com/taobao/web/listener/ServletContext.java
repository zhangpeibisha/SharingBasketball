package com.taobao.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Create by zhangpe0312@qq.com on 2018/2/27.
 * 该监听器用来监听ServletContext的初始化和销毁事件
 * 容器启动关闭监听器
 */
public class ServletContext implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("contextInitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("contextDestroyed");
    }
}
