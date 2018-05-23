/* Implementation of a Cop.
 *
 * Wanders around the simulation grid; arrests active Agents.
 * 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Cop {

    private Cell location;      // location simulation grid

    public Cop(Cell location) {
        this.location = location;
        this.location.enter(this);
    }


    // moves to a random cell in the vicinity of current location
    public void move() {
        // get random unoccupied cell in vicinity
        Cell target = this.location.getRandomUnoccupiedNeighbor();
        // change location if possible
        if (target != null) {
            this.moveTo(target);
        }
    }

    private void moveTo(Cell target) {
        this.location.leave(this);
        target.enter(this);
        this.location = target;
    }

    public void enforce() {
        ArrayList<Agent> active = this.location.getActiveAgentsInNeighborhood();

        // if active agent visible, then arrest
        if (active.size() > 0) {
            Collections.shuffle(active);
            Agent agent = active.get(0);
            Cell target = agent.getLocation();
            this.moveTo(target);
            agent.arrest(Rebellion.generateJailTerm());
        }
    }

    public Cell getLocation(){
        return this.location;
    }

}
