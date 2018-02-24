package com.taobao.dao.databasesDao;

import org.hibernate.ScrollableResults;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 * <p>
 * 功能：超级DAO接口，通过使用泛型，将所有的dao操作定义;
 * iterator方法操作大量数据时会溢出，注意使用情景，使用srcoll可以获取比较好的性能
 * 如果dml操作涉及大量数据且会填充2级缓存时，在此方法中执行sql语句前必须关闭2级缓存，语句执行后清除缓存
 */
public interface SupperBaseDAO<T> {

    /**
     * 保存
     *
     * @param object 需保存对象
     * @return 影响行数
     */
     Serializable save(final T object);

    /**
     * 更新对象持久化信息，必须带有主键
     *
     * @param object 需更新对象
     */
     void update(final T object);

    /**
     * 删除对象持久化信息，必须带有主键
     *
     * @param object 需删除对象
     */
     void delete(final T object);

    /**
     * 使用SQL语句删除记录
     *
     * @param id id
     */
     void delete(final Serializable id);

    /**
     * 根据Id查找对象
     *
     * @param id id
     * @return T
     */
     <T> T findById(final Serializable id);

    /**
     * 根据Id查找对象
     *
     * @param id id
     * @return T
     */
     <T> T loadById(final Serializable id);

    /**
     * 根据唯一属性get对象
     *
     * @param propertyName 属性名称
     * @param value        属性值
     * @return T
     */
     <T> T findByProperty(final String propertyName, final Object value);

    /**
     * 根据唯一属性值对获取对象
     *
     * @param propertyNames 属性名称数组
     * @param values        属性值数组
     * @param <T>           泛型
     * @return T
     */
     <T> T findByProperties(final String[] propertyNames, final Object[] values);

    /**
     * 查询实体
     *
     * @param object
     * @param <T>
     * @return
     */
     <T> T findByEntity(final T object) throws IllegalAccessException;

    /**
     * 同步并清空数据库连接session缓存
     */
     void flushANDclear();

    /**
     * 强制清空缓存
     */
     void clean();

    /**
     * 强制同步到数据库
     */
     void flush();

    /**
     * 查找所有对象
     * 数据量很大时，此方法会溢出
     *
     * @return List<T>
     */
     <T> List<T> findAll();

    /**
     * 分页查询所有记录,HQL
     *
     * @param propertyName 排序属性名称
     * @param desc         是否倒序
     * @param startRow     起始记录
     * @param pageSize     分页大小
     * @param <T>
     * @return
     */
     <T> List<T> findAllByPage(final String propertyName, final boolean desc, final Integer startRow, final Integer pageSize);

    /**
     * 查询类对应的所有记录行数
     *
     * @return Long
     */
     Long findAllCount();

    /**
     * 查询所有对象,对于数据量很大时，此方法会溢出
     *
     * @param <T> 泛型
     * @return 所有记录
     */
     <T> List<T> findAllIterator();

    /**
     * 使用hibernate的iterator方法分页查询，对于2级缓存中已存在大量需查询对象，或预期查询结果包含大量相同对象
     * 或存在很多不使用对象时此方法可提升性能
     *
     * @param HQL      HQL查询语句
     * @param startRow 开始行数
     * @param pageSize 页码
     * @param <T>      泛型
     * @return List
     */
     <T> List<T> batchGetDataIteratorByHQL(final String HQL, final Integer startRow, final Integer pageSize);

    /**
     * 游标查询，大量数据时会获得较高性能
     * 使用HQL语句进行查询，使用游标，
     *
     * @param HQL      HQL查询语句
     * @param startRow 起始行
     * @param pageSize 要查询多少行
     * @return ScrollableResults
     */
     ScrollableResults batchGetDataScrollByHQL(final String HQL, final Integer startRow, final Integer pageSize);

    /**
     * 游标查询，大量数据时会获得较高性能
     * 使用SQL语句进行查询，使用游标，
     *
     * @param SQL      SQL查询语句
     * @param startRow 起始行
     * @param pageSize 要查询多少行
     * @return ScrollableResults
     */
     ScrollableResults batchGetDataScrollBySQL(final String SQL, final Integer startRow, final Integer pageSize);

    /**
     * HQL查询总数
     *
     * @param HQL HQL语句
     * @return 记录数
     */
     Long findCountByHQL(final String HQL);

    /**
     * 用HQL语句获取对象列表
     *
     * @param HQL      HQL语句
     * @param startRow 起始行数
     * @param pageSize 分页大小
     * @param <T>
     * @return
     */
     <T> List<T> findObjsByHQL(final String HQL, final Integer startRow, final Integer pageSize);

    /**
     * SQL查询总数
     *
     * @param SQL
     * @return
     */
     Long findCountBySQL(final String SQL);

    /**
     * 根据属性分页查询对象链表，数组实现,HQL
     *
     * @param propertyNames
     * @param values
     * @param startRow
     * @param pageSize
     * @param sortColumn
     * @param isDesc
     * @return
     */
     <T> List<T> findByProperty(final String[] propertyNames, final Object[] values, final Integer startRow, final Integer pageSize, final String sortColumn, final boolean isDesc);

    /**
     * 获取记录总数
     *
     * @param propertyNames
     * @param values
     * @return
     */
     Long findByPropertyCount(final String[] propertyNames, final Object[] values);

    /**
     * 根据一个属性值分页查询对象链表,HQL
     *
     * @param propertyName
     * @param value
     * @param startRow
     * @param pageSize
     * @param sortColumn
     * @param isDesc
     * @return
     */
     <T> List<T> findByProperty(final String propertyName, final Object value, final Integer startRow, final Integer pageSize, final String sortColumn, final boolean isDesc);

    /**
     * 获取记录总数
     *
     * @param propertyName
     * @param value
     * @return
     */
     Long findByPropertyCount(final String propertyName, final Object value);

    /**
     * 根据对象分页查询对象链表,HQL
     *
     * @param object
     * @param startRow
     * @param pageSize
     * @param sortColumn
     * @param isDesc
     * @return
     */
     <T> List<T> findByProperty(final T object, final Integer startRow, final Integer pageSize, final String sortColumn, final boolean isDesc);

    /**
     * 查询记录总数
     *
     * @param object
     * @return
     */
     Long findByPropertyCount(final T object);

    /**
     * 根据属性值分页查询对象链表，map实现,HQL
     *
     * @param map
     * @param startRow
     * @param pageSize
     * @param sortColumn
     * @param isDesc
     * @return
     */
     <T> List<T> findByProperty(final Map<String, Object> map, final Integer startRow, final Integer pageSize, final String sortColumn, final boolean isDesc);

    /**
     * 查询记录总数
     *
     * @param map
     * @return
     */
     Long findByPropertyCount(final Map<String, Object> map);

    /**
     * 通过sql语句查询对象链表
     *
     * @param sql
     * @param values
     * @return
     */
     <T> List<T> findBySql(final String sql, final Object... values);

    /**
     * 查询记录总数
     *
     * @param sql
     * @param values
     * @return
     */
     Long findBySqlCount(final String sql, final Object... values);

    /**
     * 通过SQL语句查询唯一对象
     *
     * @param sql
     * @param values
     * @return
     */
     <T> T findUniqueBySql(final String sql, final Object... values);

    /**
     * 通过SQL语句批量更新或删除
     *
     * @param sql
     * @param values
     * @return
     */
     int batchUpdateOrDelete(final String sql, final Object... values);

    /**
     * 通过HQL语句执行批量插入
     *
     * @param objects
     * @param size
     */
     void batchInsertByHQL(final List<T> objects, final Integer size);

    /**
     * 使用SQL语句执行批量插入
     *
     * @param sql
     * @param values
     */
     void batchInsertBySQL(final String sql, final List<Object[]> values);

    /**
     * 使用HQL查询返回MAP
     *
     * @param HQL
     * @param startRow
     * @param pageSize
     * @return
     */
     List<Map> getMapByHQL(final String HQL, final Integer startRow, final Integer pageSize);

    /**
     * 使用HQL查询返回List
     *
     * @param HQL
     * @param startRow
     * @param pageSize
     * @return
     */
     List<List> getListByHQL(final String HQL, final Integer startRow, final Integer pageSize);

    /**
     * 使用SQL查询返回List,List中存放对象
     *
     * @param SQL
     * @return
     */
     <T> List<T> getListBySQL(final String SQL);

    /**
     * 使用SQL查询返回List，List中存放结果集数组
     *
     * @param SQL
     * @return
     */
     List<Object[]> getListObjBySQL(final String SQL);

    /**
     * 使用SQL查询返回MAP
     *
     * @param SQL
     * @return
     */
     <T> List<Map> getMapBySQL(final String SQL);

    /**
     * 模糊查询
     *
     * @param object   模糊查询对象
     * @param startRow 开始行数
     * @param pageSize 每页条数
     * @param <T>      泛型
     * @return List<T>
     */
     <T> List<T> findByCriteria(final T object, final Integer startRow, final Integer pageSize);

    /**
     * 模糊查询记录总数
     *
     * @param object
     * @param <T>
     * @return
     */
     <T> Long findByCriteriaCount(final T object);

    /**
     * 此方法适用于需要返回对像和与对象有关联的其他对象
     *
     * @param SQL
     * @param addClazz
     * @return
     */
     List findBySqlWithJoin(final String SQL, final List<List> addClazz, final Integer startRow, final Integer pageSize);

    /**
     * 此方法返回多个对象
     *
     * @param SQL
     * @param alias
     * @return
     */
     List findMoreBeanBySql(final String SQL, final List<List> alias, final Integer startRow, final Integer pageSize);

    /**
     * 使用SQL查询非受管实体，一般指DTO
     *
     * @param SQL      sql语句
     * @param claz     返回对象的类
     * @param startRow 起始数据
     * @param pageSize 获取数据量,大于0将使用分页，否则不会使用分页
     * @return List
     */
     <T> List<T> getListBySQL(final String SQL, final Class<T> claz, final Integer startRow, final Integer pageSize);

    /**
     * 执行SQL语句
     *
     * @param sql sql语句
     */
     void excudeSQL(final String sql);

    /**
     * 使用SQL查询单实体链表，不需要分页时使用
     *
     * @param sql sql语句
     * @param <T>
     * @return
     */
     <T> List<T> findEntityBySQL(final String sql, final Class<T> clas);

    /**
     * 使用SQL查询单实体链表，分页时使用
     *
     * @param sql      sql语句
     * @param clas     查询实体类
     * @param startRow 开始记录
     * @param pageSize 分页大小
     * @param <T>
     * @return List
     */
     <T> List<T> findEntityBySQL(final String sql, final Class<T> clas, final Integer startRow, final Integer pageSize);

}
