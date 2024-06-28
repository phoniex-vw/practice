package com.yw2ugly.generic;

import java.lang.reflect.Array;

public class T1 {

    public static void main(String[] args) {
        Pair[] pairs = {};



    }

    static  class Pair<T> {
        Class<T> clazz;
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(clazz,2);

    }
}
