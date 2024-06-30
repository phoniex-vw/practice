package com.yw2ugly.aop.spring;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

@Slf4j
public class TestSpringAop {
    public static void main(String[] args) {
        //1.切入点
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression("execution(* com.yw2ugly.aop.spring.TestSpringAop.Target.run())");
        //2.通知,环绕通知
        MethodInterceptor advice = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                log.info("before Advice...");
                Object result = invocation.proceed();
                log.info("After Advice...");
                return result;
            }
        };
        //3.切面
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(aspectJExpressionPointcut,advice);
        //4.代理
        Target target = new Target();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvisor(advisor);
        Target proxy = (Target) proxyFactory.getProxy();

        System.err.println(proxy);
        proxy.run();

    }

    @Slf4j
    static class Target {
        public void run(){
            log.info("Target run executed...");
        }
    }
}
