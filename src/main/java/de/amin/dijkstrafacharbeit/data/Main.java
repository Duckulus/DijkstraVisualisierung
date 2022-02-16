package de.amin.dijkstrafacharbeit.data;

public class Main {

    public static void main(String[] args) {
        Graph graph = new Graph();
        Vertex a = new Vertex("a");
        Vertex b = new Vertex("b");
        Vertex c = new Vertex("c");
        Vertex d = new Vertex("d");

        Edge e1 = new Edge(a,b,2);
        Edge e2 = new Edge(a,c,1);
        Edge e3 = new Edge(b,d,5);
        Edge e4 = new Edge(c,d,5);

        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(c);
        graph.addVertex(d);

        graph.addEdge(e1);
        graph.addEdge(e2);
        graph.addEdge(e3);
        graph.addEdge(e4);

        Dijkstra dijkstra = new Dijkstra();
//        List<Vertex> path = dijkstra.shortestPath(graph, a, d);
//
//        path.toFirst();
//        while (path.hasAccess()) {
//            System.out.println(path.getContent().getID());
//            path.next();
//        }

        System.out.println(dijkstra.toPathString(dijkstra.shortestPath(graph, a, d)));


    }

}
