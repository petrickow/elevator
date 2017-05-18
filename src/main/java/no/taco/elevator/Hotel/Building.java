package no.taco.elevator.Hotel;

import no.taco.elevator.Agent.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by catoda on 18.05.17.
 */
public class Building {

    public List<Floor> floors;
    public List<Visitor> currentVisitors;
    public ElevatorManager elevatorManager;

    public Building(int numFloors, int numElevators) {
        floors = new ArrayList<Floor>(numFloors);
        for (int i = 0; i < numFloors ; i++) {
            floors.add(new Floor(i));
        }
        elevatorManager = new ElevatorManager(numElevators);
        currentVisitors = new ArrayList<Visitor>();
        //floors.stream().forEach(floor -> System.out.println("Floor " + floor.level + " set up"));
    }

    public void moveElevators() {

        for (Elevator elevator : elevatorManager.elevators) {
            elevator.move();
        }
    }

    public void inspectVisitors() {
        for (Visitor visitor : currentVisitors) {
            //if (visitor.intendedFloor > visitor.currentFloor) {

            //}


        }
    }
}
