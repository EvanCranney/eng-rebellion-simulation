/* Main class for the Rebellion simulation.
 *
 * Contains all the input parameters for the simulation.
 *
 * Responsible for:
 *      (1) Instantiating all objects in the simulation
 *      (2) Calling the run-loop which executes the simulation
 *      (3) Collecting metrics on the simulation, writing out to csv
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Rebellion {

    // dimensions of the simulation grid
    public static final int WORLD_WIDTH = 40;
    public static final int WORLD_HEIGHT = 40;

    // simulation parameters
    public static final double GOVERNMENT_LEGITIMACY = 0.03;
    public static final boolean MOVEMENT_ON = true;
    public static final double AGENT_DENSITY = 0.70;
    public static final double VISION = 7.0;
    public static final int MAX_JAIL_TERM = 30;
    public static final double COP_DENSITY = 0.04;

    // number of steps to run the simulation for
    public static final int STEPS = 200;

    // writeout file for simulation metrics
    public static final String OUTFILE = "metrics.csv";

    public static Random rand = new Random();
    
    /* Parameters for the model extension.
     *
     */
    private enum SimulationType {
        ORIGINAL, ITERATIVE, SINGLE
    }
    // ###### CHANGE THIS TO SWITCH BETWEEN EXTENSIONS AND ORIGIAL ######
    public static final SimulationType simulation =
        SimulationType.SINGLE;

    // size of iterative decrement in govt legitimacy for extension 1
    public static final double EXT1_LEGITIMACY_DECREMENT = 0.5;

    // size of single decrement in govt legitimacy for extension 2,
    //  and time-step in which decrement should occur
    public static final double EXT2_LEGITIMACY_DECREMENT = 30;
    public static final int EXT2_DECREMENT_TIMESTEP = 100;


    /* Main function
     *  (1) instantiates objects for simulation
     *  (2) executes driver loop for simulation
     */
    public static void main(String[] args) throws FileNotFoundException {

        // instantiate grid
        Grid grid = new Grid(WORLD_WIDTH, WORLD_HEIGHT, VISION);

        // instantiate agents
        int numAgents = computeNumAgents();
        ArrayList<Agent> agents = instantiateAgents(numAgents, grid);

        // instantiate cops
        int numCops = computeNumCops();
        ArrayList<Cop> cops = instantiateCops(numCops, grid);

        // set up stream to write output
        PrintWriter pw = new PrintWriter(new File(OUTFILE));
        StringBuilder sb = new StringBuilder();
        sb = createCSVFileHeader(sb);

        // the driver loop
        for (int t=0; t<STEPS; t++) {

            // government legitimacy, variable used in extended model
            double legitimacy = GOVERNMENT_LEGITIMACY;

            // variables to record agent types in each timestep
            int jailedAgent = 0;
            int activeAgent = 0;
            int quietAgent = 0;

            // randomize the order of agents and cops
            Collections.shuffle(agents);
            Collections.shuffle(cops);

            // (1) move agents if MOVEMENT=ON
            if (Rebellion.MOVEMENT_ON) {
                for (Agent agent : agents) {
                    agent.move();
                }
            }

            // (2) move all cops
            for (Cop cop : cops) {
                cop.move();
            }

            // (3) check whether each agent should rebel
            for (Agent agent : agents) {
                agent.rebel(legitimacy);

                // increment the relevant agent-type counter
                if (agent.isActive()) {
                    activeAgent++;
                } else if (agent.isJailed()) {
                    jailedAgent++;
                } else {
                    quietAgent++;
                }
            }

            // (4) execute enforce action for each cop
            for (Cop cop : cops) {
                cop.enforce();
            }

            // add formatting components to output stream
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

            // model
            if (Rebellion.simulation == SimulationType.ITERATIVE) {
                if (legitimacy - EXT1_LEGITIMACY_DECREMENT > 0) {
                    legitimacy -= EXT1_LEGITIMACY_DECREMENT;
                }
            } else if (Rebellion.simulation == SimulationType.SINGLE) {
                if (t == EXT2_DECREMENT_TIMESTEP - 1) {
                    legitimacy -= EXT2_LEGITIMACY_DECREMENT;
                }
            }
        }

        // write metrics to output file
        pw.write(sb.toString());
        pw.close();
    }
    // computes the number of agents to generate for simulation,
    //   calculated percentage of the total number of cells in grid
    public static int computeNumAgents() {
        return (int) (WORLD_HEIGHT * WORLD_WIDTH * AGENT_DENSITY);
    }

    // computes the number of cops to generate for the simulation,
    //   calculated as percentage of total number of cells in grid
    public static int computeNumCops() {
        return (int) (WORLD_HEIGHT * WORLD_WIDTH * COP_DENSITY);
    }

    // instantiate all agents for the simulation
    public static ArrayList<Agent> instantiateAgents(int n, Grid grid) {

        // fetch all cells in the grid (since we need to instantiate
        //   agents in a particular cell)
        ArrayList<Cell> cells;
        cells = grid.asArray();

        // filter out any occupied cells (note, there shouldn't be any
        //   occupied cells at this point in the current implementation)
        Iterator<Cell> iter = cells.iterator();
        while (iter.hasNext()) {
            Cell cell = iter.next();
            if (cell.isOccupied())
                iter.remove();
        }

        // shuffle the remaining cells, so that agents inserted randomly
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

    // instantiate all cops for the simulation
    public static ArrayList<Cop> instantiateCops(int n, Grid grid) {

        // fetch all cells in the grid
        ArrayList<Cell> cells;
        cells = grid.asArray();

        // filter out occupied cells
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

    // generate a random jail term
    //   used by the Cop class to figure out how long a jail-term to
    //   assign an arrested Agent
    // Note: the function is left in this main class so that we can
    //   - if necessary - adjust the jail term assigned by Cops. It is
    //   cohesive to treat it like a parameter, and hence belongs here.
    public static int generateJailTerm() {
        return rand.nextInt(Rebellion.MAX_JAIL_TERM);
    }

    // generates random risk aversion in the range [0,1], used to
    //   instantiate agents
    public static double generateRandomAversion() {
        return rand.nextDouble();
    }

    // generates random hardship in the range [0,1], used to
    //   instantiate agents
    public static double generateRandomHardship() {
        return rand.nextDouble();
    }

    // create the header for the output csv file, detailing run metadata
    private static StringBuilder createCSVFileHeader(StringBuilder sb) {
        Date date = new Date();
        sb.append("Java Model MCSS (Evan & Shalitha)");
        sb.append('\n');
        sb.append("Rebellion");
        sb.append('\n');
        sb.append(OUTFILE);
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
}
