package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.NodeStatus;
import de.jpp.algorithm.interfaces.SearchResult;
import de.jpp.algorithm.interfaces.SearchStopStrategy;
import de.jpp.model.interfaces.Graph;

import java.util.ArrayDeque;
import java.util.Queue;

public class BreadthFirstSearch<N, A, G extends Graph<N, A>> extends BreadthFirstSearchTemplate<N, A, G>  {

    public BreadthFirstSearch(G graph, N start){
        this.graph = graph;
        this.start = start;
    }
    @Override
    public SearchResult<N, A> findPaths(SearchStopStrategy<N> type) {

        Queue<N> queue = new ArrayDeque<>();
        NodeInformation<N, A> sInfo = new NodeInformation<>();
        sInfo.setPredecessor(null);
        sInfo.setDistance(0);
        result.open(start, sInfo);
        queue.add(start);

        while (!(queue.isEmpty() || stopped)){
            N cur = queue.poll();
            result.close(cur, result.getInformation(cur));

            if(type.stopSearch(cur) || stopped){
                return result;
            }

            graph.getNeighbours(cur).stream()
                    .filter(e -> result.getNodeStatus(e.getDestination()) != NodeStatus.CLOSED)
                    .forEach(e ->
                    {
                        NodeInformation<N, A> info = new NodeInformation<>();
                        info.setPredecessor(e);
                        info.setDistance(result.getInformation(e.getStart()) == null ? 0 : result.getInformation(e.getStart()).getDistance() + 1);
                        result.open(e.getDestination(), info);
                        queue.add(e.getDestination());
                    });
        }

        return result;
    }
}
