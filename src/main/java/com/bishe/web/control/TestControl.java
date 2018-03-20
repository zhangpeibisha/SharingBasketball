package com.bishe.web.control;

import com.bishe.dao.databasesDaoImpl.*;
import com.bishe.dao.entity.*;
import com.bishe.utils.sign.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
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
    private RoleDaoImpl roleDao;

    @Autowired
    private RentDaoImpl rentDao;


    @Autowired
    MD5 md5;

    /**
     * 添加校园卡
     * 53 123456789111  123456
     * 56 201410610113  123456
     *
     * @return
     */
    @RequestMapping(value = "/testAddSchool", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> testAddEntity() {

        Map<String, String> map = new HashMap<>();
        SchoolCard schoolCard = new SchoolCard();
        schoolCard.setMoney(100);
        schoolCard.setPassword(md5.encryption("123456"));
        schoolCard.setSchoolID("201410610113");
        schoolCardDao.save(schoolCard);

        map.put("data", "添加成功");
        return map;
    }

    /**
     * 添加用户
     *
     * @return
     */
    @RequestMapping(value = "/testAddUser", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> testAddUser() {

        Map<String, String> map = new HashMap<>();

        User user = new User();
        user.setSchoolID("20");
        user.setPhone("18203085236");
        user.setCreateTime(new Date());
        user.setRole(roleDao.findRoleByName("普通用户"));
        user.setSchooleCard(schoolCardDao.findByProperty("schoolID", "123456789111"));
        user.setPassword(md5.encryption("123456"));

        userDao.save(user);

        map.put("data", "添加成功");
        return map;
    }

    @RequestMapping(value = "/testAddBasketball", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> testAddBasketball() {

        Map<String, String> map = new HashMap<>();


        //设置压力保存三位小数
        DecimalFormat df = new DecimalFormat("######0.000");

        for (int i = 0; i < 200; i++) {
            Basketball basketball = new Basketball();
            basketball.setCreateTime(new Date());
            basketball.setPressure(8);//标准压力 7-9磅
            //设置型号
            int tempClass = (int) (Math.random() * 3);
            //设置是否损坏
            int tempBad = Math.random() > 0.5 ? 1 : 0;
            basketball.setIsBad(tempBad);
            double random = (Math.random() * 5);
            double nowPerssuer =  7;

            //默认没有借出
            basketball.setIsRent(0);
            //好的
            if (tempBad == 0 && random<=2){
                basketball.setNowPerssure(Double.parseDouble(df.format(nowPerssuer + random)));
                basketball.setCabinet(0);
            }else {
                if (random<=2){
                    basketball.setNowPerssure(Double.parseDouble(df.format(nowPerssuer - random)));
                }else {
                    basketball.setNowPerssure(Double.parseDouble(df.format(nowPerssuer + random)));
                }
                basketball.setCabinet(1);
            }

            basketball.setRent(rentDao.findByProperty("id", 1));
            basketballDao.save(basketball);
        }


        map.put("data", "添加成功");

        return map;
    }


    @RequestMapping(value = "/testAddRole", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> testAddRole() {
        Map<String, String> map = new HashMap<>();

        Role role = new Role();
        role.setName("普通用户");
        role.setDescription("一般用户能够操作的接口和页面的权限集合");
        role.setCreateTime(new Date());
        roleDao.save(role);

        map.put("data", "添加成功");


        return map;
    }

    @RequestMapping(value = "/testAddRent", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> testAddRent() {

        Map<String, String> map = new HashMap<>();

        Rent rent = new Rent();
        rent.setCreateTime(new Date());
        rent.setDeposit(50);
        rent.setBilling(5);
        rentDao.save(rent);
        map.put("data", "添加成功");
        return map;
    }


    /**
     * 测试代码样板
     *
     * @return
     */
    @RequestMapping(value = "/testModel", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> testModel() {

        Map<String, Object> map = new HashMap<>();
        map.put("data", "添加成功");
        return map;
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> test(HttpServletRequest req, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        User user = userDao.findById(505);

        long create = user.getCreateTime().getTime();
        long now = System.currentTimeMillis();
        System.out.println("创建了 " + ((now - create) / 1000 / 60.0 / 60.0));
        map.put("time", ((now - create) / 100 / 60.0));
        return map;
    }
}
