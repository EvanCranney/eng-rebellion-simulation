/* Implementation of a Cop.
 *
 * Wanders around the simulation grid; arrests active Agents.
 * 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Cop extends Person {

    public Cop(Cell location) {
        super(location);
    }

    // Execute the "enforce" action for the cop: searches for all active
    //   agents in the neighborhood, randomly selects one, and arrests
    //   them. Does nothing if there are no active agents within signt.
    public void enforce() {
        // fetch the list of all active agents in neighborhood
        ArrayList<Agent> active = 
            super.getLocation().getActiveAgentsInNeighborhood();

        // if there is at least one active agent within sight ...
        if (active.size() > 0) {
            // ... choose one of them at random
            Collections.shuffle(active);
            Agent agent = active.get(0);

            // move to the active agent's location
            Cell target = agent.getLocation();
            super.moveTo(target);

            // and arrest them
            agent.arrest(Rebellion.generateJailTerm());
        }
    }
}
