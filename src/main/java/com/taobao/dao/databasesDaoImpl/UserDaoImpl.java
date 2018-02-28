package com.taobao.dao.databasesDaoImpl;

import com.taobao.dao.databasesDao.SupperBaseDAOImp;
import com.taobao.dao.entity.User;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 * <p>
 * 用户的操作数据库的最高层服务类
 */
@Transactional
@Service
public class UserDaoImpl extends SupperBaseDAOImp<User> {

    @Override
    public <T> List<T> findByCriteria(T object, Integer startRow, Integer pageSize) {
        return null;
    }

    @Override
    public <T> Long findByCriteriaCount(T object) {
        return null;
    }

    /**
     * data：0为登录成功，1为不存在当前用户，2为用户密码不正确,3为不是登陆页的请求
     *
     * @param user     校园卡id
     * @param password 用户登陆密码
     * @return 返回参数代码 并将获取到的用户实例返回 key:user
     */
    public Map<String,Object> findUserBySchoolIDOrPhone(String user, String password) {

        Map<String,Object> map = new HashMap<>();

        int length = user.length();
        User haveUser;
        if (length ==  11){
            haveUser = findByProperty("phone", user);
        }else if (length == 12){
            haveUser = findByProperty("schoolID", user);
        }else{
            map.put("result" , "2");
            return map;
        }

        if (haveUser == null) {
            map.put("result" , "1");
        } else if (haveUser.getPassword().equals(password)) {
            map.put("result" , "0");
            map.put("user" , haveUser);
        } else {
            map.put("result" , "2");
        }
        return map;
    }

    /**
     * 通过用户名直接获取到用户的所有信息
     * @param user 用户的校园卡id
     * @return 用户信息
     */
    public User findUserBySchoolID(String user){
        return findByProperty("schoolID", user);
    }

    /**
     * 通过手机号码直接获取到用户的所有信息
     * @param phone 用户的手机号码
     * @return 用户信息
     */
    public User findUserByPhone(String phone){
        return findByProperty("phone", phone);
    }

}
