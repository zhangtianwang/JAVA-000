package kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Consumer {

    public final CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = "${kafka.topic}")
    public void listen(String foo) {
        System.out.println("Received: " + foo);
        this.latch.countDown();
    }

}
