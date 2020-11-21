package ex1.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {
    weighted_graph g;

    /**
     * Initialize a graph to the algorithms class in order to use the methods.
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        this.g=g;
    }

    /**
     * @return The graph representing this class.
     */
    @Override
    public weighted_graph getGraph() {
        return g;
    }

    /**
     * Computing a deep copy of this graph and return it.
     * The method copying the nodes first and then all the edges from this graph.
     * @return the copied new graph.
     */
    @Override
    public weighted_graph copy() {
        WGraph_DS NewGraph=new WGraph_DS();

        for (node_info run : g.getV() ) {
            NewGraph.addNode(run.getKey());
        }

        for(node_info run : g.getV()) {
            if(NewGraph.edgeSize()==g.edgeSize()) break;
            for (node_info run1 : g.getV(run.getKey()))
                NewGraph.connect(run.getKey(), run1.getKey(), g.getEdge(run.getKey(), run1.getKey()));

        }
        return NewGraph;

    }

    /**
     * return true iff there is a path from any node to every other node.
     * the method inserts a node into a Stack and has a HashMap called "visited".
     * for each iteration pop a node from the Stack (and mark it as visited in "visited")
     * and for each of it's neighbors, check:
     * if the neighbor has been visited (according to "visited") do nothing
     * else , the neighbor hasn't been visited and add it to the Stack in order to check it's neighbors as well
     * in the end, we wish that the number of visited nodes will be the same as the number of the nodes in the graph.
     * if so , return true. if not , meaning at least one node hasn't been visited , the graph in not connected.
     * @return true if the graph is connected, otherwise , return false.
     */
    @Override
    public boolean isConnected() {
        if(g==null || g.nodeSize()==1) return true;
        HashMap<Integer,Boolean> visited = new HashMap<>();
        Stack<node_info> stack = new Stack<>();
        for (node_info run : g.getV()){
            stack.push(run);
            break;
        }
        while(!stack.isEmpty()){
            node_info temp = stack.pop();
            visited.put(temp.getKey(),true);
            for (node_info run1 : g.getV(temp.getKey())){
                if(visited.get(run1.getKey())==null){
                    visited.put(run1.getKey() ,true);
                    stack.push(run1);
                }
            }
        }
        return (visited.size()==g.nodeSize());

    }

    @Override
    public double shortestPathDist(int src, int dest) {
        if(getGraph().getNode(src) == null || getGraph().getNode(dest)==null) {
            System.err.println("One or both of the nodes are not in the graph!!");
            return -1;
        }
        LinkedList<node_info> answer = (LinkedList<node_info>) shortestPath(src,dest);
        if(answer==null) return -1;
        return g.getNode(dest).getTag();


    }

    /**
     * This method computes the shortest path between two nodes. A shortest path is the minimum sum of edge weights from
     * "src" to "dest". The method has a "visited" and "parent" collection. The first one purpose is to know whether or not
     * a node has been visited and the second collection's purpose is to know the path we came through the visited nodes.
     * The method has a Priority Queue as well. A priority is given to the node with the smallest value of the tag.
     * The tag represents the total weight from "src" to it's node.
     * First, we initialize all the node's tag in the graph to infinity and the "src" node to 0 and insert "src" to the PQ.
     * Then, we search for the "dest" node in graph, but we traverse through the graph in a way that we pop from the
     * PQ the node with the smallest total weight from "src" to it. The method calculates the path from "src" to it's
     * neighbors and their neighbors... by adding the edge weight to the node's tag.
     * When we hit the desired "dest" node, we made sure that the have traversed through the path with the smallest value
     * in the graph thanks to the PriorityQueue that polls the node with smallest tag value not matter the order of the
     * inserting.
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        PriorityQueue<node_info> PQ = new PriorityQueue<>();
        HashMap<node_info,Boolean> visited = new HashMap<>();
        HashMap<node_info,node_info> parent = new HashMap<>();
        LinkedList<node_info> answer = new LinkedList<>();
        node_info sofi = null;
        boolean flag= true;
        for (node_info run : g.getV())
            run.setTag(Double.POSITIVE_INFINITY);
        g.getNode(src).setTag(0);
        PQ.offer(g.getNode(src));

        while(!PQ.isEmpty()&&flag){
            node_info temp = PQ.poll();
            if(visited.get(temp)==null) { // means temp wasn't visited yet
                if (temp.getKey() == dest) { sofi = temp ; flag=false;}
                visited.put(temp,true);
                for (node_info run : g.getV(temp.getKey())){
                    if(visited.get(run)==null){ // means run wasn't visited
                        double path = temp.getTag() + g.getEdge(temp.getKey(), run.getKey());
                        if (path < run.getTag()){
                            run.setTag(path);
                            parent.put(run,temp);
                            PQ.offer(run);
                        }
                    }
                }
            }

        }
        if(sofi==null) return answer;
        if(sofi.getKey()!=dest) return answer;
        boolean flag1=true;
        try{
            answer.add(g.getNode(dest));
            while(flag1) {
                if (sofi.getKey() == src) {
                    flag1 = false;
                    break;
                }

                node_info answersfather = parent.get(sofi);
                answer.addFirst(answersfather);
                sofi = parent.get(sofi);
            }
        }
        catch (Exception e) {
            System.out.println("Wasn't found");}
        return answer;


    }

    /**
     * The method saves the graph into a file so we will be able to load the full graph.
     * The methods first saves the nodes and then all the edges with the weights.
     * @param file - the file name (may include a relative path).
     * @return true if saving completed, otherwise return false.
     */
    @Override
    public boolean save(String file) {
        try{
            PrintWriter pw = new PrintWriter(new File(file));
            pw.write("Graph information: \n");
            pw.write("Mode Count: "+g.getMC() + " ,Total vertices: "+ g.nodeSize() + " ,Total edges: "+g.edgeSize() +"\n");
            pw.write("Vertices: \n");
            StringBuilder sb = new StringBuilder();
            for(node_info run : g.getV()){
                String str = run.getInfo();
                pw.write(str);
                pw.write("\n");
            }
            pw.write("Edges: \n");
            for(node_info run: g.getV()){
                sb.setLength(0);
                for(node_info run1: g.getV(run.getKey())){
                    sb.append(run1.getKey());
                    sb.append("(");
                    sb.append(g.getEdge(run.getKey(), run1.getKey()));
                    sb.append("), ");
                }
                StringBuilder sb1 = new StringBuilder();
                sb1.append("Node's key: ");
                sb1.append(run.getKey());
                sb1.append(" is connected with: ");
                sb1.append(sb);
                sb1.append("\n");
                pw.write(sb1.toString());
            }
            pw.close();
            System.out.println("DONE");
            return true;

        }
        catch(Exception e){
            e.printStackTrace();
        }


        return false;
    }

    /**
     * The method loads from a file the full graph and put it as the representing graph of this class.
     * @param file - file name
     * @return true if loading succeed, otherwise return false.
     */
    @Override
    public boolean load(String file) {
        WGraph_DS NewGraph = new WGraph_DS();
        String line="";
        int neighbor;

        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();br.readLine();br.readLine();
            while(!(line = br.readLine()).equals("Edges: ")){
                String[] s1 = line.split(", ");
                int A = Integer.parseInt(s1[0].substring(12));
                Double B = Double.parseDouble(s1[1].substring(5));
                NewGraph.addNode(A);
                NewGraph.getNode(A).setTag(B);
            }
            while((line=br.readLine())!=null){
                String[] base = line.split(": ");
                int A = Integer.parseInt(base[1].split(" ")[0]);
                if( base.length >2 ){
                    String[] NumberOfNeighbors = base[2].split(",");
                    for (int i = 0; i < NumberOfNeighbors.length - 1; i++) {
                        String[] Nei = NumberOfNeighbors[i].split("\\(");
                        if (i == 0)
                            neighbor = Integer.parseInt(Nei[0]);
                        else neighbor = Integer.parseInt(Nei[0].substring(1));


                        String[] Wei = Nei[1].split("\\)");
                        double weight = Double.parseDouble(Wei[0]);
                        NewGraph.connect(A, neighbor, weight);
                    }
                }


            }
            g = NewGraph;
            return true;
        }

        catch (Exception e){
            e.printStackTrace();
        }


        return false;
    }
}
