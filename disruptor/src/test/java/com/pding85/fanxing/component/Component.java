package com.pding85.fanxing.component;

import java.util.HashSet;
import java.util.Set;

public class Component<T> {

    DispatchRegister<T> register;

    public Component() {

    }

    public void init(DispatchRegister<T> register) {

        String name = "com.pding85.fanxing.component.MyProcess";
        try {
            Class c = Class.forName(name);
            register.addProcess(convert(c.newInstance()));
        } catch (Exception e) {

        }

        String container = "com.pding85.fanxing.component.MySet";
        try {
            Class c = Class.forName(container);
            register.setContainer(convert2(c.newInstance()));
        } catch (Exception e) {

        }

        this.register = register;

    }

    private IProcess<? extends MessageStore, T> convert(Object instance) {
        return (IProcess<? extends MessageStore, T>) instance;
    }


    private Set<T> convert2(Object instance) {
        return (Set<T>) instance;
    }

    public <M extends MessageStore> Set<T> run(MessageWapper<M> message) {
        return ((DispatchRegister<T> )register).get().run(message);
    }

    public static void main(String[] args) {
        Component<Integer> component = new Component<>();
        component.init(new DispatchRegister<Integer>());

        MessageWapper<M1> mes = new MessageWapper();
        mes.setType(1);
        mes.setData(new M1());

        component.run(mes);
    }
}
