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

    final int capacity = 8;


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
            case UP: currentFloor++; break;
            case DOWN: currentFloor--; break;
            case STATIONARY: break;
        }
        System.out.printf("Elevator %d moved to %d with %d passengers\n\tdirection %s... going to %d\n", id, currentFloor, passengers.size(), direction, route.peekFirst() );
    }

    public void addFloorToRoute(int floorLevel) {
        route.add(floorLevel);
        //route.sort(Integer::compareTo); //? TODO: logic to place the level in between floors to avoid overshooting floors
    }

    public int currentTarget() throws NoSuchElementException {
        return route.peekFirst();
    }
}




