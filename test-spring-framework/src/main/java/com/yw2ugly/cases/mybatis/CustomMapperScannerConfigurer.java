package com.yw2ugly.cases.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Arrays;

// 模拟mybatis @Mapper 注解的接口是怎么被spring 管理的
// BeanFactoryPostProcessor
@Slf4j
public class CustomMapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 资源路径解析器
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        //类元数据读取器
        CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
        //bean name generator
        AnnotationBeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
        try {
            Resource[] resources = patternResolver.getResources("com/yw2ugly/cases/mybatis/mapper/**/*.class");
            Arrays.stream(resources).forEach(
                    resource -> {
                        log.info("Resource: {}", resource);
                        try {
                            MetadataReader metadataReader = cachingMetadataReaderFactory.getMetadataReader(resource);
                            ClassMetadata classMetadata = metadataReader.getClassMetadata();
                            if(classMetadata.isInterface()){
                                AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(MapperFactoryBean.class)
                                        .addConstructorArgValue(classMetadata.getClassName())
                                        .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
                                        .getBeanDefinition();

                                //仅仅用来生成名字，因为bean name generator 是根据beanName definition 的class name 决定的
                                AbstractBeanDefinition beanDefinitionForBeanNameGenerator = BeanDefinitionBuilder.genericBeanDefinition(classMetadata.getClassName())
                                        .getBeanDefinition();

                                String beanName = beanNameGenerator.generateBeanName(beanDefinitionForBeanNameGenerator, registry);
                                registry.registerBeanDefinition(beanName, beanDefinition);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

}
