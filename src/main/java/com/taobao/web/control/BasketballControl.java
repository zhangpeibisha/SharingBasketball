package com.taobao.web.control;

import com.taobao.dao.databasesDaoImpl.BasketballDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by zhangpe0312@qq.com on 2018/2/26.
 * <p>
 * 关于篮球的一些使用接口
 */
@Controller
public class BasketballControl {

    @Autowired
    private BasketballDaoImpl basketballDao;

    /**
     * 返回可以租借、并且完好的篮球列表
     *
     * @param req
     * @return
     */
    @RequestMapping(name = "/basketballList", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> basketballList(HttpServletRequest req) {
        Map<String, Object> map = new HashMap<>();

        return map;

    }

}
