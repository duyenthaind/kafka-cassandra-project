/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kafka.artifact;

import com.mycompany.entities.MessageSent;
import com.mycompany.utils.MessageUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 *
 * @author DuyenThai
 */
public class KafKaConsumer {

    private final static String TOPIC = "testDemo";
    private final static String BOOTSTRAP_SERVERS
            = "localhost:9092,localhost:9093,localhost:9094";

    public static Consumer<Long, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer" + System.currentTimeMillis());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());

        final Consumer<Long, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList(TOPIC));
        return consumer;
    }

    public List<MessageSent> runConsumer() throws InterruptedException {
        final Consumer<Long, String> consumer = createConsumer();
        List<MessageSent> out = new ArrayList<MessageSent>();

        final int giveUp = 100;
        int noRecordsCount = 0;

        while (true) {
            final ConsumerRecords<Long, String> consumerRecords = consumer.poll(100);

            if (consumerRecords.count() == 0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) {
                    break;
                } else {
                    continue;
                }
            }

            consumerRecords.forEach(record -> {
                System.out.printf("Consumer Record:(%d, %s, %d, %d)\n",
                        record.key(), record.value(),
                        record.partition(), record.offset());
                out.add(MessageUtils.decodeJson(record.value()));
            });

            consumer.commitAsync();
        }
        consumer.close();
        System.out.println("DONE");
        return out;
    }
}
