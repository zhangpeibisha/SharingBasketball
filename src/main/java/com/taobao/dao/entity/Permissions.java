package com.taobao.dao.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

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
}
