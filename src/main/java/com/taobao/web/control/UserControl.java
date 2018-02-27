package com.taobao.web.control;

import com.taobao.dao.databasesDaoImpl.RoleDaoImpl;
import com.taobao.dao.databasesDaoImpl.SchoolCardDaoImpl;
import com.taobao.dao.databasesDaoImpl.UserDaoImpl;
import com.taobao.dao.entity.SchoolCard;
import com.taobao.dao.entity.User;
import com.taobao.service.sms.SendSMS;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    private UserDaoImpl userDao;

    @Autowired
    private RoleDaoImpl roleDao;

    @Autowired
    private SendSMS sendSMS;

    @Autowired
    private SchoolCardDaoImpl schooleCardDao;

    /**
     * 登陆采用Get方法
     *
     * @return 返回登陆结果
     */
    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    public @ResponseBody
    Map<String, String> login(HttpServletRequest req , HttpSession session) {
        Map<String, String> map = new HashMap<>();

        try {
            String user = req.getParameter("user");
            String password = req.getParameter("password");

            System.out.println("打印密码 " + password);
            Map<String,Object> result = userDao.findUserBySchoolIDOrPhone(user, password);

            map.put("data", (String) result.get("result"));

            //登陆成功时，session记录用户信息
            if (result.get("result").equals("0")){
                logger.info("用户 " + user + " 登陆成功");
                session.setAttribute("user",result.get("user"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url格式错误 " + req.getRequestURL() + " " + e);
            map.put("data", "3");
        }
        return map;
    }

    /**
     * 注册账号接口
     * 注册的都是普通用户，所以默认添加为普通用户，有更改
     * 请通过后台管理员修改
     *
     * @param req
     * @return 返回请求结果
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> register(HttpServletRequest req , HttpSession session) {
        Map<String, String> map = new HashMap<>();

        try {

            String password = req.getParameter("password");
            String phone = req.getParameter("phone");
            String schoolID = req.getParameter("card");
            String code = req.getParameter("code");

            if (session.getAttribute("code").equals(code)){
                //生成用户信息
                User user = new User();
                //使用加密码
                user.setPassword(password);
                user.setRole(roleDao.findRoleByName("ceshi001"));
                user.setCreateTime(new Date());
                user.setPhone(phone);
                user.setSchoolID(schoolID);
                user.setSchooleCard((SchoolCard) session.getAttribute("card"));
                userDao.save(user);
                logger.info("用户 " + schoolID + " 注册成功");
                map.put("data", "0");
            }else {
                logger.error("用户验证码输入错误 " + schoolID);
                map.put("data", "2");
            }
        } catch (Exception e) {
            logger.error("用户注册异常 " + e);
            map.put("data", "1");
        }
        return map;
    }

    /**
     * 验证校园卡密码和卡号
     * @param req
     * @return
     */
    @RequestMapping(value = "/isSchoolCard", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> isSchoolCard(HttpServletRequest req,HttpSession session) {
        Map<String, String> map = new HashMap<>();

        try {
            String card = req.getParameter("user");
            String password = req.getParameter("password");
            SchoolCard card1 = schooleCardDao.findCardByIDAndPassword(card,password);
            if (card1!=null){
                map.put("data","0");
                session.setAttribute("card",card1);
                logger.info("校园卡号用户 " + card1 + " 验证校园卡成功");
                return map;
            }
            map.put("data","1");
            logger.info("校园卡号用户验证校园卡失败");
        }catch (Exception e){
            map.put("data","2");
            logger.info("校园卡号用户验证校园卡异常 " + e);
        }
        return map;
    }




    /**
     * 更新密码
     *
     * @param req
     * @return 返回修改结果
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> updatePasswordByOldPassword(HttpServletRequest req) {
        Map<String, String> map = new HashMap<>();

        try {
            String user = req.getParameter("user");
            User haveUser = userDao.findUserBySchoolID(user);

            if (haveUser == null) {
                map.put("data", "2");
                logger.info("没有找到用户 " + user + " 更改密码失败");
                return map;
            }

            String newPassword = req.getParameter("password");

            haveUser.setPassword(newPassword);
            userDao.update(haveUser);

            map.put("data", "0");

            logger.info("用户 " + user + " 更改密码成功");
        } catch (Exception e) {
            logger.error("url格式错误 " + req.getRequestURL() + " " + e);
            map.put("data", "3");
        }
        return map;
    }


    /**
     * 发送短信验证码
     * 用户注册过后使用的
     * @param req
     * @return
     */
    @RequestMapping(value = "/sendSMSCode", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> sendSMSCode(HttpServletRequest req , HttpSession session) {
        Map<String, String> map = new HashMap<>();

        try {

            String regsterPhone = req.getParameter("phone");
            //为了注册的时候使用
            if (regsterPhone!=null){
                map = sendSMS.sendVerificationCode(regsterPhone);
                map.put("data","0");
                logger.info("手机号码为 " + regsterPhone + " 的用户申请的验证码为 " + map.get("code"));

                //将验证码放入session中
                session.setAttribute("code" , map.get("code"));
                return map;
            }else{
                //为了注册后使用验证码

                String user = req.getParameter("user");
                //查找这个用户得到手机号码
                User haveUser = userDao.findUserBySchoolID(user);
                if (haveUser == null) {
                    map.put("data", "2");
                    logger.error("用户 " + user + "不存在，发送信息失败");
                    return map;
                }
                String phone = haveUser.getPhone();
                //发送手机号码
                map = sendSMS.sendVerificationCode(phone);
                map.put("data", "0");
                logger.info("用户 " + user + " 申请验证码 " + map.get("code") + " 成功");
            }
        } catch (Exception e) {
            logger.error("请求异常 " + req.getRequestURL());
            map.put("data", "2");
            return map;
        }
        return map;
    }

    @RequestMapping(value = "" , method = RequestMethod.POST)
    public @ResponseBody Map<String,String> recharge(){
        Map<String, String> map = new HashMap<>();

        return map;
    }



}
