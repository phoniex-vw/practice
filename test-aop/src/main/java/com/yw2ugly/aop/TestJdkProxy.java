package com.yw2ugly.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

public class TestJdkProxy {

    public static void main(String[] args) {

        jdkDynamicProxy();
        //jdkDynamicProxyMock();
    }

    private static void jdkDynamicProxyMock() {
        Foo1 foo1 = new Foo1();
        $Proxy0 $Proxy0 = new $Proxy0((proxy, method, args1) -> {
            System.err.println("before()");
            return method.invoke(foo1, args1);
        });

        $Proxy0.foo();
    }

    private static void jdkDynamicProxy() {
        Foo1 foo1 = new Foo1();
        Foo fooProxy = (Foo) Proxy.newProxyInstance(TestJdkProxy.class.getClassLoader(), new Class[]{Foo.class},
                (proxy, method, args1) -> {
                    System.err.println("before()");
                    return method.invoke(foo1, args1);
                }
        );
        fooProxy.foo();
    }


    interface  Foo {
        void foo();
    }

    static class Foo1 implements  Foo{

        @Override
        public void foo() {
            System.err.println("foo()");
        }
    }

   //mock jdk proxy
    static class $Proxy0  implements  Foo{
        private final InvocationHandler invocationHandler;

       public $Proxy0(InvocationHandler invocationHandler) {
           this.invocationHandler = invocationHandler;
       }

       static Method foo;
       static {
            try {
                foo = Foo.class.getDeclaredMethod("foo");
            } catch (NoSuchMethodException e) {
                throw new NoSuchMethodError(e.getMessage());
            }
        }


        @Override
        public void foo() {
            try {
                this.invocationHandler.invoke(this,foo,new Object[0]);
            }catch (Error | RuntimeException e){
                throw  e;
            }catch (Throwable e) {
                throw new UndeclaredThrowableException(e);
            }
        }
    }




}
