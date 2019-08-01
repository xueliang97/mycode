package com.hdu.xl.mycode;

import java.util.List;

public class SolarNode extends Node {
    protected double solar_intensity;
    public SolarNode(int type,int id, double x_coordinate, double y_coordinate, double residual_energy,double solar_intensity,List<Integer> NodeCoverTar,List<Integer> routeToSink) {
        super(type,id, x_coordinate, y_coordinate,residual_energy,NodeCoverTar,routeToSink);
        this.solar_intensity = solar_intensity;

    }



    @Override
    public String toString() {
        return "太阳能节点"+id+"：剩余能量为: "+residual_energy+" 太阳能强度："+solar_intensity;
    }
    public double getSolar_intensity() {
        return solar_intensity;
    }

    public void setSolar_intensity(double solar_intensity) {
        this.solar_intensity = solar_intensity;
    }
}
