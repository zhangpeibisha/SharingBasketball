package com.taobao.dao.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 *
 * 订单实体
 */
@Entity
@Table(name = "order_basketball_user")
public class Order {


    private int orderID;

    //借出时间就是订单创建时间
    private Date lendTime;

    //归还时间
    private Date returnTime;

    //消费金额 默认设置位 -1 -1则为未付款
    private double castMoney;

    //一个订单对应一个篮球  一个篮球对应一个订单  维护端
    private Basketball basketball;

    //一个订单对应一个用户  一个用户对应多个订单 维护端 ok
    private User user;


    //set and get

    @Id
    @Column(name = "id", unique = true, length = 32, nullable = false)
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "identity")
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    @Column(name = "lendTime", length = 19, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLendTime() {
        return lendTime;
    }

    public void setLendTime(Date lendTime) {
        this.lendTime = lendTime;
    }

    @Column(name = "returnTime", length = 19, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    @Column(name = "castMoney", nullable = false, length = 10)
    public double getCastMoney() {
        return castMoney;
    }

    public void setCastMoney(double castMoney) {
        this.castMoney = castMoney;
    }

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name="user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToOne
    @JoinColumn(name = "basketball")
    public Basketball getBasketball() {
        return basketball;
    }

    public void setBasketball(Basketball basketball) {
        this.basketball = basketball;
    }
}
