package com.taobao.web.control;

import com.taobao.dao.databasesDaoImpl.BasketballDaoImpl;
import com.taobao.dao.databasesDaoImpl.SchoolCardDaoImpl;
import com.taobao.dao.databasesDaoImpl.UserDaoImpl;
import com.taobao.dao.entity.Basketball;
import com.taobao.dao.entity.SchoolCard;
import com.taobao.dao.entity.User;
import com.taobao.utils.sign.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by zhangpe0312@qq.com on 2018/2/27.
 * <p>
 * 控制金额的扣除、充值、返回等操作
 */
@Controller
public class TestControl {

    @Autowired
    private SchoolCardDaoImpl schoolCardDao;

    @Autowired
    private UserDaoImpl userDao;

    @Autowired
    private BasketballDaoImpl basketballDao;

    @Autowired
    MD5 md5;

    /**
     * 添加校园卡
     * @return
     */
    @RequestMapping(value = "/testAddSchool" , method = RequestMethod.GET)
    public @ResponseBody Map<String,String> testAddEntity() {

        Map<String,String> map = new HashMap<>();
        SchoolCard schoolCard = new SchoolCard();
        schoolCard.setMoney(10);
        schoolCard.setPassword(md5.encryption("123456"));
        schoolCard.setSchoolID("123456789111");
        schoolCardDao.save(schoolCard);

        map.put("data","添加成功");
        return map;
    }

    /**
     * 添加用户
     * @return
     */
    @RequestMapping(value = "/testAddUser" , method = RequestMethod.GET)
    public @ResponseBody Map<String,String> testAddUser() {

        Map<String,String> map = new HashMap<>();

        User user = new User();
        user.setSchoolID("123456789111");
        user.setPhone("18203085236");
        user.setCreateTime(new Date());
        user.setPassword(md5.encryption("123456"));
        userDao.save(user);

        map.put("data","添加成功");
        return map;
    }

    @RequestMapping(value = "/testAddBasketball" , method = RequestMethod.GET)
    public @ResponseBody Map<String,String> testAddBasketball() {

        Map<String,String> map = new HashMap<>();

        for (int i = 0; i <50 ; i++) {
            Basketball basketball = new Basketball();
            basketball.setIsBad(i%2);
            basketball.setCreateTime(new Date());
            if (i%2 == 0){
                basketball.setModel("A型");
            }else {
                basketball.setModel("B型");
            }
            basketball.setIsRent(i%2);
            basketball.setPressure(0.06);
            basketball.setNowPerssure((Math.random()*10)*0.01);
            basketballDao.save(basketball);
        }


        map.put("data","添加成功");

        return map;
    }


}
