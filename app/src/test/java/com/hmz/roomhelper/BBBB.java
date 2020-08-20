package com.hmz.roomhelper;

import java.util.HashMap;
import java.util.Map;

public class BBBB {

    Map<IIIII, String> mmm;

    public BBBB() {

        mmm = new HashMap<>();
    }

    public void setIII(IIIII iii, String s) {
        this.mmm.put(iii, s);
    }

    public void ccc() {
        for (IIIII iiiii : mmm.keySet()) {
            System.out.println("----kkkkk---->" + iiiii.getClass().getName());
            System.out.println("----vvvvv---->" + mmm.get(iiiii));
        }
    }



}
