package com.hdu.xl.mycode;


import java.util.ArrayList;
import java.util.List;


public class Test {

    public static void main(String[] args){
        List<Integer> list=new ArrayList<>();
        list.add(1);
        System.out.println(list);
        test(list);
        System.out.println(list);


    }

    public static void test(List<Integer> list){
        list = new ArrayList<>();
        list.add(2);
    }


}
