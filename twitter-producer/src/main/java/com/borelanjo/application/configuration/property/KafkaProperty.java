package com.borelanjo.application.configuration.property;

import java.util.Properties;

public class KafkaProperty {
    private final String KAFKA_BOOTSTRAP_SERVERS = "kafka.bootstrap.servers";
    private final String KAFKA_AUTO_OFFSET_RESET = "kafka.auto.offset.reset";

    private final String PROPERTY_FILENAME = "application.properties";


    private Properties prop;
    private String bootstrapServers;
    private String autoOffsetReset;

    public KafkaProperty() {
        prop = new PropertiesReader().read(PROPERTY_FILENAME);

        this.bootstrapServers = getProperty(KAFKA_BOOTSTRAP_SERVERS);
        this.autoOffsetReset = getProperty(KAFKA_AUTO_OFFSET_RESET);

    }

    private String getProperty(String key) {
        return prop.getProperty(key);
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }
}
