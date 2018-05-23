/* Main class for the Rebellion simulation.
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Rebellion {

    public static final int WORLD_WIDTH = 40;
    public static final int WORLD_HEIGHT = 40;

    public static double GOVERNMENT_LEGITIMACY = 0.03;
    public static final boolean MOVEMENT_ON = true;
    public static final double AGENT_DENSITY = 0.70;
    public static final double VISION = 7.0;
    public static final int MAX_JAIL_TERM = 30;
    public static final double COP_DENSITY = 0.04;

    public static final String FILENAME = "Rebellion experiment 0 (Java)";
    public static final int STEPS = 500;

    //EXTENDED FEATURES
    public static final boolean EXTENDED_FEATURE_ON = false;
    public static final double MAX_GOVERNMENT_LEGITIMACY = 0.96;
    public static final double INCREMENTAL_VALUE_GOVERNMENT_LEGITIMACY = 0.002;

    public static Random rand = new Random();

    public static void main(String[] args) throws FileNotFoundException {

        PrintWriter pw = new PrintWriter(new File(FILENAME+".csv"));
        StringBuilder sb = new StringBuilder();

        sb = createCSVFileHeader(sb);

        // instantiate grid
        Grid grid = new Grid(WORLD_WIDTH, WORLD_HEIGHT);

        // instantiate agents
        int numAgents = computeNumAgents();
        ArrayList<Agent> agents = instantiateAgents(numAgents, grid);

        // instantiate cops
        int numCops = computeNumCops();
        ArrayList<Cop> cops = instantiateCops(numCops, grid);
        //printCurrentGrid(grid);

        // driver loop
        for (int i=0; i<STEPS; i++) {
            int jailedAgent = 0;
            int activeAgent = 0;
            int quietAgent = 0;

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
                agent.rebel(GOVERNMENT_LEGITIMACY);
                if (agent.getState(agent) == Agent.State.JAILED){
                    jailedAgent = jailedAgent + 1;
                } else if (agent.getState(agent) == Agent.State.ACTIVE) {
                    activeAgent = activeAgent + 1;
                } else {
                    quietAgent = quietAgent + 1;
                }
            }

            for (Cop cop : cops) {
                cop.enforce();
            }

            sb.append("");
            sb.append(',');
            sb.append(jailedAgent);
            sb.append(',');
            sb.append(activeAgent);
            sb.append(',');
            sb.append(quietAgent);
            sb.append(',');
            sb.append(numCops);
            sb.append('\n');

            //System.out.println();
            //printCurrentGrid(grid);
            if (EXTENDED_FEATURE_ON){
                if (GOVERNMENT_LEGITIMACY < MAX_GOVERNMENT_LEGITIMACY) {
                    GOVERNMENT_LEGITIMACY = GOVERNMENT_LEGITIMACY +
                            INCREMENTAL_VALUE_GOVERNMENT_LEGITIMACY;
                }
            }
        }
        pw.write(sb.toString());
        pw.close();
    }

    private static StringBuilder createCSVFileHeader(StringBuilder sb) {
        Date date = new Date();
        sb.append("Java Model MCSS (Evan & Shalitha)");
        sb.append('\n');
        sb.append("Rebellion");
        sb.append('\n');
        sb.append(FILENAME);
        sb.append('\n');
        sb.append(date.toString());
        sb.append('\n');
        sb.append("min-pxcor,max-pxcor,min-pycor,max-pycor");
        sb.append('\n');
        sb.append("0," + (WORLD_WIDTH - 1) + ",0," + (WORLD_HEIGHT - 1));
        sb.append('\n');
        sb.append("[run number],1,1,1");
        sb.append('\n');
        sb.append("government-legitimacy," + GOVERNMENT_LEGITIMACY);
        sb.append('\n');
        sb.append("movement?," + MOVEMENT_ON);
        sb.append('\n');
        sb.append("initial-agent-density," + AGENT_DENSITY*100);
        sb.append('\n');
        sb.append("vision," + VISION);
        sb.append('\n');
        sb.append("max-jail-term," + MAX_JAIL_TERM);
        sb.append('\n');
        sb.append("visualization, N/A");
        sb.append('\n');
        sb.append("initial-cop-density, " + COP_DENSITY*100);
        sb.append('\n');
        sb.append("[reporter]");
        sb.append(',');
        sb.append("JAILED");
        sb.append(',');
        sb.append("ACTIVE");
        sb.append(',');
        sb.append("QUIET");
        sb.append(',');
        sb.append("COPS");
        sb.append('\n');
        sb.append("[final]");
        sb.append(',');
        sb.append('\n');
        sb.append("[min]");
        sb.append(',');
        sb.append('\n');
        sb.append("[max]");
        sb.append(',');
        sb.append('\n');
        sb.append("[mean]");
        sb.append(',');
        sb.append('\n');
        sb.append("[steps]," + STEPS + "," + STEPS + "," + STEPS );
        sb.append(',');
        sb.append('\n');
        sb.append("");
        sb.append(',');
        sb.append('\n');
        sb.append("[all run data]");
        sb.append(',');
        sb.append("JAILED");
        sb.append(',');
        sb.append("ACTIVE");
        sb.append(',');
        sb.append("QUIET");
        sb.append(',');
        sb.append("COPS");
        sb.append('\n');
        return sb;
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
        System.out.println("AGENTS COUNT " + ((int) (WORLD_HEIGHT *
                WORLD_WIDTH *
                AGENT_DENSITY)));
        return (int) (WORLD_HEIGHT * WORLD_WIDTH * AGENT_DENSITY);
    }

    public static int computeNumCops() {
        System.out.println("COPS COUNT " + ((int) (WORLD_HEIGHT * WORLD_WIDTH *
                COP_DENSITY)));
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
