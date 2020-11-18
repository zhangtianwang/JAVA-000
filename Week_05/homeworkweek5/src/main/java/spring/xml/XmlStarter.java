package spring.xml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * XML注入
 */
public class XmlStarter {

    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
            context.refresh();
            School school = context.getBean(School.class);
            System.out.println(school);
        }catch (Exception ex){
            String msg = ex.getMessage();
        }

    }

}
