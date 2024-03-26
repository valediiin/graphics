package de.jpp.io;

import de.jpp.io.interfaces.GraphReader;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;

import java.util.*;
import java.util.stream.Collectors;

public abstract class GridReaderTemplate<F> implements GraphReader<XYNode, Double, TwoDimGraph, F> {

    protected static String buildNeighborKey(String src, int modX, int modY){

        List<Integer> pos = Arrays.stream(src.split("\\|"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        if(pos.get(0) + modX < 0 || pos.get(1) < 0){
            return null;
        }

        return String.format("%s|%s", pos.get(0) + modX, pos.get(1) + modY);
    }

    private void addEdge(TwoDimGraph graph, XYNode node1, XYNode node2){

        if(node1 == null || node2 == null){
            return;
        }

        graph.addEdge(node1, node2, Optional.of(1d));
    }

    protected TwoDimGraph createGraph(Map<String, XYNode> nodes){

        TwoDimGraph graph = new TwoDimGraph();
        graph.addNodes(nodes.values());

        List<List<Integer>> dirs = List.of(List.of(0, -1), List.of(0, 1), List.of(-1, 0), List.of(1, 0));

        nodes.forEach((xy, node) -> dirs.stream()
                .map(integers -> buildNeighborKey(xy, integers.get(0), integers.get(1)))
                .filter(Objects::nonNull).collect(Collectors.toList()).forEach(k -> addEdge(graph, node, nodes.get(k))));


        return graph;
    }
}
