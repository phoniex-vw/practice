package com.yw2ugly.aop;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class MyAspect {
    private Logger log = LoggerFactory.getLogger(MyAspect.class);

    @Before("execution(* com.yw2ugly.aop.TestAspectJ.test_aspectJ())")
    public void before_aspectJ(){
      log.info("Aspect before, aspectJ");
    }

    @Before("execution(* com.yw2ugly.aop.TestJavaagent.test_java_agent())")
    public void before_java_agent(){
        log.info("Aspect before, javaagent");
    }

}
