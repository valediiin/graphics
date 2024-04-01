package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.SearchStopStrategy;


public class StartToDestStrategy<N> implements SearchStopStrategy<N> {
    private final N dest;

    public StartToDestStrategy(N dest) {
        this.dest = dest;
    }

    @Override
    public boolean stopSearch(N lastClosedNode) {
        return dest.equals(lastClosedNode);
    }

    /**
     * Returns the destination node of this search
     *
     * @Returns the destination node of this search
     */
    public N getDest() {
        return dest;
    }
}
