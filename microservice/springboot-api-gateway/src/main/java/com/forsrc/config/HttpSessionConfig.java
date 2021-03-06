package com.forsrc.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Component;

@Configuration
@EnableRedisHttpSession
public class HttpSessionConfig {

//    @Bean
//    DefaultCookieSerializer defaultCookieSerializer() {
//        MyDefaultCookieSerializer defaultCookieSerializer = new MyDefaultCookieSerializer();
//        defaultCookieSerializer.setUseBase64Encoding(false);
//        defaultCookieSerializer.setUseHttpOnlyCookie(false);
//        return defaultCookieSerializer;
//    }
//
//    public static class MyDefaultCookieSerializer extends DefaultCookieSerializer implements Serializable {
//        private static final long serialVersionUID = -1659660457374060851L;
//    }

    @Component
    public static class SessionFixBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
        private static final String SERIALIZATION_ID = "4086d293-966c-4d89-8485-f1c1f5c09218";

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            if ((beanFactory instanceof DefaultListableBeanFactory)) {
                DefaultListableBeanFactory dlbf = (DefaultListableBeanFactory) beanFactory;
                dlbf.setSerializationId(SERIALIZATION_ID);
            }
        }
    }
}
