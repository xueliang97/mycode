package com.hdu.xl.mycode;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    protected List<Node> vertexList; //存储顶点的列表
    protected double[][] edges;
    protected int numOfEdges;

    public Graph(int n) {
        edges = new double[n][n];
        vertexList = new ArrayList<>();
        numOfEdges = 0;
    }

    public int getNumOfEdges() {//得到边的数量
        return numOfEdges;
    }

    public double getWeight(Node v1,Node v2) {//得到两点之间的权重
        return edges[v1.id][v2.id];
    }

    public void insertVertex(Node v) {//插入节点
        vertexList.add(v);
    }

    public void insertEdge(Node v1,Node v2,double weight) {//插入边
        edges[v1.id][v2.id] = weight;
        numOfEdges++;
    }
}
