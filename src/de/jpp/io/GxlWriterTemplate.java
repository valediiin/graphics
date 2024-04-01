package de.jpp.io;

import de.jpp.io.interfaces.GraphWriter;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class GxlWriterTemplate<N, A, G extends Graph<N, A>> implements GraphWriter<N, A, G, String> {

    protected Map<N, String> nodes = new HashMap<>();
    protected Map<Edge<N,A>, String> edges = new HashMap<>();
    protected int nodeCount = 0;
    protected int edgeCount = 0;

    protected static Element createAttribute(String name, String type, String value) {

        return new Element("attr").setAttribute("name", name).addContent(new Element(type).setText(value));
    }

    @Override
    public String write(G graph){
        nodeCount = 0;
        edgeCount = 0;
        nodes = graph.getNodes().stream().collect(Collectors.toMap(n -> n, this::calculateId));
        edges = graph.getEdges().stream().collect(Collectors.toMap(e -> e, this::calculateId));

        Element subRoot = new Element("graph");
        nodes.forEach((node, id) -> subRoot.addContent(writeNode(node).setAttribute("id", id)));
        edges.forEach((edge, id) -> subRoot.addContent(writeEdge(edge).setAttribute("id", id)));

        return new XMLOutputter().outputString(new Document().setRootElement(new Element("gxl").setContent(subRoot)));
    }
    public abstract Element writeNode(N node);

    public abstract Element writeEdge(Edge<N, A> edge);

    public abstract String calculateId(N node);

    public abstract String calculateId(Edge<N, A> edge);
}
