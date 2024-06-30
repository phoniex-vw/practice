package com.yw2ugly.aop.spring;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class TestAdviceAndAdvisor {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.registerBean(CustomAspect.class);
        applicationContext.registerBean(Config.class);
        //用于搜集所有低级切面Advisor以及将高级切面@Aspect转换成低级切面
        applicationContext.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);
        //用于解析@Configuration 注解以及其中的@Bean等注解
        applicationContext.addBeanFactoryPostProcessor(new ConfigurationClassPostProcessor());

        applicationContext.refresh();

        log.info("BeanDefinitions as blow >>>>>>>>>>>>>>>>>>>>");
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(beanName -> {
            log.info("beanName: {}", beanName);
        });

        log.info("Advisors as blow >>>>>>>>>>>>>>>>>>>>");

        AnnotationAwareAspectJAutoProxyCreator awareAspectJAutoProxyCreator = applicationContext.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
        Method findEligibleAdvisors = AbstractAdvisorAutoProxyCreator.class.getDeclaredMethod("findEligibleAdvisors", Class.class, String.class);
        findEligibleAdvisors.setAccessible(true);

        List<Advisor> advisors = (List<Advisor>) findEligibleAdvisors.invoke(awareAspectJAutoProxyCreator, E.class, "");

        advisors.stream()
                        .forEach(advisor -> {
                            log.info("Advisor: {}", advisor);
                        });

        Method wrapIfNecessary = AbstractAutoProxyCreator.class.getDeclaredMethod("wrapIfNecessary", Object.class, String.class, Object.class);
        wrapIfNecessary.setAccessible(true);

        E proxiedObject = (E) wrapIfNecessary.invoke(awareAspectJAutoProxyCreator,new E(), "", "test");
        F noProxiedObject = (F) wrapIfNecessary.invoke(awareAspectJAutoProxyCreator, new F(), "", "test");

        log.info("ProxiedObject: {}, NoProxiedObject: {}", proxiedObject.getClass(), noProxiedObject.getClass());

        proxiedObject.m();
        noProxiedObject.m1();

        applicationContext.close();

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.yw2ugly.aop.spring.TestAdviceAndAdvisor.E.m()))");

        System.err.println(pointcut.matches(E.class));
    }


    @Aspect
    @Slf4j
    static class CustomAspect {
        @Before("execution(* com.yw2ugly.aop.spring.TestAdviceAndAdvisor.E.m()))")
        public void before(){
            log.info("before advice");
        }
        @After("execution(* com.yw2ugly.aop.spring.TestAdviceAndAdvisor.E.m()))")
        public void after(){
            log.info("after advice");
        }

    }

    @Configuration
    static class Config {
        @Bean
        public DefaultPointcutAdvisor advisor3(Advice advice3){
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* com.yw2ugly.aop.spring.TestAdviceAndAdvisor.E.m1())");
            return new DefaultPointcutAdvisor(pointcut, advice3);
        }

        @Bean
        public Advice advice3(){
            return (MethodInterceptor) invocation -> null;
        }
    }

    static  class E {
        public void m(){}
        public void m1(){}
    }

    static  class F {
        public void m1(){}
    }
}
