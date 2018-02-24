package com.taobao.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 *
 * 出租规则实体
 */
@Entity
@Table(name = "rent")
public class Rent {

    private String id;
    //押金
    private int deposit;
    //计费位按每小时多少钱算
    private int billing;
    //创建时间
    private Date createTime;



}
