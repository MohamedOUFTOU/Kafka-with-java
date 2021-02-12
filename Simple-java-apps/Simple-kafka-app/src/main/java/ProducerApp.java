import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ProducerApp {
    public static void main(String[] args) {
        AtomicReference<Integer> count = new AtomicReference<>(0);
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.put(ProducerConfig.CLIENT_ID_CONFIG,"client-producer-1");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        KafkaProducer<Integer,String> kafkaProducer = new KafkaProducer<Integer, String>(properties);

        Random random = new Random();
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(()->{
            count.updateAndGet(v -> v + 1);
            String value = String.valueOf(random.nextDouble()*999);
            kafkaProducer.send(new ProducerRecord<Integer,String>("test",count.get(),value),(metadata,exception)->{
                System.out.println("Sending message:"+value+";Partition="+metadata.partition());
            });
        },1000,1000, TimeUnit.MILLISECONDS);
    }
}
