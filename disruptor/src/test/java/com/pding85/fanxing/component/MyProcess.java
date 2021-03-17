package com.pding85.fanxing.component;

public class MyProcess implements IProcess<M1, String> {
    @Override
    public String process(MessageWapper<M1> data) {
        return "a11";
    }

    @Override
    public String getKey() {
        return "1";
    }
}
