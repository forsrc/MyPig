package com.forsrc.sso.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.client.RestTemplate;

import brave.Tracing;
import brave.http.HttpTracing;
import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import brave.spring.web.TracingClientHttpRequestInterceptor;
import brave.spring.webmvc.TracingHandlerInterceptor;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

@Configuration
@Import({ TracingClientHttpRequestInterceptor.class, TracingHandlerInterceptor.class })
public class ZipkinConfig {

    @Value("${spring.zipkin.base-url}")
    private String zipkinUrl;
    @Value("${spring.zipkin.service.name}")
    private String serviceName;

    @Autowired
    private TracingHandlerInterceptor serverInterceptor;

    @Autowired
    private TracingClientHttpRequestInterceptor clientInterceptor;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate loadBalancedRestTemplate;

    @Autowired
    @Qualifier("tccOAuth2RestTemplate")
    private OAuth2RestTemplate tccOAuth2RestTemplate;
    
    @Bean
    public Sender sender() {
        return OkHttpSender.create(zipkinUrl + "/api/v2/spans");
    }

    /** Configuration for how to buffer spans into messages for Zipkin */
    @Bean
    public AsyncReporter<Span> spanReporter() {
        return AsyncReporter.create(sender());
    }

    /** Controls aspects of tracing such as the name that shows up in the UI */
    @Bean
    public Tracing tracing() {
        return Tracing.newBuilder()
                .localServiceName(serviceName)
                .propagationFactory(ExtraFieldPropagation.newFactory(B3Propagation.FACTORY, "user-name"))
                //.currentTraceContext(ThreadContextCurrentTraceContext.create()) // puts trace IDs into logs
                .spanReporter(spanReporter())
                .build();
    }

    // decides how to name and tag spans. By default they are named the same as the
    // http method.
    @Bean
    public HttpTracing httpTracing(Tracing tracing) {
        return HttpTracing.create(tracing);
    }

    @PostConstruct
    public void config() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(restTemplate.getInterceptors());
        interceptors.add(clientInterceptor);
        restTemplate.setInterceptors(interceptors);

        interceptors = new ArrayList<>(loadBalancedRestTemplate.getInterceptors());
        interceptors.add(clientInterceptor);
        loadBalancedRestTemplate.setInterceptors(interceptors);

        interceptors = new ArrayList<>(tccOAuth2RestTemplate.getInterceptors());
        interceptors.add(clientInterceptor);
        tccOAuth2RestTemplate.setInterceptors(interceptors);
    }
}
