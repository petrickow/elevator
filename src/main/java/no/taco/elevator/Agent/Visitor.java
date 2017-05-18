package no.taco.elevator.Agent;

/**
 * An agent looking to get to **random** floor for **random** amount of time
 * Will queue for elevator when spawned
 */
public class Visitor {

    public int intendedFloor;
    public int stayFor;
    public int currentFloor;

    public Visitor() {

    }

    public Visitor(int intendedFloor, int stayFor) {
        this.intendedFloor = intendedFloor;
        this.stayFor = stayFor;
        this.currentFloor = 0; // Everyone enters through the ground floor, batman is not allowed
    }
}
