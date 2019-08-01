package com.hdu.xl.mycode;

import java.util.List;

public class Target {

    protected int id;
    protected double x_coordinate;
    protected double y_coordinate;
    protected boolean isCovered;
    protected List<Integer> TarCoveredByNode;
    public Target(int id,double x_coordinate,double y_coordinate,boolean isCovered,List<Integer> TarCoveredByNode){
        this.id = id;
        this.isCovered = isCovered;
        this.x_coordinate = x_coordinate;
        this.y_coordinate = y_coordinate;
        this.TarCoveredByNode = TarCoveredByNode;

    }

    public String toString(){
        return "目标"+id+" x坐标:"+x_coordinate+" y坐标:"+y_coordinate+" 被覆盖"+isCovered;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX_coordinate() {
        return x_coordinate;
    }

    public void setX_coordinate(double x_coordinate) {
        this.x_coordinate = x_coordinate;
    }

    public double getY_coordinate() {
        return y_coordinate;
    }

    public void setY_coordinate(double y_coordinate) {
        this.y_coordinate = y_coordinate;
    }

    public boolean isCovered() {
        return isCovered;
    }

    public void setCovered(boolean covered) {
        isCovered = covered;
    }

}
