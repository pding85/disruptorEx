package com.pding85.fanxing.component;

import java.util.ArrayList;
import java.util.List;

public class Test1<T> {

    List<T> data =new ArrayList<>();

    public void register(T dd) {
        data.add(dd);
    }

    public void add(Object e) {
        getData().add((T )e);
    }

    public static void main(String[] args) {
        Test1<Integer> sdd = new Test1() ;

        sdd.add("a11");
        System.out.println(sdd);
    }

    public List<T> getData() {
        List<T> data = new ArrayList<T>();
        this.data = data;
        return data;
    }

    @Override
    public String toString() {
        return "Test1{" +
                "data=" + data +
                '}';
    }
}
