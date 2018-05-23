/* Implementation of a single Cell in the simulation Grid.
 *
 * Used as the "location" for entities that are occupying the Cell.
 *
 * Keeps track of all other Cells in the grid that are within the distance
 * specified by VISION. These constitute the "neighborhood" centred on this
 * Cell. Used by Agents and Cops to (1) find other Cells in the neighborhood
 * to move to, and (2) find other Agents and Cops in the neighborhood.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.lang.Math;

public class Cell {

    // row and column of the cell in the simulation grid
    private int row;
    private int col;

    // the Cell's neighbors
    private ArrayList<Cell> neighbors;

    // any agents and cops occuping the cell
    // Note: even though there is a rule which prevents Agents and Cops from
    //  entering occupiped cells, we still have to implement Agent/Cop
    //  storage using an Array. This is because there are a few corner case
    //  in Rebellion which may result in multiple Agents or Cops occuping
    //  one cell (e.g. if an Agent comes out of the jailed state in this
    //  Cell when another Agent is already occupying it).
    private ArrayList<Agent> agents;
    private ArrayList<Cop> cops;

    private Random rand;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.neighbors = new ArrayList<Cell>();
        this.agents = new ArrayList<Agent>();
        this.cops = new ArrayList<Cop>();
        this.rand = new Random();
    }

    // Agent enters the cell
    public void enter(Agent agent) {
        this.agents.add(agent);
    }

    // Agent leaves the cell
    public void leave(Agent agent) {
        this.agents.remove(agent);
    }

    // Cop enters the cell
    public void enter(Cop cop) {
        this.cops.add(cop);
    }

    // Cop leaves the cell
    public void leave(Cop cop) {
        this.cops.remove(cop);
    }

    // checks whether the cell is occupied; a cell is regarded as occupied
    //   if there is any cop in the cell OR there is any non-jailed agent
    //   in the cell
    public boolean isOccupied() {

        // if the cell has cops in it, it's occupied
        if (!this.cops.isEmpty()) {
            return true;
        }

        // if the cell has any active agent in it, it's occupied
        for (Agent agent : this.agents) {
            if (!agent.isJailed()) {
                return true;
            }
        }
        
        return false;
    }
    
    // Add neighbors to the cell; that is, all Cells in the given grid
    //   whose coordinatesd are within the given distance (vision) of this
    //   grid's coordinates
    public void addNeighbors(Grid grid, double vision) {
    
        // fetch row dimensions (used to compute wraparound distance)
        int rows = grid.getNumRows();
        int cols = grid.getNumCols();

        // find all cells within given distance (vision) of the cell
        // Note: this includes the cell itself; a cell can be its own
        //   neighbor
        Cell other;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                other = grid.getCellAt(r, c);
                if (this.distanceTo(other, rows, cols) <= vision) {
                    this.addNeighbor(other);
                }
            }
        }
    }

    // adds the given cell to the neighbors of this cell. helper to previous
    //   method
    private void addNeighbor(Cell neighbor) {
        this.neighbors.add(neighbor);
    }


    // randomly select an unoccupied cell from the cell's neighbors,
    //   used by Agent and Cop to determine where to move to
    public Cell getRandomUnoccupiedNeighbor() {

        // add all unoccupied neighbors to potential list of targets
        ArrayList<Cell> targets = new ArrayList<Cell>();
        for (Cell cell : this.neighbors) {
            if (!cell.isOccupied()) {
                targets.add(cell);
            }
        }

        // if there are no unoccupied cells, then return null
        if (targets.size() <= 0) {
            return null;

        }

        // otherwise, randomly choose a cell from targets
        return targets.get(this.rand.nextInt(targets.size()));
    }

    // counts the number of cops in the neighborhood
    //  used by agents to compute risk of rebelling
    public int countCopsInNeighborhood() {
        int count = 0;
        for (Cell neighbor : this.neighbors) {
            count = count + neighbor.countCops();
        }
        return count;
    }

    // counts the number of active agents in the neighborhood
    //  used by agents to compute risk of rebelling
    public int countActiveAgentsInNeighborhood() {
        int count = 0;
        for (Cell neighbor : this.neighbors) {
            count = count + neighbor.countActiveAgents();
        }
        return count;
    }

    // counts number of cops in the cell
    public int countCops() {
        return this.cops.size();
    }

    // counts number of active agents in the cell
    public int countActiveAgents() {
        int count = 0;
        for (Agent agent : this.agents) {
            if (agent.isActive()) {
                count++;
            }
        }
        return count;
    }

    // returns a list of all the active agents in the neighborhood
    //   used by cops when looking for active agents to arrest
    public ArrayList<Agent> getActiveAgentsInNeighborhood() {
        ArrayList<Agent> agents = new ArrayList<Agent>();
        for (Cell neighbor : this.neighbors) {
            agents.addAll(neighbor.getActiveAgents());
        }
        return agents;
    }

    // gets all the active agents in the cell. helper to previous method
    public ArrayList<Agent> getActiveAgents() {
        ArrayList<Agent> active = new ArrayList<Agent>();
        for (Agent agent : this.agents) {
            if (agent.isActive()) {
                active.add(agent);
            }
        }
        return active;
    }

    // computes distance from this cell to other cell, accounting for
    //   fact that the grid in the simulation wraps
    private double distanceTo(Cell other, int rows, int cols) {

        // get row and col of other cell
        int otherRow = other.getRow();
        int otherCol = other.getCol();

        // compute shortest distance between this cell's row and other's row
        double rowDistance = Math.abs(this.row - otherRow);
        // if the other cell is in opposite half of the grid ...
        if (rowDistance > rows/2) {
            // ... then shortest distance is to go to edge of grid and wrap
            rowDistance = rows - rowDistance;
        }

        // compute shortest distance between cell's col and other's col
        //   same as computation for row.
        double colDistance = Math.abs(this.col - otherCol);
        if (colDistance > cols/2) {
            colDistance = cols - colDistance;
        }
   
        // Euclidean distance between the cells
        return Math.hypot(rowDistance, colDistance);
    }

    // get row index that the cell occupies in simulation grid
    public int getRow() {
        return row;
    }

    // get col index that row occupies in simulation grid
    public int getCol() {
        return col;
    }
}
