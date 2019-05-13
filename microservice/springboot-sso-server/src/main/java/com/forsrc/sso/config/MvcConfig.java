package com.forsrc.sso.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("/login");;
        registry.addViewController("/").setViewName("/test");
        //registry.addViewController("/").setViewName("/gwt/test");
        //registry.addViewController("/oauth/confirm_access").setViewName("authorize");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/gwt/**")
            .addResourceLocations("classpath:/static/gwt/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedHeaders("*")
            .allowedMethods("*")
            .allowedOrigins("*");

    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry
//                    .addMapping("/**")
//                    .allowedHeaders("*")
//                    .allowedMethods("*")
//                    .allowedOrigins("*");
//            }
//        };
//    }
}
