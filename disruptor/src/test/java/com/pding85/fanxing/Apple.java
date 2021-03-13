package com.pding85.fanxing;

public class Apple extends Fruit
{

    volatile  boolean  flag = true;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}