package no.taco.elevator.Simulation;

import no.taco.elevator.Hotel.Building;
import no.taco.elevator.Agent.Visitor;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 */
public class Simulation {
    private final int spawnFreqency; // 1 in sf ticks yield
    private final int maxSimultaniousVisitors; //

    private int totalSpawns;
    private final int numTicks;
    private final int numVisitors;
    private final SimulationState[] stateArray;
    private Building building;

    public Simulation(Building building, int numTicks, int numVisitors) {
        totalSpawns = 0;
        spawnFreqency = 3;
        maxSimultaniousVisitors = 10;

        this.building = building;
        this.numTicks = numTicks;
        this.numVisitors = numVisitors;
        this.stateArray = prepSimulationState(numTicks);
    }


    private SimulationState[] prepSimulationState(int numTicks) {
        SimulationState[] states = new SimulationState[numTicks];
        for(int i = 0; i < states.length; i++) {
            states[i] = new SimulationState("Step " + i);
        }
        return states;
    }

    /**
     * Main workhorse of simulation
     * @return true when simulation successfully completed
     */
    public boolean getGoing() {
        //TODO: error detection/handling
        for(int i = 1; i <= stateArray.length; i++) {

            printStatus(i);
            // Start off with creating new visitors
            int arrivedVisitors
                    = spawnVisitors();

            // Check visitors. Visitors make requests when they need to get moving.
            building.inspectVisitors();

            // assign requests to elevators
            building.elevatorManager.assignRequests();

            // update elevators includes: (bit to much)
            //      * elevator movement
            //      * unload / load passengers
            //      * update route / direction
            building.elevatorManager.update(building.floors); //
        }

        System.out.println("Total visitors in simulation: " + totalSpawns);
        return true;
    }

    private int spawnVisitors() {
        int numberOfArrivingVisitors = 0;
        ArrayList<Visitor> newVisitors = new ArrayList<Visitor>();
        // TODO Add check to ensure the number of visitors is reached
        if (ThreadLocalRandom.current().nextInt(0,spawnFreqency) == 1) { // 1 in n chance of spawning a dude
            numberOfArrivingVisitors = ThreadLocalRandom.current().nextInt(1, maxSimultaniousVisitors + 1); // 1 -> max

            for (int i = 0; i<numberOfArrivingVisitors; i++) {

                Visitor v = new Visitor(
                        ThreadLocalRandom.current().nextInt(0, building.floors.size() +1), // target floor
                        ThreadLocalRandom.current().nextInt(1, 20 +1) // number of ticks on floor
                );
                building.currentVisitors.add(v);
            }
            totalSpawns += numberOfArrivingVisitors;
        }
        return numberOfArrivingVisitors;
    }

    /// Simulation step:
    // # Check agents, get requests
    // # Get the list of requests for the current tick (and existing ones) --> find elevators on route
    // # Iterate elevators [1], assign requests
    // # Move elevators (as per request) [2] or unload/load passengers
    // # Move visitors in out of elevator-queues

    /*
    [1]:
        if STATIONARY -> load from queue, update (state) queue
    [2]:
        _FIRST_ iteration: FIFO
    */

    private void printStatus(int itteration) {
        System.out.printf("Step %d\t\n" +
                "Visitors: %d" +
                "...",
                itteration, building.currentVisitors.size());
    }
}
