package com.hdu.xl.mycode;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Code1 {
    public static final int SINK = 0;//node的类型
    public static final int SOLAR = 1;
    public static final int WIRELESS = 2;
    public static final int LENGTH = 200; //米
    public static final int WIDTH = 200;  //米
    public static final int NODE_NUM = 200;
    public static final int TARGET_NUM = 20;
    public static final double PROPORTION = 0.5;//太阳能节点占总节点比例
    public static final double Es = 150e-9; // J/b，感知能耗
    public static final double Er = 150e-9; // J/b，接收数据能耗
    public static final double et = 50e-9;// J/b,计算发送能耗Et的常量     Et = et + b*dis^alpha
    public static final double b = 100e-12;
    public static final int alpha = 2;
    public static final int u = 10000; // b/s,数据产生速率
    public static final double BATTERY_SIZE = 200;// J
    public static final int SENSING_RANAGE = 30;// 米
    public static final int COMMUINCATION_RANAGE = 60;
    public static final double SORLAR_EFFICIENCY = 0.15;
    public static final double SOLAR_INTENSITY = 4e-7;// J/s
    public static final int INFINITY = 1111111111;
    public static final double THRESHOLD = 0.2;

    protected static Sink sink;
    protected static List<Node> sensors;
    protected static List<SolarNode> solar_sensors; //id0为sink,节点id从1开始
    protected static List<WirelessNode> wireless_sensors;
    protected static List<Target> target_list;
    protected static List<Integer> beCoveredTar;//被覆盖的目标
    protected static Graph graph;


    public static void main(String[] args){
        init(NODE_NUM,TARGET_NUM,PROPORTION);

        calCover(sensors,target_list);
        if (beCoveredTar.size()!=TARGET_NUM) {
            System.out.println("有目标未被覆盖");
            return;
        }
        initGraph(sensors);
   //     resetWeight();
        int counter = 0;
        List<Integer> live_sensors = new ArrayList<>();
        List<Integer> source_sensors = new ArrayList<>();
        for(int i=1;i<sensors.size();i++){ //初始化存活节点集合和源节点集合
            if (sensors.get(i).residual_energy>0)
                live_sensors.add(i);
        }
        for (Integer i:live_sensors){
            if (!sensors.get(i).NodeCoverTar.isEmpty())
                source_sensors.add(i);
        }
        while(counter < 1){
            List<Integer> tree ;
            tree = construct_Tree(live_sensors,source_sensors);
            System.out.println("树中源节点为："+tree);
            for(Integer i:tree)
                System.out.println("源节点覆盖的目标数："+sensors.get(i).NodeCoverTar.size());
            Set<Integer> set = new HashSet<>();
            for (Integer i:tree){
                for (int j=0;j<sensors.get(i).NodeCoverTar.size();j++){
                    set.add(sensors.get(i).NodeCoverTar.get(j));
                }
            }
            System.out.println("被覆盖的目标集合为："+set);
            counter++;
            calEnergyConsumption(tree);
            for(Integer i:tree) {
                System.out.println("源节点消耗能量：" + sensors.get(i).energy_consumption);
                System.out.println("对应路径"+sensors.get(i).routeToSink);
            }

            for(Integer i:sensors.get(tree.get(tree.size()-1)).routeToSink)
                System.out.println("路径上节点消耗能量："+sensors.get(i).energy_consumption);



            resetSomeValue();

        }
    }

    public static void init(int nodenum, int targetnum, double proportion) {
        sensors = new ArrayList<>();
        target_list = new ArrayList<>();
        solar_sensors = new ArrayList<>();
        wireless_sensors = new ArrayList<>();
        beCoveredTar = new ArrayList<>();
        sink = new Sink(SINK,0,Math.random()*LENGTH,Math.random()*WIDTH,BATTERY_SIZE,new ArrayList<Integer>(),new ArrayList<Integer>());
        sensors.add(sink);
        int solar_num = (int) (NODE_NUM*proportion);
        int wireless_num = (int)(NODE_NUM*(1-proportion));
        for(int i=1;i<=solar_num;i++){ //太阳能节点初始化
            SolarNode solarNode = new SolarNode(SOLAR,i,Math.random()*LENGTH, Math.random()*WIDTH,
                    BATTERY_SIZE,Math.random()*SOLAR_INTENSITY,new ArrayList<Integer>(),new ArrayList<Integer>());
            solarNode.setRoute_weight(0);
            sensors.add(solarNode);
            solar_sensors.add(solarNode);
        }
        for (int i=solar_num+1;i<=NODE_NUM;i++){//无线充电节点初始化
            WirelessNode wirelessNode = new WirelessNode(WIRELESS,i,Math.random()*LENGTH,
                    Math.random()*WIDTH,BATTERY_SIZE,new ArrayList<Integer>(),new ArrayList<Integer>());
            wirelessNode.setRoute_weight(0);
            sensors.add(wirelessNode);
            wireless_sensors.add(wirelessNode);
        }
        for(int i=0;i<TARGET_NUM;i++){
            Target target = new Target(i,Math.random()*LENGTH,Math.random()*WIDTH,false,new ArrayList<Integer>());
            target_list.add(target);
        }
    }

    public static void initGraph(List<Node> node){//初始图，邻接矩阵中存放距离
        graph = new Graph(sensors.size());
        for (Node n:node)
            graph.vertexList.add(n);
        for (int i=0;i<sensors.size();i++){
            for (int j=0;j<sensors.size();j++){
                double temp = Util.dis_Node_Node(node.get(i),node.get(j));
                if (temp<=COMMUINCATION_RANAGE){
                    graph.insertEdge(node.get(i),node.get(j),temp);
                    graph.insertEdge(node.get(j),node.get(i),temp);
                }
                else {
                    graph.insertEdge(node.get(i),node.get(j),INFINITY);
                    graph.insertEdge(node.get(j),node.get(i),INFINITY);
                }
            }
        }
    }

    public static void calCover(List<Node> nodes,List<Target> target){
        for(int i=1;i<nodes.size();i++){
            for(int j=0;j<target.size();j++){
                if (Util.dis_Node_Tar(nodes.get(i),target.get(j))<=SENSING_RANAGE){
                    nodes.get(i).NodeCoverTar.add(target.get(j).id);
                    target.get(j).TarCoveredByNode.add(nodes.get(i).id);
           //         target.get(j).isCovered = true;
                    if (!beCoveredTar.contains(target.get(j).id))
                        beCoveredTar.add(j);
                }
            }
        }
    }

    public static double Et(Node si,Node sj,double et,double b,int alpha){//计算i向j发送1比特数据需要消耗的能量
        return et+b*Math.pow(Util.dis_Node_Node(si,sj),alpha);
    }

    public static List<Integer> construct_Tree(List<Integer> live_sensors,List<Integer> source_sensors){
        List<Integer> res = new ArrayList<>();
        live_sensors.clear();source_sensors.clear();
        for(int i=1;i<sensors.size();i++){
            if (sensors.get(i).residual_energy>0)
                live_sensors.add(i);
        }
        for (Integer i:live_sensors){
            if (!sensors.get(i).NodeCoverTar.isEmpty())
                source_sensors.add(i);
        }
        resetWeight();
        for (int i=1;i<sensors.size();i++)
            dijkstra(i,0);
        for(int i=0;i<source_sensors.size();i++) //第一次选择时的权值
            sensors.get(source_sensors.get(i)).node_weight = sensors.get(source_sensors.get(i)).NodeCoverTar.size()/sensors.get(source_sensors.get(i)).route_weight;
        List<Integer> selectedTargets = new ArrayList<>();
        PriorityQueue<Node> toBeSelectedNode = new PriorityQueue<>(new Comparator<Node>() { //大顶堆，从大到小
            @Override
            public int compare(Node node1, Node node2) {
                if (node1.node_weight<node2.node_weight)
                    return 1;
               else if(node1.node_weight>node2.node_weight)
                   return -1;
                else
                    return 0;
            }
        });
        for (Integer i:source_sensors)
            toBeSelectedNode.offer(sensors.get(i));
        while(selectedTargets.size()!=TARGET_NUM && !toBeSelectedNode.isEmpty()){
            Node temp = toBeSelectedNode.poll();
            res.add(temp.id);
            for (Integer i:temp.NodeCoverTar){
                if(!selectedTargets.contains(i)) {
                    selectedTargets.add(i);
                    target_list.get(i).isCovered = true;
                }
            }
            Queue<Node> auxiliaryQueue = new LinkedList<>();
            auxiliaryQueue.addAll(toBeSelectedNode);
            toBeSelectedNode.clear();
            while (!auxiliaryQueue.isEmpty()){
                Node temp2 = auxiliaryQueue.poll();
                temp2.node_weight = Util.calNodeUncoveredNum(temp2.NodeCoverTar,selectedTargets)/temp2.route_weight;
                toBeSelectedNode.offer(temp2);
            }

        }
        beCoveredTar.clear();beCoveredTar.addAll(selectedTargets);
        return res;
    }

    public static void dijkstra(int start,int end){
        int t = end;
        int[] P =new int[sensors.size()]; //存储得到的最短路径
        double[] D = new double[sensors.size()];//存储源点到其他节点的长度
        int[] visited = new int[sensors.size()];//记录节点是否被访问过
        for (int i=0;i<sensors.size();i++){
            D[i] = graph.edges[start][i];
            P[i] = start;
            visited[i] = 0;
        }
        visited[start] = 1;
        for (int i=1;i<sensors.size();i++){
            double min = INFINITY;
            for (int w=0;w<sensors.size();w++){
                if (visited[w]==0&&D[w]<min){
                    end = w;
                    min = D[w];
                }
            }
            visited[end] = 1;
            for (int w=0;w<sensors.size();w++){
                if (visited[w]==0&&(min+graph.edges[end][w]<D[w])){
                    D[w] = min+graph.edges[end][w];
                    P[w] = end;
                }
            }
        }
        sensors.get(start).routeToSink.add(t);
        do {
            sensors.get(start).routeToSink.add(P[t]);
            t = P[t];
        }while (t!=start);//读出反向路径
        Collections.reverse(sensors.get(start).routeToSink);
        if (sensors.get(start).routeToSink.size()==2){
            double temp = Util.dis_Node_Node(sensors.get(sensors.get(start).routeToSink.get(0)),sensors.get(sensors.get(start).routeToSink.get(1)));
            if (temp>COMMUINCATION_RANAGE)
                sensors.get(start).routeToSink.clear();
        }
        for (int i=0;i<sensors.get(start).routeToSink.size()-1;i++){
            sensors.get(start).route_weight += graph.edges[sensors.get(start).routeToSink.get(i)][sensors.get(start).routeToSink.get(i+1)];
        }



    }

    public static void resetWeight(){//重置权值
        for (int i=0;i<sensors.size();i++){
            sensors.get(i).routeToSink.clear();
            sensors.get(i).node_weight = 0;
            sensors.get(i).route_weight = 0;
            for (int j=0;j<sensors.size();j++){
                if(sensors.get(j).residual_energy<=BATTERY_SIZE*THRESHOLD || Util.dis_Node_Node(sensors.get(i),sensors.get(j))>COMMUINCATION_RANAGE) {//如果节点剩余寿命小于阈值则设为不可达
                    graph.edges[i][j] = INFINITY;
                    graph.edges[j][i] = INFINITY;
                }
                if(sensors.get(j).residual_energy>BATTERY_SIZE*THRESHOLD && Util.dis_Node_Node(sensors.get(i),sensors.get(j))<=COMMUINCATION_RANAGE){
                    graph.edges[i][j] = Util.dis_Node_Node(sensors.get(i),sensors.get(j));
                    graph.edges[j][i] = Util.dis_Node_Node(sensors.get(i),sensors.get(j));

                }
                if (graph.edges[i][j]!=INFINITY){//可以修改这里
                    graph.edges[i][j] = Math.log10(1+sensors.get(j).type+sensors.get(i).type)*Et(sensors.get(i),sensors.get(j),et,b,alpha)*BATTERY_SIZE/sensors.get(i).residual_energy;
                }
                if (i==j)
                    graph.edges[i][j] = 0;
            }
        }
    }

    public static void calEnergyConsumption(List<Integer> treeLeaves){//节点每秒的能量消耗
        for (Integer t:treeLeaves){
            int coverTarNum = sensors.get(t).NodeCoverTar.size();
            sensors.get(t).energy_consumption += coverTarNum*u*Es;//源节点感知能耗
            for(int i=1;i<sensors.get(t).routeToSink.size()-1;i++){
                sensors.get(sensors.get(t).routeToSink.get(i)).energy_consumption += (Er+Et(sensors.get(sensors.get(t).routeToSink.get(i)),
                                                             sensors.get(sensors.get(t).routeToSink.get(i+1)),et,b,alpha))*u*coverTarNum;
            }
        }
    }

    public static void resetSomeValue(){
        for(Node n:sensors){
            n.energy_consumption = 0;
        }
    }

    public static double calInterval(List<Integer> treeLeaves){
        return 0.0;
    }





}
