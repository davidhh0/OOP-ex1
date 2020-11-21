# Ex1
## Weighted Graph 
The assignment is about weighted graphs and implementing methods. 
The main class "WGraph_DS" has two collections for the graph vertices and all the edges in the graph and it holds simple methods of weighted graph such as adding,removing and get a node to the graph. Also, it allows connecting two nodes with weight and removing an edge. The class has it's own private class for implementin a node in the graph. Every node has a key (integer) and a tag (represents a total weight distance in the following algorithms).

The second class is "WGraph_Algo" that has several algorithms for graph such as asking whether or not a graph is connected, computing a deep copy of a graph and return it and of course computing the shortest path from a node to another one (shortest path is considered to be the path from source to destination with the least weight value).
The class also allows an user to save a complete graph to a file and load it after.

The last class is a tester for all the methos in both of the classes.
