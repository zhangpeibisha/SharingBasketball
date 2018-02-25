package com.taobao.dao.entity;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 * <p>
 * 角色实体
 */
@Entity
@Table(name = "role")
public class Role {

    @Id
    @Column(name = "id", unique = true, length = 32, nullable = false)
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "uuid")
    private String roleID;

    //角色名字
    @Column(name = "name", nullable = false, length = 10, unique = true)
    private String name;

    //角色描述 默认没有描述
    @Column(name = "description", nullable = false, length = 200, columnDefinition = " ")
    private String description;

    //创建时间
    @Column(name = "createTime", length = 19, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    //一个角色有多个用户 一个用户一个角色 被维护端
    @OneToMany(targetEntity=Order.class,cascade=CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name="role",updatable=false)
    private Set<User> users;

    //一个角色有多个权限 一个权限有多个角色 维护端
    @ManyToMany
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    @JoinTable(name = "role_permissions",                       //指定第三张表
            joinColumns = {@JoinColumn(name = "role")},             //本表与中间表的外键对应
            inverseJoinColumns = {@JoinColumn(name = "user")})
    private Set<Permissions> permissions = new HashSet<>();


    //set and get

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
