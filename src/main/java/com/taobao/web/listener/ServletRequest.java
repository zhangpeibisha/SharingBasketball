package com.taobao.web.listener;

import com.taobao.dao.entity.User;
import org.apache.log4j.Logger;


import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Create by zhangpe0312@qq.com on 2018/2/27.
 */
public class ServletRequest implements ServletRequestListener {

    private static Logger logger = Logger.getLogger(ServletRequest.class);

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        HttpServletRequest request = (HttpServletRequest) servletRequestEvent.getServletRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null)
            logger.info(user.getSchoolID() + "请求路径 " + request.getRequestURL() + "?" + request.getQueryString());
        else logger.info("游客请求路径 " + request.getRequestURL() + "?" + request.getQueryString());
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        HttpServletRequest request = (HttpServletRequest) servletRequestEvent.getServletRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null)
            logger.info(user.getSchoolID() + "退出路径 " + request.getRequestURL() + "?" + request.getQueryString());
        else logger.info("游客退出路径 " + "退出路径 " + request.getRequestURL() + "?" + request.getQueryString());
    }
}
