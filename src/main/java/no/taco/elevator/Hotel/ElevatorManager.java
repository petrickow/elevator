package no.taco.elevator.Hotel;

import no.taco.elevator.Agent.Visitor;

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
    public void update(List<Floor> floors) {
        for (Elevator elevator : elevators) {
            elevator.move();

            try {
                if (elevator.direction != STATIONARY && elevator.currentTarget() == elevator.currentFloor) { // currentTarget throws NoSuchElementException
                    int storedDirectionForRequest = elevator.direction == UP ? 1 : -1;
                    Floor currentFloor = floors.get(elevator.currentFloor);

                    unloadPassengers(elevator, currentFloor); // <-- passengers in elevator moved into floor

                    //      load passengers from correct queue
                    // based on route, update direction
                    int currentLevel = elevator.route.removeFirst();

                    if (elevator.route.isEmpty()) { // no more stops in route
                        elevator.direction = STATIONARY;
                    } else { // we still have stops to make, update direction in necessary
                        elevator.direction = currentLevel < elevator.currentTarget() ? UP : DOWN; // Updates direction
                        if (loadPassengers(elevator, currentFloor) > 0) {
                            // if not all passengers get to go, make a new request with the old direction
                            requestElevator(storedDirectionForRequest, currentFloor.level); // we keep the direction for the new requestx
                        }
                    }
                } else {
                    //keep on moving?
                }
            } catch (NoSuchElementException e) { // ...
                elevator.direction = STATIONARY;
            }
        }
    }

    /**
     * Move passengers from elevator to floor
     *
     * @param elevator
     * @param currentFloor
     * @return passengers left in elevator
     */
    private int unloadPassengers(Elevator elevator, Floor currentFloor) {
        for (Visitor visitor : elevator.passengers) {
            if (visitor.intendedFloor == currentFloor.level) {
                currentFloor.addVisitor(visitor);
                elevator.passengers.remove(visitor);
            }
        }
        return elevator.passengers.size();
    }

    /**
     * Move passengers from floor to elevator
     *
     * @param elevator
     * @param currentFloor
     * @return number of passengers left in elevator
     */
    private int loadPassengers(Elevator elevator, Floor currentFloor) {
        List<Visitor> floorQueue = elevator.direction == UP ? currentFloor.queueUp : currentFloor.queueDown;

        while (elevator.passengers.size() < elevator.capasity && floorQueue.size() > 0) {
            elevator.passengers.add(floorQueue.remove(0));
        }

        return floorQueue.size();
    }

    /***
     * TODO: rewrite to take direction as parameter to avoid from-to error. Request makers should not be concerned with
     * levels
     * Insert a request into the system.
     * @param to
     * @param from
     * @return true if a new request has been added, false when a request for the direction already exist
     */
    public boolean requestElevator(int to, int from) { // TODO: remove to from Request logic (visitors know where they are going, elevators only need to know when visitor enters)

        // * check direction
        Elevator.ElevatorDirection requestDirection = from > to ? DOWN : UP;

        // * check if already called
        for (Request r : requests) { // TODO, move this responsibility to each floor
            if (r.from == from && requestDirection == (r.direction)) {
                return false; // we already have a request in that direction
            }
        }
        // * if not: make request
        return requests.add(new Request(to, from));
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
