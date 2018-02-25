package com.taobao.dao.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 *
 * 篮球实体
 */
@Entity
@Table(name = "basketball")
public class Basketball {

    @Id
    @Column(name = "id", unique = true, length = 32, nullable = false)
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "uuid")
    private String basketballID;

    //是否损坏
    @Column(name = "isBad", nullable = false, columnDefinition = "false")
    private boolean isBad;

    //是否出租
    @Column(name = "isRent", nullable = false, columnDefinition = "true")
    private boolean isRent;

    //篮球型号
    @Column(name = "model", nullable = false, length = 10)
    private String model;

    //这个篮球正常使用的压力值
    @Column(name = "pressure", nullable = false, length = 10)
    private int pressure;

    //上架时间
    @Column(name = "createTime", length = 19, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    //一个篮球只有一个出租规则 一个出租规则由多个篮球  多方 维护端
    @ManyToOne(targetEntity=Rent.class)
    @JoinColumn(name = "rent")
    private Rent rent;


    //一个篮球属于一个订单 被维护端
    @OneToOne(mappedBy = "basketball",cascade = CascadeType.ALL)
    private Order order;

    //set and get

    public String getBasketballID() {
        return basketballID;
    }

    public void setBasketballID(String basketballID) {
        this.basketballID = basketballID;
    }

    public boolean isBad() {
        return isBad;
    }

    public void setBad(boolean bad) {
        isBad = bad;
    }

    public boolean isRent() {
        return isRent;
    }

    public void setRent(boolean rent) {
        isRent = rent;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Rent getRent() {
        return rent;
    }

    public void setRent(Rent rent) {
        this.rent = rent;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
