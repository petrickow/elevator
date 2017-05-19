package no.taco.elevator.Hotel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static no.taco.elevator.Hotel.Elevator.ElevatorDirection.DOWN;
import static no.taco.elevator.Hotel.Elevator.ElevatorDirection.STATIONARY;
import static no.taco.elevator.Hotel.Elevator.ElevatorDirection.UP;

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

    /***
     * This method updates the state of elevators.
     */
    public void update() {
        for (Elevator elvis : elevators) {
            elvis.move();

            try {
                if (elvis.currentTarget() == elvis.currentFloor) {

                } else {

                }

            } catch (NoSuchElementException e) { // no
                elvis.direction = STATIONARY;
            }
        }
        //TODO: ...
    }

    public void requestElevator(int to, int from) {

        Elevator.ElevatorDirection requestDirection = from > to ? DOWN : UP;

        LinkedList<Request> floorRequests = new LinkedList<>();
        for (Request r : requests) { // TODO, move this responsibility to each floor
            if (r.from == from &&
                    requestDirection == (r.direction)) {
                return; // we already have a request in that direction
            }
        }
        requests.add(new Request(to, from));
        // * check direction
        // * check if already called
        // * if not: make request
    }


    /**
     * Reads all requests in the queue and assigns them to elevators.
     *
     * Not optimized in any way!
     */
    public void assignRequests() {
        for (Request r : requests) {
            NaiveAssignment(r);
            // Read this (https://www.amazon.com/Elevator-Traffic-Handbook-Theory-Practice/dp/1138852325) and improve implementation
        }
    }


    //TODO: check if we already have an elevator on the same floor
    // TODO: refactor: sort elevators -> stationary, upward, downward. Sort by proximity to request <-- less duplication of code
    private void NaiveAssignment(Request r) {
        if (r.direction == UP) {
            //direction UP --> get all elevators below (primarily the ones on their way up)
            List<Elevator> upwardElevators = new ArrayList<>();
            for (Elevator elevator : elevators) { // TODO: rewrite as stream.filter?
                if (elevator.direction == UP && elevator.currentFloor < r.from) {
                    upwardElevators.add(elevator);
                }
            }
            //select one -- current implementation naive and unfinished
            Elevator selectedElevator = elevators.get(0);

            if (upwardElevators.size() > 0) {
                //Elevator sortedUpwardElevators = upwardElevators.sort(x -> x.currentFloor); // TODO, find the closest
                selectedElevator = upwardElevators.get(0);
            }

            selectedElevator.addToRoute(r.from);
        }

        else if (r.direction == DOWN) {
            //direction DOWN --> get all elevators above (primarily the ones on their way down)
            List<Elevator> downwardElevators = new ArrayList<>();
            for (Elevator elevator : elevators) {
                if (elevator.direction == DOWN && elevator.currentFloor > r.from) {
                    downwardElevators.add(elevator);
                }
            }
            //select one
            Elevator selectedElevator = elevators.get(0);

            if (downwardElevators.size() > 0) {
                //Elevator sortedDownwardElevators = downwardElevators.sort(x -> x.currentFloor); // TODO, find the closest
                selectedElevator = downwardElevators.get(0);
            }

            selectedElevator.addToRoute(r.from);
        }
        else {
            // TODO error handling
        }
    }

    private class Request {
        public int to, from;
        Elevator.ElevatorDirection direction;

        public Request(int to, int from) {
            this.to = to;
            this.from = from;
            direction = from > to ? DOWN : UP;
        }
    }
}
