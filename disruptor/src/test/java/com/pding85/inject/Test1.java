package com.pding85.inject;

import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;

public class Test1 {
    private  String id;
    private  String name;

    public  Test1(String id, String name){
        this.id   = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) throws Exception {
        ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
        Constructor constructor = reflectionFactory.newConstructorForSerialization(Test1.class ,  Object.class.getDeclaredConstructor());
        constructor.setAccessible(true);
        Test1 test1 = (Test1) constructor.newInstance();
        System.out.println(test1.getId());
        System.out.println(test1.getName());
        System.out.println(test1.getClass());
    }
}