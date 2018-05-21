import java.util.ArrayList;

public class Grid {
    
    private Cell[][] grid;
    private ArrayList<Cell> arr;
    private int numRows;
    private int numCols;

    public Grid(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.grid = new Cell[numRows][numCols];
        this.arr = new ArrayList<Cell>();
        this.instantiateCells();
    }

    private void instantiateCells() {
        for (int r = 0; r < this.numRows; r++) {
            for (int c = 0; c < this.numCols; c++) {
                this.grid[r][c] = new Cell(r, c);
                this.arr.add(this.grid[r][c]);
            }
        }
        for (int r = 0; r < this.numRows; r++) {
            for (int c = 0; c < this.numCols; c++) {
                this.grid[r][c].addNeighbors(this);
            }
        }
    }

    public ArrayList<Cell> asArray() {
        return (new ArrayList<Cell>(this.arr));
    }

    public Cell getCellAt(int row, int col) {
        return grid[row][col];
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }
}
