package com.taobao.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Create by zhangpe0312@qq.com on 2018/2/26.
 */
@Entity
@Table(name = "SchooleCard")
@JsonIgnoreProperties(value={"handler","hibernateLazyInitializer","user"})
public class SchoolCard {

    private int id;
    private String schoolID;
    private String password;
    private int money;

    //校园卡与用户一对一
    private User user;


    @Id
    @Column(name = "id", unique = true, length = 32, nullable = false)
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "identity")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "schoolID" , nullable = false , length = 12 , unique = true)
    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    @Column(name = "password" , nullable = false , length = 32 )
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "money" , nullable = false , length = 10)
    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @OneToOne(mappedBy = "schooleCard", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
