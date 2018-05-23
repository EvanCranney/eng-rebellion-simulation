/* Abstract class, Person. The parent for Agents and Cops.
 *
 * Exists to house functionality common to both Agents and Cops that 
 * relate to movement around the grid.
 *
 */

public abstract class Person {

    // the Person's current location
    private Cell location;

    public Person(Cell location) {
        this.location = location;
        this.location.enter(this);
    }

    // move to a random unoccupied cell
    public void move() {
        Cell target = this.location.getRandomUnoccupiedNeighbor();
        if (target != null) {
            this.moveTo(target);
        }
    }

    // helper function, move to a target cell, leave the current one
    public void moveTo(Cell target) {
        this.location.leave(this);
        target.enter(this);
        this.location = target;
    }

    // get the current location of the Person
    public Cell getLocation() {
        return location;
    }
}
