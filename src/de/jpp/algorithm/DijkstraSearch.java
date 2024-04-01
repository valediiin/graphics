package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.NodeStatus;
import de.jpp.algorithm.interfaces.SearchResult;
import de.jpp.algorithm.interfaces.SearchStopStrategy;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.WeightedGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DijkstraSearch<N, A, G extends WeightedGraph<N, A>> extends BreadthFirstSearchTemplate<N, A, G> {

    public DijkstraSearch(G graph, N start) {
        this.graph = graph;
        this.start = start;
    }

    public static void main(String[] args) {

        TwoDimGraph graph = new TwoDimGraph();

        // Knoten hinzufügen
        XYNode nodeA = new XYNode("A", 0, 0);
        XYNode nodeB = new XYNode("B", 1, 1);
        XYNode nodeC = new XYNode("C", 2, 2);
        XYNode nodeD = new XYNode("D", 3, 3);

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);

        // Kanten mit Distanzen basierend auf ihrer Position im 2D-Raum hinzufügen
        graph.addEdge(nodeA, nodeB, Optional.of(1.0));
        graph.addEdge(nodeB, nodeC, Optional.of(2.0));
        graph.addEdge(nodeC, nodeD, Optional.of(3.0));
        graph.addEdge(nodeA, nodeC, Optional.of(4.0));


        DijkstraSearch<XYNode, Double, TwoDimGraph> dijkstraSearch = new DijkstraSearch<>(graph, nodeA);

        SearchResult<XYNode, Double> searchResult = dijkstraSearch.findPaths(node -> node.equals(nodeB));

        Optional<List<Edge<XYNode, Double>>> pathOpt = searchResult.getPathTo(nodeB);

        if (pathOpt.isPresent()) {
            List<Edge<XYNode, Double>> path = pathOpt.get();
            System.out.println("Pfad von " + nodeA + " zu " + nodeB + ":");
            for (Edge<XYNode, Double> edge : path) {
                System.out.println(edge.getStart() + " -> " + edge.getDestination() + " (Distanz: " + edge.getAnnotation().orElse(null) + ")");
            }
        } else {
            System.out.println("Kein Pfad von " + nodeA + " zu " + nodeB + " gefunden.");
        }
    }

    @Override
    public SearchResult<N, A> findPaths(SearchStopStrategy<N> type) {


        /*graph.getNodes().forEach(n ->
        {
            NodeInformation<N, A> info = new NodeInformation<>();
            info.setDistance(Double.MAX_VALUE);
            info.setPredecessor(null);
            result.setInformation(n, info);
        });

        NodeInformation<N, A> sInfo = new NodeInformation<>();
        sInfo.setDistance(0);
        sInfo.setPredecessor(null);
        result.open(start, sInfo);

        Comparator<N> comp = (n1, n2) ->
        {
            if(n1 == null ^ n2 == null){
                return n1 == null ? -1 : 1;
            }

            return Double.compare(result.getInformation(n1).getDistance(), result.getInformation(n2).getDistance());
        };

        List<N> list = new ArrayList<>(graph.getNodes());

        while (!(list.isEmpty() || stopped)) {
            N cur = list.stream()
                    .min(comp).get();
            list.remove(cur);
            result.close(cur, result.getInformation(cur));

            if (type.stopSearch(cur) || stopped) {
                return result;
            }

            for (Edge<N, A> neigh : graph.getNeighbours(cur)) {
                if (list.contains(neigh.getDestination())) {
                    NodeInformation<N, A> info = new NodeInformation<>();
                    info.setPredecessor(neigh);
                    info.setDistance(result.getInformation(cur).getDistance() + graph.getDistance(neigh));
                    result.open(neigh.getDestination(), info);
                }
            }

            graph.getNeighbours(cur).stream()
                    .map(Edge::getDestination)
                    .filter(list::contains)
                    .forEach(e ->
                    {
                        result.close(e, result.getInformation(e));
                        openIfShorter(cur, result.getInformation(e));
                    });
        }

        return result;*/


        graph.getNodes().forEach(n -> {
            NodeInformation<N, A> info = new NodeInformation<>();
            info.setDistance(Double.MAX_VALUE);
            info.setPredecessor(null);
            result.setInformation(n, info);
        });

        NodeInformation<N, A> sInfo = new NodeInformation<>();
        sInfo.setDistance(0);
        sInfo.setPredecessor(null);
        //result.setInformation(start, sInfo);
        result.open(start, sInfo); // Listener benachrichtigen

        Comparator<N> comp = Comparator.comparingDouble(n -> result.getInformation(n).getDistance());
        List<N> list = new ArrayList<>(graph.getNodes());

        while (!list.isEmpty() && !stopped) {
            N cur = list.stream().min(comp).orElse(null);
            if (cur == null) break; // Überprüfung
            list.remove(cur);
            result.close(cur, result.getInformation(cur));

            if (type.stopSearch(cur) || stopped) {
                break;
            }

            for (Edge<N, A> neigh : graph.getNeighbours(cur)) {
                if (list.contains(neigh.getDestination())) {
                    double newDist = result.getInformation(cur).getDistance() + graph.getDistance(neigh);
                    if (newDist < result.getInformation(neigh.getDestination()).getDistance()) {
                        NodeInformation<N, A> info = new NodeInformation<>();
                        info.setPredecessor(neigh);
                        info.setDistance(newDist);
                        /*if (result.getNodeStatus(neigh.getDestination()) != NodeStatus.OPEN) {
                            result.open(neigh.getDestination(), info); // Listener benachrichtigen
                        } else {
                            result.setInformation(neigh.getDestination(), info);
                        }*/

                        result.setInformation(neigh.getDestination(), info);
                        result.open(neigh.getDestination(), info);
                    }
                }
            }
        }

        return result;
    }
}
