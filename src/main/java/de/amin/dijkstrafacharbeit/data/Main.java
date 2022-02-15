package de.amin.dijkstrafacharbeit.data;

public class Main {

    public static void main(String[] args) {
        Graph graph = new Graph();
        Vertex a = new Vertex("a");
        Vertex b = new Vertex("b");
        Vertex c = new Vertex("c");
        Vertex d = new Vertex("d");
        Vertex x = new Vertex("x");

        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(c);
        graph.addVertex(d);
        graph.addVertex(x);
        graph.addEdge(new Edge(a,b,1));
        graph.addEdge(new Edge(b,c,1));
        graph.addEdge(new Edge(a,d,1));
        graph.addEdge(new Edge(c,x, 5));
        graph.addEdge(new Edge(d,x,1));


        Dijkstra dijkstra = new Dijkstra();
        List<Vertex> path = dijkstra.shortestPath(graph, a, b);

        path.toFirst();
        while (path.hasAccess()) {
            System.out.println(path.getContent().getID());
            path.next();
        }


    }

}
