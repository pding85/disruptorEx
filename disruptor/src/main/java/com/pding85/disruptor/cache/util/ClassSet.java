package com.pding85.disruptor.cache.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Set of classes represented as prefix tree.
 * {@code *} symbol is allowed and indicates that all packages and classes are included.
 */
public class ClassSet {
    /** Corresponds to {@code *} symbol. */
    private static final Map<String, Node> ALL = Collections.emptyMap();

    /** Root. */
    private Node root = new Node();

    /**
     * Adds class name to the set.
     *
     * @param clsName Class name.
     */
    public void add(String clsName) {
        String[] tokens = clsName.split("\\.");

        Node cur = root;

        for (int i = 0; i < tokens.length; i++) {
            if (cur.children == ALL)
                return;

            if (tokens[i].equals("*")) {
                if (i != tokens.length - 1)
                    throw new IllegalArgumentException("Incorrect class name format.");

                cur.children = ALL;

                return;
            }

            if (cur.children == null)
                cur.children = new HashMap<>();

            Node n = cur.children.get(tokens[i]);

            if (n == null) {
                n = new Node();

                cur.children.put(tokens[i], n);
            }

            cur = n;
        }
    }

    /**
     * @param clsName Class name.
     */
    public boolean contains(String clsName) {
        String[] tokens = clsName.split("\\.");

        Node cur = root;

        for (int i = 0; i < tokens.length; i++) {
            if (cur.children == ALL)
                return true;

            if (cur.children == null)
                return false;

            Node n = cur.children.get(tokens[i]);

            if (n == null)
                return false;

            if (i == tokens.length - 1)
                return true;

            cur = n;
        }

        return false;
    }

    /** */
    private static class Node {
        /** Children. */
        private Map<String, Node> children;
    }
}