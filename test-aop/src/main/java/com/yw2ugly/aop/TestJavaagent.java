package com.yw2ugly.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// -javaagent:/Users/harvey/.m2/repository/org/aspectj/aspectjweaver/1.9.7/aspectjweaver-1.9.7.jar
// javaagent for aop
// 在类加载的时候修改字节码，但是编译生成的class文件没有做修改
public class TestJavaagent {

    static Logger log = LoggerFactory.getLogger(TestAspectJ.class);

    public static void main(String[] args) {
        TestJavaagent testJavaagent = new TestJavaagent();
        testJavaagent.test_java_agent();

    }

    public void test_java_agent(){
        log.info("test java agent");
    }
}
