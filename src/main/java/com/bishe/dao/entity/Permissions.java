package com.bishe.dao.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(value={"handler","hibernateLazyInitializer","roles"})
public class Permissions {


    private int permissionsID;

    //权限url
    private String url;

    //权限描述
    private String description;

    //创建时间
    private Date createTime;

    //一个权限有多个角色
    private Set<Role> roles = new HashSet<>();
    //set and get

    @Id
    @Column(name = "id", unique = true, length = 32, nullable = false)
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "identity")
    public int getPermissionsID() {
        return permissionsID;
    }

    public void setPermissionsID(int permissionsID) {
        this.permissionsID = permissionsID;
    }

    @Column(name = "url", nullable = false, length = 200 , unique = true)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "description", nullable = false, length = 200)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "createTime", length = 19, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinTable(name="permission_role",joinColumns = {@JoinColumn(name="permission")},
            inverseJoinColumns =@JoinColumn(name = "role"))
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
