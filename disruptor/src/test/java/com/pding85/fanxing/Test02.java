package com.pding85.fanxing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test02 {

    public static void main(String[] args) {
        //

        List<Orange> code = new ArrayList<>();

        int i = 0;
        try {
            for (; ; ++i ) {
                Orange data = null;
                  System.out.println(System.identityHashCode(data));
                code.add(data);
                System.out.println(i);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        // ASCIIString a = new ASCIIString("aaa");

    }
}
