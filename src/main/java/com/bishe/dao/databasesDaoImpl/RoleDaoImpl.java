package com.bishe.dao.databasesDaoImpl;

import com.bishe.dao.databasesDao.SupperBaseDAOImp;
import com.bishe.dao.entity.Role;
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

    public Role findRoleByName(String name){
        return findByProperty("name",name);
    }
}
