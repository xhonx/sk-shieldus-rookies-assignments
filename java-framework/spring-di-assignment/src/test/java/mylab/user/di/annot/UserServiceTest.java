package mylab.user.di.annot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:mylab-user-di.xml")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testUserServiceInjection() {
        assertNotNull(userService);
        assertNotNull(userService.getUserRepository());
        assertEquals("MySQL", userService.getUserRepository().getDbType());
        assertNotNull(userService.getSecurityService());
    }

    @Test
    public void testRegisterUserWithPassword() {
        assertTrue(userService.registerUser("user01", "홍길동", "password123"));
    }

    @Test
    public void testRegisterUserWithoutPassword() {
        assertFalse(userService.registerUser("user02", "김철수", ""));
    }
}
