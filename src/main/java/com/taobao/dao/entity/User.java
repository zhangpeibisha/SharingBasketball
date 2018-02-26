package com.taobao.dao.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 *
 * 用户实体
 */
@Entity
@Table(name = "user")
public class User {


    private String userID;

    //校园卡ID  青山学院校园卡id为 12位
    private String schoolID;

    //账户密码 最少6位 最长18位
    private String password;

    //用户手机号码 长度11位
    private String  phone;

    //用户注册时间
    private Date createTime;

    //默认钱为0元
    private double money ;

    //一个用户拥有一个角色  维护端
    private Role role;

    //一个用户有多个订单 一个订单一个用户
    private Set<Order> orders = new HashSet<>();

    //set and get

    @Id
    @Column(name = "id", unique = true, length = 32, nullable = false)
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "uuid")
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Column(name = "schoolID", nullable = false, length = 12, unique = true)
    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    @Column(name = "password", nullable = false , length = 32 , unique = true)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "phone", nullable = false, length = 11, unique = true)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "createTime", length = 19, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "money", nullable = false, length = 10)
    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @ManyToOne(targetEntity = Role.class)
    @JoinColumn(name="role")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
