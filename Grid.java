/* Implementation of a simulation Grid. Can be thought of as the map or
 * landscape made up of individual Patches. The Grid itself has an infinite
 * horizon, in that when an entiy moves off the edge of a the Grid, it
 * should appear on the opposite side.
 *
 */

import java.util.ArrayList;
import java.util.HashMap;

public class Grid {

    private int rows;
    private int cols;
    private double range;

    private Patch[][] grid;
    private HashMap<Patch, ArrayList<Patch>> neighborhoods;

    public Grid(int rows, int cols, double range) {
        this.rows = rows;
        this.cols = cols;
        this.range = range;

        grid = generateGrid(rows, cols);

        neighborhoods = generateNeighborhoods(grid, range);
    }

    public Patch[][] getGrid() {
        return grid;
    }

    public void setGrid(Patch[][] grid) {
        this.grid = grid;
    }

    private Patch[][] generateGrid(int rows, int cols) {
        // instantiate the grid
        grid = new Patch[rows][cols];

        // instantiate a Patch in every cell
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid[row][col] = new Patch(row, col);
            }
        }

        return grid;
    }

    private HashMap<Patch, ArrayList<Patch>> generateNeighborhoods(
            Patch[][] grid, double range) {
        // instantiate neigbourhoods
        neighborhoods = new HashMap<Patch, ArrayList<Patch>>();

        // find the neighbourhood for each cell
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                neighborhoods.put(grid[row][col],
                        generateNeighborhood(grid[row][col], range));
            }
        }

        return neighborhoods;
    }

    private ArrayList<Patch> generateNeighborhood(
            Patch centre, double range) {
        // check whether each cell in the grid is within range of centre
        int centreRow = centre.getRow();
        int centreCol = centre.getCol();
        ArrayList<Patch> neighborhood = new ArrayList<Patch>();
        Patch other;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (distance(centreRow, centreCol, row, col) <= range) {
                    other = grid[row][col];
                    neighborhood.add(other);
                }
            }
        }
        return neighborhood;
    }

    // computes grid distance between two cells
    private double distance(int row1, int col1, int row2, int col2) {
        int rowDistance;
        int colDistance;

        if (Math.abs(row1 - row2) < rows / 2) {
            rowDistance = Math.abs(row1 - row2);
        } else if (row1 < rows / 2) {
            rowDistance = Math.abs(row1 - (row2 - rows));
        } else {
            rowDistance = Math.abs(row1 - (row2 + rows));
        }

        if (Math.abs(col1 - col2) < cols / 2) {
            colDistance = Math.abs(col1 - col2);
        } else if (col1 < cols / 2) {
            colDistance = Math.abs(col1 - (col2 - cols));
        } else {
            colDistance = Math.abs(col1 - (col2 + cols));
        }

        return Math.hypot(rowDistance, colDistance);
    }

    public ArrayList<Patch> getTargets(Patch centre) {
        ArrayList<Patch> targets = new ArrayList<Patch>();
        for (Patch patch : neighborhoods.get(centre)) {
            if (!patch.isOccupied()) {
                targets.add(patch);
            }
        }
        return targets;
    }

    public ArrayList<Occupant> getNeighbors(Patch centre) {
        ArrayList<Occupant> neighbors = new ArrayList<Occupant>();
        for (Patch patch : neighborhoods.get(centre)) {
            neighbors.addAll(patch.getOccupants());
        }
        return neighbors;
    }
}
