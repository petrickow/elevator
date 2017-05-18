package no.taco.elevator.Hotel;

import no.taco.elevator.Agent.Visitor;
import java.util.List;

/**
 * Created by catoda on 18.05.17.
 */
public class Floor {
    public int level;
    public List<Visitor> queueUp;
    public List<Visitor> queueDown;

    public Floor(int level) {
        this.level = level;
    }


}
