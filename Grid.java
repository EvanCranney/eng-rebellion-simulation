/* Implentation of the simulation Grid.
 *
 * Contains the Cells that can be occupied by Agents and Cops.
 *
 */

import java.util.ArrayList;

public class Grid {
    
    // matric representation of cells in grid
    private Cell[][] grid;

    // array representation of cells in grid - this is useful
    //   for when we want to pick a random cell out of the grid
    private ArrayList<Cell> arr;

    // dimensions of the grid
    private int numRows;
    private int numCols;

    // radius of each of the neighborhoods centred around each cell
    private double vision;

    public Grid(int numRows, int numCols, double vision) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.vision = vision;
        this.grid = new Cell[numRows][numCols];
        this.arr = new ArrayList<Cell>();
        this.instantiateCells();
    }
    
    // instantiate a cell object for each coordinate in the matricx
    private void instantiateCells() {
        
        // first pass: instantiate all the cells
        for (int r = 0; r < this.numRows; r++) {
            for (int c = 0; c < this.numCols; c++) {
                this.grid[r][c] = new Cell(r, c);
                // add it to the array representation at the same time
                this.arr.add(this.grid[r][c]);
            }
        }
    
        // second pass: add the neighbors of each cell, now that they're
        //   all instantiated
        for (int r = 0; r < this.numRows; r++) {
            for (int c = 0; c < this.numCols; c++) {
                this.grid[r][c].addNeighbors(this, this.vision);
            }
        }
    }

    // return arraylist representation of array
    public ArrayList<Cell> asArray() {
        return (new ArrayList<Cell>(this.arr));
    }

    // return the cell at the given row and column
    public Cell getCellAt(int row, int col) {
        return grid[row][col];
    }

    // get the number of rows in the matrix
    public int getNumRows() {
        return numRows;
    }

    // get the number of cols in the matrix
    public int getNumCols() {
        return numCols;
    }
}
