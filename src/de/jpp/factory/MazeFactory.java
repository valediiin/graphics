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
            if (i != rnd2) {
                System.out.println("Setting HWall at: " + (x + i) + "|" + (y + rnd1) + " to " + true);
                m.setHWall(x + i, y + rnd1, true);
            }


        divide(m, ran, x, y, w, rnd1 + 1);
        divide(m, ran, x, y + rnd1 + 1, w, h - rnd1 - y - 1);
        //divide(m, ran, x, y + rnd1 + 1, w, h - rnd1 - 1);
    }

    private static void divideHoriz(Maze m, Random ran, int x, int y, int w, int h) {
        int rnd1 = ran.nextInt(w - 1);
        int rnd2 = ran.nextInt(h);

        for (int i = 0; i < h; i++)
            if (i != rnd2)
                m.setVWall(x + rnd1, y + i, true);

        divide(m, ran, x, y, rnd1 + 1, h);
        divide(m, ran, x + rnd1 + 1, y, w - rnd1 - x - 1, h);
        //divide(m, ran, x + rnd1 + 1, y, w - rnd1 - 1, h);
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
        Color yellow = new Color(255,255,0);
        Color orange = new Color(255,105,0);


        BufferedImage img = new BufferedImage(maze.getWidth() * 2 + 1, maze.getHeight() * 2 + 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();
        graphics.setPaint(white); // Das ganze bild auf weiß setzen
        graphics.fillRect(1, 1, maze.getWidth() * 2 - 1, maze.getHeight() * 2 - 1);


       for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                img.setRGB(x * 2, y * 2, black.getRGB());


               if (y < maze.getHeight() - 1 && maze.isHWallActive(x, y)) {
                   img.setRGB(x * 2 , y * 2, black.getRGB());

               }

               if (x < maze.getWidth() - 1 && maze.isVWallActive(x, y)) {
                   img.setRGB(x * 2, y * 2, black.getRGB());

               }

            }
        }

      /* // HWall
       for(int y = 0; y < maze.getHeight(); y++){
           for(int x = 0; x < maze.getWidth() - 1; x++){

               if(maze.isHWallActive(x,y)){
                   img.setRGB(x * 2 + 2,y * 2 + 1, yellow.getRGB());
                   System.out.println(x * 2 + 2 + " " + y);
               } else {
                   img.setRGB(x * 2 + 2,y * 2 + 1, white.getRGB());
               }
           }
       }

        // VWall
        for(int y = 0; y < maze.getHeight() - 1; y++){
            for(int x = 0; x < maze.getWidth(); x++){

                if(maze.isVWallActive(x,y)){
                    img.setRGB(x * 2 + 1,y * 2 + 2, orange.getRGB());
                    System.out.println(x * 2 + 1 + " " + y);
                } else {
                    img.setRGB(x * 2 + 1,y * 2 + 2, white.getRGB());
                }
            }
        }*/

        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                // Horizontale Wand und nicht am unteren Rand des Labyrinths
                if (y < maze.getHeight() - 1) {
                    if (maze.isHWallActive(x, y)) {
                        img.setRGB((x * 2) + 2, (y * 2) + 1, yellow.getRGB());
                    }
                }

                // Vertikale Wand und nicht am rechten Rand des Labyrinths
                if (x < maze.getWidth() - 1) {
                    if (maze.isVWallActive(x, y)) {
                        // Beibehalten der korrekten Position für das Zeichnen der vertikalen Wand
                        img.setRGB((x * 2) + 1, (y * 2) + 2, orange.getRGB());
                    }
                }
            }
        }

        return img;
    }

    public static void main(String[] args) throws IOException {
        Random ran = new Random();
        //Maze m = MazeFactory.getRandomMaze(ran, 10, 10)
        Maze m = new MazeFactory().getEmptyMaze(5, 3);
        m.setAllWalls(true);
        //m.setHWall(0,0,true);
        //m.setHWall(4,2,true);
        m.setVWall(1,1,true);
        m.setVWall(0,0,true);

        BufferedImage img = new MazeFactory().getMazeAsImage(m);
        ImageIO.write(img, "png", new File("img.png"));

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
