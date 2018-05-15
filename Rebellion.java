/**
 *
 */

import java.util.ArrayList;
import java.util.Random;

public class Rebellion {

    // simulation parameters
    public static final int GRID_HEIGHT = 80;
    public static final int GRID_WIDTH = 80;
    public static final double RANGE = 7.0;

    // 
    public static final double AGENT_DENSITY = 0.80;
    public static final double COP_DENSITY = 0.04;

    public static final double NUM_COPS = Rebellion.AGENT_DENSITY
        * (Rebellion.GRID_HEIGHT * Rebellion.GRID_WIDTH);
    public static final double NUM_AGENTS = Rebellion.COP_DENSITY
        * (Rebellion.GRID_HEIGHT * Rebellion.GRID_WIDTH);

    //
    public static final boolean MOVEMENT_ON = false;

    public static void main(String[] args) {

        // create the grid-world
        Grid grid = new Grid(Rebellion.GRID_WIDTH, Rebellion.GRID_HEIGHT,
            Rebellion.RANGE);

        // create agents
        ArrayList<Agent> agents = instantiateAgents(grid);

        // create cops
        ArrayList<Cop> cops = instantiateCops(grid);

        // run the simulation
        while (true) {
            tick(grid, agents, cops);
        }
    }

    public static ArrayList<Agent> instantiateAgents(Grid grid) {
        // get the set of unoccupied patches
        ArrayList<Agent> agents = new ArrayList<Agent>();
        ArrayList<Patch> unoccupied = new ArrayList<Patch>();
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                if (!grid.getGrid()[row][col].isOccupied()) {
                    unoccupied.add(grid.getGrid()[row][col]);
                }
            }
        }

        // put agents into them
        Patch location;
        Random rand = new Random();
        for (int i = 0; i < NUM_AGENTS; i++) {
            // select a random patch
            location = unoccupied.get(rand.nextInt(unoccupied.size()));

            // put the agent in it
            Agent agent = new Agent(rand.nextDouble(), rand.nextDouble(),
                grid, location);
            agents.add(agent);

            // then remove the patch from the list
            unoccupied.remove(location);
        }
        return agents;
    }

    public static ArrayList<Cop> instantiateCops(Grid grid) {
        ArrayList<Cop> cops = new ArrayList<Cop>();
        ArrayList<Patch> unoccupied = new ArrayList<Patch>();
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                if (!grid.getGrid()[row][col].isOccupied()) {
                    unoccupied.add(grid.getGrid()[row][col]);
                }
            }
        }

        Patch location;
        Random rand = new Random();
        for (int i = 0; i < NUM_COPS; i++) {
            location = unoccupied.get(rand.nextInt(unoccupied.size()));

            // put the agent in it
            Cop cop = new Cop(grid, location);
            cops.add(cop);

            // then remove the patch from the list
            unoccupied.remove(location);
        }
        return cops;
    }

    public static void tick(Grid grid, ArrayList<Agent> agents,
            ArrayList<Cop> cops) {

        // move all agents/cops
        if (MOVEMENT_ON) {
            for (Agent agent : agents) {
                agent.move();
            }
        }
        for (Cop cop : cops) {
            cop.move();
        }

        // determine if agents should be active/inactive
        for (Agent agent : agents) {
            agent.rebel();
        }

        // make arrests for cops
        for (Cop cop : cops) {
            cop.enforce();
        }

        // agent jail redux
        for (Agent agent : agents) {
            agent.reduceTerm();
        }
    }
}
