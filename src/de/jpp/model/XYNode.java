package de.jpp.model;

import java.util.Objects;

public class XYNode {

    /**
     * Creates a new XYNode with the specified label and coordinate
     *
     * @param label the label
     * @param x     the x value of the coordinate
     * @param y     the y value of the coordinate
     */

    private final double x;
    private final double y;
    private final String label;
    public XYNode(String label, double x, double y) {

        if(label == null){
            throw new IllegalArgumentException("Label can not be null");
        }

        this.label = label;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the label of this node
     * @return
     */
    public String getLabel() {

        return label;
    }

    /**
     * Returns the x coordinate of this node
     * @return
     */
    public double getX() {

        return x;
    }

    /**
     * Returns the y coordinate of this node
     * @return
     */
    public double getY() {

        return y;
    }

    /**
     * Calculates the euclidian distance to the specified XYNode
     *
     * @param other the node to calculate the distance to
     * @return the euclidian distance to the specified XYNode
     */
    public double euclidianDistTo(XYNode other) {

        return Math.sqrt(Math.pow((other.x - x), 2) + Math.pow((other.y - y), 2));

    }

    @Override
    public String toString() {

        return String.format("'%s' at pos (%s|%s)", label, x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XYNode)) return false;

        XYNode xyNode = (XYNode) o;

        return this.label.equals(xyNode.label) && this.x == xyNode.x && this.y == xyNode.y;
    }

    @Override
    public int hashCode() {
        return String.format("%s%s%s", label, x, y).hashCode();
    }

}
