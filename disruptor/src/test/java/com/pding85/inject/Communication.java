package com.pding85.inject;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 *
 * https://www.jianshu.com/p/7fba7b43146a
 *
 *
 */
@Singleton
public class Communication {
    @Inject
    private Communicator communicator;

    public Communication(Boolean keepRecords) {
        if (keepRecords) {
            System.out.println("Message logging enabled");
        }
    }

    public boolean sendMessage(String message) {
        communicator.sendMessage(message);
        return true;
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new BasicModule());

        Communication comms = injector.getInstance(Communication.class);

        comms.sendMessage("hello world");
    }
}