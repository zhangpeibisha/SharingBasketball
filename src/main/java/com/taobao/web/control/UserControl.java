package com.taobao.web.control;

import com.taobao.dao.databasesDaoImpl.RoleDaoImpl;
import com.taobao.dao.databasesDaoImpl.UserDaoImpl;
import com.taobao.dao.entity.Role;
import com.taobao.dao.entity.User;
import com.taobao.utils.sign.MD5;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by zhangpe0312@qq.com on 2018/2/26.
 */
@Controller
public class UserControl {

    private static Logger logger = Logger.getLogger(UserControl.class);

    @Autowired
    private MD5 md5;

    @Autowired
    private UserDaoImpl userDao;

    @Autowired
    private RoleDaoImpl roleDao;

    /**
     * 登陆采用Get方法
     *
     * @return 返回登陆结果
     */
    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    public @ResponseBody
    Map<String, String> login(HttpServletRequest req) {
        Map<String, String> map = new HashMap<>();

        try {
            String user = req.getParameter("user");
            //加密后的字符串与登陆页面过来的值是否相同
            if (!req.getParameter("login").equals(md5.encryption(user+"login").toLowerCase())) {
                map.put("data", "3");
                logger.error("用户 " + user + "未通过登陆页面请求数据");
                return map;
            }
            String password = req.getParameter("password");



            int resutl = userDao.findUserBySchoolID(user, password);

            map.put("data", resutl + "");

            logger.info("用户 " + user + " 登陆登陆代码位 " + resutl);



        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url格式错误 " + req.getRequestURL());
            map.put("data", "3");
        }
        return map;
    }

    /**
     * 验证校园卡是否正确
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/schoolCard", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> schoolCard(HttpServletRequest req) {
        Map<String, String> map = new HashMap<>();


        return map;
    }

    /**
     * 注册账号接口
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> register(HttpServletRequest req) {
        Map<String, String> map = new HashMap<>();

        return map;
    }

    /**
     * 找回密码接口 通过手机短信
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/updatePasswordBySMS", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> updatePasswordBySMS(HttpServletRequest req) {
        Map<String, String> map = new HashMap<>();

        return map;
    }

    /**
     * 通过旧密码更新新密码
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/updatePasswordByOldPassword", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> updatePasswordByOldPassword(HttpServletRequest req) {
        Map<String, String> map = new HashMap<>();

        try {
            String user = req.getParameter("user");
            //加密后的字符串与登陆页面过来的值是否相同
            if (!req.getParameter("updateOldPassword").equals(md5.encryption(user + "updateOldPassword").toLowerCase())) {
                map.put("data", "3");
                logger.error("用户 " + user + "未通过登陆页面请求数据");
                return map;
            }

            User haveUser = userDao.findByProperty("schoolID",user);

            if (haveUser == null){
                map.put("data","2");
                logger.info("没有找到用户 " + user + " 更改密码失败");
                return map;
            }

            String newPassword = req.getParameter("password");

            haveUser.setPassword(newPassword);
            userDao.update(haveUser);

            map.put("data","0");

            logger.info("用户 " + user + " 更改密码成功");
        } catch (Exception e) {
            logger.error("url格式错误 " + req.getRequestURL());
            map.put("data", "3");
        }
        return map;
    }


    /**
     * 发送短信验证码
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/sendSMS", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> sendSMS(HttpServletRequest req) {
        Map<String, String> map = new HashMap<>();

        try {

            String user = req.getParameter("user");

            if (!req.getParameter("sendSMS").equals(md5.encryption(user + "send"))) {
                map.put("data", "2");
                return map;
            }
            //生成验证码

            //查找这个用户得到手机号码

            //发送手机号码

            map.put("data", "0");
        } catch (Exception e) {
            logger.error("请求url异常 " + req.getRequestURL());
            map.put("data", "2");
            return map;
        }
        return map;
    }

}
