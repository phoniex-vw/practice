package com.yw2ugly.factory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Arrays;

public class FactoryTest {

    public static void main(String[] args) throws IOException {

        ApplicationContext ac = new AnnotationConfigApplicationContext();

        Resource[] resources = ac.getResources("classpath*:META-INF/spring.factories");
        for (Resource resource : resources) {
            System.err.println(resource);
        }

    }
}
