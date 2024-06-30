package com.yw2ugly.aop.spring;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectInstanceFactory;
import org.springframework.aop.aspectj.AspectJAfterAdvice;
import org.springframework.aop.aspectj.AspectJAfterReturningAdvice;
import org.springframework.aop.aspectj.AspectJAfterThrowingAdvice;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//模拟高级切面到低级切面的转换
@Slf4j
public class TestAspectAdvisorSwitch {
    public static void main(String[] args) throws Throwable {

        AspectInstanceFactory aspectInstanceFactory = new SingletonAspectInstanceFactory(new Aspect1());
        List<Advisor> advisorList = new ArrayList<>();
        Arrays.stream(Aspect1.class.getDeclaredMethods()).forEach(method -> {
            if(method.isAnnotationPresent(Before.class)){
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(method.getAnnotation(Before.class).value());
                AspectJMethodBeforeAdvice beforeAdvice = new AspectJMethodBeforeAdvice(method, pointcut, aspectInstanceFactory);
                advisorList.add(new DefaultPointcutAdvisor(pointcut, beforeAdvice));
            } else if (method.isAnnotationPresent(After.class)){
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(method.getAnnotation(After.class).value());
                AspectJAfterAdvice afterAdvice = new AspectJAfterAdvice(method, pointcut, aspectInstanceFactory);
                advisorList.add(new DefaultPointcutAdvisor(pointcut, afterAdvice));
            } else if (method.isAnnotationPresent(AfterReturning.class)) {
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(method.getAnnotation(AfterReturning.class).value());
                AspectJAfterReturningAdvice afterReturningAdvice = new AspectJAfterReturningAdvice(method, pointcut, aspectInstanceFactory);
                advisorList.add(new DefaultPointcutAdvisor(pointcut, afterReturningAdvice));
            } else if (method.isAnnotationPresent(AfterThrowing.class)) {
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(method.getAnnotation(AfterThrowing.class).value());
                AspectJAfterThrowingAdvice afterThrowingAdvice = new AspectJAfterThrowingAdvice(method, pointcut, aspectInstanceFactory);
                advisorList.add(new DefaultPointcutAdvisor(pointcut, afterThrowingAdvice));
            } else if (method.isAnnotationPresent(Around.class)){
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(method.getAnnotation(Around.class).value());
                AspectJAroundAdvice aroundAdvice = new AspectJAroundAdvice(method, pointcut, aspectInstanceFactory);
                advisorList.add(new DefaultPointcutAdvisor(pointcut, aroundAdvice));
            }
        });

        advisorList.stream().forEach(advisor -> {
            log.info("Advisor: {}", advisor);
        });

        // 统一转换成环绕通知，MethodInterceptor, 适配器模式
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(new F());
        proxyFactory.addAdvice(ExposeInvocationInterceptor.INSTANCE); //暴露调用链对象MethodInvocation 到当前线程上下文中以便后续环绕通知执行
        proxyFactory.addAdvisors(advisorList);
        Method foo = F.class.getDeclaredMethod("foo");

        List<Object> methodInterceptorList = proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(foo, F.class);
        log.info("环绕通知如下>>>>>>>>>>>>>>>>>>>>>>");
        methodInterceptorList.stream()
                .forEach(ele -> {
                    log.info("MethodInterceptor: {}", ele);
                });

        //创建执行调用链
        MethodInvocation methodInvocation =  new ReflectiveMethodInvocation(null, new F(), foo, new Object[]{}, F.class, methodInterceptorList){};
        methodInvocation.proceed();

    }


    @Aspect
    @Slf4j
    static class Aspect1 {
        @Before("execution(* foo()))")
        public void before(){
            log.info("Before advice ...");
        }
        @After("execution(* foo()))")
        public void after(){
            log.info("After advice ...");
        }
        @AfterReturning("execution(* foo()))")
        public void afterReturning(){
            log.info("AfterReturning advice ...");
        }
        @AfterThrowing("execution(* foo()))")
        public void afterThrowing(){
            log.info("AfterThrowing advice ...");
        }
        @Around("execution(* foo()))")
        public void around(){
            log.info("Around advice ...");
        }
    }

    static class F {
        public void foo(){};
    }
}
