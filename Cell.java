import java.util.ArrayList;
import java.lang.Math;

public class Cell {

    private int row;
    private int col;
    private ArrayList<Cell> neighbors;

    private ArrayList<Agent> agents;
    private ArrayList<Cop> cops;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.neighbors = new ArrayList<Cell>();
    }

    public void enter(Agent agent) {
        this.agents.add(agent);
    }

    public void exit(Agent agent) {
        this.agents.remove(agent);
    }

    public void enter(Cop cop) {
        this.cops.add(cop);
    }

    public void leave(Cop cop) {
        this.cops.remove(cop);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    public void addNeighbors(Grid grid) {
        int rows = grid.getNumRows();
        int cols = grid.getNumCols();
        Cell other;
        //System.out.println("START");
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                other = grid.getCellAt(r, c);
                if (this.distanceTo(other, rows, cols)
                        <= Rebellion.PARAM_VISION) {
                    this.addNeighbor(other);
                    //System.out.println(other);
                }
            }
        }
       // System.out.println(this.neighbors.size());
    }

    private void addNeighbor(Cell neighbor) {
        this.neighbors.add(neighbor);
    }

    private double distanceTo(Cell other, int rows, int cols) {
        int otherRow = other.getRow();
        int otherCol = other.getCol();

        //System.out.println(otherRow);
        //System.out.println(otherCol);

        // smallest row distance
        double rowDistance = Math.abs(this.row - otherRow);
        if (rowDistance > rows/2) {
            rowDistance = rows - rowDistance;
        }

        // smallest col distance
        double colDistance = Math.abs(this.col - otherCol);
        if (colDistance > cols/2) {
            colDistance = cols - colDistance;
        }
   
        //System.out.println(Math.hypot(rowDistance, colDistance));
        return Math.hypot(rowDistance, colDistance);
    }

    public String toString() {
        return ("Cell(" + Integer.toString(this.row) + "," + Integer.toString(this.col) + ")");
    }
}
