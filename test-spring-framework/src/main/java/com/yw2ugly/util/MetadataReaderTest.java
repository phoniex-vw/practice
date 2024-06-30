package com.yw2ugly.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;

@Slf4j
public class MetadataReaderTest {
    public static void main(String[] args) throws IOException {
        //读取类及注解的元数据
        CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
        ClassPathResource resource = new ClassPathResource("com/yw2ugly/util/config/WebConfig.class");
        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();

        log.info("class metadata: {}", classMetadata);
        log.info("annotation metadata: {}", annotationMetadata);
        System.err.println(annotationMetadata.getAnnotations().get(Configuration.class).getMetaTypes().get(0));


    }

}
