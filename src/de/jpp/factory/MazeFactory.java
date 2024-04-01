package de.jpp.factory;

import de.jpp.io.TwoDimMazeReader;
import de.jpp.io.interfaces.GraphReader;
import de.jpp.maze.Maze;
import de.jpp.maze.MazeImpl;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class MazeFactory {

    public static void main(String[] args) throws IOException {
        Random ran = new Random();
        //Maze m = MazeFactory.getRandomMaze(ran, 10, 15);
        Maze m = new MazeFactory().getEmptyMaze(5, 5);
        m.setHWall(0,0, true);
        BufferedImage img = new MazeFactory().getMazeAsImage(m);
        ImageIO.write(img, "png", new File("img.png"));

    }

    /**
     * Returns a random maze with specified width, height created by the specified algorithm specified from
     * the instruction <br>
     * Random numbers are only taken from the specified random number generator (RNG) and only as specified
     * in the instruction
     *
     * @param ran    the random number generator (RNG)
     * @param width  the width
     * @param height the height
     * @return a random maze with specified width and height
     */
    public static Maze getRandomMaze(Random ran, int width, int height) {
        Maze m = new MazeFactory().getEmptyMaze(width, height);
        divide(m, ran, 0, 0, width, height);
        return m;
    }

    private static void divide(Maze m, Random ran, int x, int y, int w, int h) {
        if (w < 2 || h < 2)
            return;

        if (w > h)
            divideHoriz(m, ran, x, y, w, h);
        else if (w < h)
            divideVert(m, ran, x, y, w, h);
        else {
            if (ran.nextBoolean())
                divideHoriz(m, ran, x, y, w, h);
            else
                divideVert(m, ran, x, y, w, h);
        }
    }

    private static void divideVert(Maze m, Random ran, int x, int y, int w, int h) {
        int rnd1 = ran.nextInt(h - 1);
        int rnd2 = ran.nextInt(w);

        for (int i = 0; i < w; i++)
            if (i != rnd2)
                m.setHWall(x + i, y + rnd1, true);

        divide(m, ran, x, y, w, rnd1 + 1);
        divide(m, ran, x, y + rnd1 + 1, w, h - rnd1 - y - 1);
    }

    private static void divideHoriz(Maze m, Random ran, int x, int y, int w, int h) {
        int rnd1 = ran.nextInt(w - 1);
        int rnd2 = ran.nextInt(h);

        for (int i = 0; i < h; i++)
            if (i != rnd2)
                m.setVWall(x + rnd1, y + i, true);

        divide(m, ran, x, y, rnd1 + 1, h);
        divide(m, ran, x + rnd1 + 1, y, w - rnd1 - x - 1, h);
    }

    /**
     * Creates a new empty maze with the specified width and height
     *
     * @param width  the width
     * @param height the height
     * @return a new empty maze with the specified width and height
     */
    public Maze getEmptyMaze(int width, int height) {
        return new MazeImpl(width, height);
    }

    /**
     * Returns a pixel representation of the specified maze
     *
     * @param maze the maze
     * @return a pixel representation of the specified maze
     */
    public BufferedImage getMazeAsImage(Maze maze) {
        Color black = new Color(0, 0, 0);
        Color white = new Color(255, 255, 255);


        BufferedImage img = new BufferedImage(maze.getWidth() * 2 + 1, maze.getHeight() * 2 + 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();
        graphics.setPaint(white);
        //graphics.fillRect(1, 1, maze.getWidth() * 2 - 1, maze.getHeight() * 2 - 1);
        graphics.fillRect(1, 1, maze.getWidth() * 2 - 1, maze.getHeight() * 2 - 1);
        // Zellen auf weiß setzen
        for (int y = 0; y < maze.getHeight(); y++) {

            for (int x = 1; x < maze.getWidth(); x++) { // x = 0 --> 1/2 ist gerade schwarz, sollte weiß sein x = 1 --> 2/1 ist gerade weiß, sollte schwarz sein
                    img.setRGB(x * 2, y * 2, black.getRGB());
                System.out.println("Position x:" + x + " y:" + y + " Farbe: " + img.getRGB(x,y));

                if (y < maze.getHeight() - 1 && maze.isHWallActive(x, y))
                    img.setRGB(x * 2 + 1, y * 2 + 2, black.getRGB());
                System.out.println("Horizontale Wand aktiv an Position x:" + x + " y:" + y  + " Farbe: " + img.getRGB(x,y));

                if (x < maze.getWidth() - 1 && maze.isVWallActive(x, y))
                    img.setRGB(x * 2 + 2, y * 2 + 1, black.getRGB());
                System.out.println("Vertikale Wand aktiv an Position x:" + x + " y:" + y + " Farbe: " + img.getRGB(x,y));
            }

        }

        return img;

    }

    /**
     * Returns a GraphReader which parses a TwoDimGraph from a Maze-Object
     *
     * @return a GraphReader which parses a TwoDimGraph from a Maze-Object
     */
    public GraphReader<XYNode, Double, TwoDimGraph, Maze> getMazeReader() {
        return new TwoDimMazeReader();
    }

}
