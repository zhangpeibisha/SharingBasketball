package com.taobao.dao.databasesDaoImpl;

import com.taobao.dao.databasesDao.SupperBaseDAOImp;
import com.taobao.dao.entity.Order;
import com.taobao.dao.entity.User;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Create by zhangpe0312@qq.com on 2018/2/26.
 */
@Service
public class OrderDaoImpl extends SupperBaseDAOImp<Order> {

    private static Logger logger = Logger.getLogger(OrderDaoImpl.class);

    @Override
    public <T> List<T> findByCriteria(T object, Integer startRow, Integer pageSize) {
        return null;
    }

    @Override
    public <T> Long findByCriteriaCount(T object) {
        return null;
    }

    /**
     * 按要求获取用户订单
     * @return
     */
    public List<Order> findUserOrderList(User user , int limit, int currentPage){
        String sql = "SELECT * FROM order_basketball_user where `user` = ? order by id desc LIMIT ? , ?";
        int start = (currentPage-1)*limit;
        return findBySql(sql,user.getUserID(),start,limit);
    }

    /**
     * 未完成订单
     * @param user
     * @return
     */
    public List<Order> findUserUndoneOrderList(User user , int limit, int currentPage){
        String sql = "SELECT * FROM order_basketball_user where `user` = ? and castMoney = 0  LIMIT ? , ?";
        int start = (currentPage-1)*limit;
        return findBySql(sql,user.getUserID(),start,limit);
    }

    /**
     * 用户完成订单
     * @param user
     * @return
     */
    public List<Order> findUserdoneOrderList(User user, int limit, int currentPage){
        String sql = "SELECT * FROM order_basketball_user where `user` = ? and castMoney > 0 order by id desc LIMIT ? , ?";
        int start = (currentPage-1)*limit;
        return findBySql(sql,user.getUserID(),start,limit);
    }
}
