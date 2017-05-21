package no.taco.elevator.Hotel;

import no.taco.elevator.Agent.Visitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

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
    public int currentFloor;
    public LinkedList<Integer> route;
    public List <Visitor> passengers;

    final int capasity = 8;


    public Elevator(int id) {
        this.id = id;

        // all elevators start on the bottom floor with no passengers
        direction = ElevatorDirection.STATIONARY;
        passengers = new ArrayList<>();
        currentFloor = 0;
        route = new LinkedList<>();
    }


    public void move() {
        switch (direction) {
            case UP: currentFloor++;
            case DOWN: currentFloor--;
            case STATIONARY: break;
        }
    }

    public void addToRoute(int floorLevel) {
        route.add(new Integer(floorLevel));
        //route.sort(Integer::compareTo); //? TODO: logic to place the level in between floors to avoid overshooting floors
    }

    public int currentTarget() throws NoSuchElementException {
        return route.peekFirst().intValue();
    }
}




