package de.jpp.model;

import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.ObservableGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ObservableGraphImpl<N, A> extends GraphWithObservableModelImpl<N, A> implements ObservableGraph<N, A> {

    private final List<Consumer<Collection<N>>> nListeners = new ArrayList<>();
    private final List<Consumer<Collection<Edge<N, A>>>> nlListeners = new ArrayList<>();
    private final List<Consumer<Collection<Edge<N, A>>>> rlListeners = new ArrayList<>();
    private final List<Consumer<Collection<Edge<N, A>>>> eListeners = new ArrayList<>();


    @Override
    public Collection<Edge<N, A>> getNeighbours(N node){

        Collection<Edge<N, A>> neighbours = super.getNeighbours(node);
        nlListeners.forEach(l -> l.accept(neighbours));
        return neighbours;
    }

    @Override
    public Collection<Edge<N, A>> getReachableFrom(N node){

        Collection<Edge<N, A>> reachables = super.getReachableFrom(node);
        rlListeners.forEach(l -> l.accept(reachables));
        return reachables;
    }

    @Override
    public Collection<N> getNodes(){

        Collection<N> nodes = super.getNodes();
        nListeners.forEach(l -> l.accept(nodes));
        return nodes;
    }

    @Override
    public Collection<Edge<N, A>> getEdges(){

        Collection<Edge<N, A>> edges = super.getEdges();
        eListeners.forEach(l -> l.accept(edges));

        return edges;
    }
    @Override
    public void addNeighboursListedListener(Consumer<Collection<Edge<N, A>>> listener) {

        nlListeners.add(listener);
    }

    @Override
    public void addReachableListedListener(Consumer<Collection<Edge<N, A>>> listener) {

        rlListeners.add(listener);
    }

    @Override
    public void addNodesListedListener(Consumer<Collection<N>> listener) {

        nListeners.add(listener);
    }

    @Override
    public void addEdgesListedListener(Consumer<Collection<Edge<N, A>>> listener) {

        eListeners.add(listener);
    }

    @Override
    public void removeNeighboursListedListener(Consumer<Collection<Edge<N, A>>> listener) {

        nlListeners.remove(listener);
    }

    @Override
    public void removeReachableListedListener(Consumer<Collection<Edge<N, A>>> listener) {

        rlListeners.remove(listener);
    }

    @Override
    public void removeNodesListedListener(Consumer<Collection<N>> listener) {

        nListeners.remove(listener);
    }

    @Override
    public void removeEdgesListedListener(Consumer<Collection<Edge<N, A>>> listener) {

        eListeners.remove(listener);
    }
}
