package com.taobao.dao.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 *
 * 用户实体
 */
@Entity
@Table(name = "user")
public class User {

    private String id;
    //校园卡ID  青山学院校园卡id为 12位
    private int schoolID;
    //账户密码 最少6位 最长18位
    private String password;
    //用户手机号码 长度11位
    private int  phone;
    //用户注册时间
    private Date createTime;
    //默认钱为0元
    private double money = 0;

    //一个用户借用多个篮球
    List<Basketball> basketballs;
    //一个用户拥有一个角色
    private Role role;




}
