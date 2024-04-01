package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.NodeStatus;
import de.jpp.algorithm.interfaces.SearchResult;
import de.jpp.algorithm.interfaces.SearchStopStrategy;
import de.jpp.model.GraphImpl;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Graph;

import java.util.Optional;
import java.util.Stack;

public class DepthFirstSearch<N, A, G extends Graph<N, A>> extends SearchAlgorithmTemplate<N, A, G> {

    public DepthFirstSearch(G graph, N start) {
        this.graph = graph;
        this.start = start;
    }

    public static void main(String[] args) {

        XYNode xyNode1 = new XYNode("", 1, 1);
        XYNode xyNode2 = new XYNode("", 1, 1);
        XYNode xyNode3 = new XYNode("", 3, 3);
        XYNode xyNode4 = new XYNode("", 4, 4);


        TwoDimGraph twoDimGraph = new TwoDimGraph();


        twoDimGraph.addEdge(xyNode1, xyNode2);
        twoDimGraph.addEdge(xyNode2, xyNode3);
        twoDimGraph.addEdge(xyNode3, xyNode4);
        twoDimGraph.addEdge(xyNode1, xyNode4);

        DepthFirstSearch depthFirstSearch = new DepthFirstSearch(twoDimGraph, xyNode1);

        System.out.println(depthFirstSearch.findPaths(new StartToDestStrategy(xyNode1)).getPathTo(xyNode1));
        System.out.println("-".repeat(60));
        System.out.println(depthFirstSearch.findPaths(new StartToDestStrategy(xyNode1)).getPathTo(xyNode1));
        //System.out.println(depthFirstSearch.findPaths(new StartToDestStrategy(xyNode3)).getPathTo(xyNode4)); // hier sollte der Pfad eigentlich ausgegeben werden

        /*
        Beginne Suche von: '' at pos (1.0|1.0)
        Debugging, Öffnen des Knotens '' at pos (1.0|1.0)
        Debugging, Öffnen des Knotens '' at pos (2.0|2.0)
        Debugging, Hinzufügen von Nachbarn zum Stack: '' at pos (2.0|2.0) from  '' at pos (1.0|1.0)
        Debugging, Schließen des Knotens: '' at pos (1.0|1.0)
        Debugging, Öffnen des Knotens '' at pos (3.0|3.0)
        Debugging, Hinzufügen von Nachbarn zum Stack: '' at pos (3.0|3.0) from  '' at pos (2.0|2.0)
        Debugging, Schließen des Knotens: '' at pos (2.0|2.0)
        Debugging, Schließen des Knotens: '' at pos (3.0|3.0)
        Debugging, Zurückverfolgen von: '' at pos (3.0|3.0) zu '' at pos (2.0|2.0)
        Debugging, Zurückverfolgen von: '' at pos (2.0|2.0) zu '' at pos (1.0|1.0)
        Optional[['' at pos (1.0|1.0)->'' at pos (2.0|2.0) (d: ?), '' at pos (2.0|2.0)->'' at pos (3.0|3.0) (d: ?)]]

        Beginne Suche von: '' at pos (1.0|1.0)
        Debugging, Öffnen des Knotens '' at pos (1.0|1.0)
        Debugging, Schließen des Knotens: '' at pos (1.0|1.0)
        Debugging, Zurückverfolgen von: '' at pos (3.0|3.0) zu '' at pos (2.0|2.0)
        Debugging, Zurückverfolgen von: '' at pos (2.0|2.0) zu '' at pos (1.0|1.0)
        Optional[['' at pos (1.0|1.0)->'' at pos (2.0|2.0) (d: ?), '' at pos (2.0|2.0)->'' at pos (3.0|3.0) (d: ?)]]
         */
    }

    @Override
    public SearchResult<N, A> findPaths(SearchStopStrategy<N> type) {

       /* result.clear();

        Stack<N> stack = new Stack<>();
        NodeInformation<N, A> sInfo = new NodeInformation<>();
        sInfo.setPredecessor(null);
        sInfo.setDistance(0);
        result.open(start, sInfo);
        stack.push(start);

        while (!(stack.isEmpty() || stopped)){
            N cur = stack.pop();
            //System.out.println(cur);
            if(result.getNodeStatus(cur) != NodeStatus.CLOSED){
                System.out.println("Graph:," + graph);
                System.out.println("Result:" + result.getAllKnownNodes());
                graph.getNeighbours(cur).stream()
                        .filter(e -> result.getNodeStatus(e.getDestination()) != NodeStatus.CLOSED)
                        .forEach(e ->
                        {
                            NodeInformation<N, A> info = new NodeInformation<>();
                            System.out.println(e);
                            info.setPredecessor(e);
                            info.setDistance(result.getInformation(e.getStart()).getDistance() + 1);
                            result.open(e.getDestination(), info);
                            stack.push(e.getDestination());
                        });

                result.close(cur, result.getInformation(cur));
            }

            if(type.stopSearch(cur) || stopped){
                return result;
            }

        }

        return result;
    }*/


        result.clear(); // Zustand löschen

        System.out.println("Beginne Suche von: " + start);

        Stack<N> stack = new Stack<>();
        NodeInformation<N, A> sInfo = new NodeInformation<>();
        sInfo.setPredecessor(null);
        sInfo.setDistance(0);
        result.open(start, sInfo);
        stack.push(start);

        boolean targetFound = false; // Flag, ob Zielknoten gefunden wurde

        if(type instanceof StartToDestStrategy && ((StartToDestStrategy<N>) type).getDest().equals(start)){
            return result; // Überprüfung Start- und Zielknoten gleich
        }

        while (!(stack.isEmpty() || stopped)) {

            if(targetFound){
                break;
            }
            N cur = stack.pop();

            if (result.getNodeStatus(cur) != NodeStatus.CLOSED) {
                graph.getNeighbours(cur).stream().filter(e -> result.getNodeStatus(e.getDestination()) != NodeStatus.CLOSED).
                        forEach(e ->
                        {
                            NodeInformation<N, A> info = new NodeInformation<>();
                            info.setPredecessor(e);
                            info.setDistance(result.getInformation(e.getStart()).getDistance() + 1);
                            result.open(e.getDestination(), info);
                            stack.push(e.getDestination());
                            System.out.println("Debugging, Hinzufuegen von Nachbarn zum Stack: " + e.getDestination() + " from  " + cur);
                        });

                result.close(cur, result.getInformation(cur));
            }

            if(type.stopSearch(cur)){ // Überprüfung, ob Zielknoten gefunden wurde
                targetFound = true;
            }


            if (targetFound && stack.isEmpty()) // Schleife beenden
                break;

        }

        return result;
    }

}
