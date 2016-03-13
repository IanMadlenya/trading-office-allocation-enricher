package com.trading;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableHystrix
@EnableHystrixDashboard
public class AllocationEnricherApplication {

    @Autowired
    CounterpartyClient counterpartyClient;

    @Autowired
    MarketDataClient marketDataClient;

    @Bean
    HystrixCounterpartyClientFallback hystrixCounterpartyClientFallback() {
        return new HystrixCounterpartyClientFallback();
    }

    @Bean
    HystrixMarketDataClientFallback hystrixMarketDataClientFallback() {
        return new HystrixMarketDataClientFallback();
    }

    public static void main(String[] args) {
        SpringApplication.run(AllocationEnricherApplication.class, args);
    }

    @Bean
    AllocationEnricher allocationEnricher() {
        return new AllocationEnricher(marketDataClient, counterpartyClient);
    }

    @Bean
    AllocationReceiver receiver(RabbitTemplate rabbitTemplate, AllocationEnricher allocationEnricher) {

        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return new AllocationReceiver(rabbitTemplate, allocationEnricher);
    }
}
