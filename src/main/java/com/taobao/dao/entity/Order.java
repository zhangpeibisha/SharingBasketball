package com.taobao.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 *
 * 订单实体
 */
@Entity
@Table(name = "order")
public class Order {

    private String id;
    private Date lendTime;
    private Date returnTime;
    private int castMoney;

    //一个订单对应一个篮球
    private Basketball basketball;
    private User user;






}
