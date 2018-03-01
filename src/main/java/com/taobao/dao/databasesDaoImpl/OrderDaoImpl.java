package com.taobao.dao.databasesDaoImpl;

import com.taobao.dao.databasesDao.SupperBaseDAOImp;
import com.taobao.dao.entity.Order;
import com.taobao.dao.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Create by zhangpe0312@qq.com on 2018/2/26.
 */
@Service
public class OrderDaoImpl extends SupperBaseDAOImp<Order> {
    @Override
    public <T> List<T> findByCriteria(T object, Integer startRow, Integer pageSize) {
        return null;
    }

    @Override
    public <T> Long findByCriteriaCount(T object) {
        return null;
    }

    /**
     * @param user        已经登陆的用户
     * @param limit       每页多少行
     * @param currentPage 开始页数
     * @return 指定的订单列表
     */
    public List<Order> findUserOrderList(User user, int limit, int currentPage) {
        List<Order> temp = new ArrayList<>();

        if (user.getOrders().isEmpty()) {
            return null;
        }

        if (limit <= 0 || currentPage <= 0) {
            return null;
        }

        Set<Order> orders = user.getOrders();
        currentPage = (currentPage - 1) * limit;
        Iterator<Order> iterator = orders.iterator();

        int point = 0;

        while (iterator.hasNext()) {
            if (currentPage <= point && point < (currentPage + limit)) {
                temp.add(iterator.next());
            }

            if (point == (currentPage + limit)) {
                return temp;
            }

            point++;
        }

        return null;
    }
}
