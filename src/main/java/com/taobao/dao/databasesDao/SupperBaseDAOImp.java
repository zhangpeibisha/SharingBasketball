package com.taobao.dao.databasesDao;

import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate4.SessionFactoryUtils;

import javax.annotation.Resource;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.lang.reflect.ParameterizedType;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 * <p>
 * 功能:实现dao接口
 */
public abstract class SupperBaseDAOImp<T> implements SupperBaseDAO<T> {

    private Class<T> clazz;
    @Resource(name = "sessionFactory")
    protected SessionFactory sessionFactory;
    protected static Logger logger = Logger.getLogger(SupperBaseDAOImp.class);

    /**
     * 构造方法，构造时得到泛型对应的类
     */
    public SupperBaseDAOImp() {
        super();
        this.clazz = null;
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void clean() {
        sessionFactory.getCurrentSession().clear();
    }

    @Override
    public void flush() {
        sessionFactory.getCurrentSession().flush();
    }

    /**
     * 提交数据并清空缓存
     */
    @Override
    public void flushANDclear() {
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
    }

    /**
     * 保存
     *
     * @param object 需要保存的对象
     * @return 保存成功的对象
     */
    @Override
    public Serializable save(final T object) {
        return sessionFactory.getCurrentSession().save(object);
    }

    /**
     * 更新
     *
     * @param object 需要更新的对象，必须包含ID及非空约束的列值(新值/旧值)
     */
    @Override
    public void update(final T object) {
        sessionFactory.getCurrentSession().update(object);
    }

    /**
     * 删除对象，物理删除
     *
     * @param object 需要删除的对象，必须包含ID及非空约束的列值
     */
    @Override
    public void delete(final T object) {
        sessionFactory.getCurrentSession().delete(object);
    }

    /**
     * 用sql根据id删除对象
     * spring3后版本
     *
     * @param id id
     */
    @Override
    public void delete(final Serializable id) {
        Annotation[] annotations = clazz.getAnnotations();
        String tableName = "";
        for (Annotation annotation : annotations) {
            if (annotation instanceof Table) {
                tableName = ((Table) annotation).name();
            }
        }
        if (tableName != null && tableName != "") {
            SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery("delete from " + tableName + " where id=?");
            sqlQuery.setInteger(0, (Integer) id);
            sqlQuery.executeUpdate();
        }
    }

    /**
     * @param id  记录ID
     * @param <T> 泛型
     * @return T
     */
    @Override
    public <T> T findById(final Serializable id) {
        return (T) sessionFactory.getCurrentSession().get(clazz, id);
    }

    /**
     * load方式加载对象，先在对象管理池（2级缓存）中查找有没有对象，没有再创建代理，当使用的时候再查询数据库
     *
     * @param id  ID
     * @param <T> 泛型
     * @return T
     */
    @Override
    public <T> T loadById(final Serializable id) {
        return (T) sessionFactory.getCurrentSession().load(clazz, id);
    }

    /**
     * 利用唯一属性获取对象，错误则返回null
     *
     * @param propertyName 属性名称
     * @param value        属性值
     * @param <T>          泛型
     * @return T
     */
    @Override
    public <T> T findByProperty(final String propertyName, final Object value) {
        if (propertyName != null && value != null) {
            String clazzName = clazz.getSimpleName();
            StringBuffer stringBuffer = new StringBuffer("select obj from ");
            stringBuffer.append(clazzName).append(" obj");
            stringBuffer.append(" where obj.").append(propertyName).append("=:value");
            Query query = sessionFactory.getCurrentSession().createQuery(stringBuffer.toString()).setParameter("value", value);
            return (T) query.uniqueResult();
        }
        return null;
    }

    /**
     * 根据属性值对获取唯一对象，属性值对必须是数据库唯一,错误则返回null
     *
     * @param propertyNames 属性名称数组
     * @param values        属性值数组
     * @param <T>           泛型
     * @return T
     */
    @Override
    public <T> T findByProperties(final String[] propertyNames, final Object[] values) {
        if (propertyNames.length != 0 && values.length != 0 && propertyNames.length == values.length) {
            String clazzName = clazz.getSimpleName();
            StringBuffer stringBuffer = new StringBuffer("select Object(obj) from ");
            stringBuffer.append(clazzName);
            Query query = DaoUtil.createConditionQuery(sessionFactory, stringBuffer, propertyNames, values, "id", false);
            //执行查询
            return (T) query.uniqueResult();
        }
        return null;
    }

    /**
     * 使用实例对象获得持久记录
     *
     * @param object 对象
     * @param <T>    泛型
     * @return 对象
     */
    @Override
    public <T> T findByEntity(T object) throws IllegalAccessException {
        Map<String, Object> map = DaoUtil.getBeanPropertiesAndValues(object);
        //转换map为属性名数组和值数组
        List<Object[]> mapList = DaoUtil.changeMapToArray(map);
        String[] propertyNames = (String[]) mapList.get(0);
        Object[] values = (Object[]) mapList.get(1);
        //组装HQL
        StringBuffer stringBuffer = new StringBuffer("select obj from ").append(object.getClass().getSimpleName());
        DaoUtil.fillParameters(stringBuffer, propertyNames);
        //填充参数值
        Query query = DaoUtil.fillValues(sessionFactory, stringBuffer, propertyNames, values);
        //返回链表
        return (T) query.uniqueResult();
    }

    /**
     * @param <T> 泛型
     * @return List<T>
     */
    @Override
    public <T> List<T> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from " + clazz.getSimpleName() + " obj").list();
    }

    public <T> List<T> findAllByPage(final String propertyName, final boolean desc, final Integer startRow, final Integer pageSize) {
        String o = desc ? "desc" : "asc";
        return sessionFactory.getCurrentSession().createQuery("from " + clazz.getSimpleName() + " obj order by obj." + propertyName + " " + o).setFirstResult(startRow).setMaxResults(pageSize).list();
    }

    /**
     * 获取记录行数
     *
     * @return Long
     */
    @Override
    public Long findAllCount() {
        return ((BigInteger) sessionFactory.getCurrentSession().createSQLQuery("select count(0) from " + DaoUtil.getTableName(clazz)).uniqueResult()).longValue();
    }

    /**
     * 用iterator方式查询数据库
     *
     * @param <T> 泛型
     * @return Iterator
     */
    @Override
    public <T> List<T> findAllIterator() {
        int length = Integer.valueOf(this.findAllCount().toString());
        List<T> list = new ArrayList(length);
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from " + clazz.getSimpleName() + " obj");
        Iterator iterator = query.iterate();
        while (iterator.hasNext()) {
            list.add((T) iterator.next());
        }
        return list;
    }

    /**
     * 用HQL Iterator分页查询
     *
     * @param HQL      HQL查询语句
     * @param startRow 开始行数
     * @param pageSize 页码
     * @param <T>      泛型
     * @return List
     */
    @Override
    public <T> List<T> batchGetDataIteratorByHQL(final String HQL, final Integer startRow, final Integer pageSize) {
        Query query = sessionFactory.getCurrentSession().createQuery(HQL);
        query.setFirstResult(startRow);
        query.setMaxResults(pageSize);
        Iterator iterator = query.iterate();
        List<T> list = new ArrayList(pageSize);
        while (iterator.hasNext()) {
            list.add((T) iterator.next());
        }
        return list;
    }

    /**
     * 用HQL Srcoll查询
     * 大量查询时获取较高性能,此查询依赖于游标，返回的数据为查询的行值数组（Object[]）
     * 可用于多表联合查询
     *
     * @param HQL      HQL查询语句
     * @param startRow 起始行
     * @param pageSize 要查询多少行
     * @return ScrollableResults
     */
    @Override
    public ScrollableResults batchGetDataScrollByHQL(final String HQL, final Integer startRow, final Integer pageSize) {
        Query query = sessionFactory.getCurrentSession().createQuery(HQL).setFirstResult(startRow).setMaxResults(pageSize);
        return query.scroll(ScrollMode.SCROLL_SENSITIVE);
    }

    /**
     * 用SQL Scroll查询,ScrollableResults取值时只能用get(int)取出Object然后强转型
     *
     * @param SQL      SQL查询语句
     * @param startRow 起始行
     * @param pageSize 要查询多少行
     * @return ScrollableResults
     */
    @Override
    public ScrollableResults batchGetDataScrollBySQL(final String SQL, final Integer startRow, final Integer pageSize) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(SQL).setFirstResult(startRow).setMaxResults(pageSize);
        ScrollableResults scrollableResults = query.scroll(ScrollMode.SCROLL_SENSITIVE);
        return scrollableResults;
    }

    /**
     * @param HQL HQL语句
     * @return Long
     */
    @Override
    public Long findCountByHQL(final String HQL) {
        if (HQL.contains("count")) {
            final int fromIndex = HQL.indexOf("from");
            final int commaIndex = HQL.indexOf(',');
            //如果from前面出现','，非法的HQL语句
            if (commaIndex != -1 && fromIndex > commaIndex)
                return 0l;
            else return (Long) sessionFactory.getCurrentSession().createQuery(HQL).uniqueResult();
        } else {
            return 0l;
        }
    }

    /**
     * @param SQL sql语句
     * @return Long
     */
    @Override
    public Long findCountBySQL(final String SQL) {
        if (SQL.contains("count")) {
            String sql = SQL.toLowerCase();
            final int fromIndex = sql.indexOf("from");
            final int commaIndex = sql.indexOf(',');
            //如果from前面出现','，非法的SQL语句
            if (commaIndex != -1 && fromIndex > commaIndex) return 0l;
            else {
                BigInteger bg = (BigInteger) sessionFactory.getCurrentSession().createSQLQuery(SQL).uniqueResult();
                return bg.longValue();
            }

        } else {
            return 0l;
        }
    }

    /**
     * 属性名称和属性值键值对数组形式查询对象链表
     *
     * @param propertyNames 属性名称数组
     * @param values        属性值数组
     * @param startRow      记录开始行数
     * @param pageSize      分页数（要取的记录行数）
     * @param <T>           泛型
     * @return List<T> or null
     */
    @Override
    public <T> List<T> findByProperty(final String[] propertyNames, final Object[] values, final Integer startRow, final Integer pageSize, final String sortColumn, final boolean isAsc) {
        if (propertyNames.length != 0 && values.length != 0 && propertyNames.length == values.length) {
            String clazzName = clazz.getSimpleName();
            StringBuffer stringBuffer = new StringBuffer("select Object(obj) from ");
            stringBuffer.append(clazzName);
            Query query = DaoUtil.createConditionQuery(sessionFactory, stringBuffer, propertyNames, values, sortColumn, isAsc);
            //分页
            query.setFirstResult(startRow);
            query.setMaxResults(pageSize);
            //执行查询
            return query.list();
        }
        return null;
    }


    /**
     * 只适用于基础数据类型属性，封装为HQL语句查询
     *
     * @param propertyNames 属性数组名称
     * @param values        属性值数组
     * @return Long
     */
    @Override
    public Long findByPropertyCount(final String[] propertyNames, final Object[] values) {
        if (propertyNames.length != 0 && values.length != 0 && propertyNames.length == values.length) {
            String clazzName = clazz.getSimpleName();
            StringBuffer stringBuffer = new StringBuffer("select count(0) from ");
            stringBuffer.append(clazzName);
            Query query = DaoUtil.createConditionQuery(sessionFactory, stringBuffer, propertyNames, values, "id", true);
            return (Long) query.uniqueResult();
        }
        return 0l;
    }

    /**
     * @param propertyName 属性名称
     * @param value        属性值
     * @param startRow     开始行数
     * @param pageSize     最大页数
     * @param sortColumn   排序字段
     * @param isAsc        是否升序
     * @param <T>          泛型
     * @return List<T>
     */
    @Override
    public <T> List<T> findByProperty(final String propertyName, final Object value, final Integer startRow, final Integer pageSize, final String sortColumn, final boolean isAsc) {
        String clazzName = clazz.getSimpleName();
        StringBuffer stringBuffer = new StringBuffer("select obj from ");
        stringBuffer.append(clazzName);
        Query query = DaoUtil.createSingleConditionHqlQuery(sessionFactory, stringBuffer, propertyName, value, sortColumn, isAsc);
        //分页
        query.setFirstResult(startRow);
        query.setMaxResults(pageSize);
        return query.list();
    }

    /**
     * 获取记录行数，封装为HQL语句查询
     *
     * @param propertyName 属性名称
     * @param value        属性值
     * @return 记录行数
     */
    @Override
    public Long findByPropertyCount(final String propertyName, final Object value) {
        String clazzName = clazz.getSimpleName();
        StringBuffer stringBuffer = new StringBuffer("select count(0) from ");
        stringBuffer.append(clazzName);
        Query query = DaoUtil.createSingleConditionHqlQuery(sessionFactory, stringBuffer, propertyName, value, "id", true);
        return (Long) query.uniqueResult();
    }

    /**
     * 用对象自动判断非空属性查询
     *
     * @param object     对象
     * @param startRow   开始行数
     * @param pageSize   每页数据
     * @param sortColumn 排序字段
     * @param isAsc      是否升序
     * @param <T>        泛参数
     * @return 列表
     */
    @Override
    public <T> List<T> findByProperty(final T object, final Integer startRow, final Integer pageSize, final String sortColumn, final boolean isAsc) {
        List list = null;
        try {
            Map<String, Object> map = DaoUtil.getBeanPropertiesAndValues(object);
            //转换map为属性名数组和值数组
            List<Object[]> mapList = DaoUtil.changeMapToArray(map);
            String[] propertyNames = (String[]) mapList.get(0);
            Object[] values = (Object[]) mapList.get(1);
            //组装HQL
            StringBuffer stringBuffer = new StringBuffer("select obj from ").append(clazz.getSimpleName());
            DaoUtil.fillParameters(stringBuffer, propertyNames);
            //排序
            String desc = isAsc ? "asc" : "desc";
            stringBuffer.append("order by obj.").append(sortColumn).append(" " + desc);
            //填充参数值
            Query query = DaoUtil.fillValues(sessionFactory, stringBuffer, propertyNames, values);
            //返回链表
            list = query.list();
            return list;
        } catch (IllegalAccessException e) {
            logger.info("抛出异常方法：getBeanPropertiesAndValues！调用方法：SupperBaseDAOImp.findByProperty()");
            e.printStackTrace();
        }
        return list;
    }


    /**
     * @param object 根据对象查询记录数量
     * @return Long
     */
    @Override
    public Long findByPropertyCount(final T object) {
        long count;
        try {
            Map<String, Object> map = DaoUtil.getBeanPropertiesAndValues(object);
            //转换map为属性名数组和值数组
            List<Object[]> mapList = DaoUtil.changeMapToArray(map);
            String[] propertyNames = (String[]) mapList.get(0);
            Object[] values = mapList.get(1);
            //组装HQL
            StringBuffer stringBuffer = new StringBuffer("select obj from ").append(clazz.getSimpleName());
            DaoUtil.fillParameters(stringBuffer, propertyNames);
            //填充参数值
            Query query = DaoUtil.fillValues(sessionFactory, stringBuffer, propertyNames, values);
            //返回链表
            count = (Long) query.uniqueResult();
            return count;
        } catch (IllegalAccessException e) {
            logger.info("抛出异常方法：getBeanPropertiesAndValues！调用方法：SupperBaseDAOImp.findByPropertyCount()");
            e.printStackTrace();
        }
        return 0l;
    }

    /**
     * 用Map参数分页查询记录
     *
     * @param map        map<String propertyName,Object value>
     * @param startRow   记录开始行数
     * @param pageSize   获取记录数
     * @param sortColumn 排序字段
     * @param isAsc      是否升序
     * @param <T>        泛型
     * @return List<T>
     */
    @Override
    public <T> List<T> findByProperty(final Map<String, Object> map, final Integer startRow, final Integer pageSize, final String sortColumn, final boolean isAsc) {
        List<Object[]> mapList = DaoUtil.changeMapToArray(map);
        String[] propertyNames = (String[]) mapList.get(0);
        Object[] values = mapList.get(1);
        StringBuffer stringBuffer = new StringBuffer("select obj from ").append(clazz.getSimpleName());
        Query query = DaoUtil.createConditionQuery(sessionFactory, stringBuffer, propertyNames, values, sortColumn, isAsc);
        query.setFirstResult(startRow);
        query.setMaxResults(pageSize);
        return query.list();
    }

    /**
     * 用Map参数分页查询记录数，封装为HQL查询
     *
     * @param map map<String propertyName,Object value>
     * @return Long
     */
    @Override
    public Long findByPropertyCount(final Map<String, Object> map) {
        List<Object[]> mapList = DaoUtil.changeMapToArray(map);
        String[] propertyNames = (String[]) mapList.get(0);
        Object[] values = mapList.get(1);
        StringBuffer stringBuffer = new StringBuffer("select count(0) from ").append(clazz.getSimpleName());
        Query query = DaoUtil.createConditionQuery(sessionFactory, stringBuffer, propertyNames, values, "id", true);
        return (Long) query.uniqueResult();
    }

    /**
     * 使用sql语句查询记录
     *
     * @param sql    sql语句，参数位置用？代替
     * @param values 不定数参数
     * @param <T>    泛型
     * @return List<T>
     */
    @Override
    public <T> List<T> findBySql(final String sql, final Object... values) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(clazz);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i]);
        }
        return query.list();
    }

    /**
     * @param sql    sql语句
     * @param values 值
     * @return Long记录数
     */
    @Override
    public Long findBySqlCount(final String sql, final Object... values) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i]);
        }
        long count = ((BigInteger) query.uniqueResult()).longValue();
        return count;
    }

    /**
     * 查询外键后会创建代理对象，可以使用get方法获取对应的关联对象
     *
     * @param sql    SQL语句
     * @param values 不定长参数
     * @param <T>    泛型
     * @return 单个对象
     */
    @Override
    public <T> T findUniqueBySql(final String sql, final Object... values) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(clazz);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i]);
        }
        return (T) query.uniqueResult();
    }

    /**
     * 使用sql语句批量删除或更新
     *
     * @param sql    sql语句
     * @param values 参数值
     * @return 影响行数
     */
    @Override
    public int batchUpdateOrDelete(final String sql, final Object... values) {
        //是否是合法的sql插入语句
        if (sql.indexOf("update") != -1) {
            Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
            //设置忽略二级缓存
            query.setCacheMode(CacheMode.IGNORE);
            query.setCacheable(false);
            return query.executeUpdate();
        }
        return 0;
    }

    /**
     * HQL批量插入，本方法线程同步且打开新的数据库连接
     *
     * @param objects 插入对象List
     * @param size    每插入多少条记录清空缓存
     */
    @Override
    public synchronized void batchInsertByHQL(List<T> objects, Integer size) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        //设置不二级缓存
        session.setCacheMode(CacheMode.IGNORE);
        int i = 0;
        final int length = objects.size();
        while (i < length) {
            session.save(objects.get(i));
            i++;
            if (i % size == 0) {
                session.flush();
                session.clear();
            }
        }
        tx.commit();
        session.close();
    }

    /**
     * SQL语句批量插入，采用预定义SQL，Preparedstatement，传入需插入的值链表，其中每个字符串数组对应一条记录
     * prepareStatement设置参数从1开始，createQuery,createSQLQuery设置参数从0开始
     *
     * @param sql    插入语句，如：insert into user_info(account,pwd) values
     * @param values 需插入数据库值
     */
    @Override
    public void batchInsertBySQL(final String sql, final List<Object[]> values) {
        final int length = values.get(0).length;
        if (length != 0) {
            try {
                StringBuffer stringBuffer = new StringBuffer(sql);
                final int size = values.size();
                //拼接values后的括号和参数位
                for (int i = 0; i < size; i++) {
                    stringBuffer.append("(");
                    for (int j = 0; j < length; j++) {
                        if (j == 0) stringBuffer.append("?");
                        if (j != 0) stringBuffer.append(",?");
                    }
                    if (i != size - 1) stringBuffer.append("),");
                    if (i == size - 1) stringBuffer.append(")");
                }
                //生成预处理SQL
                Connection conn = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
                PreparedStatement statement = conn.prepareStatement(stringBuffer.toString());
                int len = size;
                List<Object> list = new ArrayList(size * length);
                //获取所有参数
                int loopI = 0;
                while (len > loopI) {
                    Object[] objects = values.get(loopI);
                    for (int i = 0; i < length; i++) {
                        list.add(objects[i]);
                    }
                    loopI++;
                }
                int listLen = list.size();
                int index = 1;
                //填充参数
                while (index <= listLen) {
                    statement.setObject(index, list.get(index - 1));
                    index++;
                }
                statement.addBatch();
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.info("##com.blog.dao.SupperBaseDAOImp.batchInsertBySQL(final String sql, final List<Object[]> values)方法出错##");
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                logger.info("##com.blog.dao.SupperBaseDAOImp.batchInsertBySQL(final String sql, final List<Object[]> values)方法出错##");
            }
        }
    }

    /**
     * the table data with sql:"select d.code,d.value from dictionary d":
     * +------+--------+
     * | code | value  |
     * +------+--------+
     * | 001  | 四川   |
     * | 002  | 北京   |
     * | 001  | 四川   |
     * +------+--------+
     * eg:select new Map(d.code, d.value) from Dictionary d
     * result:[{0=001，1=四川},{0=002，1=北京},{0=001，1=四川}]
     * the result is List<Map>,one Map is one record,if not define the alias name,the map's key will be the column index and start with 0;
     * if you define a alias name ,it to be like this:
     * select new Map(d.value as value, d.code as code) from Dictionary d
     * [{value=四川, code=001},{value=北京, code=002},{value=四川, code=001}]
     * select new Map(d.value, d.code as code) from Dictionary d
     * [{0=四川, code=001},{0=北京, code=002},{0=四川, code=001}]
     * the map's key will be the alias name!
     *
     * @param HQL      HQL语句，如：select new Map(d.code as code, d.value as value) from Dictionary d;标明别名的‘as’是必须的
     * @param startRow 开始行数
     * @param pageSize 返回记录数
     * @return List<Map>
     */
    @Override
    public List<Map> getMapByHQL(final String HQL, final Integer startRow, final Integer pageSize) {
        Query query = sessionFactory.getCurrentSession().createQuery(HQL).setFirstResult(startRow).setMaxResults(pageSize);
        return query.list();
    }

    /**
     * table data:
     * +----+------+--------+
     * | id | code | value  |
     * +----+------+--------+
     * |  1 | 001  | 四川   |
     * |  2 | 002  | 北京   |
     * |  3 | 001  | 四川   |
     * +----+------+--------+
     * eg:select new List(d.code,d.value) from Dictionary d
     * result:[[001, 四川], [002, 北京], [001, 四川]]
     * the result is List<List>, one inner list is filled by column's value using one record
     *
     * @param HQL      HQL语句
     * @param startRow 开始行数
     * @param pageSize 返回记录数
     * @return List<List>
     */
    @Override
    public List<List> getListByHQL(final String HQL, final Integer startRow, final Integer pageSize) {
        Query query = sessionFactory.getCurrentSession().createQuery(HQL).setFirstResult(startRow).setMaxResults(pageSize);
        return query.list();
    }

    /**
     * 通过SQL语句查询记录，如果换addEntity使用query.addScalar(String,Hibernate.xxx)会产生List<Object[]>，且只返回addScalar声明过的字段
     * 此方法要求SQL语句中的返回列名称与实体属性名称匹配且等于或少于实体属性数量，此方法局限于：只能返回对应类本身的简单属性，不能返回类属性
     *
     * @param SQL sql语句
     * @return List<T>
     */
    @Override
    public <T> List<T> getListBySQL(final String SQL) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(SQL).addEntity(clazz);
        return query.list();
    }

    /**
     * 使用SQL查询非受管实体，一般指DTO
     *
     * @param SQL      sql语句
     * @param claz     返回对象的类
     * @param startRow 起始数据
     * @param pageSize 获取数据量,大于0将使用分页，否则不会使用分页
     * @return List
     */
    public <T> List<T> getListBySQL(final String SQL, final Class<T> claz, final Integer startRow, final Integer pageSize) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(SQL).setResultTransformer(Transformers.aliasToBean(claz));
        if (pageSize > 0) {
            query.setFirstResult(startRow).setMaxResults(pageSize);
        }
        return query.list();
    }

    /**
     * SQL查询返回List，List中存放的是Object数组，每个数组对应一条记录，数组的索引对应查询返回的列的索引（从0开始）
     *
     * @param SQL sql语句
     * @return List<Object [ ]>
     */
    @Override
    public List<Object[]> getListObjBySQL(String SQL) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(SQL);
        return query.list();
    }

    /**
     * 通过SQL查询记录，一个map是一个记录，Map的Key是查询出来的column的名称，值为对应记录的值
     *
     * @param SQL sql语句
     * @return List<Map>
     */
    @Override
    public List<Map> getMapBySQL(String SQL) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(SQL).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    /**
     * 用SQL查询返回当前泛型对应实体对象，只返回一种类对应实体对象列表，包含子/关联实体
     * SQL需要视情况使用别名注入
     * 例如：
     * 一对一
     * createSQLQuery("select {login.*},{user.*} from user_login login,user_info user where user.loginid=login.id and loginid=1").addEntity("lgonin",UserLogin.class).addJoin("user","login.userInfo").list();
     * 一对多：
     * createSQLQuery("select {ar.*},{ca.*} from article_category ca,article_content ar where ar.category=ca.id").addEntity("ca",ArticleCategory.class).addJoin("ar","ca.articleContents").list();
     * 将会返回List<Object[]>,每个Object[]中有一个UserLogin(ArticleCategory)和一个UserInfo(ArticleContent)对象[其中ArticleCategory中的一对多关联的SET集合被初始化]，亦即是说关联会多产生被关联对象，Object[0]中的对象永远是addEntity中类的对象，List.size=记录的条数，即一对一时：多少条记录对应size；多对多时：多的一方记录条数对应size。
     *
     * @param addClazz List<List<Object>>，第一个inner List放返回的对象的别名和类，接着的放关联对象的别名和类
     * @param startRow 开始行数
     * @param pageSize 取记录数量 当此量大于0时才会调用Hibernate的分页功能
     * @return List
     */
    @Override
    public List findBySqlWithJoin(final String SQL, final List<List> addClazz, final Integer startRow, final Integer pageSize) {
        List ls = addClazz.get(0);
        Class claz = (Class) ls.get(1);
        SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(SQL).addEntity((String) ls.get(0), claz);
        //如果需要关联
        if (addClazz.size() > 1) {
            List join = addClazz.subList(1, addClazz.size());
            for (int i = 0; i < join.size(); i++) {
                List sub = (List) join.get(i);
                query.addJoin((String) sub.get(0), (String) sub.get(1));
            }
        }
        if (startRow >= 0 && pageSize > 0) {
            query.setFirstResult(startRow);
            query.setMaxResults(pageSize);
        }
        return query.list();
    }


    /**
     * 查询多个实体，必须包含全字段
     * createSQLQuery("select {ar.*},{ca.*} from article_category ca,article_content ar where ar.id=ca.id").addEntity("ca",ArticleCategory.class).addEntity("ar",ArticleContents.class).list();
     * 将会返回List<Object[]>,每个Object[]中有一个ArticleCategory和一个ArticleContent对象[其中ArticleCategory中的一对多关联的SET集合被初始化]，亦即是说关联会多产生被关联对象，
     * Object[0]中的对象永远是第一个addEntity中类的对象，List.size=查询到的记录的条数。
     *
     * @param SQL      SQL语句，可包含分页信息，如果不要分页pageSize要设为<=0
     * @param alias    类名参数数组
     * @param startRow 记录开始行数
     * @param pageSize 获取记录数量
     * @return List
     */
    @Override
    public List findMoreBeanBySql(final String SQL, final List<List> alias, final Integer startRow, final Integer pageSize) {
        SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(SQL);
        //添加实体
        for (int i = 0; i < alias.size(); i++) {
            List sub = alias.get(i);
            query.addEntity((String) sub.get(0), (Class) sub.get(1));
        }
        if (startRow > 0 && pageSize > 0) {
            query.setFirstResult(startRow);
            query.setMaxResults(pageSize);
        }
        return query.list();
    }

    @Override
    public void excudeSQL(final String sql) {
        sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
    }

    @Override
    public <T> List<T> findObjsByHQL(final String HQL, final Integer startRow, final Integer pageSize) {
        return sessionFactory.getCurrentSession().createQuery(HQL).setFirstResult(startRow).setMaxResults(pageSize).list();
    }

    @Override
    public <T> List<T> findEntityBySQL(final String sql, final Class<T> clas) {
        return sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(clas).list();
    }

    @Override
    public <T> List<T> findEntityBySQL(String sql, Class<T> clas, Integer startRow, Integer pageSize) {
        return sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(clas).setFirstResult(startRow).setMaxResults(pageSize).list();
    }
}
