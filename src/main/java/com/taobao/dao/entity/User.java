package com.taobao.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.taobao.dao.databasesDaoImpl.UserDaoImpl;
import org.apache.log4j.Logger;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * Create by zhangpe0312@qq.com on 2018/2/24.
 * <p>
 * 用户实体
 */
@Entity
@Table(name = "user")
@JsonIgnoreProperties(value={"handler","hibernateLazyInitializer"})
public class User implements HttpSessionBindingListener {


    private static Logger logger = Logger.getLogger(User.class);

    @Autowired
    private UserDaoImpl userDao;

    private int userID;

    //校园卡ID  青山学院校园卡id为 12位
    private String schoolID;

    //账户密码 最少6位 最长18位
    private String password;

    //用户手机号码 长度11位
    private String phone;

    //用户注册时间
    private Date createTime;

    //默认钱为0元
    private double money;

    //一个用户拥有一个角色  维护端
    private Role role;

    //一个用户有多个订单 一个订单一个用户
    private Set<Order> orders = new HashSet<>();

    //一个用户有一个绑定的校园卡号
    private SchoolCard schooleCard;

    //set and get

    @Id
    @Column(name = "id", unique = true, length = 32, nullable = false)
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "identity")
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Column(name = "schoolID", nullable = false, length = 12, unique = true)
    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    @Column(name = "password", nullable = false, length = 32)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "phone", nullable = false, length = 11, unique = true)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "createTime", length = 19, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "money", nullable = false, length = 10)
    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @ManyToOne(targetEntity = Role.class , fetch = FetchType.LAZY)
    @JoinColumn(name = "role")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "schooleCard")
    public SchoolCard getSchooleCard() {
        return schooleCard;
    }

    public void setSchooleCard(SchoolCard schooleCard) {
        this.schooleCard = schooleCard;
    }

    /**
     * 在setAttribute时触发
     *
     * @param httpSessionBindingEvent
     */
    @Override
    public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent) {
        logger.info("用户 " + userID + " 上线了");
        //获取到该用户的权限，并放入session中
        User user = userDao.findById(userID);
        //获取角色
        Role role = user.getRole();
        //通过角色获取到权限
        Set<Permissions> permissions = role.getPermissions();
        //存入session中
        httpSessionBindingEvent.getSession().setAttribute("permissions", permissions);
    }

    /**
     * 在session销毁时触发
     * <p>
     * valueUnbound方法将被以下任一条件触发
     * <p>
     * a. 执行session.setAttribute("user", 非user对象) 时。
     * b. 执行session.removeAttribute("user") 时。
     * c. 执行session.invalidate()时。
     * d. session超时后。
     *
     * @param httpSessionBindingEvent
     */
    @Override
    public void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent) {
        logger.info("用户 " + userID + " 下线了");
    }
}
