package com.taobao.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 *
 * 篮球实体
 */
@Entity
@Table(name = "basketball")
public class Basketball {

    private String id;
    //是否损坏
    private boolean isBad;
    //是否出租
    private boolean isRent;
    //篮球型号
    private String model;
    //这个篮球正常使用的压力值
    private int pressure;
    //上架时间
    private Date createTime;

    //一个篮球只有一个出租规则
    private Rent rent;


}
