package de.jpp.io;

import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Graph;
import org.jdom2.Element;

import java.util.Map;
import java.util.Optional;

public class TwoDimGraphGxlReader extends GxlReaderTemplate<XYNode, Double, TwoDimGraph> {
    @Override
    public Graph<XYNode, Double> createGraph() {
        return new TwoDimGraph();
    }

    @Override
    public String readNodeId(XYNode node, Element elem) {
        if(elem.getAttributeValue("id") == null){
            errored = true;
            msg = String.format("%s is missing an id", elem);
            return null;
        }

        return elem.getAttributeValue("id");
    }

    @Override
    public XYNode readNode(Element elem) {
        Map<String, String> attributes = getAttributes(elem);

        try {
            return new XYNode(attributes.getOrDefault("description", ""), Double.parseDouble(attributes.get("x")),
                    Double.parseDouble(attributes.get("y")));
        } catch (NumberFormatException | NullPointerException ex){
            errored = true;
            msg = String.format("Attribute element is missing x and y attributes or has invalid values, lead to Error %s: %s", msg, ex.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Double> readAnnotation(Element element) {
        Map<String, String> attributes = getAttributes(element);
        return attributes.containsKey("cost") ? Optional.of(Double.parseDouble(attributes.get("cost"))) : Optional.empty();
    }
}
