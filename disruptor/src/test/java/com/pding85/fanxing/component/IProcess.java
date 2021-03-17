package com.pding85.fanxing.component;

public interface IProcess<M extends MessageStore, T> {

    T process(MessageWapper<M> data);

    String getKey();
}
