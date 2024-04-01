package de.jpp.io;

import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;
import org.jdom2.Element;

public class TwoDimGraphGxlWriter extends GxlWriterTemplate<XYNode, Double, TwoDimGraph> {

    @Override
    public String write(TwoDimGraph graph){

        return super.write(graph);
    }
    @Override
    public Element writeNode(XYNode node) {

        return new Element("node").addContent(createAttribute("description", "string", node.getLabel()))
                .addContent(createAttribute("x", "int", String.valueOf(node.getX())))
                .addContent(createAttribute("y", "int", String.valueOf(node.getY())));
    }

    @Override
    public Element writeEdge(Edge<XYNode, Double> edge) {

        Element element = new Element("edge").setAttribute("from", nodes.get(edge.getStart()))
                .setAttribute("to", nodes.get(edge.getDestination()));

        if(edge.getAnnotation().isPresent()){
            element.addContent(createAttribute("cost", "float", String.valueOf(edge.getAnnotation().get())));
        }

        return element;
    }

    @Override
    public String calculateId(XYNode node) {
        return String.format("nid%s", nodeCount++);
    }

    @Override
    public String calculateId(Edge<XYNode, Double> edge) {
        return String.format("eid%s", edgeCount++);
    }
}
