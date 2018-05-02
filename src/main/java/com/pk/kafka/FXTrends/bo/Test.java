package com.pk.kafka.FXTrends.bo;

import org.apache.kafka.common.serialization.Serdes;

/**
 * Author: purnimakamath
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(Serdes.String().getClass().getName());
    }
}
