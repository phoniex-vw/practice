package com.yw2ugly.ioc;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import javax.sql.DataSource;
import java.util.Arrays;

public class TestBeanFactoryPostProcessor {
    public static void main(String[] args) {
        GenericApplicationContext applicationContext = new GenericApplicationContext();

        applicationContext.registerBean(Config.class);

        //mybatis @Mapper annotation scan
        applicationContext.registerBean(MapperScannerConfigurer.class, bd -> {
            bd.getPropertyValues().add("basePackage","com.yw2ugly.ioc.mapper");
        });

        // BeanFactoryPostProcessor, ConfigurationClassPostProcessor  用于解析@Configuration 注解
        // 添加额外的BeanDefinition @ComponentScan @Bean @Import @ImportResource
        applicationContext.addBeanFactoryPostProcessor(new ConfigurationClassPostProcessor());


        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(
                System.err::println
        );


        applicationContext.refresh();
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(
                System.err::println
        );


        //applicationContext.close();



    }

    @Configuration
    @ComponentScan("com.yw2ugly.ioc.bean")
    static class Config {

        @Bean
        public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource){
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);
            return sqlSessionFactoryBean;
        }

        @Bean(initMethod = "init")
        public DataSource dataSource(){
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setUrl("jdbc:mysql://localhost:3306/test");
            druidDataSource.setUsername("root");
            druidDataSource.setPassword("123456");

            System.err.println("datasource created: " + druidDataSource);
            return druidDataSource;
        }

    }
}
