package de.jpp.algorithm;

import de.jpp.model.interfaces.Edge;

public class NodeInformation <N,A> {

    private Edge<N,A> predecessor = null;
    private double distance = 0;

    public Edge<N,A> getPredecessor(){
        return predecessor;
    }

    public void setPredecessor(Edge<N, A> predecessor){
        this.predecessor = predecessor;
    }

    public double getDistance(){
        return distance;
    }

    public void setDistance(double distance){
        this.distance = distance;
    }
}
