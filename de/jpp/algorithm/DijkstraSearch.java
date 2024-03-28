package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.SearchResult;
import de.jpp.algorithm.interfaces.SearchStopStrategy;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.WeightedGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DijkstraSearch<N, A, G extends WeightedGraph<N, A>> extends BreadthFirstSearchTemplate<N, A, G>  {

    public DijkstraSearch(G graph, N start){
        this.graph = graph;
        this.start = start;
    }
    @Override
    public SearchResult<N, A> findPaths(SearchStopStrategy<N> type) {

        graph.getNodes().forEach(n ->
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

        return result;
    }
}
