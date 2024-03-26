package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.SearchResult;
import de.jpp.algorithm.interfaces.SearchStopStrategy;
import de.jpp.model.interfaces.WeightedGraph;

public class AStarSearch<N, A, G extends WeightedGraph<N, A>> extends BreadthFirstSearchTemplate<N, A, G>  {
    @Override
    public SearchResult<N, A> findPaths(SearchStopStrategy<N> type) {
        return null;
    }
}
