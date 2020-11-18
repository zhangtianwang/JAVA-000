package spring.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Starter {

    /**
     * Factory注入Bean
     * @param args
     */
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(StudentFactory.class);
        context.refresh();
        StudentFactory factory =  context.getBean(StudentFactory.class);
        Student student = factory.getObject();
        System.out.println(student);
    }

}
