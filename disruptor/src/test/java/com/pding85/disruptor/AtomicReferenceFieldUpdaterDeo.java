package com.pding85.disruptor;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.BinaryOperator;

/**
 *  known AtomicReferenceFieldUpdater<br>
 *  //原子更新指定对象的指定字段的值，当当前值等于预期值时，则用update更新该值，成功返回true<br>
 * public final boolean compareAndSet(T obj, V expect, V update);
 * //将给定对象的指定字段设置为指定值<br>
 *  public final void set(T obj, V newValue);<br>
 * //原子更新指定对象的指定字段的值<br>
 * public abstract boolean weakCompareAndSet(T obj, V expect, V update);<br>
 * //将给定对象的指定字段更新为指定值，与set不同的是，该方法不会立刻修改旧值，而是会延迟一些<br>
 * public final void lazySet(T obj, V newValue);<br>
 * //获取此更新器管理的指定对象的字段的当前值<br>
 * public final V get(T obj);<br>
 * //原子更新该更新器管理的指定对象的字段的值为newValue，返回旧值<br>
 * public V getAndSet(T obj, V newValue);<br>
 * 原子更新更新器管理的指定对象的值，newValue为updateFunction的apply方法返回的值<br>
 * public final V updateAndGet(T obj, UnaryOperator<V> updateFunction);<br>
 * //通过给定值和当前值计算新值，进行原子更新，返回旧值
 * public final V getAndAccumulate(T obj, V x,BinaryOperator<V> accumulatorFunction);<br>
 * //与上一个方法相同，不同在于返回新值
 * public final V accumulateAndGet(T obj, V x,BinaryOperator<V> accumulatorFunction);<br>
 */
public class AtomicReferenceFieldUpdaterDeo {

    public static void main(String[] args) {
        Person person=new Person(1001,"whisper");
        AtomicReferenceFieldUpdater<Person,String>
                updater = AtomicReferenceFieldUpdater.newUpdater(Person.class,String.class,"name");

        // 測試1
        boolean isSuccess = updater.compareAndSet
                (person,"whisper","godyan");
        System.out.println("修改后的name为:"+person.getName());

        // 測試2
        String result = updater.getAndAccumulate(person,
                "godyan",new BinaryOperator<String>() {
                    @Override
                    public String apply(String t, String u) {
                        // TODO Auto-generated method stub
                        return t+u;
                    }
                });
        System.out.println("修改前的name为："+result+"-修 改后的name为："+person.getName());
    }

    public static class Person {

        int code;
        volatile String name;

        Person(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
