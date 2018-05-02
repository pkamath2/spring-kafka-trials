package com.pk.kafka.spring_kafka_trials.bo;

import org.apache.kafka.common.serialization.Serdes;

/**
 * Author: purnimakamath
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(Serdes.String().getClass().getName());
    }
}
