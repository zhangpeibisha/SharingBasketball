package com.taobao.dao.databasesDaoImpl;

import com.taobao.dao.databasesDao.SupperBaseDAOImp;
import com.taobao.dao.entity.User;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * @return 返回参数代码
     */
    public int findUserBySchoolID(String user, String password) {
        User haveUser = findByProperty("schoolID", user);
        if (haveUser == null) {
            return 1;
        } else if (haveUser.getPassword().equals(password)) {
            return 0;
        } else {
            return 2;
        }
    }
}
