package de.jpp.io;

import de.jpp.model.LabelMapGraph;
import de.jpp.model.interfaces.Graph;
import org.jdom2.Element;

import java.util.Map;
import java.util.Optional;

public class LabelMapGraphGxlReader extends GxlReaderTemplate<String, Map<String, String>, LabelMapGraph> {
    @Override
    public Graph<String, Map<String, String>> createGraph() {
        return new LabelMapGraph();
    }

    @Override
    public String readNodeId(String node, Element elem) {

        if(elem.getAttributeValue("id") == null){
            errored = true;
            msg = String.format("%s is missing an id", elem);
            return null;
        }

        return elem.getAttributeValue("id");
    }

    @Override
    public String readNode(Element elem) {

        Map<String, String> attributes = getAttributes(elem);

        return attributes.get("description");
    }

    @Override
    public Optional<Map<String, String>> readAnnotation(Element element) {

        Map<String, String> attributes = getAttributes(element);

        return attributes.isEmpty() ? Optional.empty() : Optional.of(attributes);
    }
}
