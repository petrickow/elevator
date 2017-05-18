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

    public void requestDown(int to, int from) {
        throw new NotImplementedException();
    }

    public void requestUp(int to, int from) {
        throw new NotImplementedException();
    }

    public void assignRequests() {
        for (Request r : requests) {
            if (r.from < r.to) {
                //direction UP --> get all elevators below (primarily the ones on their way up)

                //select one
            }
            else if (r.from > r.to) {
                //direction DOWN --> get all elevators above (primarily the ones on their way down)

                //select one

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
