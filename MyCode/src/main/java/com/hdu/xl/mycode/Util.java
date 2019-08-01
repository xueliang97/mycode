package com.hdu.xl.mycode;

import java.util.List;

public class Util {
    public static double dis_Node_Node(Node x,Node y){
        return Math.sqrt((x.x_coordinate-y.x_coordinate)*(x.x_coordinate-y.x_coordinate)+(x.y_coordinate-y.y_coordinate)*(x.y_coordinate-y.y_coordinate));
    }

    public static double dis_Node_Tar(Node x,Target y){
        return Math.sqrt((x.x_coordinate-y.x_coordinate)*(x.x_coordinate-y.x_coordinate)+(x.y_coordinate-y.y_coordinate)*(x.y_coordinate-y.y_coordinate));
    }
    public static int calNodeUncoveredNum(List<Integer> list1,List<Integer> list2){ //第一个参数为节点内覆盖的目标集合，第二个参数为已选择的目标集合
        int count=0;
        for(int i=0;i<list1.size();i++){
            for (int j=0;j<list2.size();j++){
                if (list1.get(i)==list2.get(j))
                    count++;
            }
        }
        return list1.size()-count;
    }
}
