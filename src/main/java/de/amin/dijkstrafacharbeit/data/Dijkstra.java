package de.amin.dijkstrafacharbeit.data;

import java.util.HashMap;
import java.util.Map;

class Dijkstra {

    /**
     * Der zu bearbeitende Graph
     */
    private final Graph graph;
    /**
     * Der Ausgangsknoten der Wegfindung
     */
    private final Vertex start;
    /**
     * Der Zielknoten der Wegfindung
     */
    private final Vertex goal;
    /**
     * Liste aller unbesuchten Knoten
     */
    private final List<Vertex> unvisited;
    /**
     * Zuordnung aller Knoten und ihrer Distanz zum Ursprung
     */
    private final Map<Vertex, Double> distance;
    /**
     * Zuordnung aller Knoten zu ihrem vorgänger
     */
    private final Map<Vertex, Vertex> previous;

    /**
     * Initialisiert ein Dijkstra Objekt, welches der Findung des kürzesten
     * Pfads zwischen zwei Knoten auf einem Graphen dient
     * @param graph Der zu bearbeitende Graph
     * @param start Der Startknoten
     * @param goal Der Zielknoten
     */
    public Dijkstra(Graph graph, Vertex start, Vertex goal) {
        this.graph = graph;
        this.start = start;
        this.goal = goal;
        unvisited = new List<>();
        distance = new HashMap<>();
        previous = new HashMap<>();

        List<Vertex> vertices = graph.getVertices();
        vertices.toFirst();
        while (vertices.hasAccess()) {
            unvisited.append(vertices.getContent()); //Markieren aller Knoten als unbesucht
            distance.put(vertices.getContent(), Double.MAX_VALUE); //Zuweisung der Distanz aller Knoten als Unendlich(Maximaler Integer Wert)
            vertices.next();
        }

        distance.put(start, 0D); //Startpunkt wird die Distanz 0 zugewiesen
        unvisited.toFirst();
        while (unvisited.hasAccess()) {
            if (unvisited.getContent() == start) {
                unvisited.remove(); //Entfernen des Startpunkts aus der Liste der Unbesuchten Knoten
            }
            unvisited.next();
        }
    }

    public String kuerzesterWeg() {
        Vertex k = start;
        while (!goalReached()) {
            List<Vertex> vertices = graph.getVertices();
            vertices.toFirst();
            while (vertices.hasAccess()) {
                if(graph.getEdge(vertices.getContent(), k)!=null) {
                    distance.put(vertices.getContent(),distance.get(k) + graph.getEdge(vertices.getContent(),k).getWeight());
                }
                vertices.next();
            }

            Vertex temp = k;
            k = knotenMitKleinsterDistanz(k);
            previous.put(k, temp);

            unvisited.toFirst();
            while (unvisited.hasAccess()) {
                if(unvisited.getContent()==k) {
                    unvisited.remove();
                }
                unvisited.next();
            }

            if(goalReached()) {
                Vertex current = k;
                String pathString = "";
                List<Vertex> vertexPath = new List<>();
                vertexPath.append(current);
                while (previous.get(current)!=null){
                    vertexPath.append(previous.get(current));
                    current = previous.get(current);
                }
                vertexPath = reverse(vertexPath);
                vertexPath.toFirst();

                vertexPath.toFirst();
                while (vertexPath.hasAccess()) {
                    pathString+=vertexPath.getContent().getID();
                    vertexPath.next();
                    if(vertexPath.hasAccess()) {
                        pathString+= "  ->  ";
                    }
                }
                pathString+="  Total Cost: " + distance.get(goal);
                return pathString;
            }
        }
        return "No Path Found";
    }

    /**
     * Diese Methode gibt den Knoten mit der kleinsten direkten Distanz zum Ursprungsknoten zurück
     * @param origin Der Ursprungsknoten
     * @return Den Knoten mit der kürzesten direkten Distanz zu origin, oder null, falls keine Direkte Verbindung besteht.
     */
    private Vertex knotenMitKleinsterDistanz(Vertex origin) {
        Vertex k = null;

        double value = Double.MAX_VALUE; //Zwischenspeicher um aktuell kleinsten wert als Vergleich zu speichern
        unvisited.toFirst();
        while (unvisited.hasAccess()) {
            Edge edge = graph.getEdge(origin, unvisited.getContent());
            if (edge != null && edge.getWeight() < value) {
                value = edge.getWeight();
                k = unvisited.getContent();
            }
            unvisited.next();
        }

        return k;
    }

    private boolean isVisited(Vertex vertex) {
        unvisited.toFirst();
        while (unvisited.hasAccess()) {
            if (unvisited.getContent()==vertex) {
                return false;
            }
            unvisited.next();
        }
        return true;
    }

    /**
     * Diese Methode überprüft ob das Ziel erreicht ist,
     * indem sie schaut ob der Zielknoten in der Liste der unbesuchten
     * Knoten ist
     * @return True wenn das Ziel erreicht ist
     */
    private boolean goalReached() {
        boolean reached = true;

        unvisited.toFirst();
        while (unvisited.hasAccess()) {
            if(unvisited.getContent()==goal) {
                reached=false;
            }
            unvisited.next();
        }

        return reached;
    }


    public<T> List<T> reverse(List<T> toReverse) {
        List<T> reversed = new List<>();
        toReverse.toLast();
        while (toReverse.hasAccess()) {
            reversed.append(toReverse.getContent());
            toReverse.remove();
            toReverse.toLast();
        }

        return reversed;
    }

}
