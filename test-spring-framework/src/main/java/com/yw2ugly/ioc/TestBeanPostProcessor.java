package com.yw2ugly.ioc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Slf4j
public class TestBeanPostProcessor {
    public static void main(String[] args) {
        //a clean application Context
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.registerBean(Bean1.class);
        applicationContext.registerBean(Bean2.class);
        applicationContext.registerBean(Bean3.class);

        //1.all annotation not supported default,

        //2.AutowiredAnnotationBeanPostProcessor to resolve @Autowired @Value annotation
        applicationContext.registerBean(AutowiredAnnotationBeanPostProcessor.class);

        //3.default AutowireCandidateResolver not able to resolve String type injection,
        // ContextAnnotationAutowireCandidateResolver can resolve @Value annotation
        applicationContext.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());

        //4.@Resource,@PostConstruct,@PreDestroy takes effect
        //CommonAnnotationBeanPostProcessor can resolve @Resource,@PostConstruct,@PreDestroy annotation
        applicationContext.registerBean(CommonAnnotationBeanPostProcessor.class);

        //5. @ConfigurationProperties annotation supported by springBoot
        // ConfigurationPropertiesBindingPostProcessor can bind properties  to POJO
        applicationContext.registerBean(Bean4.class);
        ConfigurationPropertiesBindingPostProcessor.register(applicationContext.getDefaultListableBeanFactory());

        applicationContext.refresh();

        Bean4 bean4 = applicationContext.getBean(Bean4.class);
        log.debug("bean4  =========> {} ",bean4);

        applicationContext.close();

    }

    static  class Bean1 {

        @Autowired
        public void setBean2(Bean2 bean2){
            log.info("@Autowired annotation takes effect {} ",bean2);
        }

        @Resource
        public void setBean3(Bean3 bean3){
            log.info("@Resource annotation takes effect {} ",bean3);
        }

        @Autowired
        public void a(@Value("${java.home}") String home){
            log.info("@Value annotation takes effect {} ",home);
        }

        @PostConstruct
        public void postConstruct(){
            log.info("@PostConstruct annotation takes effect");
        }
        @PreDestroy
        public void destroy(){
            log.info("@PreDestroy annotation takes effect");
        }
    }

    static class Bean2{}
    static class Bean3{}

    @Data
    @ConfigurationProperties(prefix = "java")
    static class Bean4{
        private String home;
        private String version;
    }

}
