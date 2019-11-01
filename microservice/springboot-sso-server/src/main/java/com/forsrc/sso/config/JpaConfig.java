package com.forsrc.sso.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
//@EnableTransactionManagement(proxyTargetClass = true)
@EnableConfigurationProperties(JpaProperties.class)
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
        basePackages = {"com.forsrc.sso"})
public class JpaConfig {

    @Autowired
    private JpaProperties jpaProperties;
    @Autowired
    private EntityManagerFactoryBuilder builder;
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

//    @Bean
//    public FilterRegistrationBean filterRegistration() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(openSessionInView());
//        registration.addUrlPatterns("/*");
//
//        return registration;
//    }
//
//    @Bean
//    public Filter openSessionInView() {
//        return new OpenSessionInViewFilter();
//    }

    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
        return new OpenEntityManagerInViewFilter();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean entityManagerFactory =  builder
                .dataSource(dataSource)
                .packages("com.forsrc.sso.domain.entity")
                .persistenceUnit("persistenceUnit-sso")
                .properties(jpaProperties.getProperties())
                .build();
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return  entityManagerFactory;
    }


    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory().getObject());
        transactionManager.setRollbackOnCommitFailure(true);
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

//    @Primary
//    @Bean(name = "transactionManager")
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }

}
