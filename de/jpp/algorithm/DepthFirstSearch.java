package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.NodeStatus;
import de.jpp.algorithm.interfaces.SearchResult;
import de.jpp.algorithm.interfaces.SearchStopStrategy;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Graph;

import java.util.Stack;

public class DepthFirstSearch<N, A, G extends Graph<N, A>> extends SearchAlgorithmTemplate<N, A, G> {

    public DepthFirstSearch(G graph, N start) {
        this.graph = graph;
        this.start = start;
    }

    public static void main(String[] args) {
        XYNode xyNode1 = new XYNode("", 1, 1);
        XYNode xyNode2 = new XYNode("", 2, 2);
        XYNode xyNode3 = new XYNode("", 3, 3);
        XYNode xyNode4 = new XYNode("", 4, 4);
        XYNode xyNode5 = new XYNode("", 5, 5);
        XYNode xyNode6 = new XYNode("", 6, 6);

        TwoDimGraph twoDimGraph = new TwoDimGraph();
        twoDimGraph.addEdge(xyNode1, xyNode2);
        twoDimGraph.addEdge(xyNode2, xyNode3);
        twoDimGraph.addEdge(xyNode3, xyNode4);
        twoDimGraph.addEdge(xyNode4, xyNode5);
        twoDimGraph.addEdge(xyNode5, xyNode6);

        DepthFirstSearch depthFirstSearch = new DepthFirstSearch(twoDimGraph, xyNode1);

        System.out.println(depthFirstSearch.findPaths(new StartToDestStrategy(xyNode4)).getPathTo(xyNode4));
    }

    @Override
    public SearchResult<N, A> findPaths(SearchStopStrategy<N> type) {
        Stack<N> stack = new Stack<>();
        NodeInformation<N, A> sInfo = new NodeInformation<>();
        sInfo.setPredecessor(null);
        sInfo.setDistance(0);
        result.open(start, sInfo);
        stack.push(start);

        while (!(stack.isEmpty() || stopped)){
            N cur = stack.pop();

            if(result.getNodeStatus(cur) != NodeStatus.CLOSED){
                graph.getNeighbours(cur).stream()
                        .filter(e -> result.getNodeStatus(e.getDestination()) != NodeStatus.CLOSED)
                        .forEach(e ->
                        {
                            NodeInformation<N, A> info = new NodeInformation<>();
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
    }
}
