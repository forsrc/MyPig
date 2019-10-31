package com.forsrc.sso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import brave.sampler.Sampler;

@Configuration
public class SleuthConfig {

    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

//    @Bean
//    // @ConditionalOnProperty(value = "sample.zipkin.enabled", havingValue = "false")
//    public Reporter<Span> spanReporter() {
//        return Reporter.CONSOLE;
//    }

}
