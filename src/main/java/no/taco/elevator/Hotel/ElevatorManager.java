package no.taco.elevator.Hotel;

import java.util.ArrayList;
import java.util.List;

public class ElevatorManager {

    List<Elevator> elevators;

    public ElevatorManager(int numElevators) {
        elevators = new ArrayList<Elevator>(numElevators);
        for (int i = 0; i < numElevators ; i++) {
            elevators.add(new Elevator(i));
        }
    }

    public void requestDown(int level) {

    }

    public void requestUp(int level) {

    }

}
