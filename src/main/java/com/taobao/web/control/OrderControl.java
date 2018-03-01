package com.taobao.web.control;

import com.taobao.dao.databasesDaoImpl.OrderDaoImpl;
import com.taobao.dao.databasesDaoImpl.RentDaoImpl;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            if (controlResult.isNull(limit, currentPage)) {
                return controlResult.nullParameter(map, logger);
            }

            if (!Validator.isNumber(limit) || !Validator.isNumber(currentPage)) {
                return controlResult.parameterFormatError(map, "页码数据格式不对", logger);
            }

            User user = (User) session.getAttribute("user");
            if (controlResult.isNull(user)) {
                return controlResult.identityOutTime(map,logger,"");
            }

            int start = Integer.parseInt(currentPage);
            int pageSize = Integer.parseInt(limit);

            List<Order> orders = orderDao.findUserOrderList(user, pageSize, start);

            map = controlResult.successfulContrl(map,"获取用户订单成功",logger);
            map.put("user",user.getSchoolID());
            map.put("phone",user.getPhone());
            map.put("deposit",user.getMoney());
            map.put("orderList",orders);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return controlResult.requestError(map,logger,e);
        }
    }


}
