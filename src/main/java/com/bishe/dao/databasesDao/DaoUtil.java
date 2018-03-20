package com.bishe.dao.databasesDao;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 *
 * 功能或描述：dao辅助类，提供一些通用功能
 */
public class DaoUtil {
    protected static Logger logger = Logger.getLogger(DaoUtil.class);
    /**
     * 获取对象对应的表名
     *
     * @param object 需获取表名对象
     * @return 类对应的表名
     */
    public static String getTableName(final Object object) {
        Annotation[] annotations = object.getClass().getAnnotations();
        String tableName = "";
        for (Annotation annotation : annotations) {
            if (annotation instanceof Table) {
                tableName = ((Table) annotation).name();
            }
        }
        return tableName;
    }

    /**
     * 获取当前类对应的数据表名称
     *
     * @return 表名称
     */
    public static String getTableName(Class clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        String tableName = "";
        for (Annotation annotation : annotations) {
            if (annotation instanceof Table) {
                tableName = ((Table) annotation).name();
            }
        }
        return tableName;
    }

    /**
     * 获取类的所有属性名称
     *
     * @param clazz 类
     * @return 属性名称数组
     */
    public static String[] getClassPropertiesNames(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        final int length = fields.length;
        String[] columnNames = new String[length];
        for (int i = 0; i < length; i++) {
            columnNames[i] = fields[i].getName();
        }
        return columnNames;
    }

    /**
     * 获取对象的属性名称数组
     *
     * @param object 需获取属性名称的对象
     * @return 属性名称
     */
    public static String[] getBeanProperties(final Object object) {
        //获取Object对应的类中的所有属性名称,不包含继承
        Class clazz = object.getClass();
        return DaoUtil.getClassPropertiesNames(clazz);
    }

    /**
     * 获取类对应的表的字段名称
     *
     * @param clazz 类
     * @return 字段名称数组
     * @throws java.beans.IntrospectionException
     */
    public static String[] getClassTableColumnName(Class clazz) throws IntrospectionException {
        Field[] fields = clazz.getDeclaredFields();//get array of properties
        //loop fields to get getmethod annotation
        List<String> columnNames = new ArrayList();
        for (Field field : fields) {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), clazz);
            Method method = propertyDescriptor.getReadMethod();
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Column) {
                    columnNames.add(((Column) annotation).name());
                }
                if (annotation instanceof JoinColumn) {
                    columnNames.add(((JoinColumn) annotation).name());
                }
            }
        }
        String[] names = new String[columnNames.size()];
        names = columnNames.toArray(names);
        return names;
    }

    /**
     * 获取对象的模板类对应的数据库表的字段名称数组
     *
     * @param object 对象
     * @return 字段数组
     * @throws IntrospectionException
     */
    public static String[] getBeanTableColumnNames(final Object object) throws IntrospectionException {
        Class clazz = object.getClass();
        return DaoUtil.getClassTableColumnName(clazz);
    }

    /**
     * 根据传入的StringBuffer和对称数组组装HQL语句query对象
     *
     * @param sessionFactory session工厂
     * @param stringBuffer  obj where前的查询语句
     * @param propertyNames 属性名数组
     * @param values        属性值数组
     * @param sortColumn    排序字段
     * @param isAsc        是升序
     * @return Query
     */
    public static Query createConditionQuery(SessionFactory sessionFactory,StringBuffer stringBuffer, final String[] propertyNames, final Object[] values, final String sortColumn, final boolean isAsc) {
        //生成带参数的HQL
        DaoUtil.fillParameters(stringBuffer, propertyNames);
        //排序
        String desc = isAsc ? "asc" : "desc";
        stringBuffer.append("order by obj.").append(sortColumn).append(" " + desc);
        //填充实参
        Query query = DaoUtil.fillValues(sessionFactory,stringBuffer, propertyNames, values);
        return query;
    }

    /**
     * 填充参数
     *
     * @param stringBuffer  obj where之前语句生成的stringBuffer
     * @param propertyNames 属性名称数组
     * @return StringBuffer
     */
    public static StringBuffer fillParameters(final StringBuffer stringBuffer, final String[] propertyNames) {
        stringBuffer.append(" obj where ");
        //生成带参数的HQL
        for (int i = 0; i < propertyNames.length; i++) {
            //第一个条件
            if (i == 0)
                stringBuffer.append("obj.").append(propertyNames[i]).append("=:").append(propertyNames[i] + " ");
            //第二个开始的条件
            if (i != 0)
                stringBuffer.append("and obj.").append(propertyNames[i]).append("=:").append(propertyNames[i] + " ");
        }
        //生成查询语句
        logger.info("组装的HQL：" + stringBuffer.toString());
        return stringBuffer;
    }

    /**
     * 填充实参
     *
     * @param sessionFactory session工厂
     * @param stringBuffer  HQL语句
     * @param propertyNames 属性名称数组
     * @param values        属性值数组
     * @return Query
     */
    public static Query fillValues( SessionFactory sessionFactory,final StringBuffer stringBuffer, final String[] propertyNames, final Object[] values) {
        Query query = sessionFactory.getCurrentSession().createQuery(stringBuffer.toString());
        //设置参数值
        final int n = propertyNames.length;
        for (int i = 0; i < n; i++) {
            query.setParameter(propertyNames[i], values[i]);
        }
        return query;
    }

    /**
     * 组装HQL语句，单属性值对,按指定字段排序
     *
     * @param stringBuffer obj where前的可变字符串
     * @param propertyName 属性名称
     * @param value        值
     * @param sortColumn   排序字段
     * @param isAsc       是否升序
     * @return Query
     */
    public static Query createSingleConditionHqlQuery(SessionFactory sessionFactory,StringBuffer stringBuffer, final String propertyName, final Object value, final String sortColumn, final boolean isAsc) {
        //组装HQL
        String desc = isAsc ? "asc" : "desc";
        stringBuffer.append(" obj where obj.").append(propertyName).append("=:").append(propertyName).append(" order by obj.").append(sortColumn).append(" " + desc);
        logger.info("组装的HQL：" + stringBuffer.toString());
        Query query = sessionFactory.getCurrentSession().createQuery(stringBuffer.toString());
        //设置参数
        query.setParameter(propertyName, value);
        return query;
    }

    /**
     * 获取对象的非空属性名称和属性值对应的map
     *
     * @param object 对象
     * @param <T>    泛型参数
     * @return map
     */
    public static <T> Map<String, Object> getBeanPropertiesAndValues(final T object) throws IllegalAccessException {
        //获取泛型对应的类中的所有属性名称
        Field[] fields = object.getClass().getDeclaredFields();
        Map<String, Object> map = new HashMap();
        Map<String, Object> returnMap = new HashMap();
        //获取属性名称和值对应的map
        for (Field field : fields) {
            //第一个属性名称
            String propertyName = field.getName();
            //获取属性的访问检查控制
            boolean isAccess = field.isAccessible();
            //取消访问检查以便可以通过反射获取属性值
            field.setAccessible(true);
            //获取属性值
            Object value = field.get(object);
            //放入map
            map.put(propertyName, value);
            //恢复访问检查控制
            field.setAccessible(isAccess);
        }
        Iterator iterator = map.keySet().iterator();
        //循环判断值是否为空
        while (iterator.hasNext()) {
            //获取键名称
            String keyName = (String) iterator.next();
            //获取非空值
            if (map.get(keyName) != null) {
                returnMap.put(keyName, map.get(keyName));
            }
        }
        return returnMap;
    }

    /**
     * 获取map中的键值数组对
     *
     * @param map MAP<String propertyName,Object value>
     * @return List<Object[]> length=2
     */
    public static List<Object[]> changeMapToArray(Map map) {
        //获取key集合
        Set keySet = map.keySet();
        //获取属性名称数组
        String[] propertyNames = new String[keySet.size()];
        propertyNames = (String[]) keySet.toArray(propertyNames);
        //获取属性名称对应的属性值
        final int length = propertyNames.length;
        Object[] values = new Object[length];
        for (int i = 0; i < length; i++) {
            values[i] = map.get(propertyNames[i]);
        }
        //返回数组对
        List<Object[]> list = new ArrayList();
        list.add(propertyNames);
        list.add(values);
        return list;
    }

    /**
     * 组装成List<List>，将obj数组按每组size个参数分组
     *
     * @param size inner List的长度
     * @param obj  不定长参数
     * @return List
     */
    public static List<List> assemblingList(final Integer size, final Object... obj) throws Exception {
        int length = obj.length;
        if (length % size != 0) {
            throw new Exception("不能按组分配 length " + length + " size " + size);
        } else {
            int outerLoop = obj.length / size;
            List<List> outerList = new ArrayList(outerLoop);
            for (int i = 0; i < outerLoop; i++) {
                List innerList = new ArrayList(size);
                for (int j = i; j < i + size; j++) {
                    innerList.add(obj[j]);
                }
                outerList.add(innerList);
            }
            return outerList;
        }
    }
}
