package de.jpp.model;

import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;

import java.util.*;
import java.util.stream.Collectors;

public class GraphImpl<N,A> implements Graph<N,A> {

    private final List<N> nodes = new ArrayList<>(); // Listen mit Nodes
    private final Map<N, List<Edge<N,A>>> edges = new HashMap<>(); // Map aus nodes und dazugehörigen edges

    // Hilfsmethode ob node existiert oder nicht

    private boolean nodeExistsOrIsNull(N node){

        return node == null || nodes.contains(node);
    }
    @Override
    public boolean addNode(N node) {

        if(nodeExistsOrIsNull(node)){
            return false;
        }

        nodes.add(node);
        edges.put(node, new ArrayList<>());
        return true;
    }

    @Override
    public boolean addNodes(Collection<? extends N> nodes) {

        List<? extends N> validNodes = nodes.stream()
                .filter(n -> !nodeExistsOrIsNull(n)).collect(Collectors.toList());

        if(validNodes.isEmpty()){
            return false;
        }

        boolean change = false;

        for(N node : validNodes){
            if(addNode(node)){
                change = true;
            }
        }

        return change;
    }

    @Override
    public boolean addNodes(N... nodes) {

        return addNodes(Arrays.stream(nodes).collect(Collectors.toList()));
    }

    @Override
    public Collection<N> getNodes() {

        return Collections.unmodifiableCollection(nodes);
    }

    @Override
    public Edge<N, A> addEdge(N start, N destination, Optional<A> annotation) {

        if(!nodes.contains(start)){
            addNode(start);
        }

        if(!nodes.contains(destination)){
            addNode(destination);
        }

        Edge<N, A> edge = new Edge<>(start, destination, annotation);
        edges.get(start).add(edge);
        return edge;
    }

    @Override
    public boolean removeEdge(Edge<N, A> edge) {

        if(edge == null || !edges.get(edge.getStart()).contains(edge)){
            return false;
        }

        return edges.get(edge.getStart()).remove(edge);
    }

    @Override
    public Collection<Edge<N, A>> getNeighbours(N node) {

        if(node == null || !edges.containsKey(node)){
            return Collections.emptyList();
        }

        return Collections.unmodifiableCollection(edges.get(node));
    }

    @Override
    public Collection<Edge<N, A>> getReachableFrom(N node) {

        if(node == null){
            return Collections.emptyList();
        }

        return edges.values().stream()
                .flatMap(Collection::stream)
                .filter(e -> e.getDestination().equals(node))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Collection<Edge<N, A>> getEdges() {
        return edges.values().stream().flatMap(Collection::stream).collect(Collectors.toUnmodifiableList());
    }

    // weitere Hilfsmethode ob Node existiert oder null ist
    protected boolean nodeDoesNotExistOrIsNull(N node){

        return node == null || !nodes.contains(node);
    }
    @Override
    public boolean removeNode(N node) {

        if(nodeDoesNotExistOrIsNull(node)){
            return false;
        }

        List.copyOf(edges.get(node)).forEach(this::removeEdge);
        edges.forEach((n, le) -> List.copyOf(le).stream()
                .filter(e -> e.getDestination().equals(node))
                .forEach(this::removeEdge));
        edges.remove(node);
        nodes.remove(node);

        return true;
    }

    @Override
    public boolean removeNodes(Collection<? extends N> nodes) {

        List<N> validNodes = nodes.stream()
                .filter(n -> !nodeDoesNotExistOrIsNull(n))
                .collect(Collectors.toList());

        if(validNodes.isEmpty()){
            return false;
        }

        boolean change = false;

        for (N node : validNodes){
            if(removeNode(node)){
                change = true;
            }
        }

        return change;
    }

    @Override
    public boolean removeNodes(N... nodes) {
        return removeNodes(Arrays.stream(nodes).collect(Collectors.toList()));
    }

    @Override
    public void clear() {

        nodes.clear();
        edges.clear();
    }

    @Override
    public String toString() {
        return String.format("Ns: %s, Es: %s", nodes.size(), getEdges().size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GraphImpl)) return false;

        Graph<N, A> graph = (Graph<N, A>) o;

        if(getNodes().size() != graph.getNodes().size() || getEdges().size() != graph.getEdges().size()) return false;

        boolean nodeIdentity = getNodes().stream()
                .allMatch(n -> Collections.frequency(getNodes(), n) == Collections.frequency(graph.getNodes(), n));
        boolean edgeIdentity = getEdges().stream()
                .allMatch(e -> Collections.frequency(getEdges(), e) == Collections.frequency(graph.getEdges(), e));

        return nodeIdentity && edgeIdentity;
    }

    @Override
    public int hashCode() {
        return nodes.stream()
                .map(Objects::hashCode)
                .reduce(0, Integer::sum)
                + getEdges().stream()
                .map(Objects::hashCode)
                .reduce(0, Integer::sum);
    }
}
