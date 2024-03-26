package de.jpp.io;

import de.jpp.model.LabelMapGraph;
import de.jpp.model.interfaces.Edge;
import org.jdom2.Element;

import java.util.Map;

public class LabelMapGraphGxlWriter extends GxlWriterTemplate<String, Map<String, String>, LabelMapGraph> {
    @Override
    public Element writeNode(String node) {

        return new Element("node").addContent(createAttribute("description", "string", node));
    }

    @Override
    public Element writeEdge(Edge<String, Map<String, String>> edge) {

        Element element = new Element("edge").setAttribute("from", nodes.get(edge.getStart()))
                .setAttribute("to", nodes.get(edge.getDestination()));

        if(edge.getAnnotation().isPresent()){
            edge.getAnnotation().get().forEach((key, value) -> element.addContent(createAttribute(key, "string", value)));
        }

        return element;
    }

    @Override
    public String calculateId(String node) {

        return String.format("nid%s", nodeCount++);
    }

    @Override
    public String calculateId(Edge<String, Map<String, String>> edge) {

        return String.format("eid%s", edgeCount++);
    }
}
