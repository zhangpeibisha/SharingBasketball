package com.taobao.dto;

import com.taobao.dao.entity.Basketball;

import java.util.Date;

/**
 * Create by zhangpe0312@qq.com on 2018/3/1.
 */
public class OrderDto {
    private int orderID;

    //借出时间就是订单创建时间
    private Date lendTime;

    //归还时间
    private Date returnTime;

    //消费金额 默认设置位 -1 -1则为未付款
    private double castMoney;

    //一个订单对应一个篮球  一个篮球对应一个订单  维护端
    private Basketball basketball;

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
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
}
