package com.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class AllocationEnricherIntegrationTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private CachingConnectionFactory connectionFactory;

    @Before
    public void setUp() throws Exception {
        AllocationEnricherApplication.main(new String[] {});

        setupRabbitMq();
    }

    @Primary
    @Bean
    CounterpartyClient counterpartyClient() {
        CounterpartyClient counterpartyClient = mock(CounterpartyClient.class);

        when(counterpartyClient.getExchange(TestData.allocation().getMicCode())).thenReturn(TestData.exchange());
        when(counterpartyClient.getPartyName(TestData.allocation().getCounterpartyId())).thenReturn(TestData.COUNTERPARTY_NAME);
        when(counterpartyClient.getPartyName(TestData.allocation().getExecutingPartyId())).thenReturn(TestData.EXECUTING_PARTY_NAME);

        return counterpartyClient;
    }

    @Primary
    @Bean
    MarketDataClient marketDataClient() {
        MarketDataClient marketDataClient = mock(MarketDataClient.class);

        InstrumentDetails instrumentDetails = mock(InstrumentDetails.class);
        when(instrumentDetails.getTicker()).thenReturn(TestData.instrument().getSymbol());
        when(marketDataClient.getInstrumentDetails(TestData.allocation().getSecurityId())).thenReturn(instrumentDetails);
        when(marketDataClient.getInstrument(TestData.instrument().getSymbol())).thenReturn(TestData.instrument());

        return marketDataClient;
    }

    private void setupRabbitMq() {
        connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUri("amqp://guest:guest@localhost");

        AmqpAdmin admin = new RabbitAdmin(connectionFactory);
        TopicExchange exchange = new TopicExchange("trading-office-exchange");
        Queue queue = new Queue("enriched.json.allocation.report");
        admin.declareQueue(queue);
        admin.declareBinding(
                BindingBuilder.bind(queue).to(exchange)
                    .with("enriched.json.allocation.report")
        );
    }

    @Test
    public void receives_allocation_and_send_enriched_one() throws Exception {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());

        String allocationAsJson = OBJECT_MAPPER.writeValueAsString(TestData.allocation());
        template.convertAndSend("trading-office-exchange", "received.json.allocation.report", allocationAsJson);

        TimeUnit.SECONDS.sleep(3);

        String enrichedAllocationAsJson = (String) template.receiveAndConvert("enriched.json.allocation.report");
        EnrichedAllocation enrichedAllocation =
                OBJECT_MAPPER.readValue(enrichedAllocationAsJson, EnrichedAllocation.class);

        assertThat(enrichedAllocation).isEqualToComparingFieldByField(TestData.enrichedAllocation());
    }
}
