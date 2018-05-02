package com.pk.kafka.spring_kafka_trials.listener;

import com.pk.kafka.spring_kafka_trials.bo.Rate;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.core.StreamsBuilderFactoryBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: purnimakamath
 */
@Configuration
public class RatesProcessor {

    @Autowired
    private Environment env;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public StreamsConfig kStreamsConfigs() {
        Map<String, String> props = getAllKnownProperties(this.env);
        return new StreamsConfig(props);
    }

    @Bean(name = "RatesBuilder")
    public StreamsBuilderFactoryBean kStreamBuilder(StreamsConfig streamsConfig) {
        return new StreamsBuilderFactoryBean(streamsConfig);
    }

    @Autowired
    @Qualifier("RatesBuilder")
    private StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @Bean
    public KStream<String, Rate> createRatesStream(@Qualifier("RatesBuilder") StreamsBuilder streamsBuilder){
        KStream<String, Rate> rateKStream = streamsBuilder.stream("FX_RATES");

        rateKStream.flatMap((s, rate) -> {

            List<KeyValue<String, String>> result = new ArrayList<>();
            String sgd = rate.getRates().get("SGD").asText();
            String inr = rate.getRates().get("INR").asText();

            result.add(new KeyValue<>("SGD", sgd));
            result.add(new KeyValue<>("INR", inr));

            return result;

        }).to("FX_RATES_CURR");

        return rateKStream;
    }

    @Bean
    public KTable<String, String> createRatesTable(@Qualifier("RatesBuilder") StreamsBuilder streamsBuilder){

        KTable<String, String> ratesTable = streamsBuilder.table("FX_RATES_CURR",
                Consumed.with(Serdes.String(), Serdes.String()),
                Materialized.as("LATEST_RATE_STORE"));


        return ratesTable;
    }


    public String getLatestRateByCurrency(String currency){

        ReadOnlyKeyValueStore<String, String> currencyStore = streamsBuilderFactoryBean.getKafkaStreams().store("LATEST_RATE_STORE",
                QueryableStoreTypes.keyValueStore());
        return currencyStore.get(currency);

    }

    //Spring Jira: https://jira.spring.io/browse/SPR-10241
    //Converting Spring Enviroment to Properties Map

    public static Map<String, String> getAllKnownProperties(Environment env) {
        Map<String, String> rtn = new HashMap<>();
        if (env instanceof ConfigurableEnvironment) {
            for (PropertySource<?> propertySource : ((ConfigurableEnvironment) env).getPropertySources()) {
                if (propertySource instanceof EnumerablePropertySource) {
                    for (String key : ((EnumerablePropertySource) propertySource).getPropertyNames()) {
                        rtn.put(key, propertySource.getProperty(key).toString());
                    }
                }
            }
        }
        return rtn;
    }


    // Uncomment for simple Kafka Listener (w/o Streams)
    // @KafkaListener(topics = "FX_RATES")
//    public void listen(ConsumerRecord<String, Rate> consumerRecord){
//        System.out.println("Received:: "+ consumerRecord.key()+"->"+consumerRecord.value());
//    }
}