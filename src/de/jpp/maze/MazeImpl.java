package de.jpp.maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MazeImpl implements Maze {
    private final List<List<Boolean>> hWalls = new ArrayList<>();
    private final List<List<Boolean>> vWalls = new ArrayList<>();
    private final int width;
    private final int height;

    public MazeImpl(int width, int height) {
        this.width = width;
        this.height = height;
        IntStream.range(0, height).forEach(i -> hWalls.add(IntStream.range(0, width - 1).mapToObj(si -> false).collect(Collectors.toList())));
        IntStream.range(0, height - 1).forEach(i -> vWalls.add(IntStream.range(0, width).mapToObj(si -> false).collect(Collectors.toList())));

        System.out.println(hWalls);
        System.out.println(vWalls);
        //System.out.println(hWalls);

        /*// Erstellen der horizontalen Wände (hWalls)
        for (int i = 0; i < height - 1; i++) {
            List<Boolean> row = new ArrayList<>();
            for (int si = 0; si < width; si++) {
                row.add(false);
            }
            hWalls.add(row);
        }

        // Erstellen der vertikalen Wände (vWalls)
        for (int i = 0; i < height; i++) {
            List<Boolean> row = new ArrayList<>();
            for (int si = 0; si < width - 1; si++) {
                row.add(false);
            }
            vWalls.add(row);
        }*/
    }

    /**
     * Sets the horizontal wall active (non-passable) or inactive (passable)
     *
     * @param x          the x coordinate of the horizontal wall
     * @param y          the y coordinate of the horizontal wall
     * @param wallActive whether the wall is active (non-passable) or not
     */
    @Override
    public void setHWall(int x, int y, boolean wallActive) {

        System.out.println("Horizontale Wand gesetzt : " + x + "/" + y);

        hWalls.get(y).set(x, wallActive);

    }

    /**
     * Sets the vertical wall active (non-passable) or inactive (passable)
     *
     * @param x          the x coordinate of the vertical wall
     * @param y          the y coordinate of the vertical wall
     * @param wallActive whether the wall is active (non-passable) or not
     */
    @Override
    public void setVWall(int x, int y, boolean wallActive) {

        System.out.println("Vertikale Wand gesetzt : " + x + "/" + y);

        vWalls.get(y).set(x, wallActive);

    }

    /**
     * Sets all vertical and horizontal walls active (non-passable) or inactive (passable)
     *
     * @param wallActive whether the walls are active (non-passable) or not
     */
    @Override
    public void setAllWalls(boolean wallActive) {

        //hWalls
        System.out.println(hWalls);
        for(int i = 0; i < height; i++){
            for (int j = 0; j < width - 1; j++){
                System.out.println(i + " " + j);
                hWalls.get(i).set(j, wallActive);
            }
        }

        //vWalls
        System.out.println(vWalls);
        for(int i = 0; i < height - 1; i++){
            for (int j = 0; j < width - 1; j++){
                //System.out.println(i + " " + j);
                vWalls.get(i).set(j, wallActive);
            }
        }


      /*  for(List<Boolean> row : hWalls){
            Collections.fill(row, wallActive);
        }

        for(List<Boolean> row : vWalls){
            Collections.fill(row, wallActive);
        }*/

        /*Collections.fill(hWalls.get(0), true);
        Collections.fill(hWalls, hWalls.get(0));
        Collections.fill(vWalls.get(0), true);
        Collections.fill(vWalls, vWalls.get(0));*/
    }

    /**
     * returns the width (amount of cells in a row) of this maze
     *
     * @return the width (amount of cells in a row) of this maze
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * returns the height (amount of cells in a column) of this maze
     *
     * @return the height (amount of cells in a column) of this maze
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Returns whether the horizontal wall at the specified coordinate is active (non-passable) or inactive (passable)
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return whether the horizontal wall at the specified coordinate is active (non-passable) or inactive (passable)
     */
    @Override
    public boolean isHWallActive(int x, int y) {

        if(x >= 0 && x < width - 1 && y >= 0 && y < height){
            return hWalls.get(y).get(x);
        }

        return false;
    }

    /**
     * Returns whether the vertical wall at the specified coordinate is active (non-passable) or inactive (passable)
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return whether the vertical wall at the specified coordinate is active (non-passable) or inactive (passable)
     */
    @Override
    public boolean isVWallActive(int x, int y) {

        if(x >= 0 && x < width && y >= 0 && y < height - 1){
            return vWalls.get(y).get(x);
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MazeImpl maze = (MazeImpl) o;
        return width == maze.width && height == maze.height && Objects.equals(hWalls, maze.hWalls) && Objects.equals(vWalls, maze.vWalls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hWalls, vWalls, width, height);
    }
}
