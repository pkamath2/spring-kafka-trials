package com.pk.kafka.FXTrends.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pk.kafka.FXTrends.bo.Rate;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Author: purnimakamath
 */
@Component
public class RatesPoller {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    @Scheduled(cron = "*/10 * * * * *")
    public void pollRates(){

        StringBuilder sb = new StringBuilder();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("data/23-04-2018")))){
            String s = bufferedReader.readLine();
            while(s!=null){
                sb.append(s);
                s = bufferedReader.readLine();
            }
            Rate rate = objectMapper.readValue(sb.toString(), Rate.class);

//            ProducerRecord<String, String> producerRecordSGD = new ProducerRecord<String, String>("FX_RATES","SGD",
//                    "{'date':"+rate.getTimestamp()+",'rate':"+rate.getRates().get("SGD")+"}");
//            ProducerRecord<String, String> producerRecordINR = new ProducerRecord<String, String>("FX_RATES","INR",
//                    "{'date':"+rate.getTimestamp()+",'rate':"+rate.getRates().get("INR")+"}");

            ProducerRecord<String, Rate> producerRecord = new ProducerRecord<String, Rate>("FX_RATES","CURRENCY", rate);

            kafkaTemplate.send(producerRecord);
        }catch(FileNotFoundException fnf){
            fnf.printStackTrace();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

    }

}
