package com.taobao.dao.entity;

import org.hibernate.annotations.GenericGenerator;

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

    @Id
    @Column(name = "id", unique = true, length = 32, nullable = false)
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "uuid")
    private String userID;

    //校园卡ID  青山学院校园卡id为 12位
    @Column(name = "schoolID", nullable = false, length = 12, unique = true)
    private int schoolID;

    //账户密码 最少6位 最长18位
    @Column(name = "password", nullable = false, length = 18, unique = true)
    private String password;

    //用户手机号码 长度11位
    @Column(name = "phone", nullable = false, length = 11, unique = true)
    private int  phone;

    //用户注册时间
    @Column(name = "createTime", length = 19, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    //默认钱为0元
    @Column(name = "money", nullable = false, length = 10 , columnDefinition = "0")
    private double money ;

    //一个用户拥有一个角色  维护端
    private Role role;

    //一个用户有多个订单
    private Order order;





    //set and get

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(int schoolID) {
        this.schoolID = schoolID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
