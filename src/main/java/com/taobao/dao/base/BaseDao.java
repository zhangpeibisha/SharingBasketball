package com.taobao.dao.base;

import java.util.List;

/**
 * Create by zhangpe0312@qq.com on 2018/2/6.
 *
 * 持久层基类
 */
public interface BaseDao<T> {
    //保存对象
    void save(T t);

    //删除对象
    void delete(T t);

    //更新对象
    void updata(T t);

    //根据id查询对象
    T getObjectByID(String id);

    //查询所有对象
    List<T> getObjectAll();

    //分页查询
    List<T> findPage(int page, int pageSize);
}
