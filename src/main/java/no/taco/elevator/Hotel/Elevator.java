package no.taco.elevator.Hotel;

import no.taco.elevator.Agent.Visitor;

import java.util.LinkedList;

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
    public LinkedList<Integer> route;

    final int capasity = 8;


    public Elevator(int id) {
        this.id = id;

        // all elevators start on the bottom floor with no passengers
        direction = ElevatorDirection.STATIONARY;
        passengers = 0;
        currentFloor = 0;
        route = new LinkedList<>();
    }


    public void move() {
        /*TODO*/
        switch (direction) {
            case UP: currentFloor++;
            case DOWN: currentFloor--;
        }
    }

    //WIP
    public boolean enter(Visitor person) {
        if (passengers >= capasity) {
            return false;
        }
        else {
            //person.update(elevator.id)... something or other TODO Monitoring, keep track of who is in the elevator
            passengers++;
            return true;
        }
    }

    public void exit() {
        passengers--;
    }


    public void addToRoute(int floorLevel) {
        route.add(new Integer(floorLevel));
    }



}
