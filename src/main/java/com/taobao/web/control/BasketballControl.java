package com.taobao.web.control;

import com.taobao.dao.databasesDaoImpl.BasketballDaoImpl;
import com.taobao.dao.entity.Basketball;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /**
     * 返回可以租借、并且完好的篮球列表
     *
     * @param req
     * @return
     */
    @RequestMapping(name = "/rentList", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> rentList(HttpServletRequest req) {
        Map<String, Object> map = new HashMap<>();

        try {

            String sql = "select * from basketball where isBad=? and isRent=?";
            List<Basketball> basketballs = basketballDao.findBySql(sql, 0, 0);
            if (basketballs.size()!=0){

                //删除压力值不合格的篮球
                for (Basketball basketball : basketballs){
                    if (basketball.getNowPerssure()<basketball.getPressure()){
                        basketballs.remove(basketball);
                    }
                }
                map.put("basketballs", basketballs);
                map.put("data", "0");
                logger.info("申请获取能够使用的篮球列表成功 数量为： " + basketballs);
            }else {
                map.put("data", "1");
                logger.info("申请获取能够使用的篮球列表失败，没有可出租的篮球了 ");
            }
        } catch (Exception e) {
            map.put("data", "2");
            logger.error("申请获取能够使用的篮球列表异常 " + e);
        }

        return map;
    }

}
