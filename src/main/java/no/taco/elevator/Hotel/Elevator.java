package no.taco.elevator.Hotel;

import no.taco.elevator.Agent.Visitor;

/**
 * Created by catoda on 18.05.17.
 */
public class Elevator {

    //  * Elevator <- modell for en gitt heis, kapasitet, på vei opp/ned, antall passasjerer
    public enum ElevatorDirection {
        UP, DOWN, STATIONARY
    }

    public ElevatorDirection direction;
    public int id;
    public int passengers;
    public int currentFloor;

    final int capasity = 8;


    public Elevator(int id) {
        this.id = id;

        // all elevators start on the bottom floor with no passengers
        direction = ElevatorDirection.STATIONARY;
        passengers = 0;
        currentFloor = 0;
    }

    /* TODO: update state
        * start traveling when queue empty previous tick or elevator filled
        *
     */


    /*TODO: list all passengers, or do passengers keep track? */
    public boolean enter(Visitor person) {
        if (passengers >= capasity) {
            return false;
        }
        else {
            //person.update(elevator.id)... something or other
            passengers++;
            return true;
        }
    }

    public void exit() {
        passengers--;
    }

}
