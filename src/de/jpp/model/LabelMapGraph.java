package de.jpp.model;

import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.WeightedGraph;

import java.util.Map;

/**
 * A LabelMapGraph. <br>
 * The abstract-tag is only set because the tests will not compile otherwise. You should remove it!
 */
public class LabelMapGraph extends GraphImpl<String, Map<String, String>> implements WeightedGraph<String, Map<String, String>> {
    @Override
    public double getDistance(Edge<String, Map<String, String>> edge) {
        return 0;
    }
}
