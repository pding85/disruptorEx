package com.pding85.fanxing.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DispatchRegister<T> {

    Map<String, IProcess<? extends MessageStore, T>> processMap;
    Set<T> container;
    ThreadPool<T> threadPool;

    public DispatchRegister() {
        processMap = new HashMap<>();
    }

    private Map<String, IProcess<? extends MessageStore, T>> init1() {
        Map<String, IProcess<? extends MessageStore, T>> in1 = new HashMap<>();
        return in1;
    }



    public <M extends MessageStore> boolean addProcess(IProcess<M, T> process) {
        processMap.put(process.getKey(), process);
        return true;
    }

    public boolean setContainer(Set<T> container) {
        this.container = container;
        return true;
    }

    public Map<String, IProcess<? extends MessageStore, T>> getProcessMap() {
        return processMap;
    }

    public Set<T> getContainer() {
        return container;
    }

    public ThreadPool<T> get() {
        if (threadPool == null) {
            threadPool = new ThreadPool(processMap, container);
        }
        return threadPool;
    }
}
