package com.taobao.web.control;

import com.taobao.dao.databasesDaoImpl.RentDaoImpl;
import com.taobao.dao.entity.Rent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by zhangpe0312@qq.com on 2018/3/1.
 */
@Controller
public class RentControl {

    private static Logger logger = Logger.getLogger(RentControl.class);

    @Autowired
    private RentDaoImpl rentDao;

    @RequestMapping(value = "/rentTableList", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> rentTableList(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        map.put("rent",rentDao.findAll());
        return map;
    }

}
