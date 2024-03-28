package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.ObservableSearchResult;
import de.jpp.algorithm.interfaces.SearchAlgorithm;
import de.jpp.algorithm.interfaces.SearchResult;
import de.jpp.model.interfaces.Graph;

public abstract class SearchAlgorithmTemplate<N, A, G extends Graph<N, A>> implements SearchAlgorithm<N, A, G> {

    protected boolean stopped = false;
    protected G graph;
    protected N start;
    protected SearchResultImpl<N, A> result = new SearchResultImpl<>();

    @Override
    public void stop(){

        stopped = true;
    }

    @Override
    public ObservableSearchResult<N, A> getSearchResult(){
        return result;
    }

    @Override
    public SearchResult<N, A> findAllPaths() {
        return findPaths(n -> false);
    }

    @Override
    public N getStart(){
        return start;
    }

    @Override
    public G getGraph(){
        return graph;
    }
}
