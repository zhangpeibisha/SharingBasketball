package com.taobao.web.control;

import com.taobao.dao.databasesDaoImpl.*;
import com.taobao.dao.entity.*;
import com.taobao.service.sms.SendSMS;
import com.taobao.utils.data.ManagementData;
import com.taobao.utils.format.Validator;
import com.taobao.utils.sign.MD5;
import com.taobao.web.control.untils.ControlResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private ControlResult controlResult;

    @Autowired
    private BasketballDaoImpl basketballDao;

    @Autowired
    private OrderDaoImpl orderDao;


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

            if (controlResult.isNull(user, password)) {
                return controlResult.nullParameter(map, logger);
            }

            Map<String, Object> result = userDao.findUserBySchoolIDOrPhone(user, password);
            //登陆成功时，session记录用户信息
            if (result.get("data").equals("0")) {
                session.setAttribute("user", result.get("user"));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return controlResult.requestError(map, logger, e);
        }
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

            if (controlResult.isNull(phone, password, schoolID, code)) {
                return controlResult.nullParameter(map, logger);
            }

            if (!Validator.isNumber(schoolID)) {
                return controlResult.parameterFormatError(map, schoolID + "不是指定数据格式", logger);
            }

            if (!Validator.isMobile(phone)) {
                return controlResult.parameterFormatError(map, phone + " 不是一个手机号码", logger);
            }


            if (schoolID.length() != 12) {
                return controlResult.parameterFormatError(map, schoolID + " 这个不是校园卡", logger);
            }


            if (session.getAttribute(phone).equals(code)) {
                //移除验证码
                session.removeAttribute(phone);
                //获取一些用户基础信息
                SchoolCard card = (SchoolCard) session.getAttribute("card");
                Role role = roleDao.findRoleByName("普通用户");

                if (controlResult.isNull(card)) {
                    return controlResult.violationControl(map, "注册违规操作", logger);
                }

                if (controlResult.isNull(role)) {
                    return controlResult.inquireFail(map, "找不到要查询的角色，注册失败", logger);
                }


                //生成用户信息
                User user = new User();
                //使用加密码
                user.setPassword(password);
                user.setCreateTime(new Date());
                user.setPhone(phone);
                user.setSchoolID(schoolID);
                user.setSchooleCard(card);
                user.setRole(role);
                userDao.save(user);

                map = controlResult.successfulContrl(map, schoolID + "用户注册成功", logger);
                //移除之前使用的用户的校园卡信息
                session.removeAttribute("card");
            } else {
                return controlResult.verificationFail(map, "用户验证码输入错误", logger);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return controlResult.requestError(map, logger, e);
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

            if (controlResult.isNull(card, password)) {
                return controlResult.nullParameter(map, logger);
            }


            if (card.length() != 12 || !Validator.isNumber(card)) {
                return controlResult.parameterFormatError(map, card + " 这个不是校园卡", logger);
            }

            SchoolCard card1 = schooleCardDao.findCardByIDAndPassword(card, password);
            if (card1 != null) {
                map = controlResult.successfulContrl(map, card + "校园卡验证成功", logger);
                //添加校园卡到session中，为了注册验证使用
                session.setAttribute("card", card1);
                return map;
            }
            return controlResult.inquireFail(map, "校园卡不存在", logger);
        } catch (Exception e) {
            e.printStackTrace();
            return controlResult.requestError(map, logger, e);
        }
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
                    return controlResult.parameterFormatError(map, regsterPhone + " 这个不是手机卡号", logger);
                }

                result = sendSMS.sendVerificationCode(regsterPhone);
                map = controlResult.successfulContrl(map, "手机号码为 " + regsterPhone + " 的用户申请的验证码为 " + result.get("code"), logger);
                map.put("smsResult", result);
                //将验证码放入session中
                session.setAttribute(regsterPhone, result.get("code"));
                return map;
            } else {
                //为了注册后使用验证码
                String user = req.getParameter("user");

                if (controlResult.isNull(user)) {
                    return controlResult.nullParameter(map, logger);
                }

                if (user.length() != 12) {
                    return controlResult.parameterFormatError(map, user + " 这个不是校园卡", logger);
                }

                //查找这个用户得到手机号码
                User haveUser = userDao.findUserBySchoolID(user);
                if (controlResult.isNull(haveUser)) {
                    return controlResult.inquireFail(map, "用户不存在，不能申请验证码", logger);
                }
                String phone = haveUser.getPhone();
                //发送手机号码
                result = sendSMS.sendVerificationCode(phone);
                map = controlResult.successfulContrl(map, phone + "申请验证码成功", logger);
                map.put("smsResult", result);

                //将验证码存入session中
                session.setAttribute(phone, result.get("code"));

            }
        } catch (Exception e) {
            e.printStackTrace();
            return controlResult.requestError(map, logger, e);
        }
        return map;
    }


    /**
     * 在修改密码的时候验证信息
     *
     * @return
     */
    @RequestMapping(value = "/phoneVerification", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> updatePasswordVerification(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        try {
            String phone = req.getParameter("phone");

            if (controlResult.isNull(phone)) {
                return controlResult.nullParameter(map, logger);
            }

            if (!Validator.isMobile(phone)) {
                return controlResult.parameterFormatError(map, phone + " 这个不是手机卡号", logger);
            }
            User user = userDao.findUserByPhone(phone);
            if (controlResult.isNull(user)) {
                return controlResult.inquireFail(map, "这个手机号未注册，不能找回密码", logger);
            }

            //获取发送信息过后的反馈信息,遍历map传入返回map中
            Map<String, String> resutl = sendSMS.sendVerificationCode(phone);
            map = controlResult.successfulContrl(map, phone + "发送修改密码验证码成功", logger);
            map.put("smsResult", resutl);
            //将验证码存入session中
            session.setAttribute(phone, resutl.get("code"));
            logger.info("发送给用户 " + user.getSchoolID() + " 信息成功");

        } catch (Exception e) {
            e.printStackTrace();
            return controlResult.requestError(map, logger, e);
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
    Map<String, Object> submitVerification(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        try {
            String phone = req.getParameter("phone");
            String code = req.getParameter("code");

            if (controlResult.isNull(phone, code)) {
                return controlResult.nullParameter(map, logger);
            }

            if (!Validator.isMobile(phone)) {
                return controlResult.parameterFormatError(map, phone + " 这个不是手机卡号", logger);
            }

            if (!Validator.isNumber(code) || code.length() != 6) {
                return controlResult.parameterFormatError(map, code + " 这个不是验证码", logger);
            }

            if (session.getAttribute(phone).equals(md5.encryption(code))) {
                map = controlResult.successfulContrl(map, phone + "验证码验证成功", logger);

                //通过验证key=手机号 value=手机号 来控制用户是否能够访问更新密码
                session.setAttribute(phone, phone);

                logger.info("存入电话号码进入session " + session.getAttribute(phone));

            } else {
                return controlResult.verificationFail(map, "用户验证码输入错误", logger);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return controlResult.requestError(map, logger, e);
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
    @RequestMapping(value = "/updatePasswordRun", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> updatePasswordRun(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        try {
            String phone = req.getParameter("phone");
            String newPassword = req.getParameter("password");

            if (controlResult.isNull(phone, newPassword)) {
                return controlResult.nullParameter(map, logger);
            }

            //如果session中的键和值相同，并且和传过来的数据相同则认为是通过正常路径进入的

            logger.info("取出电话号码 session " + session.getAttribute(phone) + " phone " + phone);

            if (session.getAttribute(phone).equals(phone)) {
                User user = userDao.findUserByPhone(phone);
                user.setPassword(newPassword);
                userDao.update(user);
                map = controlResult.successfulContrl(map, phone + "修改密码成功", logger);
            } else {
                return controlResult.violationControl(map, "用户" + phone + "更新密码时违规操作", logger);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return controlResult.requestError(map, logger, e);
        }

        return map;
    }


    /**
     * 租用接口
     *
     * @param req
     * @param session
     * @return
     */
    @RequestMapping(value = "/rent", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> rent(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        try {

            //篮球id
            String basketballId = req.getParameter("basketballId");

            if (controlResult.isNull( basketballId)) {
                return controlResult.nullParameter(map, logger);
            }

            //判断参数格式
            if (!Validator.isNumber(basketballId)) {
                controlResult.parameterFormatError(map, "请输入的参数不是指定数据格式", logger);
            }

            //如果session过期，那么就是身份过期，请重新登陆
            User user1 = (User) session.getAttribute("user");
            if (controlResult.isNull(user1)) {
                return controlResult.identityOutTime(map, logger, "");
            }

            Basketball basketball = basketballDao.findById(Integer.parseInt(basketballId));

            if (controlResult.isNull(basketball)) {
                return controlResult.inquireFail(map, "这个篮球不存在", logger);
            }

            if (basketball.getIsBad() == 1 || basketball.getIsRent() == 1
                    || basketball.getNowPerssure() < basketball.getPressure()) {
                return controlResult.dataIsNotAvailable(map, basketballId + "该篮球不允许出租", logger);
            }

            //篮球租金
            double rentMoney = basketball.getRent().getDeposit();
            double userMoney = user1.getMoney();

            //需要金额
            double needMoney = rentMoney+10;

            //可以借球
            if (userMoney>=needMoney){

                //更新用户钱
                User saveUser = userDao.findById(user1.getUserID());
                saveUser.setMoney(userMoney-rentMoney);
                userDao.update(saveUser);

                //设置篮球不可以借
                basketball.setIsRent(1);
                basketballDao.update(basketball);

                //订单生成
                Order order = new Order();
                order.setBasketball(basketball);
                order.setUser(user1);
                order.setLendTime(new Date());
                orderDao.save(order);

                return controlResult.successfulContrl(map, user1.getSchoolID() + "租用" + basketballId + "的订单生成成功", logger);

            }else {
                return controlResult.dataIsNotAvailable(map,"用户金额不足，请保证余额大于等于60元",logger);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return controlResult.requestError(map, logger, e);
        }

    }


    /**
     * 缴费模块 , 订单结束时使用的接口
     *
     * @param req
     * @param session
     * @return
     */
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> payment(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        try {
            //校园卡号
            String user = req.getParameter("user");
            //订单号
            String orderNumber = req.getParameter("orderNumber");
            //现在的压力值
            String nowperssure = req.getParameter("nowperssure");
            //是否损坏
            String isbad = req.getParameter("isbad");

            if (controlResult.isNull(user, orderNumber,nowperssure,isbad)) {
                return controlResult.nullParameter(map, logger);
            }

            if (!Validator.isNumber(user) || user.length() != 12) {
                return controlResult.parameterFormatError(map, user + "这个参数不是校园卡", logger);
            }

            if (!Validator.isNumber(orderNumber)) {
                return controlResult.parameterFormatError(map, orderNumber + "这个参数不是订单号", logger);
            }
            int orderID = Integer.parseInt(orderNumber);

            if (orderID < 0) {
                return controlResult.parameterFormatError(map, orderNumber + "这个参数不是订单号", logger);
            }

            if (!Validator.isNumber(nowperssure) || !Validator.isNumber(isbad)){
                return controlResult.parameterFormatError(map, nowperssure + "这个参数不是压力值或者表示是否损坏", logger);
            }

            double perssure = Double.parseDouble(nowperssure);

            int bad = Integer.parseInt(isbad);

            Order order = orderDao.findById(orderID);
            //计算时间
            long createTime = order.getLendTime().getTime();
            Date nowDateTime = new Date();
            long nowTime = nowDateTime.getTime();
            double useTime = ((nowTime - createTime) / 1000 / 60.0 / 60.0);
            logger.info("panyemnt time = " + useTime);
            //得到计算费用金额
            double billing = order.getBasketball().getRent().getBilling();
            //计算金额
            double money =  (useTime * billing);
            BigDecimal b   =   new   BigDecimal(money);
            money =  b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
            logger.info("panyemnt money = " + money);
            //得到这个订单的用户
            User user1 = order.getUser();

            //剩余的钱，押金一起算钱
            long remaining = (long) (user1.getMoney() - money) + 50;
            if (remaining >= 0) {

                //更新用户金额
                user1.setMoney(remaining);
                userDao.update(user1);

                //更新订单
                order.setReturnTime(nowDateTime);
                order.setCastMoney(money);
                orderDao.update(order);

                //更新篮球
                String info = "";
                Basketball basketball = order.getBasketball();
                if (perssure<basketball.getPressure() || bad == 1){
                    basketball.setIsRent(1);
                    sendSMS.send(ManagementData.MANAGE_PHONE,basketball.getBasketballID()+"号篮球已经损坏，请及时处理");
                    info = basketball.getBasketballID() + "篮球损坏了";
                }else {
                    basketball.setIsRent(0);
                }
                basketball.setIsBad(bad);
                basketball.setNowPerssure(perssure);
                basketballDao.update(basketball);

                map = controlResult
                        .successfulContrl(map, user1.getSchoolID() + "的" + orderNumber + "订单缴费成功 " + info, logger);

                map.put("money",money);
                //剩余的钱
                map.put("deposit", remaining);
                //订单信息
                map.put("order", order);
                return map;
            } else {
                return controlResult.dataIsNotAvailable(map, "余额不足",logger);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return controlResult.requestError(map, logger, e);
        }
    }


    /**
     * 充值的简单接口
     * @param req
     * @param session
     * @return
     */
    @RequestMapping(value = "/recharge", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> recharge(HttpServletRequest req, HttpSession session) {

        Map<String, Object> map = new HashMap<>();

        try {
            String money = req.getParameter("money");
            double addMoney = Double.parseDouble(money);
            User user = (User) session.getAttribute("user");
            addMoney = user.getMoney()+addMoney;
            user.setMoney(addMoney);
            userDao.update(user);
        }catch (Exception e){
         e.printStackTrace();
        }
        map.put("data", "添加成功");
        return map;
    }

}
