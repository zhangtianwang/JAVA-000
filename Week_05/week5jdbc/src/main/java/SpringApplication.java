
import com.StudentService;
import jdbc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;


@Configuration
@ComponentScan
public class SpringApplication {

    @Autowired
    private StudentService studentService;

    public static void main(String[] args) {
        try {
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            context.register(UserDao.class);
            context.refresh();

            SpringApplication application = new SpringApplication();
            application.test();

            try {
                InsertWithPool(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<User> list = getAll(context);
            System.out.println(list);
        }catch (Exception ex){
            String msg = ex.getMessage();
        }

    }

    private void test(){
        studentService.say();
    }

    private static void insert(ApplicationContext context) {
        try {
            UserDao userDao = context.getBean(UserDao.class);
            User user = new User();
            user.setName("张天王");
            user.setAge(20);
            user.setCreateTime(new Date());
            userDao.insert(user);
        } catch (Exception ex) {
            String msg = ex.getMessage();
        }
    }

    /**
     * 事务的插入
     * @param context
     */
    private static void insertWithTransaction(ApplicationContext context) {
        try {
            UserDao userDao = context.getBean(UserDao.class);
            User user = new User();
            user.setName("张天王");
            user.setAge(20);
            user.setCreateTime(new Date());
            userDao.insertWithTransaction(user);
        } catch (Exception ex) {
            String msg = ex.getMessage();
        }
    }

    /**
     * 批量插入
     * @param context
     */
    private static void batchInsert(ApplicationContext context) {
        try {
            UserDao userDao = context.getBean(UserDao.class);
            User user = new User();
            user.setName("张小二");
            user.setAge(30);
            user.setCreateTime(new Date());
            userDao.batchInsert(user);
        } catch (Exception ex) {
            String msg = ex.getMessage();
        }
    }

    /**
     * 使用连接池插入
     * @param context
     */
    private static void InsertWithPool(ApplicationContext context) {
        try {
            UserDao userDao = context.getBean(UserDao.class);
            User user = new User();
            user.setName("张小二");
            user.setAge(30);
            user.setCreateTime(new Date());
            userDao.insertWithPool(user);
        } catch (Exception ex) {
            String msg = ex.getMessage();
        }
    }

    private static List<User> getAll(ApplicationContext context) {
        List<User> userList = null;
        try {
            UserDao userDao = context.getBean(UserDao.class);
            userList = userDao.getAll();
        } catch (Exception ex) {
            String msg = ex.getMessage();
        }
        return userList;
    }


}
