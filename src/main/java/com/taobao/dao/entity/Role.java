package com.taobao.dao.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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

    @Id
    @Column(name = "id", unique = true, length = 32, nullable = false)
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "uuid")
    private String roleID;

    //角色名字
    @Column(name = "name", nullable = false, length = 10 , unique = true)
    private String name;

    //角色描述 默认没有描述
    @Column(name = "description", nullable = false, length = 200 , columnDefinition = " ")
    private String description;

    //创建时间
    @Column(name = "createTime", length = 19, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    //一个角色有多个用户
    private List<User> users ;

    //一个角色有多个权限
    private List<Permissions> permissions;





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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
    }
}
