package test.com.taobao.dao.databasesDaoImpl;

import com.taobao.dao.databasesDaoImpl.UserDaoImpl;
import com.taobao.dao.entity.User;
import org.junit.jupiter.api.Test;
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

    @Test
    public void testFindByCriteria() throws Exception {
        User user = new User();
        user.setCreateTime(new Date());
        userDao.save(user);

    }

    /**
     * Method: findByCriteriaCount(T object)
     */
    @Test
    public void testFindByCriteriaCount() throws Exception {
//TODO: Test goes here... 
    }


} 
