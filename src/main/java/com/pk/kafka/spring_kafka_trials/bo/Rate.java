package com.pk.kafka.spring_kafka_trials.bo;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Author: purnimakamath
 */
public class Rate {

    private String disclaimer;
    private String license;
    private long timestamp;
    private String base;
    private JsonNode rates;

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public JsonNode getRates() {
        return rates;
    }

    public void setRates(JsonNode rates) {
        this.rates = rates;
    }
}
