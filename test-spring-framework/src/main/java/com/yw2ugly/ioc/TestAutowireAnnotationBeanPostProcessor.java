package com.yw2ugly.ioc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Slf4j
public class TestAutowireAnnotationBeanPostProcessor {
    public static void main(String[] args) throws Throwable {

        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        //this way , the bean will not be applied the spring bean lifeCycle
        factory.registerSingleton("bean2",new Bean2());
        factory.registerSingleton("bean3",new Bean3());

        Bean1 bean1 = new Bean1();
        AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        autowiredAnnotationBeanPostProcessor.setBeanFactory(factory);
        autowiredAnnotationBeanPostProcessor.postProcessProperties(null, bean1,"bean1");

        //get all autowire inject point
        Method method = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class,
                Class.class, PropertyValues.class);
        method.setAccessible(true);
        InjectionMetadata injectionMetadata = (InjectionMetadata)method.invoke(autowiredAnnotationBeanPostProcessor, "bean1", Bean1.class, (PropertyValues)null);

        //inject to finish  autowire injection
        injectionMetadata.inject(bean1,"bean1",null);

        log.info("bean1 ============> {} ",bean1);


        //internal logic in Inject behalf on bean factory
        Field bean2 = Bean4.class.getDeclaredField("bean2");
        DependencyDescriptor fieldDependencyDescriptor = new DependencyDescriptor(bean2, true);
        Object o = factory.doResolveDependency(fieldDependencyDescriptor, null, null, null);
        log.info("resolved field dependency {} ", o);

        Method setBean3 = Bean4.class.getDeclaredMethod("setBean3", Bean3.class);
        DependencyDescriptor methodDependencyDescriptor = new DependencyDescriptor(new MethodParameter(setBean3, 0),true);
        Object o1 = factory.doResolveDependency(methodDependencyDescriptor, null, null, null);
        log.info("resolved method dependency {} ",o1);


    }







    @Data
    static class Bean1 {

        @Autowired
        private Bean2 bean2;
        private Bean3 bean3;

        @Autowired
        public void setBean3(Bean3 bean3){
            this.bean3 = bean3;
        }

    }

    @Data
    static class Bean4{
        @Autowired
        private Bean2 bean2;
        private Bean3 bean3;
        @Autowired
        public void setBean3(Bean3 bean3){
            this.bean3 = bean3;
        }
    }

    static class Bean2{}
    static class Bean3{}


}
