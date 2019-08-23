package com.hdu.xl.mycode;


import java.util.ArrayList;
import java.util.List;


public class Test {

    public static void main(String[] args){
        int[] data = {1,2,3};
        List<Integer> tr = new ArrayList<>();
        tr.add(1);tr.add(2);
        tt(tr);
        System.out.println(tr);
    }

    public static void tt(List<Integer> t){
        List res = new ArrayList();
        res.add(3);
        t.clear();
        t.addAll(res);
    }





}
