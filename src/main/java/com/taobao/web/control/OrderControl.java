package com.taobao.web.control;

import com.taobao.dao.databasesDaoImpl.OrderDaoImpl;
import com.taobao.dao.databasesDaoImpl.RentDaoImpl;
import com.taobao.dao.databasesDaoImpl.UserDaoImpl;
import com.taobao.dao.entity.Order;
import com.taobao.dao.entity.User;
import com.taobao.utils.format.Validator;
import com.taobao.web.control.untils.ControlResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Create by zhangpe0312@qq.com on 2018/3/1.
 */
@Controller
public class OrderControl {

    private static Logger logger = Logger.getLogger(OrderControl.class);

    @Autowired
    private OrderDaoImpl orderDao;

    @Autowired
    private ControlResult controlResult;


    @Autowired
    private UserDaoImpl userDao;

    /**
     * 用户订单信息和个人信息
     *
     * @param req
     * @param session
     * @return
     */
    @RequestMapping(value = "/orderList", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> orderList(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        try {
            //每页行数
            String limit = req.getParameter("limit");
            //当前页数
            String currentPage = req.getParameter("currentPage");
            //表示请求什么列表
            String all = req.getParameter("all");

            if (controlResult.isNull(limit, currentPage,all)) {
                return controlResult.nullParameter(map, logger);
            }

            if (!Validator.isNumber(limit) || !Validator.isNumber(currentPage) || !Validator.isNumber(all)) {
                return controlResult.parameterFormatError(map, "数据格式不对", logger);
            }

            User user = (User) session.getAttribute("user");
            if (controlResult.isNull(user)) {
                return controlResult.identityOutTime(map, logger, "");
            }

            int start = Integer.parseInt(currentPage);
            int pageSize = Integer.parseInt(limit);
            User dataUser = userDao.findById(user.getUserID());
            List<Order> orders;
            if (all.equals("0")){
                orders = orderDao.findUserOrderList(dataUser, pageSize, start);
            }else if (all.equals("1")){
                orders = orderDao.findUserUndoneOrderList(dataUser,pageSize,start,true);
            }else  if (all.equals("2")){
                orders = orderDao.findUserUndoneOrderList(dataUser,pageSize,start,false);
            }else {
                return controlResult.parameterFormatError(map,"参数错误",logger);
            }
            map = controlResult.successfulContrl(map, "获取用户订单成功", logger);
            map.put("user", dataUser.getSchoolID());
            map.put("phone", dataUser.getPhone());
            map.put("money", dataUser.getMoney());

            if (orders != null) {
                map.put("total", orders.size());
                map.put("orderList", orders);
            } else {
                map.put("total", 0);
                map.put("orderList", null);
            }

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return controlResult.requestError(map, logger, e);
        }
    }

    /**
     * 订单详情列表
     * @param req
     * @param session
     * @return
     */
    @RequestMapping(value = "/orderDetail", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> orderDetail(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        try {
            String orderId = req.getParameter("orderId");
            if (controlResult.isNull(orderId)) {
                return controlResult.nullParameter(map, logger);
            }

            if (!Validator.isNumber(orderId)){
                return controlResult.parameterFormatError(map,orderId+"不是订单号",logger);
            }

            //查看用户是否登陆
            User user = (User) session.getAttribute("user");
            if (controlResult.isNull(user)){
                return controlResult.identityOutTime(map,logger,"");
            }

            Order order = orderDao.findById(Integer.parseInt(orderId));
            if (controlResult.isNull(order)){
                return controlResult.identityOutTime(map,logger,"");
            }

            user = userDao.findById(user.getUserID());
            Set<Order> orders = user.getOrders();

            //如果存在这个订单。则返回该有的信息
            if (orders.contains(order)){
                map = controlResult.successfulContrl(map,user.getSchoolID()+"查看订单"+orderId+"成功",logger);
                map.put("user",user.getSchoolID());
                map.put("phone",user.getPhone());
                map.put("deposit",user.getMoney());

                order.setReturnTime(new Date());
                //计算时间
                long createTime = order.getLendTime().getTime();
                Date nowDateTime = new Date();
                long nowTime = nowDateTime.getTime();
                long useTimeMS = nowTime - createTime;
                long useTimeH = (long) (useTimeMS/ 1000 / 60.0 / 60.0);
                //得到计算费用金额
                double billing = order.getBasketball().getRent().getBilling();
                //计算金额
                double money = (long) (useTimeH * billing);

                //设定现在时间
                order.setReturnTime(nowDateTime);
                //设定现在花费
                order.setCastMoney(money);
                map.put("orderDetail",order);
                map.put("totalTime",useTimeMS);
                return map;
            }else{
               return controlResult.dataIsNotAvailable(map,"用户没有这个订单,不能查看",logger);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return controlResult.requestError(map, logger, e);
        }
    }

}
