package com.taobao.web.control;

import com.taobao.dao.databasesDaoImpl.BasketballDaoImpl;
import com.taobao.dao.entity.Basketball;
import com.taobao.utils.format.ChangeFormat;
import com.taobao.utils.format.Validator;
import com.taobao.web.control.untils.ControlError;
import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Create by zhangpe0312@qq.com on 2018/2/26.
 * <p>
 * 关于篮球的一些使用接口
 */
@Controller
public class BasketballControl {

    private static Logger logger = Logger.getLogger(BasketballControl.class);

    @Autowired
    private BasketballDaoImpl basketballDao;

    @Autowired
    private ControlError controlError;
    /**
     * 返回可以租借、并且完好的篮球列表
     *
     * @return 返回可用篮球列表数据
     */
    @RequestMapping(value = "/rentList", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> rentList(HttpServletRequest req) {
        Map<String, Object> map = new HashMap<>();
        try {

            String currentPage = req.getParameter("currentPage");
            String limit = req.getParameter("limit");

            if (controlError.isNull(currentPage,limit)){
                logger.error("请求参数为空，请求失败 " + req.getPathInfo());
                return controlError.nullParameter(map);
            }

            if (!Validator.isNumber(currentPage)&&!Validator.isNumber(limit)){
                map.put("data","-4");
                map.put("message","数据格式不是数字");
                logger.error("数据格式不是数字");
                return map;
            }

            int start = Integer.parseInt(currentPage);
            int pageSize = Integer.parseInt(limit);

            List<Basketball> basketballs = basketballDao.findRentList(start,pageSize);
            String sql = "select count(*) from basketball where isBad=0 and isRent=0\n" +
                    "                    and basketball.nowPerssure>=basketball.pressure";
            long count = basketballDao.findCountBySQL(sql);
            if (basketballs.size() != 0) {
                map.put("basketballs", basketballs);
                map.put("data", "0");
                map.put("total",count);
                logger.info("申请获取能够使用的篮球列表成功： " + basketballs.size());
            } else {
                map.put("data", "1");
                logger.info("申请获取能够使用的篮球列表失败，没有可出租的篮球了 ");
            }
        } catch (Exception e) {
            logger.error(" 请求异常 " + req.getRequestURL() + " " + e);
            e.printStackTrace();
            return controlError.requestError(map);
        }

        return map;
    }



}
