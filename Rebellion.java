/**
 *
 */

import java.util.ArrayList;
import java.util.Random;

public class Rebellion {

    // simulation parameters
    public static final int GRID_HEIGHT = 10;
    public static final int GRID_WIDTH = 10;
    public static final double RANGE = 3.0;

    // 
    public static final double AGENT_DENSITY = 0.01;
    public static final double COP_DENSITY = 0.01;

    public static final double NUM_COPS = 2.0;
    public static final double NUM_AGENTS = 3.0;

    //
    public static final boolean MOVEMENT_ON = false;
    public static Grid grid = new Grid(Rebellion.GRID_WIDTH, Rebellion
            .GRID_HEIGHT, Rebellion.RANGE);
    ;

    public static void main(String[] args) {

        // create the grid-world
//        grid = new Grid(Rebellion.GRID_WIDTH, Rebellion.GRID_HEIGHT,
//            Rebellion.RANGE);

        // create agents

//        for (int row = 0; row < GRID_HEIGHT; row++) {
//            for (int col = 0; col < GRID_WIDTH; col++) {
//                grid.getGrid()[row][col].setOccupied(false);
//            }
//        }
        printGrid();

        ArrayList<Agent> agents = instantiateAgents(grid);
        printGrid();
        // create cops
        ArrayList<Cop> cops = instantiateCops(grid);
        printGrid();
        //printAvailableSlotsInGrid();
        // run the simulation

        while (true) {
            tick(grid, agents, cops);
        }
    }

    public static void printGrid() {
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                System.out.print("[" + grid.getGrid()[row][col] + ", " +
                        grid.getGrid()[row][col].isOccupied() + "]");
            }
            System.out.println("");
        }
    }

    public static ArrayList<Agent> instantiateAgents(Grid grid) {
        // get the set of unoccupied patches
        ArrayList<Agent> agents = new ArrayList<Agent>();
        ArrayList<Patch> unoccupied = new ArrayList<Patch>();
        System.out.println("AGENTS");
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                if (!grid.getGrid()[row][col].isOccupied()) {
                    unoccupied.add(grid.getGrid()[row][col]);
                    System.out.print("[" + grid.getGrid()[row][col] + "]");
                }
            }
            System.out.println();
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
            System.out.println(location.toString());
            // then remove the patch from the listRandom rand = new Random();
            unoccupied.remove(location);
        }

        // TODO this one need a better solution
        //System.out.println(unoccupied);
        for (int i = 0; i < unoccupied.size(); i++) {
            for (int row = 0; row < GRID_HEIGHT; row++) {
                for (int col = 0; col < GRID_WIDTH; col++) {
                    if (grid.getGrid()[row][col].equals(unoccupied.get(i))) {
                        grid.getGrid()[row][col].setOccupied(false);
                        continue;
                        //System.out.print("[" +grid.getGrid()[row][col] + "]");
                    } else {
                        grid.getGrid()[row][col].setOccupied(true);
                    }
                }

            }
            //System.out.println();
        }
//        for (Agent agent: agents) {
//            System.out.println(agent.getLocation().toString());
//        }


        return agents;
    }

    public static ArrayList<Cop> instantiateCops(Grid grid) {
        ArrayList<Cop> cops = new ArrayList<Cop>();
        ArrayList<Patch> unoccupied = new ArrayList<Patch>();
        System.out.println("COPS");
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                if (!grid.getGrid()[row][col].isOccupied()) {
                    unoccupied.add(grid.getGrid()[row][col]);
                    //System.out.print("[" +grid.getGrid()[row][col] + "]");
                }
            }
            //System.out.println();
        }

        Patch location;
        Random rand = new Random();
        for (int i = 0; i < NUM_COPS; i++) {
            location = unoccupied.get(rand.nextInt(unoccupied.size()));
            System.out.println(location);
            // put the cop in it
            Cop cop = new Cop(grid, location);
            cops.add(cop);

            // then remove the patch from the list
            unoccupied.remove(location);
        }

        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                for (int i = 0; i < unoccupied.size(); i++) {
                    if (unoccupied.get(i) == (grid.getGrid()[row][col])) {
                        grid.getGrid()[row][col].setOccupied(true);
                        //System.out.print("[" +grid.getGrid()[row][col] + "]");
                    }

                }

            }
            //System.out.println();
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
