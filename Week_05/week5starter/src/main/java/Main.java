import com.SchoolService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

//@EnableAutoConfiguration
public class Main {
    public static void main2(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Main.class);
        Object studentService = ctx.getBean("studentService");
        System.out.print(34343);
    }
}