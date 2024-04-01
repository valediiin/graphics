package de.jpp.model.interfaces;

import java.util.Optional;

public class Edge<N, A> {


    /**
     * Creates a new edge with the specified start node, destination node and annotation
     *
     * @param start      the start node
     * @param dest       the destination node
     * @param annotation the annotation
     */

    private final N start;
    private final N dest;
    private final Optional<A> annotation;

    public Edge(N start, N dest, Optional<A> annotation) {

        if(start == null || dest == null){
            throw new IllegalArgumentException("Nodes can't be null");
        }else if(start == dest){ // weglassen oder da lassen?, ohne Endlosschleife
            // start.equals(dest) Objektüberprüfung, Graphfuzzer streitet
            throw new IllegalArgumentException("Start and dest can't be equal.");
        }

        this.start = start;
        this.dest = dest;
        this.annotation = annotation == null ? Optional.empty() : annotation; // null check
    }

    /**
     * Returns the start node of this edge
     *
     * @return the start node of this edge
     */
    public N getStart() {

        return start;
    }

    /**
     * Returns the destination node of this edge
     *
     * @return the destination node of this edge
     */
    public N getDestination() {

        return dest;
    }

    /**
     * Returns the annotation of this edge
     *
     * @return the annotation of this edge
     */
    public Optional<A> getAnnotation() {

        return annotation;
    }

    @Override
    public String toString() {
        return String.format("%s->%s (d: %s)", start, dest, annotation.isPresent() ? annotation.get() : "?");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;

        Edge<N,A> edge = (Edge<N, A>) o;
        return this.start.equals(edge.start) && this.dest.equals(edge.dest) && this.annotation.equals(edge.annotation);
    }

    @Override
    public int hashCode() {

        return start.hashCode() + dest.hashCode() + annotation.hashCode();
    }


}
