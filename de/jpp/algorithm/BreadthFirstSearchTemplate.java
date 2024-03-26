package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.SearchAlgorithm;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;

public abstract class BreadthFirstSearchTemplate<N, A, G extends Graph<N, A>> extends SearchAlgorithmTemplate<N, A, G> {

    protected NodeInformation<N, A> getNodeInformation(Edge<N, A> edge, double weight){
        NodeInformation<N, A> ni = new NodeInformation<>();
        ni.setPredecessor(edge);
        ni.setDistance(weight);
        return ni;
    }

    protected void openIfShorter(N node, NodeInformation<N, A> info){
        NodeInformation<N, A> old = result.getInformation(node);

        if(info.getDistance() < old.getDistance()){
            result.setOpen(node);
        }
    }
}
