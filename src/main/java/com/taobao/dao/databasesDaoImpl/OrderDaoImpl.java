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
     * @param user        已经登陆的用户
     * @param limit       每页多少行
     * @param currentPage 开始页数
     * @return 指定的订单列表
     */
    public Map<String,Object> findUserOrderList(User user, int limit, int currentPage) {
        Map<String,Object> map = new HashMap<>();

        List<Order> temp = new ArrayList<>();

        if (user == null) {
            map.put("length",0);
            map.put("listOrder",null);
            return map;
        }

        if (user.getOrders().isEmpty()) {
            map.put("length",0);
            map.put("listOrder",null);
            return map;
        }

        if (limit <= 0 || currentPage <= 0) {
            map.put("length",0);
            map.put("listOrder",null);
            return map;
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
                break;
            }

            point++;
        }

        Collections.sort(temp,new SortByID());
        map.put("length",orders.size());
        map.put("listOrder",temp);

        return map;
    }

    class SortByID implements Comparator {
        public int compare(Object o1, Object o2) {
            Order s1 = (Order) o1;
            Order s2 = (Order) o2;
            return (s1.getOrderID()+"").compareTo(s2.getOrderID()+"");
        }
    }


    /**
     * 查找用户指定的订单
     *
     * @param user
     * @param limit
     * @param currentPage
     * @return
     */
    public Map<String,Object> findUserUndoneOrderList(User user, int limit, int currentPage , boolean isUndone) {

        Map<String,Object> map = new HashMap<>();
        List<Order> temp = new ArrayList<>();

        if (user == null) {
            map.put("length",0);
            map.put("listOrder",null);
            return map;
        }

        if (user.getOrders().isEmpty()) {
            map.put("length",0);
            map.put("listOrder",null);
            return map;
        }

        if (limit <= 0 || currentPage <= 0) {
            map.put("length",0);
            map.put("listOrder",null);
            return map;
        }

        Set<Order> orders = user.getOrders();
        currentPage = (currentPage - 1) * limit;
        Iterator<Order> iterator = orders.iterator();

        while (iterator.hasNext()) {
            Order order = iterator.next();
            //没有完成的订单
            if (order.getReturnTime() == null && isUndone ) {
                temp.add(order);
            }
            //完成了的订单
            if (order.getReturnTime() != null && !isUndone ) {
                temp.add(order);
            }
        }

        map.put("length",temp.size());
        //移除不符合要求的订单
        for (int i = 0; i < temp.size(); i++) {
            if (i < currentPage || i >= (currentPage + limit)) {
                temp.remove(temp.get(i));
            }
        }
        Collections.sort(temp,new SortByID());
        map.put("listOrder",temp);
        return map;
    }

}
