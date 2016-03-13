package com.trading;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "COUNTERPARTY-SERVICE", fallback = HystrixCounterpartyClientFallback.class)
interface CounterpartyClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/exchange/mic/{micCode}")
    Exchange getExchange(@PathVariable("micCode") String micCode);

    @RequestMapping(method = RequestMethod.GET, value = "/api/party/{id}")
    String getPartyName(@PathVariable("id") String id);
}

class HystrixCounterpartyClientFallback implements CounterpartyClient {

    private static final String NOT_SET = "NOT SET";

    @Override
    public Exchange getExchange(String micCode) {
        Exchange exchange = new Exchange();

        exchange.setAcronym(NOT_SET);
        exchange.setCity(NOT_SET);
        exchange.setCountry(NOT_SET);
        exchange.setCountryCode(NOT_SET);
        exchange.setName(NOT_SET);

        return exchange;
    }

    @Override
    public String getPartyName(String id) {
        return NOT_SET;
    }
}
