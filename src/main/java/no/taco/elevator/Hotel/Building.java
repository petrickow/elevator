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

    /**
     * Check all visitors on all floors and update state. Either put in queue or update stay counter
     */
    public void inspectVisitors() {

        for (Floor floor : floors) {
            for(Visitor v : floor.visitors) {
                if (floor.level == 0 || v.stayFor == 0) { // quick hack. new arrival or old timer ready to leave, TODO: add flag for
                    floor.queueForTransfer(v);
                }
                else {
                    v.stayFor--;
                }
            }
        }
    }


    public void assignRequests() {

    }
}
