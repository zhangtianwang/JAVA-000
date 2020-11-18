package spring.config;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * JavaConfig注入
 */
public class Starter {

    public static void main(String[] args) {
        try {
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            context.register(JavaConfig.class);
            context.refresh();
            Student student = context.getBean(Student.class);
            School school = context.getBean(School.class);
            System.out.println(student);
            System.out.println(school);
        }catch (Exception ex){
            String msg = ex.getMessage();
        }

    }

}
