/*
 *
 */

public abstract class Person {

    private Cell location;

    public Person(Cell location) {
        this.location = location;
        this.location.enter(this);
    }

    public void move() {
        Cell target = this.location.getRandomUnoccupiedNeighbor();
        if (target != null) {
            this.moveTo(target);
        }
    }

    private void moveTo(Cell target) {
        this.location.leave(this);
        target.enter(this);
        this.location = target;
    }

    public Cell getLocation() {
        return location;
    }
}
