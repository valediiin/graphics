package de.jpp.io;

import de.jpp.io.interfaces.GraphReader;
import de.jpp.io.interfaces.GraphWriter;
import de.jpp.io.interfaces.ParseException;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TwoDimGraphDotIO implements GraphReader<XYNode, Double, TwoDimGraph, String>, GraphWriter<XYNode, Double, TwoDimGraph, String> {

    private final char norm = '$';
    private final String res = "$dollar$";
    private final Map<Character, String> normToRes =
            Map.of('/', "$slash$", '#', "$hash$", '*', "$star$", '>', "$greater$", ' ', "$space", '=', "$equals$", '[', "$leftBracket$", ']', "$rightBracket$");

    private boolean errored = false;
    private String msg = "";

    public static void main(String[] args) throws ParseException {

        TwoDimGraphDotIO rw = new TwoDimGraphDotIO();
        TwoDimGraph g = new TwoDimGraph();
        XYNode n1 = new XYNode("a", 100, 100);
        XYNode n2 = new XYNode("b", 200, 200);
        g.addNodes(n1, n2);
        g.addEdge(n1, n2, Optional.empty());
        System.out.println(rw.write(rw.read(rw.write(g))));
    }
    @Override
    public TwoDimGraph read(String input) throws ParseException {

        TwoDimGraph graph = new TwoDimGraph();
        Map<Integer, XYNode> nodes = new HashMap<>();

        boolean multiLine = false;
        boolean comment = false;
        boolean inQuotes = false;

        StringBuilder mod = new StringBuilder();
        char old = '?';

        for(char c : input.toCharArray()){

            if(c == '"'){
                inQuotes = !inQuotes;
            }

            if(inQuotes){
                if(c == norm){
                    mod.append(res);
                } else if(normToRes.containsKey(c)){
                    mod.append(normToRes.get(c));
                } else {
                    mod.append(c);
                }
            } else {
                if (old == '/' && c == '*') {
                    mod.deleteCharAt(mod.length() - 1);
                    multiLine = true;
                } else if (c == '#') {
                    comment = true;
                } else if (old == '/' && c == '/') {
                    mod.deleteCharAt(mod.length() - 1);
                    comment = true;
                }

                if (!(comment || multiLine) && !((old == ' ' || old == '\t') && c == ' ')) {
                    mod.append(c);
                }

                if (old == '*' && c == '/' && multiLine) {
                    multiLine = false;
                } else if (c == '\n' && comment) {
                    comment = false;
                }
            }

                old = c;
            }

            if(inQuotes){
                throw new ParseException("Missing closing quote");
            }

            String structure = mod.toString().strip();
            Pattern start = Pattern.compile("^\\s*(di)?graph(\\s+\\w+)?\\s*\\{");
            Pattern end = Pattern.compile("}\\s*$");

            if(!start.matcher(structure).find()){
                throw new ParseException("Graph does not start with the keyword graph and opening bracket");
            } else if (!end.matcher(structure).find()){
                throw new ParseException("Closing bracket at the end not detected");
            }

            structure = structure.replaceAll(start.pattern(), "");
            structure = structure.replaceAll(end.pattern(), "");

            Comparator<String> comparator = (s1, s2) ->
            {
                if(s1.contains("->") ^ s2.contains("->")){
                    return s1.contains("->") ? 1: -1;
                }

                return s1.compareTo(s2);
            };

            Arrays.stream(structure.split("\n")).sorted(comparator).forEach(l -> parseLine(graph, nodes, l));

            if(errored){

                throw new ParseException(msg);
            }


        return graph;
    }

    public void parseLine(TwoDimGraph graph, Map<Integer, XYNode> nodes, String rawLine){

        if(rawLine.isBlank() || errored){
            return;
        }

        String line = rawLine.strip();

        if(!line.contains("->")){
            parseNode(graph, nodes, line);
        } else {
            parseEdge(graph, nodes, line);
        }
    }

    public void parseNode(TwoDimGraph graph, Map<Integer, XYNode> nodes, String rawNode){

        String[] parts = rawNode.split("\\s+", 2);

        if(parts.length != 2){
            errored = true;
            msg = String.format("Node either does not have an id or attributes: %s", rawNode);
            return;
        }

        try {
            int nid = Integer.parseInt(parts[0]);
            String rawAttributes = parts[1].replaceAll("\\[\\s*", "").replaceAll("\\s*]", "");
            Map<String, String> attributes = parseAttributes(rawAttributes);

            XYNode node =
                    new XYNode(attributes.getOrDefault("label", ""), Double.parseDouble(attributes.get("x")), Double.parseDouble(attributes.get("y")));
            nodes.put(nid, node);
            graph.addNode(node);
        } catch (NumberFormatException ex){
            errored = true;
            msg = String.format("NoteId, x or y could not be parsed to number: %s", ex.getMessage());
        } catch (NullPointerException ex){
            errored = true;
            msg = String.format("Failed to get the label, x or y value: %s", ex.getMessage());
        }
    }

    public void parseEdge(TwoDimGraph graph, Map<Integer, XYNode> nodes, String rawEdge){

        String[] subParts = rawEdge.split("\\s*->\\s*", 2);

        if(subParts.length != 2){
            errored = true;
            msg = String.format("Edge either does not have specified direction, missing start or destination: %s", rawEdge);
            return;
        }

        String[] backs = subParts[1].strip().split("\\s+", 2);

        if(backs.length != 2){
            errored = true;
            msg = String.format("Edge is missing destination or attributes: %s", rawEdge);
            return;
        }

        String [] parts = {subParts[0], backs[0], backs[1]};

        try {
            int sid = Integer.parseInt(parts[0]);
            int did = Integer.parseInt(parts[1]);

            String rawAttributes = parts[2].replaceAll("\\[\\s*", "").replaceAll("\\s*]", "");
            Map<String, String> attributes = parseAttributes(rawAttributes);
            Optional<Double> option = attributes.containsKey("dist") ? Optional.of(Double.parseDouble(attributes.get("dist"))) : Optional.empty();

            graph.addEdge(nodes.get(sid), nodes.get(did), option);
        } catch (NumberFormatException ex){
            errored = true;
            msg = String.format("StartId, DestinationId or distance could not be parsed to number: %s", ex.getMessage());
        } catch (NullPointerException ex){
            errored = true;
            msg = String.format("Missing start or destination ode: %s", ex.getMessage());
        }
    }

    public Map<String, String> parseAttributes(String rawAttributes){
        Map<String, String> attributes = new HashMap<>();
        Arrays.stream(rawAttributes.split("\\s+"))
                .filter(s -> !s.isBlank())
                .forEach(r -> addToAttributes(attributes, r.split("=", 2)));
        return attributes;
    }

    private void addToAttributes(Map<String, String> attributes, String[] kv){

        String value = kv[1];

        for(Map.Entry<Character, String> nr : normToRes.entrySet()){
            value = value.replace(nr.getValue(), String.valueOf(nr.getKey()));
        }

        attributes.put(kv[0], value.replace(res, String.valueOf(norm)).replace("\"", ""));
    }

    @Override
    public String write(TwoDimGraph graph) {

        List<XYNode> nodes = new ArrayList<>(graph.getNodes());

        String nodeStack = graph.getNodes().stream()
                .map(n -> String.format("%s [label=\"%s\" x=%s y=%s]", nodes.indexOf(n), n.getLabel(), n.getX(), n.getY()))
                .collect(Collectors.joining("\n"));

        String dirStack = graph.getEdges().stream()
                .map(e -> String.format("%s -> %s [%s]", nodes.indexOf(e.getStart()), nodes.indexOf(e.getDestination()),
                        e.getAnnotation().isPresent() ? String.format("dist=%s", e.getAnnotation().get()) : ""))
                .collect(Collectors.joining("\n"));
        return String.format("digraph{\n%s\n%s\n}", nodeStack, dirStack);
    }
}
