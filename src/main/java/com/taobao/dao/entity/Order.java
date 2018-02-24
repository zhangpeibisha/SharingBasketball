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
@Table(name = "order")
public class Order {

    @Id
    @Column(name = "id", unique = true, length = 32, nullable = false)
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "uuid")
    private String orderID;

    //借出时间就是订单创建时间
    @Column(name = "lendTime", length = 19, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lendTime;

    //归还时间
    @Column(name = "returnTime", length = 19, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnTime;

    //消费金额 默认设置位 -1 -1则为未付款
    @Column(name = "castMoney", nullable = false, length = 10 , columnDefinition = "-1")
    private double castMoney;

    //一个订单对应一个篮球 维护端
    @OneToOne
    @JoinColumn(name = "basketball")
    private Basketball basketball;

    //一个订单对应一个用户 维护端
    private User user;


    //set and get

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Date getLendTime() {
        return lendTime;
    }

    public void setLendTime(Date lendTime) {
        this.lendTime = lendTime;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public double getCastMoney() {
        return castMoney;
    }

    public void setCastMoney(double castMoney) {
        this.castMoney = castMoney;
    }

    public Basketball getBasketball() {
        return basketball;
    }

    public void setBasketball(Basketball basketball) {
        this.basketball = basketball;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
