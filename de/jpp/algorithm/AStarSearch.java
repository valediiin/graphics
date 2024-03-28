package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.SearchResult;
import de.jpp.algorithm.interfaces.SearchStopStrategy;
import de.jpp.model.interfaces.WeightedGraph;
import de.jpp.algorithm.interfaces.EstimationFunction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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


    @Override
    public SearchResult<N, A> findPaths(SearchStopStrategy<N> type) {
        Comparator<N> comp = (n1, n2) ->
        {
            if(n1 == null ^ n2 == null){
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

        while (!(opens.isEmpty() || stopped)){
            N cur = opens.stream().min(comp).get();
            opens.remove(cur);
            result.close(cur, result.getInformation(cur));

            if(type.stopSearch(cur) || stopped){
                return result;
            }

            graph.getNeighbours(cur).forEach(e ->
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
        }

        return result;
    }
}
