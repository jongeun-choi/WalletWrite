package com.clonecoin.walletwrite.adaptor;

import com.clonecoin.walletwrite.config.KafkaProperties;
import com.clonecoin.walletwrite.domain.event.AnalysisDTO;
import com.clonecoin.walletwrite.service.Impl.WalletServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class WalletWriteConsumer {
    private final Logger log = LoggerFactory.getLogger(WalletWriteConsumer.class);

    private final AtomicBoolean closed = new AtomicBoolean(false);

    public static final String TOPIC = "topic_analysis";

    private final KafkaProperties kafkaProperties;

    private KafkaConsumer<String, String> kafkaConsumer;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public WalletWriteConsumer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Autowired
    private WalletServiceImpl walletService;

    @PostConstruct
    public void start(){
        log.info("Kafka consumer starting ...");
        this.kafkaConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        // subscribe 할 토픽명을 지정
        kafkaConsumer.subscribe(Collections.singleton(TOPIC));

        log.info("Kafka consumer started");

        executorService.execute(()-> {
                    try {

                        while (!closed.get()){
                            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(3));
                            for(ConsumerRecord<String, String> record: records) {
                                log.info("\n\nConsumed message in {} : {}", TOPIC, record.value());
                                ObjectMapper objectMapper = new ObjectMapper();

                                AnalysisDTO analysisDTO = objectMapper.readValue(record.value(), AnalysisDTO.class);

                                walletService.updateInvestment(analysisDTO); // 매수, 매도로 변경된 총투자금액을 갱신


                            }
                        }
                        kafkaConsumer.commitSync();
                    }catch (WakeupException e){
                        if(!closed.get()){
                            throw e;
                        }

                    }catch (Exception e){
                        log.error(e.getMessage(), e);
                    }finally {
                        log.info("kafka consumer close");
                        kafkaConsumer.close();
                    }
                }
        );
    }

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public void shutdown() {
        log.info("Shutdown Kafka consumer");
        closed.set(true);
        kafkaConsumer.wakeup();
    }


}
