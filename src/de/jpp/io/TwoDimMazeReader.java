package de.jpp.io;

import de.jpp.io.interfaces.ParseException;
import de.jpp.maze.Maze;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;

import java.util.HashMap;
import java.util.Map;

public class TwoDimMazeReader extends GridReaderTemplate<Maze> {

    /**
     * Creates a graph from the specified input source
     *
     * @param input the input source
     * @return a graph from the input source
     * @throws ParseException if the input cannot be parsed correctly
     */
    @Override
    public TwoDimGraph read(Maze input) throws ParseException {
        Map<String, XYNode> nodes = new HashMap<>();

        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {

                nodes.put(String.format("%s|%s", x * 2, y * 2), new XYNode("", x * 2, y * 2));

                if (y < input.getHeight() - 1 && !input.isHWallActive(x, y))
                    nodes.put(String.format("%s|%s", x * 2 + 1 , y * 2), new XYNode("", x * 2 + 1, y * 2));

                if (x < input.getWidth() - 1 && !input.isVWallActive(x, y))
                    nodes.put(String.format("%s|%s", x * 2, y * 2 + 1), new XYNode("", x * 2, y * 2 + 1));
            }
        }

        return createGraph(nodes);
    }
}
