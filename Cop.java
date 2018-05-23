/* Implementation of a Cop.
 *
 * Wanders around the simulation grid; arrests active Agents.
 * 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Cop {

    // location in simulation grid
    private Cell location;

    public Cop(Cell location) {
        this.location = location;
        this.location.enter(this);
    }

    // Execute the "move" action for the cop: moves to a random unoccupied
    //  unoccupied cell in the vicinity; stays at current location if all
    //  nearby cells are occupied.
    public void move() {
        Cell target = this.location.getRandomUnoccupiedNeighbor();
        if (target != null) {
            this.moveTo(target);
        }
    }

    // Move from current location to the given target cell.
    private void moveTo(Cell target) {
        this.location.leave(this);
        target.enter(this);
        this.location = target;
    }

    // Execute the "enforce" action for the cop: searches for all active
    //   agents in the neighborhood, randomly selects one, and arrests
    //   them. Does nothing if there are no active agents within signt.
    public void enforce() {
        // fetch the list of all active agents in neighborhood
        ArrayList<Agent> active = 
            this.location.getActiveAgentsInNeighborhood();

        // if there is at least one active agent within sight ...
        if (active.size() > 0) {
            // ... choose one of them at random
            Collections.shuffle(active);
            Agent agent = active.get(0);

            // move to the active agent's location
            Cell target = agent.getLocation();
            this.moveTo(target);

            // and arrest them
            agent.arrest(Rebellion.generateJailTerm());
        }
    }

    // Getter: returns cop location
    public Cell getLocation(){
        return this.location;
    }
}
