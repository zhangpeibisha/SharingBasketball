package com.taobao.web.control;

import com.taobao.dao.databasesDaoImpl.BasketballDaoImpl;
import com.taobao.dao.entity.Basketball;
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
    private ControlResult controlResult;

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

            if (controlResult.isNull(currentPage, limit)) {
                return controlResult.nullParameter(map, logger);
            }

            if (!Validator.isNumber(currentPage) && !Validator.isNumber(limit)) {
                return controlResult.parameterFormatError(map, "参数数据类型错误，请对参数输入数字类型", logger);
            }

            int start = Integer.parseInt(currentPage);
            int pageSize = Integer.parseInt(limit);

            List<Basketball> basketballs = basketballDao.findRentList(start, pageSize);
            String sql = "select count(*) from basketball where isBad=0 and isRent=0\n" +
                    "                    and basketball.nowPerssure>=basketball.pressure";
            long count = basketballDao.findCountBySQL(sql);

            map.put("basketballs", basketballs);
            map.put("total", count);
            map = controlResult.successfulContrl(map, "获取能够租用的篮球列表成功", logger);

        } catch (Exception e) {
            return controlResult.requestError(map, logger, e);
        }

        return map;
    }

    /**
     * 显示全部篮球信息
     *
     * @param req
     * @param session
     * @return
     */
    @RequestMapping(value = "/basketList", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> basketList(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        try {

            //每页最大显示行数
            String limit = req.getParameter("limit");
            //当前页数
            String currentPage = req.getParameter("currentPage");

            if (controlResult.isNull(limit, currentPage)) {
                return controlResult.nullParameter(map, logger);
            }

            if (!Validator.isNumber(limit) || !Validator.isNumber(currentPage)) {
                return controlResult.parameterFormatError(map, "参数数据类型错误，请对参数输入数字类型", logger);
            }

            int start = Integer.parseInt(currentPage);
            int pageSize = Integer.parseInt(limit);

            List<Basketball> basketballs = basketballDao.findBastketballList(start, pageSize);

            String sql = "select count(*) from basketball";
            long count = basketballDao.findCountBySQL(sql);
            map = controlResult.successfulContrl(map, "获取全部篮球列表成功", logger);
            map.put("basketballs", basketballs);
            map.put("total",count);

        } catch (Exception e) {
            return controlResult.requestError(map, logger, e);
        }

        return map;
    }

    /**
     * 查看篮球详情
     * @param req
     * @param session
     * @return
     */
    @RequestMapping(value = "/basketballDetail", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> basketballDetail(HttpServletRequest req, HttpSession session) {

        Map<String, Object> map = new HashMap<>();

        try {

            String basketballID = req.getParameter("basketballID");

            if (controlResult.isNull(basketballID)){
                return controlResult.nullParameter(map,logger);
            }

            if (!Validator.isNumber(basketballID)){
                return controlResult.parameterFormatError(map,basketballID+"不是篮球id",logger);
            }

            Integer id = Integer.parseInt(basketballID);

            if (id<0){
                return controlResult.parameterFormatError(map,basketballID+"不是篮球id",logger);
            }

            User user = (User) session.getAttribute("user");
            if (controlResult.isNull(user)) {
                return controlResult.identityOutTime(map, logger, "");
            }

            Basketball basketball = basketballDao.findById(id);
            if (controlResult.isNull(basketball)){
                return controlResult.inquireFail(map,"没有查找到这个篮球",logger);
            }

            user.setPassword(null);
            user.setOrders(null);
            user.setCreateTime(null);
            map.put("user",user);
            map.put("basketball",basketball);
            map = controlResult.successfulContrl(map,user.getSchoolID()+"获取"+id+"篮球的详细信息成功",logger);
            return map;
        }catch (Exception e){
            e.printStackTrace();
            return controlResult.requestError(map,logger,e);
        }
    }

}
