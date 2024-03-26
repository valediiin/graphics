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
        IntStream.range(0, height - 1).forEach(i -> hWalls.add(IntStream.range(0, width).mapToObj(si -> false).collect(Collectors.toList())));
        IntStream.range(0, height).forEach(i -> vWalls.add(IntStream.range(0, width - 1).mapToObj(si -> false).collect(Collectors.toList())));
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

        if(x >= 0 && x < width && y >= 0 && y < height){
            hWalls.get(y).set(x, wallActive);
        } else {
            throw new IllegalArgumentException("Invalid x and y");
        }
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

        if(x >= 0 && x < width - 1 && y >= 0 && y < height){
            vWalls.get(y).set(x, wallActive);
        } else {
            throw new IllegalArgumentException("Invalid x and y");
        }
    }

    /**
     * Sets all vertical and horizontal walls active (non-passable) or inactive (passable)
     *
     * @param wallActive whether the walls are active (non-passable) or not
     */
    @Override
    public void setAllWalls(boolean wallActive) {

        for(List<Boolean> row : hWalls){
            Collections.fill(row, wallActive);
        }

        for(List<Boolean> row : vWalls){
            Collections.fill(row, wallActive);
        }

        /*Collections.fill(hWalls.get(0), true);
        Collections.fill(hWalls hWalls.get(0));
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

        if(x >= 0 && x < width && y >= 0 && y < height - 1){
            return hWalls.get(y).get(x);
        } else {
            throw new IllegalArgumentException("Invalid x and y");
        }
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

        if(x >= 0 && x < width - 1 && y >= 0 && y < height){
            return vWalls.get(y).get(x);
        } else {
            throw new IllegalArgumentException("Invalid x and y");
        }


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
