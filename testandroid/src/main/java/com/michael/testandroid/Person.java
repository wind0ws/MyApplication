package com.michael.testandroid;

import java.io.Serializable;

/**
 * Created by Jiang on 2015/5/15.
 */
public class Person implements Serializable {
    private String name;
    private int age;
    public Person(String name,int age){
        this.name=name;
        this.age=age;
    }

    @Override
    public String toString() {
        return "姓名"+name+"  年龄"+age;
    }
}
