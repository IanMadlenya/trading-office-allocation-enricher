package com.trading;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;

@FeignClient(name = "MARKET-DATA-SERVICE", fallback = HystrixMarketDataClientFallback.class)
interface MarketDataClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/instruments/sedol/{id}")
    InstrumentDetails getInstrumentDetails(@PathVariable("id") String id);

    @RequestMapping(method = RequestMethod.GET, value = "/api/instrument/{symbol}")
    Instrument getInstrument(@PathVariable("symbol") String symbol);
}

class HystrixMarketDataClientFallback implements MarketDataClient {

    private static final String NOT_SET = "NOT SET";

    @Override
    public InstrumentDetails getInstrumentDetails(String id) {
        InstrumentDetails instrumentDetails = new InstrumentDetails();

        instrumentDetails.setName(NOT_SET);
        instrumentDetails.setSecurityType(NOT_SET);
        instrumentDetails.setTicker(NOT_SET);

        return instrumentDetails;
    }

    @Override
    public Instrument getInstrument(String symbol) {
        Instrument instrument = new Instrument();

        instrument.setName(NOT_SET);
        instrument.setCurrency(NOT_SET);
        instrument.setExchange(NOT_SET);
        instrument.setSymbol(NOT_SET);
        instrument.setPrice(BigDecimal.ZERO);

        return instrument;
    }
}