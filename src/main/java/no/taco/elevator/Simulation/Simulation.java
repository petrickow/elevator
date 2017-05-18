package no.taco.elevator.Simulation;

import no.taco.elevator.Hotel.Building;

/**
 *
 */
public class Simulation {

    private final int numTicks;
    private final int numVisitors;
    private final SimulationState[] stateArray;
    SimulationState currentState;
    Building building;

    public Simulation(Building building, int numTicks, int numVisitors) {
        this.building = building;
        this.numTicks = numTicks;
        this.numVisitors = numVisitors;
        this.stateArray = prepSimulationState(numTicks);
    }

    private SimulationState[] prepSimulationState(int numTicks) {
        SimulationState[] states = new SimulationState[numTicks];

        for(int i = 0; i < stateArray.length; i++) {
            stateArray[i] = new SimulationState("Step " + i);
        }
        return states;
    }

    public boolean getGoing() {
        return false;
    }
}
