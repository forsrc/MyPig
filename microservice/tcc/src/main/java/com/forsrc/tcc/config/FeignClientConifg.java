package com.forsrc.tcc.config;

import com.forsrc.common.core.sso.feignclient.UserTccFeignClient;
import com.forsrc.common.core.tcc.feignclient.TccFeignClient;
import feign.Client;
import feign.Contract;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Configuration
@EnableFeignClients(basePackageClasses = {
        UserTccFeignClient.class, TccFeignClient.class
})
public class FeignClientConifg {


    @Bean
    // @ConditionalOnMissingBean
    public Client feignClient(CachingSpringLoadBalancerFactory cachingFactory,
                              SpringClientFactory clientFactory) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("SSL");
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{trustManager}, null);
        return new LoadBalancerFeignClient(new Client.Default(ctx.getSocketFactory(),
                new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession sslSession) {
                        return true;
                    }
                }),
                cachingFactory, clientFactory);

    }


    @Bean
    public Contract feignContract() {
        return new Contract.Default();
    }
}
