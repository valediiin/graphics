package de.jpp.factory;

import de.jpp.algorithm.StartToDestStrategy;
import de.jpp.algorithm.interfaces.SearchStopStrategy;

public class SearchStopFactory {
    /**
     * Returns a SearchStopStrategy which never stops on its own
     *
     * @param <N> the exact types of nodes in the graph underlying the search
     * @return a SearchStopStrategy which never stops on its own
     */
    public <N> SearchStopStrategy<N> expandAllNodes() {
        return node -> false;
    }

    /**
     * Returns a SearchStopStrategy which stops after a specified number of nodes are CLOSED
     *
     * @param maxCount the maximum number of nodes which may be CLOSED
     * @param <N>      the exact type of nodes in the graph underlying the search
     * @return a SearchStopStrategy which stops after a specified number of nodes are CLOSED
     */
    public <N> SearchStopStrategy<N> maxNodeCount(int maxCount) {
        SearchMaxStopStrategy<N> strategy = new SearchMaxStopStrategy<>();
        strategy.setMaxCount(maxCount);
        return strategy;
    }

    /**
     * Returns a SearchStopStrategy which stops after a specified destination has been reached
     *
     * @param dest the destination
     * @param <N>  the exact type of nodes in the graph underlying the search
     * @return a SearchStopStrategy which stops after a specified destination has been reached
     */
    public <N> StartToDestStrategy<N> startToDest(N dest) {
        return new StartToDestStrategy<>(dest);
    }

    private static class SearchMaxStopStrategy<N> implements SearchStopStrategy<N> {
        private int maxCount;
        private int count = 0;

        public void setMaxCount(int maxCount) {
            this.maxCount = maxCount;
        }

        @Override
        public boolean stopSearch(N lastClosedNode) {
            if (++count >= maxCount) {
                count = 0;
                return true;
            }

            return false;
        }
    }

}
