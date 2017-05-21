package no.taco.elevator.Hotel;

import no.taco.elevator.Agent.Visitor;

import java.util.ArrayList;
import java.util.List;

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

            // check if elevator is moving in the correct direction
            if (elevator.route.isEmpty()) { // NOthing to do
                elevator.direction = STATIONARY;
                return;
            }



            if (elevator.currentTarget() == elevator.currentFloor) { // currentTarget throws NoSuchElementException
                System.out.println("We reached a target");
                Floor currentFloor = floors.get(elevator.currentFloor);
                unloadPassengers(elevator, currentFloor); // <-- passengers in elevator moved into floor

                elevator.route.removeFirst();
                if (elevator.route.isEmpty() && currentFloor.level == 0) {
                    elevator.direction = UP;
                } // TODO make rule for top-floor, and what about

                if (loadPassengers(elevator, currentFloor) > 0) {
                    System.out.printf("Passengers added, route %d long", elevator.route.size());
                // if not all passengers get to go, make a new request with the old direction//      load passengers from correct queue
                    //requestElevator(currentFloor.level + storedDirectionForRequest, currentFloor.level); // we keep the direction for the new requestx// based on route, update direction
                }

                if (elevator.route.isEmpty()) { // no more stops in route
                    elevator.direction = STATIONARY;
                } else { // we still have stops to make, update direction in necessary
                    elevator.direction = currentFloor.level < elevator.currentTarget() ? UP : DOWN; // Updates direction
                }
            }

            elevator.move(); // increment/decrement currentFloor

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
        List<Visitor> toRemove = new ArrayList<>();
        for (Visitor visitor : elevator.passengers) {
            if (visitor.intendedFloor == currentFloor.level) {
                currentFloor.addVisitor(visitor);
                toRemove.add(visitor);
            }
        }
        elevator.passengers.removeAll(toRemove);

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
        // TODO: if we have a stationary elevator, take the queue with the most people?
        List<Visitor> floorQueue;
        if (elevator.direction != STATIONARY) {
            floorQueue = elevator.direction == UP ? currentFloor.queueUp : currentFloor.queueDown;
        }
        else {
            floorQueue = currentFloor.queueUp.size() > currentFloor.queueDown.size() ? currentFloor.queueUp : currentFloor.queueDown;
            elevator.direction = currentFloor.queueUp.size() > currentFloor.queueDown.size() ? UP : DOWN; // surp
        }

        while (elevator.passengers.size() < elevator.capacity && floorQueue.size() > 0) {
            Visitor v = floorQueue.remove(0);
            currentFloor.visitors.remove(v);
            elevator.passengers.add(v); // TODO: Let passenger add floor level to route
            elevator.route.add(v.intendedFloor);
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
        Elevator.ElevatorDirection requestDirection = to > from ? UP : DOWN;

        // * check if already called
        for (Request r : requests) { // TODO, move this responsibility to each floor
            if (r.from == from && requestDirection == r.direction) {
                return false; // we already have a request in that direction
            }
        }
        // * if not: make request
        requests.add(new Request(to, from));
        return true;
    }

    /**
     * Reads all requests in the queue and assigns them to elevators.
     *
     * Not optimized in any way!
     */
    public void assignRequests() {
        for (Request r : requests) {
            System.out.printf("r: %s to %d\n", r.direction, r.to);
            naiveAssignment(r);
            // Read this (https://www.amazon.com/Elevator-Traffic-Handbook-Theory-Practice/dp/1138852325) and improve implementation
        }
        requests.clear();

    }

    //TODO: check if we already have an elevator on the same floor
    // TODO: refactor: sort elevators -> stationary, upward, downward. Sort by proximity to request <-- less duplication of code
    private void naiveAssignment(Request req) {
        if (req.direction == UP) {
            //direction UP --> get all elevators below (primarily the ones on their way up)
            List<Elevator> upwardElevators = new ArrayList<>();

            for (Elevator elevator : elevators) {
                if (elevator.direction == UP && elevator.currentFloor < req.from) {
                    upwardElevators.add(elevator);
                }
            }
            //select one -- current implementation naive and unfinished
            Elevator selectedElevator = elevators.get(0);

            if (upwardElevators.size() > 0) {
                //Elevator sortedUpwardElevators = upwardElevators.sort(x -> x.currentFloor); // TODO, find the closest
                selectedElevator = upwardElevators.get(0);
            }

            selectedElevator.addFloorToRoute(req.from); //TODO: Visitor should add the to-level when entering
        }

        else if (req.direction == DOWN) {
            //direction DOWN --> get all elevators above (primarily the ones on their way down)
            List<Elevator> downwardElevators = new ArrayList<>();
            for (Elevator elevator : elevators) {
                if (elevator.direction == DOWN && elevator.currentFloor > req.from) {
                    downwardElevators.add(elevator);
                }
            }
            //select one
            Elevator selectedElevator = elevators.get(elevators.size()-1); // To vary it up

            if (downwardElevators.size() > 0) {
                //Elevator sortedDownwardElevators = downwardElevators.sort(x -> x.currentFloor); // TODO, find the closest
                selectedElevator = downwardElevators.get(0);
            }

            selectedElevator.addFloorToRoute(req.from);
        }
        else {
            // TODO error handling, no request should be STATIONARY
            System.out.println("****************** ERROR");
        }
    }

    private class Request {
        public int to, from;
        Elevator.ElevatorDirection direction;

        public Request(int to, int from) {
            this.to = to;
            this.from = from;
            direction = to > from ? UP :DOWN;
        }
    }
}
