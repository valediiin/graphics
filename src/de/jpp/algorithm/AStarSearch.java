package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.SearchResult;
import de.jpp.algorithm.interfaces.SearchStopStrategy;
import de.jpp.model.GraphImpl;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;
import de.jpp.model.interfaces.WeightedGraph;
import de.jpp.algorithm.interfaces.EstimationFunction;
import org.w3c.dom.Node;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class AStarSearch<N, A, G extends WeightedGraph<N, A>> extends BreadthFirstSearchTemplate<N, A, G>  {

    private final N dest;
    private final EstimationFunction<N> estFunc;

    public AStarSearch(G graph, N start, N dest, EstimationFunction<N> estFunc){

        this.graph = graph;
        this.start = start;
        this.dest = dest;
        this.estFunc = estFunc;
    }

    public static void main(String[] args) {

        TwoDimGraph graph = new TwoDimGraph();


        XYNode nodeA = new XYNode("A", 0, 0);
        XYNode nodeB = new XYNode("B", 1, 1);
        XYNode nodeC = new XYNode("C", 2, 2);
        XYNode nodeD = new XYNode("D", 3, 3);
        XYNode nodeE = new XYNode("E", 4, 4);


        //Testing Node expansion on (-4|0) <-> (-3|0) <-> (-2|0) <-> (-1|0) <-> (0|0) <-> (1|1) <-> (2|2) <-> (3|3) <-> (4|4) + (0|0) <-> (-1.5|-1.5) <-> (-2.5|-2.5) <-> (-3.5|-3.5)
        // (A* Algorithm) expected:<[0|0, 1|1, 2|2, 3|3, 4|4]> but was:<[0|0, 1|1, -1|0, -1.5|-1.5, 2|2, -2|0, -2.5|-2.5, -3|0, 3|3, -3.5|-3.5, -4|0]>


        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);

        graph.addEdge(nodeA, nodeB, Optional.of(1.0));
        graph.addEdge(nodeB, nodeC, Optional.of(1.0));
        graph.addEdge(nodeC, nodeD, Optional.of(1.0));
        graph.addEdge(nodeD, nodeE, Optional.of(1.0)); // Längerer Pfad


        EstimationFunction<XYNode> estFunc = (n1, n2) -> n1.euclidianDistTo(n2);


        AStarSearch<XYNode, Double, TwoDimGraph> aStarSearch = new AStarSearch<>(graph, nodeA, nodeE, estFunc);


        SearchResult<XYNode, Double> result = aStarSearch.findPaths(n -> n.equals(nodeE));


        result.getPathTo(nodeE).ifPresent(path -> {
            System.out.println("Pfad von " + nodeA + " nach " + nodeE + ":");
            path.forEach(edge -> System.out.println(edge.getStart() + " -> " + edge.getDestination() + " (Distanz: " + edge.getAnnotation().orElse(null) + ")"));
        });
    }

    @Override
    public SearchResult<N, A> findPaths(SearchStopStrategy<N> type) {
        Comparator<N> comp = (n1, n2) ->
        {
            if (n1 == null ^ n2 == null) {
                return n1 == null ? -1 : 1;
            }

            return (int) (result.getInformation(n1).getDistance() - result.getInformation(n2).getDistance());
        };

        List<N> opens = new ArrayList<>();
        Map<N, Double> sScore = graph.getNodes().stream()
                .collect(Collectors.toMap(e -> e, e -> Double.MAX_VALUE));
        Map<N, Double> dScore = graph.getNodes().stream()
                .collect(Collectors.toMap(e -> e, e -> Double.MAX_VALUE));

        opens.add(start);
        sScore.put(start, 0d);
        dScore.put(start, estFunc.getEstimatedDistance(start, dest));
        NodeInformation<N, A> sInfo = new NodeInformation<>();
        sInfo.setPredecessor(null);
        sInfo.setDistance(0);
        result.close(start, sInfo);

        while (!(opens.isEmpty() || stopped)) {
            N cur = opens.stream().min(comp).get();

            // zusätzliche Checks

            if (cur == null) {
                break;
            }

            if (cur.equals(dest) || type.stopSearch(cur)) {
                break;
            }

            opens.remove(cur);
            result.close(cur, result.getInformation(cur));

            /*if(type.stopSearch(cur) || stopped){
                return result;
            }*/

           /* graph.getNeighbours(cur).forEach(e ->
            {
                double tentativeSScore = sScore.get(cur) + graph.getDistance(e);
                NodeInformation<N, A> info = new NodeInformation<>();
                info.setPredecessor(e);
                info.setDistance(tentativeSScore);
                result.open(e.getDestination(), info);
            });

            graph.getNeighbours(cur).forEach(e ->
            {
                double tentativeSScore = sScore.get(cur) + graph.getDistance(e);
                result.close(e.getDestination(), result.getInformation(e.getDestination()));

                if(tentativeSScore < sScore.get(e.getDestination())){
                    sScore.put(e.getDestination(), tentativeSScore);
                    dScore.put(e.getDestination(), tentativeSScore + estFunc.getEstimatedDistance(e.getDestination(), dest));

                    if(!opens.contains(e.getDestination())) {
                        opens.add(e.getDestination());
                        NodeInformation<N, A> info = new NodeInformation<>();
                        info.setPredecessor(e);
                        info.setDistance(tentativeSScore);
                        result.open(e.getDestination(), info);
                    }
                }
            });
        }*/

            for (Edge<N, A> e : graph.getNeighbours(cur)) {
                N neighbor = e.getDestination();
                double tentativeSScore = sScore.get(cur) + graph.getDistance(e);

                if (tentativeSScore < sScore.get(neighbor)) {
                    // Aktualisieren sScore und dScore für Nachbarn
                    sScore.put(neighbor, tentativeSScore);
                    dScore.put(neighbor, tentativeSScore + estFunc.getEstimatedDistance(neighbor, dest));

                    // Aktualisieren NodeInformation und öffnen Nachbarn
                    NodeInformation<N, A> info = new NodeInformation<>();
                    info.setPredecessor(e);
                    info.setDistance(tentativeSScore);
                    result.setInformation(neighbor, info); // NodeInformation korrekt aktualisieren
                    if (!opens.contains(neighbor)) {
                        opens.add(neighbor);
                        result.open(neighbor, info); // Listener benachrichtigen
                    }
                }
            }
        }
        return result;
    }
}
