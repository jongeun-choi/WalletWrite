package com.clonecoin.walletwrite.adaptor;

import com.clonecoin.walletwrite.config.KafkaProperties;
import com.clonecoin.walletwrite.domain.event.WalletDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutionException;

public class WalletWriteProducerImpl implements WalletWriteProducer {

    private final Logger log = LoggerFactory.getLogger(WalletWriteProducerImpl.class);

    private static final String TOPIC_WALLETREAD = "topic_walletRead";

    private final KafkaProperties kafkaProperties;

    private final static Logger logger = LoggerFactory.getLogger(WalletWriteProducerImpl.class);
    private KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WalletWriteProducerImpl(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize() {
        log.info("Kafka producer initializing...");
        this.producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        log.info("Kafka producer initialized");
    }

    // 1일 기준 수익률 walletRead 서버로 전송
    public void sendToWalletRead(WalletDTO walletDTO) throws ExecutionException, InterruptedException, JsonProcessingException {
        String message = objectMapper.writeValueAsString(walletDTO);
        producer.send(new ProducerRecord<>(TOPIC_WALLETREAD, message));
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutdown Kafka producer");
        producer.close();
    }
}
