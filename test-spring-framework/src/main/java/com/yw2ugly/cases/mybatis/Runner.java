package com.yw2ugly.cases.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Arrays;

@Slf4j
public class Runner {
    public static void main(String[] args) {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.registerBean(MybatisConfig.class);
        // beanFactory post Processor
        applicationContext.addBeanFactoryPostProcessor(new ConfigurationClassPostProcessor());
        applicationContext.addBeanFactoryPostProcessor(new CustomMapperScannerConfigurer());

        applicationContext.refresh();
        //print bean definitions
        log.info("---------------Bean definitions as blow----------------");
        Arrays.stream(applicationContext.getBeanDefinitionNames()).
                forEach(beanName -> log.info("beanName: {}", beanName));

        applicationContext.close();

    }
}
