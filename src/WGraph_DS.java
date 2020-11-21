package ex1.src;

import java.util.*;


public class WGraph_DS implements weighted_graph {
    private HashMap<Integer, node_info> vertices;
    private HashMap<node_info, HashMap<Integer, Double>> edges;
    private int ModeCount = 0;
    private int NumberOfEdges = 0;

    /**
     * Empty constructor for a weighed graph
     */
    public WGraph_DS() {
        vertices = new HashMap<>();
        edges = new HashMap<>();
    }


    /**
     *Computing if this and g1 graph are the same: Having the same nodes collection and the exact same edges in the graph.
     * @param g1
     * @return true if both graphs are the same
     */
    @Override
    public boolean equals(Object g1) {
        if(g1==this)
            return true;
        if (!(g1 instanceof weighted_graph))
            return false;
        weighted_graph g0 = (weighted_graph)g1;
        if(this.nodeSize()!=g0.nodeSize()) return false;
        if(this.edgeSize()!=g0.edgeSize()) return false;
        for(node_info run : g0.getV()){
            if(!this.vertices.containsKey(run.getKey())) return false;
        }
        for(node_info run : g0.getV()){
            for(node_info run1: g0.getV(run.getKey())){
                if(this.getEdge(run.getKey(), run1.getKey()) != g0.getEdge(run.getKey(), run1.getKey()))
                    return false;
            }
        }
        return true;


    }


    /**
     * @param key - the node_id
     * @return node_info represented by "key"
     */
    @Override
    public node_info getNode(int key) {
        if(!vertices.containsKey(key))
        {
            System.err.println("(getNode) The nodes is not in the graph.");
            return null;
        }
        return vertices.get(key);
    }

    /**
     * checking whether or not two nodes are connected
     * @param node1
     * @param node2
     * @return true if the two nodes are connected, otherwise , false.
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if(!vertices.containsKey(node1) || !vertices.containsKey(node2))
            return false;
        return (edges.get(getNode(node1)).get(node2) != null || edges.get(getNode(node2)).get(node1) != null);
    }

    /**
     * This methods returns the weight between two nodes. If these is not an edge, it will return -1.
     * @param node1
     * @param node2
     * @return the weight value that connects the two nodes.
     */
    @Override
    public double getEdge(int node1, int node2) {
        if(edges.get(getNode(node1)).get(node2)== null)
            return -1;
        return edges.get(getNode(node1)).get(node2);
    }

    /**
     * adding node to the graph.
     * @param key
     */
    @Override
    public void addNode(int key) {
        if(!vertices.containsKey(key)){

            try {
                NodeInfo NodeToAdd = new NodeInfo(key);
                vertices.put(key, NodeToAdd);
                HashMap<Integer, Double> EdgesHashMap = new HashMap<>();
                if (edges != null)
                    edges.put(NodeToAdd, EdgesHashMap);
                ModeCount++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Connecting two nodes with an edge weighted "w". Weights equal to zero are meaningless and less than zero not acceptable.
     * @param node1
     * @param node2
     * @param w
     */
    @Override
    public void connect(int node1, int node2, double w) {

        if (!vertices.containsKey(node1) || !vertices.containsKey(node2) || node1 == node2 || w<=0) {
            System.err.println("(connect issue) One or both of the nodes are not in the graph OR both of the nodes key are the same");
            return;
        }
        try {
            if (edges.get(getNode(node1)).get(node2) == null || edges.get(getNode(node2)).get(node1) == null)
                NumberOfEdges++;

            edges.get(getNode(node1)).put(node2, w);
            edges.get(getNode(node2)).put(node1, w);
            ModeCount++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Collection of the graph nodes.
     */
    @Override
    public Collection<node_info> getV() {
        try {
            if (vertices == null)
                return Collections.<node_info>emptyList();

            return vertices.values();
        } catch (Exception e) {
            System.err.println("No vertices");
        }
        return null;
    }

    /**
     * @param node_id
     * @return Collection of the neighbors of the given "node_id".
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        try {
            HashMap<node_info, Double> toReturn = new HashMap<>();
            for (Integer run : edges.get(getNode(node_id)).keySet()) {
                toReturn.put(getNode(run), edges.get(getNode(node_id)).get(run));
            }
            if (toReturn == null)
                return Collections.<node_info>emptyList();

            return toReturn.keySet();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removing a node from the graph and all it's edges.
     * @param key
     * @return the removed node.
     */
    @Override
    public node_info removeNode(int key) {
        if(vertices.containsKey(key)){
            node_info NodeToRemove = getNode(key);
            if (NodeToRemove != null) {
                ArrayList<Integer> keysToremove = new ArrayList();

                for (Integer run :edges.get(getNode(key)).keySet()){
                    keysToremove.add(run);
                }
                for(int i=0;i<keysToremove.size();i++){
                    removeEdge(key, keysToremove.get(i));
                }

            }
            vertices.remove(key);
            edges.remove(NodeToRemove);
            ModeCount++;
            return NodeToRemove;
        }
        return null;
    }

    /**
     * Removing an edge between two nodes. If there is not such an edge, it does nothing.
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if(edges.get(getNode(node1))!=null && edges.get(getNode(node2))!=null) {
            edges.get(getNode(node1)).remove(node2);
            edges.get(getNode(node2)).remove(node1);
            NumberOfEdges--;
            ModeCount++;
        }
    }

    /**
     * @return number of nodes in the graph.
     */
    @Override
    public int nodeSize() {
        return vertices.size();
    }

    /**
     * @return number of total edges in the graph.
     */
    @Override
    public int edgeSize() {
        return NumberOfEdges;
    }

    /**
     * @return ModeCount for the graph. ModeCount counts how many changes the graph has had.
     */
    @Override
    public int getMC() {
        return ModeCount;
    }


    private class NodeInfo implements node_info, Comparable<node_info> {
        private int key;
        private double Tag;

        /**
         * Constructs a node with the given "key".
         * @param key
         */
        public NodeInfo(int key) {
            this.key = key;

        }

        /**
         * @return the key represented this node.
         */
        @Override
        public int getKey() {
            return key;
        }

        /**
         * @return information about this node, key and tag.
         */
        @Override
        public String getInfo() {
            return "Node's key: " + key + ", Tag: " + Tag;
        }

        /**
         * setting this node's key and tag.
         * @param s
         */
        @Override
        public void setInfo(String s) {
            String[] s1 = s.split(", ");
            int A = Integer.parseInt(s1[0].substring(12));
            Double B = Double.parseDouble(s1[1].substring(5));
            this.key = A;
            this.Tag = B;
        }

        /**
         * @return this nodes' tag. A node's tag will be useful for the graph algorithms.
         */
        @Override
        public double getTag() {
            return Tag;
        }

        /**
         * setting this node's tag. A node's tag will be useful for the graph algorithms.
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            Tag = t;
        }

        /**
         * Comparing two nodes by their tags. The smaller value tag will be prioritized.
         * @param o
         * @return
         */
        @Override
        public int compareTo(node_info o) {
            if (o.getTag() > this.getTag()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

}
