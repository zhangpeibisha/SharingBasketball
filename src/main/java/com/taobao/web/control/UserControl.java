package com.taobao.web.control;

import com.taobao.dao.databasesDaoImpl.RoleDaoImpl;
import com.taobao.dao.databasesDaoImpl.SchoolCardDaoImpl;
import com.taobao.dao.databasesDaoImpl.UserDaoImpl;
import com.taobao.dao.entity.SchoolCard;
import com.taobao.dao.entity.User;
import com.taobao.service.sms.SendSMS;
import com.taobao.utils.format.Validator;
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
    Map<String, String> login(HttpServletRequest req, HttpSession session) {
        Map<String, String> map = new HashMap<>();

        try {
            String user = req.getParameter("user");
            String password = req.getParameter("password");

            if (user == null || password == null || user.isEmpty() || password.isEmpty()) {
                map.put("data", "-1");
                map.put("message", "请求异常，参数为空");
                logger.error("请求参数为空，请求失败");
                return map;
            }

            Map<String, Object> result = userDao.findUserBySchoolIDOrPhone(user, password);

            map.put("data", (String) result.get("result"));

            //登陆成功时，session记录用户信息
            if (result.get("result").equals("0")) {
                logger.info("用户 " + user + " 登陆成功");
                session.setAttribute("user", result.get("user"));
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
    Map<String, String> register(HttpServletRequest req, HttpSession session) {
        Map<String, String> map = new HashMap<>();

        try {

            String password = req.getParameter("password");
            String phone = req.getParameter("phone");
            String schoolID = req.getParameter("card");
            String code = req.getParameter("code");

            if (password == null || phone == null || schoolID == null || code == null
                    || password.isEmpty() || phone.isEmpty() || schoolID.isEmpty() || code.isEmpty()) {
                map.put("data", "-1");
                map.put("message", "请求异常，参数为空");
                logger.error("请求参数为空，请求失败");
                return map;
            }


            if (schoolID.length() != 12) {
                map.put("data", "-21");
                map.put("message", schoolID + " 不是校园卡号");
                logger.error(schoolID + "请求参数不是校园卡号 ");
                return map;
            }

            if (!Validator.isMobile(phone)) {
                map.put("data", "-2");
                map.put("message", "请求参数不是手机号码");
                logger.error(phone + "请求参数不是手机号码 ");
                return map;
            }

            if (session.getAttribute(phone).equals(code)) {
                //移除验证码
                session.removeAttribute(phone);
                //生成用户信息
                User user = new User();
                //使用加密码
                user.setPassword(password);
                user.setRole(roleDao.findRoleByName("ceshi001"));
                user.setCreateTime(new Date());
                user.setPhone(phone);
                user.setSchoolID(schoolID);
                user.setSchooleCard((SchoolCard) session.getAttribute("card"));
                user.setRole(roleDao.findRoleByName("普通用户"));
                userDao.save(user);
                logger.info("用户 " + schoolID + " 注册成功");
                map.put("data", "0");
                map.put("message", "用户 " + user.getSchoolID() + " 注册成功");
                logger.info("用户 " + user.getSchoolID() + " 注册成功");
                //移除之前使用的用户的校园卡信息
                session.removeAttribute("card");
            } else {
                logger.error("用户验证码输入错误 " + schoolID);
                map.put("data", "2");
                map.put("message", "验证码输入错误");
            }
        } catch (Exception e) {
            logger.error("用户注册异常 " + e);
            map.put("data", "1");
            map.put("message", "服务器发生了异常");
        }
        return map;
    }

    /**
     * 验证校园卡密码和卡号
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/isSchoolCard", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> isSchoolCard(HttpServletRequest req, HttpSession session) {
        Map<String, String> map = new HashMap<>();

        try {
            String card = req.getParameter("user");
            String password = req.getParameter("password");

            if (card == null || password == null || card.isEmpty() || password.isEmpty()) {
                map.put("data", "-1");
                map.put("message", "请求失败，参数为空");
                return map;
            }

            SchoolCard card1 = schooleCardDao.findCardByIDAndPassword(card, password);
            if (card1 != null) {
                map.put("data", "0");
                map.put("message", "校园卡验证成功");
                session.setAttribute("card", card1);
                logger.info("校园卡号用户 " + card1 + " 验证校园卡成功");
                return map;
            }
            map.put("data", "1");
            map.put("message", "找不到这个校园卡号");
            logger.info("校园卡号用户验证校园卡失败");
        } catch (Exception e) {
            map.put("data", "2");
            map.put("message", "服务器请求发生了异常");
            logger.info("校园卡号用户验证校园卡异常 " + e);
        }
        return map;
    }


    /**
     * 发送短信验证码
     * 用户注册过后使用的
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/sendSMSCode", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> sendSMSCode(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> result;
        try {
            String regsterPhone = req.getParameter("phone");

            //为了注册的时候使用
            if (regsterPhone != null) {

                if (!Validator.isMobile(regsterPhone)) {
                    map.put("data", "-2");
                    map.put("message", "请求参数不是手机号码");
                    logger.error("请求参数不是手机号码 ");
                    return map;
                }

                result = sendSMS.sendVerificationCode(regsterPhone);
                map.put("data", "0");
                map.put("message", "发送信息成功");
                map.put("smsResult", result);
                logger.info("手机号码为 " + regsterPhone + " 的用户申请的验证码为 " + result.get("code"));

                //将验证码放入session中
                session.setAttribute(regsterPhone, result.get("code"));
                return map;
            } else {
                //为了注册后使用验证码
                String user = req.getParameter("user");

                if (user == null || user.isEmpty()){
                    map.put("data", "-1");
                    map.put("message", "请求失败，参数为空");
                    return map;
                }


                //查找这个用户得到手机号码
                User haveUser = userDao.findUserBySchoolID(user);
                if (haveUser == null) {
                    map.put("data", "2");
                    map.put("message", "用户不存在");
                    logger.error("用户 " + user + "不存在，发送信息失败");
                    return map;
                }
                String phone = haveUser.getPhone();
                //发送手机号码
                result = sendSMS.sendVerificationCode(phone);
                map.put("data", "0");
                map.put("message", "申请验证码成功");
                map.put("smsResult", result);

                //将验证码存入session中
                session.setAttribute(phone, result.get("code"));

                logger.info("用户 " + user + " 申请验证码 " + result.get("code") + " 成功");
            }
        } catch (Exception e) {
            logger.error("请求异常 " + req.getRequestURL() + " " + e);
            e.printStackTrace();
            map.put("data", "2");
            map.put("message", "服务器发生异常");
            return map;
        }
        return map;
    }


    /**
     * 在修改密码的时候验证信息
     *
     * @return
     */
    @RequestMapping(value = "/phoneVerification", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> updatePasswordVerification(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        try {
            String phone = req.getParameter("phone");

            if (phone == null || phone.isEmpty()) {
                map.put("data", "-1");
                map.put("message", "接收参数失败，请检测url是否输入正确");
                logger.error("接收参数失败，请检测url是否输入正确 " + req.getPathInfo());
                return map;
            }

            if (!Validator.isMobile(phone)) {
                map.put("data", "-2");
                map.put("message", "这个数据不是手机号码");
                logger.error("这个数据不是手机号码 " + phone);
                return map;
            }
            User user = userDao.findUserByPhone(phone);
            if (user == null) {
                map.put("data", "1");
                map.put("message", "这个手机号未注册，申请验证码失败");
                logger.info("这个手机号未注册，申请验证码失败 " + phone);
                return map;
            }

            //获取发送信息过后的反馈信息,遍历map传入返回map中
            Map<String, String> resutl = sendSMS.sendVerificationCode(phone);
            map.put("data", "0");
            map.put("message", "发送信息成功");
            map.put("smsResult", resutl);
            //将验证码存入session中
            session.setAttribute(phone, resutl.get("code"));
            logger.info("发送给用户 " + user.getSchoolID() + " 信息成功");

        } catch (Exception e) {

        }
        return map;
    }

    /**
     * 验证验证码是否正确
     *
     * @return
     */
    @RequestMapping(value = "/submitVerification", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> submitVerification() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }


}
