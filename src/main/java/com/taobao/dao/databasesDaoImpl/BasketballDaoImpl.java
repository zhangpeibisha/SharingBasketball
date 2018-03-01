package com.taobao.dao.databasesDaoImpl;

import com.taobao.dao.databasesDao.SupperBaseDAOImp;
import com.taobao.dao.entity.Basketball;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by zhangpe0312@qq.com on 2018/2/26.
 */
@Service
public class BasketballDaoImpl extends SupperBaseDAOImp<Basketball> {

    private static Logger logger = Logger.getLogger(BasketballDaoImpl.class);

    @Override
    public <T> List<T> findByCriteria(T object, Integer startRow, Integer pageSize) {
        return null;
    }

    @Override
    public <T> Long findByCriteriaCount(T object) {
        return null;
    }

    /**
     * 起始页从1开始 当页码小于等于一时 为非法页码 返回空值
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Basketball> findRentList(int pageNo, int pageSize) {

        if (pageNo <= 0 || pageSize <= 0 ) {
            return null;
        }

        pageNo = (pageNo - 1) * pageSize;

        logger.info("页码信息 star " + pageNo + " size " + pageSize);

        String sql = "select * from basketball \n" +
                "               where isBad=0 and isRent=0\n" +
                "               and basketball.nowPerssure>=basketball.pressure \n" +
                "               LIMIT " + pageNo + "," + pageSize;
        List<Basketball> basketballs = findEntityBySQL(sql, Basketball.class);

        for (Basketball basketball : basketballs) {
            basketball.getRent().setBasketballs(null);
        }
        return basketballs;

    }

    /**
     * 返回所有篮球
     *
     * @param pageNo   开始页
     * @param pageSize 每页多少数据
     * @return
     */
    public List<Basketball> findBastketballList(int pageNo, int pageSize) {

        if (pageNo <= 0 || pageSize <= 0 ) {
            return null;
        }

        pageNo = (pageNo - 1) * pageSize;

        logger.info("页码信息 star " + pageNo + " size " +pageSize);

        String sql = "select * from basketball LIMIT " + pageNo + "," +  pageSize;
        List<Basketball> basketballs = findEntityBySQL(sql, Basketball.class);

        for (Basketball basketball : basketballs) {
            basketball.getRent().setBasketballs(null);
        }
        return basketballs;

    }
}
