package com.taobao.dao.databasesDaoImpl;

import com.taobao.dao.databasesDao.SupperBaseDAOImp;
import com.taobao.dao.entity.User;

import java.util.List;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 *
 * 用户的操作数据库的最高层服务类
 */
public class UserDao extends SupperBaseDAOImp<User> {

    @Override
    public <T> List<T> findByCriteria(T object, Integer startRow, Integer pageSize) {
        return null;
    }

    @Override
    public <T> Long findByCriteriaCount(T object) {
        return null;
    }
}
