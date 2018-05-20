public class Grid {
    
    private Cell[][] grid;
    private int numRows;
    private int numCols;

    public Grid(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.grid = new Cell[numRows][numCols];
        this.instantiateCells();
    }

    private void instantiateCells() {
        for (int r = 0; r < this.numRows; r++) {
            for (int c = 0; c < this.numCols; c++) {
                this.grid[r][c] = new Cell(r, c);
            }
        }
        for (int r = 0; r < this.numRows; r++) {
            for (int c = 0; c < this.numCols; c++) {
                this.grid[r][c].addNeighbors(this);
            }
        }
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
