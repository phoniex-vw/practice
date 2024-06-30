package com.yw2ugly.aop.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

@Slf4j
public class TestPointCut {

    public static void main(String[] args) throws NoSuchMethodException {
        //simpleExpressionPointCut();
        //annotationExpressionPointCut();
        staticMethodPointCut();

    }

    private static void staticMethodPointCut() throws NoSuchMethodException {
        // 可以写更多更复杂的自定义的逻辑，例如派生注解的处理，父类，接口上的接口定义
        StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                MergedAnnotations mergedAnnotations = MergedAnnotations.from(method);
                //方法上有@Transactional
                if (mergedAnnotations.isPresent(Transactional.class)) {
                    return true;
                }
                mergedAnnotations = MergedAnnotations.from(targetClass, SearchStrategy.TYPE_HIERARCHY);

                //类，父类，接口上有@Transactional
                if (mergedAnnotations.isPresent(Transactional.class)) {
                    return true;
                }
                return false;
            }
        };

        Method m1 = T3.class.getDeclaredMethod("m1");
        boolean matches = pointcut.matches(m1, T3.class);
        log.info("T3 m1 matches: {}", matches);

        m1 = T4.class.getDeclaredMethod("m1");
        matches = pointcut.matches(m1, T4.class);
        log.info("T4 m1 matches: {}", matches);

        m1 = T5.class.getDeclaredMethod("m1");
        matches = pointcut.matches(m1, T5.class);
        log.info("T5 m1 matches: {}", matches);
    }

    private static void annotationExpressionPointCut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("@annotation(org.springframework.transaction.annotation.Transactional)");

        Method m1 = T2.class.getDeclaredMethod("m1");
        boolean matches = pointcut.matches(m1, T2.class);
        log.info("m1 matches: {}", matches);

        Method m2 = T2.class.getDeclaredMethod("m2");
        matches = pointcut.matches(m2, T2.class);
        log.info("m2 matches: {}", matches);
    }

    private static void simpleExpressionPointCut() throws NoSuchMethodException {
        //基于aspectJ表达式的切入点实现
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression("execution(* com.yw2ugly.aop.spring.TestPointCut.T1.t1())");

        Method m1 = T1.class.getDeclaredMethod("m1");
        boolean matches = aspectJExpressionPointcut.matches(m1, T1.class);
        log.info("m1 matches: {}", matches);

        Method m2 = T1.class.getDeclaredMethod("m2");
        matches = aspectJExpressionPointcut.matches(m2, T1.class);
        log.info("m2 matches: {}", matches);
    }

    @Slf4j
    static class T1 {
        public void m1(){
            log.info("m1 ... ");
        }
        public void m2(){
            log.info("m2 ... ");
        }
    }

    @Slf4j
    static class T2 {
        public void m1(){
            log.info("m1 ...");
        }
        @Transactional
        public void m2(){
            log.info("m2 ...");
        }
    }

    @Transactional
    static class T3 {
        public void m1(){}
    }
    @Transactional
    static interface  P {}
    static class T4 implements P{
        public void m1(){}
    }
    static class T5 {
        @Transactional
        public void m1(){}
    }
}


