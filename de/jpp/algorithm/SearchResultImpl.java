package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.NodeStatus;
import de.jpp.algorithm.interfaces.ObservableSearchResult;
import de.jpp.algorithm.interfaces.SearchResult;
import de.jpp.model.interfaces.Edge;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class SearchResultImpl<N, A> implements ObservableSearchResult<N, A> {

    private final Map<N, NodeStatus> statusMap = new HashMap<>();
    private final Map<N, NodeInformation<N, A>> infoMap = new HashMap<>();
    private final List<BiConsumer<N, SearchResult<N, A>>> onOpen = new ArrayList<>();
    private final List<BiConsumer<N, SearchResult<N, A>>> onClose = new ArrayList<>();
    @Override
    public void addNodeOpenedListener(BiConsumer<N, SearchResult<N, A>> onOpen) {
        this.onOpen.add(onOpen);
    }

    @Override
    public void removeNodeOpenedListener(BiConsumer<N, SearchResult<N, A>> onOpen) {
        this.onOpen.remove(onOpen);
    }

    @Override
    public void addNodeClosedListener(BiConsumer<N, SearchResult<N, A>> onClose) {
        this.onClose.add(onClose);
    }

    @Override
    public void removeNodeClosedListener(BiConsumer<N, SearchResult<N, A>> onClose) {
        this.onClose.remove(onClose);
    }

    @Override
    public NodeStatus getNodeStatus(N node) {
        return statusMap.get(node);
    }

    @Override
    public Optional<Edge<N, A>> getPredecessor(N node) {
        return infoMap.containsKey(node) && infoMap.get(node).getPredecessor() != null ? Optional.of(infoMap.get(node).getPredecessor())
                : Optional.empty();
    }

    @Override
    public Collection<N> getAllKnownNodes() {
        List<N> closed = statusMap.entrySet().stream()
                .filter(ns -> NodeStatus.CLOSED == ns.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        closed.addAll(getAllOpenNodes());
        return Collections.unmodifiableCollection(closed);
    }

    @Override
    public Collection<N> getAllOpenNodes() {
        return statusMap.entrySet().stream()
                .filter(ns -> NodeStatus.OPEN == ns.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void setClosed(N node) {
        if(statusMap.get(node) == NodeStatus.CLOSED){
            return;
        }

        statusMap.put(node, NodeStatus.CLOSED);
        onClose.forEach(l -> l.accept(node, this));
    }

    @Override
    public void setOpen(N node) {
        if(statusMap.get(node) == NodeStatus.OPEN){
            return;
        }

        statusMap.put(node, NodeStatus.OPEN);
        onOpen.forEach(l -> l.accept(node, this));
    }

    @Override
    public void clear() {
        infoMap.clear();
        statusMap.clear();
    }

    @Override
    public Optional<List<Edge<N, A>>> getPathTo(N dest) {

        if(statusMap.get(dest) != NodeStatus.CLOSED){
            return Optional.empty();
        }

        List<Edge<N, A>> edges = new ArrayList<>();
        Edge<N, A> cur = getPredecessor(dest).orElse(null);

        while (cur != null){
            edges.add(cur);
            cur = getPredecessor(cur.getStart()).orElse(null);
        }

        Collections.reverse(edges);

        return Optional.of(edges);
    }

    public void open(N node, NodeInformation<N, A> info){
        infoMap.put(node, info);
        setOpen(node);
    }

    public void close(N node, NodeInformation<N, A> info){
        infoMap.put(node, info);
        setClosed(node);
    }

    public NodeInformation<N, A> getInformation(N node){
        return infoMap.get(node);
    }

    public void setInformation(N node, NodeInformation<N, A> info){
        infoMap.put(node, info);
    }

    public void setStatusUnkown(N node){
        statusMap.put(node, NodeStatus.UNKOWN);
    }
}
