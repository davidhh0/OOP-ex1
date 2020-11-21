package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.PriorityQueue;

class JunitTest {
    WGraph_DS graph = new WGraph_DS();
    double EPS = 0.0001;

    @Test
    public void AddAndRemoveTest(){
        graph.addNode(1);
        graph.addNode(2);
        int b = graph.getNode(2).getKey();
        int a = graph.removeNode(2).getKey();
        Assertions.assertEquals(a,b);
    }

    @Test
    public void ConnectivityTest(){
        graph.addNode(1);
        graph.addNode(2);
        graph.connect(1,2,3.55);
        Assertions.assertEquals(graph.getEdge(1,2),3.55);
        Assertions.assertEquals(graph.getEdge(2,1),3.55);
        Assertions.assertTrue(graph.hasEdge(1, 2));
        Assertions.assertTrue(graph.hasEdge(2, 1));
    }

    @Test
    public void DeepCopyTest(){

        graph.addNode(1);
        graph.addNode(2);
        graph.connect(1,2,3.55);
        weighted_graph_algorithms graph2 = new WGraph_Algo();
        graph2.init(graph);
        weighted_graph NewGraph = graph2.copy();
        Assertions.assertEquals(NewGraph.nodeSize(),graph.nodeSize());
        Assertions.assertEquals(NewGraph.getEdge(1,2), graph.getEdge(2,1));

    }

    @Test
    public void isConnectedTest(){
        graph.addNode(1);
        graph.addNode(2);
        graph.connect(1,2,3.55);
        graph.addNode(3);
        graph.connect(1,3,6);
        weighted_graph_algorithms graph2 = new WGraph_Algo();
        graph2.init(graph);
        Assertions.assertTrue(graph2.isConnected());

    }
    @Test
    public void PQcheck() {
        PriorityQueue<node_info> PQ = new PriorityQueue<node_info>();
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.getNode(1).setTag(5);
        graph.getNode(2).setTag(10);
        graph.getNode(3).setTag(2);
        PQ.offer(graph.getNode(1));
        PQ.offer(graph.getNode(2));
        PQ.offer(graph.getNode(3));
        Assertions.assertEquals(PQ.poll().getKey(), 3);
        Assertions.assertEquals(PQ.poll().getKey(), 1);
    }
    @Test
    public void Dijkstra(){
        graph.addNode(0);
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);
        graph.connect(0,1,2);
        graph.connect(1,2,1);
        graph.connect(1,4,6);
        graph.connect(2,3,2);
        graph.connect(3,4,1);
        System.out.println(graph.getV(2).size());
        weighted_graph_algorithms graph2 = new WGraph_Algo();
        graph2.init(graph);
        LinkedList<node_info> check = (LinkedList<node_info>) graph2.shortestPath(0,4);
        Assertions.assertEquals(graph2.shortestPathDist(0,4),6);



    }
    @Test
    public void Dijkstra2() {
        graph.addNode(0);
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);
        graph.addNode(5);
        graph.addNode(6);
        graph.addNode(7);
        graph.connect(0,1,1);
        graph.connect(0,2,2);
        graph.connect(0,3,5);

        graph.connect(1,4,4);
        graph.connect(1,5,11);

        graph.connect(2,4,9);
        graph.connect(2,5,5);
        graph.connect(2,6,16);

        graph.connect(3,6,2);

        graph.connect(4,7,18);

        graph.connect(5,7,13);

        graph.connect(6,7,2);

// =======================================================================================

        weighted_graph_algorithms graph2 = new WGraph_Algo();
        graph2.init(graph);
        LinkedList<node_info> check = (LinkedList<node_info>) graph2.shortestPath(0,7);
        Assertions.assertEquals(check.size(),4);
        Assertions.assertEquals(graph2.shortestPathDist(0,7),9);
        Assertions.assertEquals(check.getLast().getKey(),7);

    }


    @Test
    public void BigGraph(){
        long start= System.currentTimeMillis();
        for(int i=0;i<100000;i++)
            graph.addNode(i);
        for (int i=0;i<100000;i++){
            double temp = Math.random()*20;
            int first = (int)(Math.random()*100000);
            int second = (int)(Math.random()*100000);
            graph.connect(first,second,temp);
        }

        weighted_graph_algorithms graph2 = new WGraph_Algo();
        graph2.init(graph);
        graph2.save("Save.txt");

        WGraph_DS lol = new WGraph_DS();
        weighted_graph_algorithms lol2 = new WGraph_Algo();
        lol2.init(lol);
        lol2.load("Save.txt");



        long finish =System.currentTimeMillis();
        long timeElapsed = finish - start;
        boolean check = timeElapsed/1000 < 8;
        Assertions.assertTrue(check);

        Assertions.assertEquals(graph.nodeSize(),lol2.getGraph().nodeSize());
        Assertions.assertEquals(graph.edgeSize(),lol2.getGraph().edgeSize());

    }

    @Test
    public void Save(){
        graph.addNode(0);
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);
        graph.connect(0,1,2);
        graph.connect(1,2,1);
        graph.connect(1,4,6);
        graph.connect(2,3,2);
        graph.connect(3,4,1);

        weighted_graph_algorithms graph2 = new WGraph_Algo();
        graph2.init(graph);
        graph2.save("Save.txt");



    }

    @Test
    public void Load(){
        graph.addNode(0);
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);
        graph.connect(0,1,2);
        graph.connect(1,2,1);
        graph.connect(1,4,6);
        graph.connect(2,3,2);
        graph.connect(3,4,1);


        weighted_graph_algorithms graph2 = new WGraph_Algo();
        graph2.init(graph);
        WGraph_DS lol = new WGraph_DS();
        graph2.load("Save.txt");

        Assertions.assertEquals(graph.edgeSize(),graph2.getGraph().edgeSize());
        Assertions.assertEquals(graph.nodeSize(),graph2.getGraph().nodeSize());


    }







}






