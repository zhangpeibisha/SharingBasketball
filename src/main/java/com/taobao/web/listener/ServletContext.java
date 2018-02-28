package com.taobao.web.listener;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Create by zhangpe0312@qq.com on 2018/2/27.
 * 该监听器用来监听ServletContext的初始化和销毁事件
 * 容器启动关闭监听器
 */
public class ServletContext implements ServletContextListener {

    private static Logger logger = Logger.getLogger(ServletContext.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("contextInitialized%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        logger.info("容器启动 *************************************************************");

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("容器销毁 *************************************************************");
        System.out.println("contextDestroyed7777777777777777777777777777777777777777777777777&&&&&&&&&&&&&&&&&&&&&&&&");
    }
}
