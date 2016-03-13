package com.trading;

import java.math.BigDecimal;

class TestData {

    static final String COUNTERPARTY_NAME = "Customer Ltd.";
    static final String EXECUTING_PARTY_NAME = "Trading Office Ltd.";

    static EnrichedAllocation allocation() {
        EnrichedAllocation allocation = new EnrichedAllocation();

        allocation.setAllocationId("1234567");
        allocation.setSecurityId("2000019");
        allocation.setCounterpartyId("CUSTUS");
        allocation.setExecutingPartyId("TROF");
        allocation.setMicCode("XNAS");

        return allocation;
    }

    static Instrument instrument() {
        Instrument instrument = new Instrument();

        instrument.setName("Amazon.com, Inc. Stocks");
        instrument.setCurrency("USD");
        instrument.setExchange("NMS");
        instrument.setSymbol("AMZN");
        instrument.setPrice(BigDecimal.valueOf(2.12));

        return instrument;
    }

    static Exchange exchange() {
        Exchange exchange = new Exchange();

        exchange.setAcronym("NASDAQ");
        exchange.setCity("NEW YORK");
        exchange.setCountry("UNITED STATES OF AMERICA");
        exchange.setCountryCode("US");
        exchange.setName("NASDAQ - ALL MARKETS");

        return exchange;
    }

    static EnrichedAllocation enrichedAllocation() {
        EnrichedAllocation enrichedAllocation = allocation();
        enrichedAllocation.enrichWith(exchange());
        enrichedAllocation.enrichWith(instrument());
        enrichedAllocation.setCounterpartyName(COUNTERPARTY_NAME);
        enrichedAllocation.setExecutingPartyName(EXECUTING_PARTY_NAME);

        return enrichedAllocation;
    }
}
