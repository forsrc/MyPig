package com.forsrc.tcc.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
// @MapperScan("com.forsrc.**.dao.mapper")
public class MyBatisConfig {

    @Configuration
    @MapperScan(basePackages = { "com.forsrc.tcc.dao.mapper" },
        sqlSessionFactoryRef = "sqlSessionFactoryPrimary",
        sqlSessionTemplateRef = "sqlSessionTemplatePrimary")
    public static class DataSourcePrimary {

        @Autowired
        //@Qualifier("dataSource")
        private DataSource dataSource;

        @Bean("sqlSessionFactoryPrimary")
        @Qualifier("sqlSessionFactoryPrimary")
        @Primary
        public SqlSessionFactory sqlSessionFactoryPrimary() throws Exception {

            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);

            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

            sqlSessionFactoryBean.setMapperLocations(
                    resolver.getResources("classpath*:com/forsrc/tcc/dao/mapper/*Mapper.xml"));
            // sqlSessionFactoryBean.setConfigLocation(new
            // ClassPathResource("config/mybatis-config.xml"));

            return sqlSessionFactoryBean.getObject();
        }

        @Bean("sqlSessionTemplatePrimary")
        @Qualifier("sqlSessionTemplatePrimary")
        @Primary
        public SqlSessionTemplate sqlSessionTemplatePrimary(
                @Qualifier("sqlSessionFactoryPrimary") SqlSessionFactory sqlSessionFactoryPrimary) {
            return new SqlSessionTemplate(sqlSessionFactoryPrimary);
        }
    }

//  @Primary
//  @Bean(name = "transactionManager")
//  public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
//      return new DataSourceTransactionManager(dataSource);
//  }

}
