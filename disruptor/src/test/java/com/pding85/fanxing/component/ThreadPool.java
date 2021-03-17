package com.pding85.fanxing.component;

import java.util.Map;
import java.util.Set;

public class ThreadPool<T > {

    Map<String, IProcess> process;

    Set<T> container;

    public ThreadPool(Map<String, IProcess> process, Set<T> container) {
        this.process = process;
        this.container = container;
    }

    public <M extends MessageStore> Set<T> run(MessageWapper<M> message) {
        String key = message.getType() + "";
        T data = (T) process.get(key).process(message);
        System.out.println(data);
        container.add(data);
        return container;
    }
}
