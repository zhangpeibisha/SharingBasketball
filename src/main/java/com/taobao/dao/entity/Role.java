package com.taobao.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 *
 * 角色实体
 */
@Entity
@Table(name = "role")
public class Role {

    private String id;
    //角色名字
    private String name;
    //角色描述
    private String description;
    //创建时间
    private Date createTime;

    //一个角色有多个用户
    List<User> users ;
    //一个角色有多个权限
    List<Permissions> permissions;

}
