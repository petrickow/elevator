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
        floors = new ArrayList<>(numFloors);
        for (int i = 0; i < numFloors ; i++) {
            floors.add(new Floor(i));
        }
        elevatorManager = new ElevatorManager(numElevators);
        currentVisitors = new ArrayList<>();
        //floors.stream().forEach(floor -> System.out.println("Floor " + floor.level + " set up"));
    }


    public void moveElevators() {
        for (Elevator elevator : elevatorManager.elevators) {
            elevator.move();
        }
    }

    /**
     * Check all visitors on all floors and update state. Either put in queue or update stay counter
     */
    public void inspectVisitors() {

        for (Floor floor : floors) {
            System.out.printf("Visitors on floor %d: %d\n", floor.level, floor.visitors.size());

            for(Visitor v : floor.visitors) { // TODO: hairy logic, rewrite, separate lobby-logic
                if (floor.level == 0 && v.stayFor != 0) { // new arrival
                    floor.queueForTransfer(v);
                    elevatorManager.requestElevator(v.currentFloor, v.intendedFloor);
                }
                else if (floor.level != 0 && v.stayFor == 0) { // old timer, ready to leave
                    floor.queueForTransfer(v);
                    elevatorManager.requestElevator(v.currentFloor, v.intendedFloor);
                } else if (floor.level == 0 && v.stayFor == 0) { // old timer has reached lobby --> leave
                    currentVisitors.remove(v);
                }
                else { // otherwise, we are on a > 0 floor, but want to stay for a bit longer...
                    v.stayFor--;
                }
            }
        }
    }
}
