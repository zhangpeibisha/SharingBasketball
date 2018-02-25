import com.taobao.dao.databasesDaoImpl.RoleDaoImpl;
import com.taobao.dao.databasesDaoImpl.UserDaoImpl;
import com.taobao.dao.entity.Role;
import com.taobao.dao.entity.User;
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


    @Test
    public void testFindByCriteria() throws Exception {
       Role role = new Role();
       role.setCreateTime(new Date());
       role.setDescription("ceshi ");
       role.setName("ceshi001");
       roleDao.save(role);

    }


} 
