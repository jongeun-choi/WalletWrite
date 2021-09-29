package com.clonecoin.walletwrite.adaptor;

import com.clonecoin.walletwrite.config.KafkaProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

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
    //public void sendToWalletRead()

    @PreDestroy
    public void shutdown() {
        log.info("Shutdown Kafka producer");
        producer.close();
    }
}
