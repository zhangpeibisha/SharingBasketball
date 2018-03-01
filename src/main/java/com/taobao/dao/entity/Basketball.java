package com.taobao.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 * <p>
 * 篮球实体
 */
@Entity
@Table(name = "basketball")
@JsonIgnoreProperties(value={"handler","hibernateLazyInitializer","order"})
public class Basketball {

    private int basketballID;

    //是否损坏 0 为正常  1损坏
    private int isBad;

    //是否出租 0 为能够出租 1为不出租
    private int isRent;

    //篮球型号
    private String model;

    //这个篮球正常使用的压力值  单位MPa
    private double pressure;

    //这个篮球实际的压力值 单位 MPa
    private double nowPerssure;

    //上架时间
    private Date createTime;

    //一个篮球只有一个出租规则 一个出租规则由多个篮球  多方 维护端
    private Rent rent;

    //一个篮球属于一个订单 被维护端
    private Order order;

    //set and get

    @Id
    @Column(name = "id", unique = true, length = 32, nullable = false)
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "identity")
    public int getBasketballID() {
        return basketballID;
    }


    @Column(name = "isBad", nullable = false)
    public int getIsBad() {
        return isBad;
    }


    @Column(name = "isRent", nullable = false)
    public int getIsRent() {
        return isRent;
    }


    @Column(name = "model", nullable = false, length = 10)
    public String getModel() {
        return model;
    }


    @Column(name = "pressure", nullable = false, length = 10)
    public double getPressure() {
        return pressure;
    }


    @Column(name = "createTime", length = 19, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }


    @OneToOne(mappedBy = "basketball", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    public Order getOrder() {
        return order;
    }

    @Column(name = "nowPerssure", length = 10)
    public double getNowPerssure() {
        return nowPerssure;
    }


    @ManyToOne(targetEntity = Rent.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "rent")
    public Rent getRent() {
        return rent;
    }


    public void setBasketballID(int basketballID) {
        this.basketballID = basketballID;
    }

    public void setIsBad(int isBad) {
        this.isBad = isBad;
    }

    public void setIsRent(int isRent) {
        this.isRent = isRent;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setNowPerssure(double nowPerssure) {
        this.nowPerssure = nowPerssure;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setRent(Rent rent) {
        this.rent = rent;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
