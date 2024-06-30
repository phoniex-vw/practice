package com.yw2ugly.aop;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//ajc compiler  for aop, use maven aspectJ compiler
public class TestAspectJ {

    static Logger log = LoggerFactory.getLogger(TestAspectJ.class);
    public static void main(String[] args) {
        TestAspectJ testAspectJ = new TestAspectJ();
        testAspectJ.test_aspectJ();

    }
    public void test_aspectJ(){
        log.info("test aspectJ compiler");
    }

}
