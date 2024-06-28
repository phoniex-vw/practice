package com.yw2ugly.ioc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Slf4j
public class TestBeanFactory {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(Config.class).getBeanDefinition();
        beanFactory.registerBeanDefinition("config",beanDefinition);
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();

        Arrays.stream(beanDefinitionNames).forEach(bd -> {
            log.info("default bean definition name is {} ", bd);
        });

        //add beanFactory bean definition
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        //excute beanFactory processors
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class)
                        .values().forEach(bf -> bf.postProcessBeanFactory(beanFactory));
        beanDefinitionNames = beanFactory.getBeanDefinitionNames();

        Arrays.stream(beanDefinitionNames).forEach(bd -> {
            log.info(" after bean factory proceed bean definition name is {} ",bd);
        });

        //pre instant singleton beans
        //beanFactory.preInstantiateSingletons();

        //add bean  post processors
        beanFactory.addBeanPostProcessor(new MyBeanProcessor());

        System.err.println(beanFactory.getBean("a", Config.A.class));



    }


    @Configuration
    static class Config{

        @Bean
        public A a(){
            return new A();
        }

        static class A{

        }

    }

    static class MyBeanProcessor implements BeanPostProcessor{
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            System.err.println("post process before initialization");
            return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            System.err.println("post process after initialization");
            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        }
    }





}
