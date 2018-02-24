package com.taobao.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 *
 * 权限实体
 */
@Entity
@Table(name = "permissions")
public class Permissions {

    private String id;
    private String url;
    private String description;
    private Date createTime;




}
