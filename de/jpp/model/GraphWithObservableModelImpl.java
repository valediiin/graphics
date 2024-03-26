package de.jpp.model;

import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.GraphWithObservableModel;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GraphWithObservableModelImpl<N, A> extends GraphImpl<N, A> implements GraphWithObservableModel<N, A> {

    private final List<Consumer<N>> anListeners = new ArrayList<>();
    private final List<Consumer<N>> rnListeners = new ArrayList<>();
    private final List<Consumer<Edge<N, A>>> aeListeners = new ArrayList<>();
    private final List<Consumer<Edge<N, A>>> reListeners = new ArrayList<>();


    // Main Test
    public static void main(String[] args) {

        GraphWithObservableModelImpl<Integer, Integer> g = new GraphWithObservableModelImpl<>();
        g.addNodeAddedListener(i -> System.out.println("in " + i));
        g.addNodeRemovedListener(i -> System.out.println("out " + i));
        g.addEdgeAddedListener(e -> System.out.println("e in " + e));
        g.addEdgeRemovedListener(e -> System.out.println("e out " + e));

        g.addNodes(1, 2, 3, 4);
        g.addNode(5);
        g.addEdge(5, 3, Optional.of(1));
        g.addNodes(1, 2, 3, 4, 5, 6, 7, 8, 9);
        g.removeNodes(1, 2, 3, 4);
        g.removeNode(5);
        g.removeNodes(1, 2, 3, 4, 5, 6, 7, 8 ,9);
    }
    @Override
    public void clear(){

        if(!getNodes().isEmpty()){
            getNodes().forEach(node -> rnListeners.forEach(l -> l.accept(node)));
        }

        if(!getEdges().isEmpty()){
            getEdges().forEach(edge -> reListeners.forEach(l -> l.accept(edge)));
        }

        super.clear();
    }

    @Override
    public boolean addNode(N node){

        boolean added = super.addNode(node);

        if(added){
            anListeners.forEach(l -> l.accept(node));
        }

        return added;
    }

    @Override
    public boolean addNodes(Collection<? extends N> nodes){

        return super.addNodes(nodes);
    }

    @Override
    public boolean addNodes(N... nodes){

        return super.addNodes(nodes);
    }

    @Override
    public boolean removeNode(N node){

        boolean removed = super.removeNode(node);

        if(removed){
            rnListeners.forEach(l -> l.accept(node));
        }

        return removed;
    }

    @Override
    public boolean removeNodes(Collection<? extends N> nodes){

        return super.removeNodes(nodes);
    }

    @Override
    public boolean removeNodes(N... nodes){

        return removeNodes(Arrays.stream(nodes).collect(Collectors.toList()));
    }

    @Override
    public Edge<N, A> addEdge(N start, N destination, Optional<A> annotation){

        Edge<N, A> edge = super.addEdge(start, destination, annotation);
        aeListeners.forEach(l -> l.accept(edge));

        return edge;
    }

    @Override
    public boolean removeEdge(Edge<N, A> edge){

        boolean removed = super.removeEdge(edge);

        if(removed){
            reListeners.forEach(l -> l.accept(edge));
        }

        return removed;
    }

    @Override
    public void addNodeAddedListener(Consumer<N> listener) {

        anListeners.add(listener);
    }

    @Override
    public void addNodeRemovedListener(Consumer<N> listener) {

        rnListeners.add(listener);
    }

    @Override
    public void addEdgeAddedListener(Consumer<Edge<N, A>> listener) {

        aeListeners.add(listener);
    }

    @Override
    public void addEdgeRemovedListener(Consumer<Edge<N, A>> listener) {

        reListeners.add(listener);
    }

    @Override
    public void removeNodeAddedListener(Consumer<N> listener) {

        anListeners.remove(listener);
    }

    @Override
    public void removeNodeRemovedListener(Consumer<N> listener) {

        rnListeners.remove(listener);
    }

    @Override
    public void removeEdgeAddedListener(Consumer<Edge<N, A>> listener) {

        aeListeners.remove(listener);
    }

    @Override
    public void removeEdgeRemovedListener(Consumer<Edge<N, A>> listener) {

        reListeners.remove(listener);
    }
}
