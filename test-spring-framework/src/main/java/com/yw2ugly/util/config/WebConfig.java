package com.yw2ugly.util.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Bean
    public DruidDataSource dataSource(){
        return new DruidDataSource();
    }
}
