package no.taco.elevator.Hotel;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class ElevatorManager {

    List<Elevator> elevators;
    List<Request> requests;

    public ElevatorManager(int numElevators) {
        elevators = new ArrayList<>(numElevators);
        requests = new ArrayList<>();

        for (int i = 0; i < numElevators ; i++) {
            elevators.add(new Elevator(i));
        }
    }

    public void requestElevator(int to, int from) {
        //TODO: check direction
        // check if already called
        // if not: make request
    }

    public void assignRequests() {
        for (Request r : requests) {
            if (r.from < r.to) {
                //direction UP --> get all elevators below (primarily the ones on their way up)
                List<Elevator> upwardElevators = new ArrayList<>();
                for (Elevator x : elevators) { // TODO: rewrite as stream.filter?
                    if (x.direction == Elevator.ElevatorDirection.UP && x.currentFloor < r.from) {
                        upwardElevators.add(x);
                    }
                }
                //select one -- current implementation naive and unfinished
                Elevator selectedElevator = elevators.get(0);

                if (upwardElevators.size() > 0) {
                    //Elevator closest = upwardElevators.sort(x -> x.currentFloor ); // TODO, find the closest
                    selectedElevator = upwardElevators.get(0);
                }
                selectedElevator.addToRoute(r.to);
            }

            else if (r.from > r.to) {
                //direction DOWN --> get all elevators above (primarily the ones on their way down)
                List<Elevator> downwardElevators = new ArrayList<>();
                for (Elevator x : elevators) { // TODO: rewrite as stream.filter?
                    if (x.direction == Elevator.ElevatorDirection.DOWN && x.currentFloor > r.from) {
                        downwardElevators.add(x);
                    }
                }
                //select one
                Elevator selectedElevator = elevators.get(0);

                if (downwardElevators.size() > 0) {
                    //Elevator closest = upwardElevators.sort(x -> x.currentFloor ); // TODO, find the closest
                    selectedElevator = downwardElevators.get(0);
                }
                selectedElevator.addToRoute(r.to);
            }
            else {
                // TODO error handling
            }
        }
    }

    private class Request {
        public int to, from;

        public Request(int to, int from) {
            this.to = to;
            this.from = from;
        }
    }
}
