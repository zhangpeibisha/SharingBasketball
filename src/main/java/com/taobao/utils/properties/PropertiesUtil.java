package com.taobao.utils.properties;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * Create by zhangpe0312@qq.com on 2018/2/1.
 */
public class PropertiesUtil {

    //根据key读取value
    public static String readValue(String filePath,String key) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            props.load(in);
            String value = props.getProperty (key);
            System.out.println(key+":"+value);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //根据Key移除value
    public static boolean removeValue(String filePath,String key) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            props.load(in);
            OutputStream fos = new FileOutputStream(filePath);
            props.remove(key);
            props.store(fos, "Delete '" + key + "' value");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //读取properties的全部信息
    @SuppressWarnings("unchecked")
    public static List<String> readProperties(String filePath) {
        List<String> svnList=new ArrayList<String>();
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream (new FileInputStream(filePath));
            props.load(in);
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                if (key.contains("_")&&!key.contains("DAS")) {
                    svnList.add(key);
                }

                //String Property = props.getProperty (key);
                //System.out.println(key+":"+Property);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return svnList;
    }
    /***
     *
     * @param filePath
     * @return 二维数组
     * 读取所有服务svn的键值对
     */
    public static String[][] readAllProperties(String filePath) {
        Map<String,String> map = new HashMap<String, String>();
        String[][] ss=null;
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream (new FileInputStream(filePath));
            props.load(in);
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();    //key
                String Property = props.getProperty (key); //value
                map.put(key, Property);
            }
            if (null!=map) {
                Set<String> set = map.keySet();
                Iterator<String> it = set.iterator();
                ss = new String[map.size()][2];
                for (int i = 0; i < map.size(); i++) {
                    ss[i][0] = it.next();
                    ss[i][1] = map.get(ss[i][0]);
                }
                for (int i = 0; i < ss.length; i++) {
                    for (int j = 0; j < ss[i].length; j++) {
                        System.out.println(ss[i][j]+"\t");
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return ss;
    }

    //写入properties信息
    public static void writeProperties(String filePath,String parameterName,String parameterValue) {
        Properties prop = new Properties();
        try {
            InputStream fis = new FileInputStream(filePath);
            //从输入流中读取属性列表（键和元素对）
            prop.load(fis);
            //调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
            //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(filePath);
            prop.setProperty(parameterName, parameterValue);
            //以适合使用 load 方法加载到 Properties 表中的格式，
            //将此 Properties 表中的属性列表（键和元素对）写入输出流
            prop.store(fos, "Update '" + parameterName + "' value");
        } catch (IOException e) {
            System.err.println("Visit "+filePath+" for updating "+parameterName+" value error");
        }
    }
}
