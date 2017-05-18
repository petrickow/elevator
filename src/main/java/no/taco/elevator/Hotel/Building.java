package no.taco.elevator.Hotel;

import no.taco.elevator.Agent.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by catoda on 18.05.17.
 */
public class Building {

    List<Floor> floors;
    List<Visitor> currentVisitors;
    ElevatorManager elevatorManager;

    public Building(int numFloors, int numElevators) {
        floors = new ArrayList<Floor>(numFloors);
        for (int i = 0; i < numFloors ; i++) {
            floors.add(new Floor(i));
        }

        elevatorManager = new ElevatorManager(numElevators);

        floors.stream().forEach(floor -> System.out.println("Hello " + floor.level));
    }
}
