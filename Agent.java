/**
 * Agent : a member of the general population, an entity which may rebel at
 * any point in time.
 */

import java.util.ArrayList;
import java.util.Random;

public class Agent implements Occupant {

    public static final AgentState INITIAL_STATE = AgentState.INACTIVE;
    public static final int INITIAL_TERM = 0;

    private double aversion;            // risk aversion
    private double hardship;            // perceived hardship
    private int term;                   // remaining jail term

    private AgentState state;           // current state

    private Grid grid;                  // simulation grid
    private Patch location;             // current location
    private boolean occupied;          //

    public Agent(double aversion, double hardship, Grid grid,
                 Patch location) {
        this.aversion = aversion;
        this.hardship = hardship;
        this.grid = grid;
        this.location = location;
        this.occupied = false;
        state = Agent.INITIAL_STATE;
        term = Agent.INITIAL_TERM;
    }

    // move agents if not jailed
    // determine if agents should be active or quiet
    // make arrests
    // reduce jail time

    // @override
    public boolean isOccupying() {
        return (state != AgentState.JAILED);
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    // moves an agent to a random, unoccupied cell, within neighbourhood,
    // provided that they are not JAILED
    public void move() {
        if (state != AgentState.JAILED) {
            // get list of unoccupied Patches in neighborhood
            ArrayList<Patch> targets = grid.getTargets(location);

            // if the size of the array is random ...
            if (targets.size() > 0) {
                // ... choose one randomly
                Random rand = new Random();
                Patch target = targets.get(rand.nextInt(targets.size()));

                // ... and move to that Patch
                location.remove(this);
                target.add(this);
                location = target;
            }

        }
    }

    // rebel if agreived enough
    public void rebel() {
        if (state == AgentState.JAILED) {
            return;
        }

        if (computeGreivance() >= computeRisk() + 0.1) {
            state = AgentState.ACTIVE;
        } else {
            state = AgentState.INACTIVE;
        }
    }

    private double computeGreivance() {
        return hardship * (1 - 0.8); // GOVERNMENT LEGITIMACY
    }

    private double computeRisk() {
        return aversion * computeDanger();
    }

    private double computeDanger() {
        int numCops = 0;
        int numActiveAgents = 1; // default start 1
        ArrayList<Occupant> neighbors = grid.getNeighbors(location);
        for (Occupant neighbor : neighbors) {
            if (neighbor instanceof Cop) {
                numCops++;
            } else if (((Agent) neighbor).isActive()) {
                numActiveAgents++;
            }
        }
        return (1 - Math.exp(-2.3 * Math.floor(numCops / numActiveAgents)));
    }

    // reduces the number of turns left before able to leave jail 
    public void reduceTerm() {
        // if not jailed, then skip
        if (state != AgentState.JAILED) {
            return;
        }

        // reduce jail term
        term--;
        if (term == 0) {
            state = AgentState.INACTIVE;
        }
    }

    public boolean isActive() {
        return (state == AgentState.ACTIVE);
    }

    // arrest
    public void arrest(int termerm) {
        this.term = termerm;
        state = AgentState.JAILED;
    }

    public Patch getLocation() {
        return this.location;
    }
}

