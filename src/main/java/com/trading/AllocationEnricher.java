package com.trading;

import java.io.IOException;

public class AllocationEnricher {

    private final MarketDataClient marketDataClient;
    private final CounterpartyClient counterpartyClient;

    public AllocationEnricher(MarketDataClient marketDataClient, CounterpartyClient counterpartyClient) {
        this.marketDataClient = marketDataClient;
        this.counterpartyClient = counterpartyClient;
    }

    EnrichedAllocation process(EnrichedAllocation allocationReport) throws IOException {

        enrichWithInstrument(allocationReport);
        enrichWithExchange(allocationReport);
        enrichWithCounterparty(allocationReport);
        enrichWithExecutingParty(allocationReport);

        return allocationReport;
    }

    private void enrichWithInstrument(EnrichedAllocation allocationReport) throws IOException {
        InstrumentDetails instrumentDetails = requestInstrumentDetails(
                allocationReport.getSecurityId()
        );

        Instrument instrument = marketDataClient.getInstrument(instrumentDetails.getTicker());
        allocationReport.enrichWith(instrument);
    }

    private InstrumentDetails requestInstrumentDetails(
            String securityId) throws IOException {

        InstrumentDetails instrumentDetails = marketDataClient.getInstrumentDetails(securityId);

        if (instrumentDetails == null) {
            throw new IOException("Cannot read instrument details");
        }

        return instrumentDetails;
    }

    private void enrichWithExchange(EnrichedAllocation allocationReport) {
        Exchange exchange = counterpartyClient.getExchange(
                allocationReport.getMicCode()
        );

        allocationReport.enrichWith(exchange);
    }

    private void enrichWithCounterparty(EnrichedAllocation allocationReport) {
        String counterparty = counterpartyClient.getPartyName(
                allocationReport.getCounterpartyId()
        );

        allocationReport.setCounterpartyName(counterparty);
    }

    private void enrichWithExecutingParty(EnrichedAllocation allocationReport) {
        String executingParty = counterpartyClient.getPartyName(
                allocationReport.getExecutingPartyId()
        );

        allocationReport.setExecutingPartyName(executingParty);
    }
}
