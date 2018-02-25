package com.taobao.dao.entity;

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
 * 出租规则实体
 */
@Entity
@Table(name = "rent")
public class Rent {


    private String rentID;

    //押金
    private int deposit;

    //计费位按每小时多少钱算
    private double billing;

    //创建时间
    private Date createTime;


    //一个出租规则对应多个篮球 一个篮球只有一个出租规则 被维护端
    private Set<Basketball> basketballs = new HashSet<>();


    //set and get

    @Id
    @Column(name = "id", unique = true, length = 32, nullable = false)
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "uuid")
    public String getRentID() {
        return rentID;
    }

    public void setRentID(String rentID) {
        this.rentID = rentID;
    }

    @Column(name = "deposit", nullable = false, length = 10)
    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    @Column(name = "billing", nullable = false, length = 10)
    public double getBilling() {
        return billing;
    }

    public void setBilling(double billing) {
        this.billing = billing;
    }

    @Column(name = "createTime", length = 19, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "rent")
    public Set<Basketball> getBasketballs() {
        return basketballs;
    }

    public void setBasketballs(Set<Basketball> basketballs) {
        this.basketballs = basketballs;
    }
}
