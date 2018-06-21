package com.forsrc.tcc.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

import com.forsrc.common.core.sso.feignclient.UserTccFeignClient;
import com.forsrc.common.core.tcc.feignclient.TccFeignClient;

@Configuration
@EnableFeignClients(basePackageClasses = {
        UserTccFeignClient.class, TccFeignClient.class
})
public class FeignClientConifg {

}
