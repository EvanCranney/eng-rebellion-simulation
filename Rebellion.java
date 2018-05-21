/* Main class for the Rebellion simulation.
 *
 */

import java.util.*;

public class Rebellion {

    public static final int WORLD_WIDTH = 10;
    public static final int WORLD_HEIGHT = 10;
    public static final double VISION = 2.0;
    public static final double AGENT_DENSITY = 0.8;
    public static final double COP_DENSITY = 0.04;
    public static final boolean MOVEMENT_ON = false;

    public static final double GOVERNMENT_LEGITIMACY = 0.8;
    public static final int MAX_JAIL_TERM = 30;

    public static Random rand = new Random();

    public static void main(String[] args) {

        // instantiate grid
        Grid grid = new Grid(WORLD_WIDTH, WORLD_HEIGHT);

        // instantiate agents
        int numAgents = computeNumAgents();
        ArrayList<Agent> agents = instantiateAgents(numAgents, grid);

        // instantiate cops
        int numCops = computeNumCops();
        ArrayList<Cop> cops = instantiateCops(numCops, grid);
        printCurrentGrid(grid);

        // driver loop
        for (int i=0; i<100; i++) {
        
            // randomize the order of agents and cops
            Collections.shuffle(agents);
            Collections.shuffle(cops);

            if (Rebellion.MOVEMENT_ON) {
                for (Agent agent : agents) {
                    agent.move();
                }
            }

            for (Cop cop : cops) {
                cop.move();
            }

            for (Agent agent : agents) {
                agent.rebel();
            }

            for (Cop cop : cops) {
                cop.enforce();
            }
            System.out.println();
            printCurrentGrid(grid);
        }
        System.out.println("\nend...\n");
        printCurrentGrid(grid);
    }

    public static void printCurrentGrid(Grid grid){
        int rows = grid.getNumRows();
        int cols = grid.getNumCols();
        Cell cell;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cell = grid.getCellAt(r, c);
                System.out.print("[" + cell.toString() + "]");

            }
            System.out.println();
        }
    }

    public static int computeNumAgents() {
        return (int) (WORLD_HEIGHT * WORLD_WIDTH * AGENT_DENSITY);
    }

    public static int computeNumCops() {
        return (int) (WORLD_HEIGHT * WORLD_WIDTH * COP_DENSITY);
    }

    public static double generateRandomAversion() {
        return rand.nextDouble();
    }

    public static double generateRandomHardship() {
        return rand.nextDouble();
    }

    public static ArrayList<Agent> instantiateAgents(int n, Grid grid) {
        // get cells in the grid
        ArrayList<Cell> cells;
        cells = grid.asArray();

        // filter out the ones that are occupied (should be none)
        Iterator<Cell> iter = cells.iterator();
        while (iter.hasNext()) {
            Cell cell = iter.next();

            if (cell.isOccupied())
                iter.remove();
        }

        // shuffle the remaining cells
        Collections.shuffle(cells);

        // one-by-one instatiate agents
        ArrayList<Agent> agents = new ArrayList<Agent>();
        for (int i = 0; i < n; i++) {
            agents.add(new Agent(
                generateRandomAversion(),
                generateRandomHardship(),
                cells.get(i)
            ));
        }
        return agents;
    }

    public static ArrayList<Cop> instantiateCops(int n, Grid grid) {
        // get cells in the grid
        ArrayList<Cell> cells;
        cells = grid.asArray();

        // filter out the ones that are occupied (should be none)
        Iterator<Cell> iter = cells.iterator();
        while (iter.hasNext()) {
            Cell cell = iter.next();

            if (cell.isOccupied())
                iter.remove();
        }

        // shuffle the remaining cells
        Collections.shuffle(cells);

        // one-by-one instantiate cops
        ArrayList<Cop> cops = new ArrayList<Cop>();
        for (int i = 0; i < n; i++) {
            cops.add(new Cop(cells.get(i)));
        }
        return cops;
    }
}
