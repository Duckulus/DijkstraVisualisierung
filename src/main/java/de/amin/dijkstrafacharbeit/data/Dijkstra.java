package de.amin.dijkstrafacharbeit.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Diese Klasse repräsentiert den Dijkstra algorithmus
 *
 * @author Amin Haddou
 */
public class Dijkstra {
    /**
     * Zuordnung von Knoten zu ihrer gesamten Diszanz zum Start
     */
    private final Map<Vertex, Double> distance;
    /**
     * Zuordnung von Knoten zu ihren Vorgängern
     */
    private final Map<Vertex, Vertex> previous;
    /**
     * Liste aller unbesuchten Knoten
     */
    private final List<Vertex> unvisited;

    /**
     * Instanziiert ein neues {@link Dijkstra} Objekt
     */
    public Dijkstra() {
        distance = new HashMap<>();
        previous = new HashMap<>();
        unvisited = new List<>();
    }

    /**
     * Berechnet den kürzesten Weg zwischen 2 Knoten auf einem Graphen
     * @param graph Der zu bearbeitende Graph
     * @param start Der Startpunkt
     * @param goal Der Zielpunkt
     * @return Eine Liste, welche alle zu übergehenden Knoten auf dem Weg vom Start zum Ziel in geordneter Reihenfolge enthält
     */
    public List<Vertex> shortestPath(Graph graph, Vertex start, Vertex goal) {
        dijkstra(graph, start);
        List<Vertex> path = new List<>();

        Vertex temp = goal;

        path.append(temp);

        while (previous.get(temp) != null) {
            temp = previous.get(temp);
            path.toFirst();
            path.insert(temp);
        }

        return path;
    }

    /**
     * Berechnet die Distanz zu allen Punkten eines Graphs von einem bestimmen Startpunkt
     * @param graph Der betroffene Graph
     * @param start Der Startpunkt
     */
    private void dijkstra(Graph graph, Vertex start) {
        initialize(graph, start);
        while (!unvisited.isEmpty()) { //Abbruchbedingung: Alle felder wurden besucht
            Vertex u = getSmallest(); //Kleinster unbesuchter Knoten als ausgangspunkt

            visit(u); //Besuchter knoten wird als besucht markiert

            List<Vertex> neighbours = getNeighbours(graph, u);
            neighbours.toFirst();
            while (neighbours.hasAccess()) { // Die Distanz zu allen Nachbarn des aktuellen Knoten wird neu berechnet
                if (!isVisited(neighbours.getContent())) {
                    updateDistance(graph, u, neighbours.getContent());
                }
                neighbours.next();
            }
        }
    }

    /**
     *  Setzt die Abstände von allen Knoten, bis auf den Startpunkt, auf Unendlich
     *  und markiert alle Knoten als unbesucht. Dies ist die Grundlage, damit der
     *  Dijkstra Algorithmus operieren kann und muss daher davor ausgeführt werden
     * @param graph Der betroffene Graph
     * @param start Der Startpunkt
     */
    private void initialize(Graph graph, Vertex start) {
        List<Vertex> vertices = graph.getVertices();

        vertices.toFirst();
        while (vertices.hasAccess()) {
            distance.put(vertices.getContent(), Double.MAX_VALUE); //initialisierung aller Knoten mit unendlichem Abstand (Double Limit)
            previous.put(vertices.getContent(), null);


            unvisited.append(vertices.getContent());

            vertices.next();
        }
        distance.put(start, 0.0);

    }

    /**
     * Gibt den unbesuchten Punkt mit der niedrigsten Distanz zurück
     * @return Unbesuchter Punkt mit niedrigster Distanz
     */
    private Vertex getSmallest() {
        Vertex smallest = null;
        double value = Double.MAX_VALUE;

        unvisited.toFirst();
        while (unvisited.hasAccess()) {
            if (distance.get(unvisited.getContent()) < value) {
                value = distance.get(unvisited.getContent());
                smallest = unvisited.getContent();
            }
            unvisited.next();
        }
        return smallest;
    }

    /**
     * Entfernt einen Knoten aus der Liste der unbesuchten Knoten
     * @param vertex Der betroffene Knoten
     */
    private void visit(Vertex vertex) {
        unvisited.toFirst();
        while (unvisited.hasAccess()) {
            if (unvisited.getContent() == vertex) {
                unvisited.remove();
            }
            unvisited.next();
        }
    }

    /**
     * Gibt die benachbarten Knoten eines Knotens in einem Graphen zurück
     * @param graph Der betroffene Graph
     * @param vertex Der betroffene Knoten
     * @return Liste mit allen verbundenen Knoten
     */
    private List<Vertex> getNeighbours(Graph graph, Vertex vertex) {
        List<Vertex> neighbours = new List<>();
        List<Vertex> vertices = graph.getVertices();
        vertices.toFirst();
        while (vertices.hasAccess()) {
            if (graph.getEdge(vertex, vertices.getContent()) != null) {
                neighbours.append(vertices.getContent());
            }
            vertices.next();
        }
        return neighbours;
    }

    /**
     * Gibt zurück ob ein Knoten bereits besucht wurde
     * @param vertex Der betroffene Knoten
     * @return True wenn der Knoten besucht wurde, anderfalls False
     */
    private boolean isVisited(Vertex vertex) {
        unvisited.toFirst();
        while (unvisited.hasAccess()) {
            if (unvisited.getContent() == vertex) {
                return false;
            }
            unvisited.next();
        }
        return true;
    }

    /**
     * Berechnet die Distanz eines Knotens neu basierend auf der Distanz einer seiner Nachbarn
     * und setzt den Nachbarn als Vorgänger
     * @param graph Der betroffene Graph
     * @param u Der benachbarte Knoten
     * @param v Der Knoten der aktualisiert wird
     */
    private void updateDistance(Graph graph, Vertex u, Vertex v) {
        double alternative = distance.get(u) + graph.getEdge(u, v).getWeight();
        if (alternative < distance.get(v)) {
            distance.put(v, alternative);
            previous.put(v, u);
        }
    }

}
