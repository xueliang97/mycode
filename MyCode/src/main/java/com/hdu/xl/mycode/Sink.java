package com.hdu.xl.mycode;

import java.util.List;

public class Sink extends Node {
    public Sink(int type,int id, double x_coordinate, double y_coordinate, double residual_energy, List<Integer> NodeCoverTar,List<Integer> routeToSink) {
        super(type,id, x_coordinate, y_coordinate, residual_energy,NodeCoverTar,routeToSink);
    }

    @Override
    public String toString() {
        return "Sink节点"+id;
    }
}
