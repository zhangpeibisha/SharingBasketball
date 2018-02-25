package com.taobao.dao.databasesDaoImpl;

import com.taobao.dao.databasesDao.SupperBaseDAOImp;
import com.taobao.dao.entity.Role;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Create by zhangpe0312@qq.com on 2018/2/26.
 */
@Service
public class RoleDaoImpl extends SupperBaseDAOImp<Role>{

    @Override
    public <T> List<T> findByCriteria(T object, Integer startRow, Integer pageSize) {
        return null;
    }

    @Override
    public <T> Long findByCriteriaCount(T object) {
        return null;
    }
}
