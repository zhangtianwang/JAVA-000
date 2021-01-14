package kafka;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableKafka
public class KafkaApplication {

    public static void main(String[] args)  {
        try {
            ConfigurableApplicationContext context = new SpringApplicationBuilder(KafkaApplication.class)
                    .web(WebApplicationType.NONE)
                    .run(args);
            Producer producer = context.getBean(Producer.class);
            producer.send("hello,kafka Test");
            context.getBean(Consumer.class).latch.await(10, TimeUnit.SECONDS);
            context.close();
        }catch (Exception ex){
            String msg = ex.getMessage();
        }

    }

}