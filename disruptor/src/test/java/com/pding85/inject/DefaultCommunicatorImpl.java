package com.pding85.inject;

public class DefaultCommunicatorImpl implements Communicator {
    public boolean sendMessage(String message) {
        System.out.println("Sending Message + " + message);
        return true;
    }
}