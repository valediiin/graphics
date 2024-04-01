package de.jpp.io;

import de.jpp.io.interfaces.GraphReader;
import de.jpp.io.interfaces.ParseException;
import de.jpp.model.interfaces.Graph;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class GxlReaderTemplate<N, A, G extends Graph<N,A>> implements GraphReader<N, A, G, String> {

    private static final String nodeName = "node";
    private static final String edgeName = "edge";
    private static final String attrName = "attr";

    protected boolean errored = false;

    protected String msg = "";

    @Override
    public G read(String input) throws ParseException {

        Element base;

        try{
            Element root = new SAXBuilder().build(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))).getRootElement();

            if(!root.getName().equals("gxl")){
                throw new ParseException("Root element is not gxl");
            }

            base = root.getChild("graph");

            if(base == null){
                throw new ParseException("Child of root is not graph");
            }

        } catch (JDOMException | IOException | IllegalStateException ex) {
            throw new ParseException(ex.getMessage());
        }

        Comparator<Element> comp = (e1, e2) ->
        {
            if(e1.getName().equals(edgeName) ^ e2.getName().equals(edgeName)){
                return e1.getName().equals(edgeName) ? 1 : -1;
            }

            return e1.getName().compareTo(e2.getName());
        };

        G graph = (G) createGraph();
        Map<String, N> nodes = new HashMap<>();
        base.getChildren().stream().sorted(comp).forEach(n ->
        {
            if(errored)
                return;

            if(n.getName().equals(nodeName)){
                N node = readNode(n);
                String nid = readNodeId(node, n);
                nodes.put(nid, node);
                graph.addNode(node);
                return;
            } else if(n.getName().equals(edgeName)){
                addEdge(graph, n, nodes);
                return;
            }

            errored = true;
            msg = String.format("%s is neither a node nor an edge" , n);
        });

        if(errored){
            throw new ParseException(msg);
        }

        return graph;
    }

    protected Map<String, String> getAttributes(Element elem){
        boolean allAttributesWithName = elem.getChildren().stream()
                .allMatch(c -> c.getName().equals(attrName) && c.getAttributeValue("name") != null);
        boolean allAttributesHaveSingleChild = elem.getChildren().stream()
                .allMatch(c -> c.getChildren().size() == 1);

        if(!(allAttributesWithName && allAttributesHaveSingleChild)){
            errored = true;
            msg = allAttributesWithName ? String.format("%s is missing a child", elem) : String.format("%s has a child without a name", elem);
            return new HashMap<>();
        }

        return elem.getChildren().stream()
                .collect(Collectors.toMap(b -> b.getAttributeValue("name"), b -> b.getChildren().get(0).getText()));
    }

    private void addEdge(G graph, Element elem, Map<String, N> nodes){
        String sid = elem.getAttributeValue("from");
        String did = elem.getAttributeValue("to");

        try {
            graph.addEdge(nodes.get(sid), nodes.get(did), readAnnotation(elem));
        } catch (IllegalArgumentException ex){
            errored = true;
            msg = String.format("Error with edge %s -> %s: %s", sid, did, ex.getMessage());
        }
    }

    public abstract Graph<N, A> createGraph();

    public abstract String readNodeId(N node, Element elem);

    public abstract N readNode(Element elem);

    public abstract Optional<A> readAnnotation(Element element);

}
