import com.taobao.dao.databasesDaoImpl.RoleDaoImpl;
import com.taobao.dao.databasesDaoImpl.UserDaoImpl;
import com.taobao.dao.entity.Role;
import com.taobao.dao.entity.User;
import com.taobao.utils.sign.MD5;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * UserDaoImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>二月 25, 2018</pre>
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring.xml"})
public class UserDaoImplTest {
    /**
     * Method: findByCriteria(T object, Integer startRow, Integer pageSize)
     */
    @Autowired
    UserDaoImpl userDao;

    @Autowired
    RoleDaoImpl roleDao;

    @Autowired
    MD5 md5;

    @Test
    public void testFindByCriteria() throws Exception {

        //生成角色
        Role role = new Role();
        role.setCreateTime(new Date());
        role.setDescription("ceshi ");
        role.setName("ceshi001");
        roleDao.save(role);

        //生成用户信息
        User user = new User();

        //加密密码
        String password = "1234567897899";
        String passwordMd5 = md5.encryption(password).toLowerCase();
        //使用加密码
        user.setPassword(passwordMd5);
        user.setRole(roleDao.findByProperty("name","ceshi001"));
        user.setCreateTime(new Date());
        user.setPhone("18203085236");
        user.setSchoolID("201410610113");
        user.setMoney(0);
        userDao.save(user);

    }


} 
