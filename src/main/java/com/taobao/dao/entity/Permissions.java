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
 *
 * 权限实体
 */
@Entity
@Table(name = "permissions")
public class Permissions {

    @Id
    @Column(name = "id", unique = true, length = 32, nullable = false)
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "uuid")
    private String permissionsID;

    //权限url
    @Column(name = "url", nullable = false, length = 200 , unique = true)
    private String url;

    //权限描述
    @Column(name = "description", nullable = false, length = 200)
    private String description;

    //创建时间
    @Column(name = "createTime", length = 19, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    //权限与角色为多对多关系  一个角色有多个权限 一个权限有多个角色
    @ManyToMany
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    @JoinTable(name = "role_permissions",                       //指定第三张表
            joinColumns = {@JoinColumn(name = "user")},             //本表与中间表的外键对应
            inverseJoinColumns = {@JoinColumn(name = "role")})
    private Set<Role> roles  = new HashSet<>();

    //set and get

    public String getPermissionsID() {
        return permissionsID;
    }

    public void setPermissionsID(String permissionsID) {
        this.permissionsID = permissionsID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
