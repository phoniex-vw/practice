package com.yw2ugly.aop;

import com.yw2ugly.aop.TestJdkProxy.Foo;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class TestCglibProxy {

    public static void main(String[] args) {
        cglibProxy();
    }

    private static void cglibProxy() {
        TestCglibProxy foo = new TestCglibProxy();
        Foo fooProxy = (Foo)Enhancer.create(Foo.class, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.err.println("before()");
                return method.invoke(foo, objects);

                //methodProxy.invoke(foo,objects); // 原始对象非反射调用
                //methodProxy.invokeSuper(foo,objects); //代理对象的非反射调用
            }
        });
        fooProxy.foo();
    }

    public void foo(){
        System.err.println("foo()");
    }

}
