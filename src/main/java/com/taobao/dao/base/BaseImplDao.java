package com.taobao.dao.base;


import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Create by zhangpe0312@qq.com on 2018/2/6.
 */
@Transactional
public class BaseImplDao<T> implements BaseDao<T> {

    public static Logger logger = Logger.getLogger(BaseDao.class);

    @Resource
    private
    SessionFactory sessionFactory;

    private Class clazz;

    //获取这个类的实际类型
    public BaseImplDao() {
        try {
            ParameterizedType type =
                    (ParameterizedType) this.getClass().getGenericSuperclass();
            clazz = (Class) type.getActualTypeArguments()[0];
            System.out.println(clazz);
            logger.info("实际类型为: " + clazz);
        } catch (Exception e) {
            logger.error("类型转换失败 " + e);
        }

    }

    public  Session getSeeion() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(T t) {
        getSeeion().save(t);
        logger.info("添加信息 " + t.toString());
    }

    @Override
    public void delete(T t) {
        getSeeion().delete(t);
        logger.info("删除信息 " + t.toString());
    }

    @Override
    public void updata(T t) {
        getSeeion().update(t);
        logger.info("更新信息 " + t.toString());
    }

    @Override
    public T getObjectByID(String id) {
        logger.info("通过 " + id + " 获取到了信息");
        return (T) getSeeion().get(clazz, id);
    }

    @Override
    public List<T> getObjectAll() {
        logger.info("得到了这个类型的所有信息：" + clazz.getSimpleName());
        return (List<T>) getSeeion().createQuery("from " + clazz.getSimpleName()).list();
    }

    /**
     * 分页查询
     *
     * @param page     开始页
     * @param pageSize 每一页的大小
     * @return 返回这页的所有数据
     */
    @Override
    public List<T> findPage(int page, int pageSize) {

//        List<T> list = getSeeion().execute(new HibernateCallback<List<T>>() {
//            @Override
//            public List<T> doInHibernate(Session session) throws HibernateException {
//                String hql = "from" + clazz.getSimpleName();
//                Query query =  session.createQuery(hql);
//
//                int begin = (page-1)*pageSize;
//                query.setFirstResult(begin);
//                query.setMaxResults(pageSize);
//                return query.list();
//            }
//        });
        return null;
    }
}
