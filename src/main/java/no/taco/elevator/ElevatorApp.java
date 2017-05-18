package no.taco.elevator;

import no.taco.elevator.Hotel.Building;
import no.taco.elevator.Simulation.Simulation;

/**
 * Created by catoda on 18.05.17.
 */
public class ElevatorApp {
    public static void main (String[] args) {

        System.out.println("Starting smarting ...");

        //TODO: parse args instead of hardcoded values
        final int numElevators = 4;
        final int numFloors = 20;
        final int numTicks = 480;
        final int numVisitors = 100;

        //Setup:
        Building building = new Building(numFloors,numElevators);
        Simulation sim = new Simulation(building, numTicks, numVisitors);

        sim.getGoing();

    }
}

/* TODO:
 * Setup --> antall heiser, besøkende, ticks -->
 * Modeller:
  * Tick <- klokke, holder styr på tidsforløp i simulering
  * Agent <- besøkende, states: fremme, kø, i heis
  * Building <- container for heiser og agenter og antall etasjer

    Hvordan skal heiser reagere på request? Modellere opp/ned request?
 */