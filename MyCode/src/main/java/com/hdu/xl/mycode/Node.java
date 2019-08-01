package com.hdu.xl.mycode;


import java.util.List;

public abstract class Node {

    protected int id;
    protected int type ;
    protected double x_coordinate;
    protected double y_coordinate;
    protected double residual_energy;
    protected double energy_consumption;
    protected double node_weight;
    protected List<Integer> NodeCoverTar;
    protected List<Integer> routeToSink;
    protected double route_weight;
    public Node(int type, int id, double x_coordinate, double y_coordinate, double residual_energy, List<Integer> NodeCoverTar, List<Integer> routeToSink){
        this.type = type;
        this.id = id;
        this.x_coordinate = x_coordinate;
        this.y_coordinate = y_coordinate;
        this.residual_energy = residual_energy;
        this.NodeCoverTar = NodeCoverTar;
        this.routeToSink = routeToSink;
    }
    public void setRoute_weight(double route_weight) {
        this.route_weight = route_weight;
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

    public double getResidual_energy() {
        return residual_energy;
    }

    public void setResidual_energy(double residual_energy) {
        this.residual_energy = residual_energy;
    }

    public String toString() {
        return "";
    }
}
