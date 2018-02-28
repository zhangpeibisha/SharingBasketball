package com.taobao.web.control;

import com.taobao.dao.databasesDaoImpl.RoleDaoImpl;
import com.taobao.dao.databasesDaoImpl.SchoolCardDaoImpl;
import com.taobao.dao.databasesDaoImpl.UserDaoImpl;
import com.taobao.dao.entity.SchoolCard;
import com.taobao.dao.entity.User;
import com.taobao.service.sms.SendSMS;
import com.taobao.utils.format.Validator;
import com.taobao.utils.sign.MD5;
import com.taobao.web.control.untils.ControlError;
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

    @Autowired
    private MD5 md5;

    @Autowired
    private ControlError controlError;

    /**
     * 登陆采用Get方法
     *
     * @return 返回登陆结果
     */
    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    public @ResponseBody
    Map<String, Object> login(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        try {
            String user = req.getParameter("user");
            String password = req.getParameter("password");

            if (controlError.isNull(user, password)) {
                logger.error("请求参数为空，请求失败 " + req.getPathInfo());
                return controlError.nullParameter(map);
            }

            Map<String, Object> result = userDao.findUserBySchoolIDOrPhone(user, password);

            map.put("data", result.get("result"));
            //登陆成功时，session记录用户信息
            if (result.get("result").equals("0")) {
                logger.info("用户 " + user + " 登陆成功");
                session.setAttribute("user", result.get("user"));
            }

        } catch (Exception e) {
            logger.error(" 请求异常 " + req.getRequestURL() + " " + e);
            e.printStackTrace();
            return controlError.requestError(map);
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
    Map<String, Object> register(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        try {

            String password = req.getParameter("password");
            String phone = req.getParameter("phone");
            String schoolID = req.getParameter("card");
            String code = req.getParameter("code");

            if (controlError.isNull(phone, password, schoolID, code)) {
                logger.error("请求参数为空，请求失败 " + req.getPathInfo());
                return controlError.nullParameter(map);
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
            logger.error(" 请求异常 " + req.getRequestURL() + " " + e);
            e.printStackTrace();
            return controlError.requestError(map);
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
    Map<String, Object> isSchoolCard(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        try {
            String card = req.getParameter("user");
            String password = req.getParameter("password");

            if (controlError.isNull(card, password)) {
                logger.error("请求参数为空，请求失败 " + req.getPathInfo());
                return controlError.nullParameter(map);
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
            logger.error(" 请求异常 " + req.getRequestURL() + " " + e);
            e.printStackTrace();
            return controlError.requestError(map);
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

                if (controlError.isNull(user)) {
                    logger.error("请求参数为空，请求失败 " + req.getPathInfo());
                    return controlError.nullParameter(map);
                }

                //查找这个用户得到手机号码
                User haveUser = userDao.findUserBySchoolID(user);
                if (controlError.isNull(haveUser)) {
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
            logger.error(" 请求异常 " + req.getRequestURL() + " " + e);
            e.printStackTrace();
            return controlError.requestError(map);
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

            if (controlError.isNull(phone)) {
                logger.error("请求参数为空，请求失败 " + req.getPathInfo());
                return controlError.nullParameter(map);
            }

            if (!Validator.isMobile(phone)) {
                map.put("data", "-2");
                map.put("message", "这个数据不是手机号码");
                logger.error("这个数据不是手机号码 " + phone);
                return map;
            }
            User user = userDao.findUserByPhone(phone);
            if (controlError.isNull(user)) {
                logger.error("这个手机号未注册 " + req.getPathInfo());
                map.put("data", "1");
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
            logger.error(" 请求异常 " + req.getRequestURL() + " " + e);
            e.printStackTrace();
            return controlError.requestError(map);
        }
        return map;
    }

    /**
     * 验证验证码是否正确
     *
     * @return
     */
    @RequestMapping(value = "/submitVerification", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> submitVerification(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        try {
            String phone = req.getParameter("phone");
            String code = req.getParameter("code");

            if (controlError.isNull(phone, code)) {
                logger.error("请求参数为空，请求失败 " + req.getPathInfo());
                return controlError.nullParameter(map);
            }
            if (session.getAttribute(phone).equals(md5.encryption(code))) {
                map.put("data", "0");
                map.put("message", "验证码验证成功");

                //通过验证key=手机号 value=手机号 来控制用户是否能够访问更新密码
                session.setAttribute(phone, phone);
                logger.info("手机号为 " + phone + " 的用户修改密码的验证码验证成功");
            } else {
                map.put("data", "1");
                map.put("message", "验证码验证失败");
                logger.info("手机号为 " + phone + " 的用户修改密码的验证码验证失败，输入与验证码不同");
            }
        } catch (Exception e) {
            logger.error(" 请求异常 " + req.getRequestURL() + " " + e);
            e.printStackTrace();
            return controlError.requestError(map);
        }

        return map;
    }


    /**
     * 实际运行更新密码操作
     *
     * @param req
     * @param session
     * @return
     */
    @RequestMapping(value = "/updatePasswordRun", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> updatePasswordRun(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        try {
            String phone = req.getParameter("phone");
            String newPassword = req.getParameter("password");

            if (controlError.isNull(phone, newPassword)) {
                logger.error("请求参数为空，请求失败 " + req.getPathInfo());
                return controlError.nullParameter(map);
            }

            //如果session中的键和值相同，并且和传过来的数据相同则认为是通过正常路径进入的
            if (session.getAttribute(phone).equals(phone)) {
                User user = userDao.findUserByPhone(phone);
                user.setPassword(newPassword);
                userDao.update(user);

                map.put("data", "0");
                map.put("messaget", user.getSchoolID() + "修改密码成功");
                logger.info(user.getSchoolID() + "修改密码成功");
            } else {
                map.put("data", "1");
                map.put("messaget", "用户操作异常，请按正规路径修改密码");
                logger.info(phone + " 用户操作异常，请按正规路径修改密码");
            }

        } catch (Exception e) {
            logger.error(" 请求异常 " + req.getRequestURL() + " " + e);
            e.printStackTrace();
            return controlError.requestError(map);
        }

        return map;
    }

    /**
     * 缴费模块
     * @param req
     * @param session
     * @return
     */
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> payment(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

}
