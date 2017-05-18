package no.taco.elevator.Simulation;

import no.taco.elevator.Hotel.Building;
import no.taco.elevator.Agent.Visitor;

import java.util.ArrayList;
import java.util.List;
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

    public boolean getGoing() {

        for(int i = 1; i <= numTicks; i++) {
            building.currentVisitors.addAll(spawnVisistors());
            building.inspectVisitors();
        }

        System.out.println("Total visitors in simulation: " + totalSpawns);
        return false;
    }

    private List<Visitor> spawnVisistors() {
        ArrayList<Visitor> newVisitors = new ArrayList<Visitor>();
        // TODO Add check to ensure the number of visitors is reached
        if (ThreadLocalRandom.current().nextInt(0,spawnFreqency) == 1) { // 1/
            int numberOfArrivingVisitors = ThreadLocalRandom.current().nextInt(1, maxSimultaniousVisitors + 1); // 1 -> max
            for (int i = 0; i<numberOfArrivingVisitors; i++) {
                newVisitors.add(new Visitor(
                        ThreadLocalRandom.current().nextInt(0, building.floors.size() +1), // target floor
                        ThreadLocalRandom.current().nextInt(1, 20 +1) // number of ticks on floor
                ));
            }
            totalSpawns += numberOfArrivingVisitors;
        }
        return newVisitors;
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
}
