import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.lang.Math;

public class Cell {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_HIGHLIGHT_YELLOW = "\033[43m";
    public static final String ANSI_HIGHLIGHT_GREEN = "\033[42m";
    public static final String ANSI_HIGHLIGHT_RED = "\033[41m";
    public static final String ANSI_HIGHLIGHT_WHITE = "\033[47m";

    private int row;
    private int col;
    private ArrayList<Cell> neighbors;

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

    public void enter(Agent agent) {
        this.agents.add(agent);
    }

    public void leave(Agent agent) {
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

    public boolean isOccupied() {
        boolean isOccupied;

        // no cops in cell
        if (!this.cops.isEmpty()) {
            return true;
        }

        // no active agents in cell
        for (Agent agent : this.agents) {
            if (!agent.isJailed()) {
                return true;
            }
        }
        
        return false;
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
                        <= Rebellion.VISION) {
                    this.addNeighbor(other);
                    //System.out.println(other);
                }
            }
        }
       // System.out.println(this.neighbors.size());
    }

    public Cell getRandomUnoccupiedNeighbor() {
        ArrayList<Cell> targets = new ArrayList<Cell>();
        for (Cell cell : this.neighbors) {
            if (!cell.isOccupied()) {
                targets.add(cell);
            }
        }
        if (targets.size() <= 0) {
            return null;

        }
        return targets.get(this.rand.nextInt(targets.size()));
    }

    public int countCopsInNeighborhood() {
        int count = 0;
        for (Cell neighbor : this.neighbors) {
            count = count + neighbor.countCops();
        }
        return count;
    }

    public int countActiveAgentsInNeighborhood() {
        int count = 0;
        for (Cell neighbor : this.neighbors) {
            count = count + neighbor.countActiveAgents();
        }
        return count;
    }

    public int countCops() {
        return this.cops.size();
    }

    public int countActiveAgents() {
        int count = 0;
        for (Agent agent : this.agents) {
            if (agent.isActive()) {
                count++;
            }
        }
        return count;
    }

    public ArrayList<Agent> getActiveAgentsInNeighborhood() {
        ArrayList<Agent> agents = new ArrayList<Agent>();
        for (Cell neighbor : this.neighbors) {
            agents.addAll(neighbor.getActiveAgents());
        }
        return agents;
    }

    public ArrayList<Agent> getActiveAgents() {
        ArrayList<Agent> active = new ArrayList<Agent>();
        for (Agent agent : this.agents) {
            if (agent.isActive()) {
                active.add(agent);
            }
        }
        return active;
    }

    private void addNeighbor(Cell neighbor) {
        this.neighbors.add(neighbor);
    }

    private double distanceTo(Cell other, int rows, int cols) {
        int otherRow = other.getRow();
        int otherCol = other.getCol();

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
        String agentList = "";

        if (agents.size() != 0) {
            for (Agent agent : agents) {
                if (agent.getState(agent) == Agent.State.JAILED){
                    agentList += ANSI_HIGHLIGHT_RED + " " + ANSI_RESET ;
                } else if (agent.getState(agent) == Agent.State.ACTIVE) {
                    agentList += ANSI_HIGHLIGHT_GREEN + " " + ANSI_RESET ;
                } else {
                    agentList += ANSI_HIGHLIGHT_WHITE + " " + ANSI_RESET ;
                }
            }
        } else {
            agentList += ANSI_BLACK + " " + ANSI_RESET ;
        }

        return ((cops.size() != 0? ANSI_BLUE + "\uD83D\uDC6E" + ANSI_RESET:
                "  ") +
                agentList);
    }
}
