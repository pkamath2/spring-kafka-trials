package com.pk.kafka.spring_kafka_trials.controller;

import com.pk.kafka.spring_kafka_trials.listener.RatesProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author: purnimakamath
 */

@Controller
public class RestController {

    @Autowired
    RatesProcessor ratesProcessor;

    @RequestMapping(method = RequestMethod.GET, path = "/rate/{currency}")
    public @ResponseBody String rate(@PathVariable String currency){

        System.out.println("Getting Latest for currency :"+currency);
        return ratesProcessor.getLatestRateByCurrency(currency);
    }
}
