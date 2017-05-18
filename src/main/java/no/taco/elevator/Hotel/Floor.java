package no.taco.elevator.Hotel;

import no.taco.elevator.Agent.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by catoda on 18.05.17.
 */
public class Floor {
    public int level;

    public List<Visitor> queueUp;
    public List<Visitor> queueDown;
    public List<Visitor> visitors;


    public Floor(int level) {
        this.level = level;
        queueDown = new ArrayList<>();
        queueUp = new ArrayList<>();
        visitors = new ArrayList<>();
    }

    public void queueForTransfer(Visitor v) {
        if (v.intendedFloor > level) {
            queueUp.add(v);
        } else {
            queueDown.add(v);
        }
    }

    /**
     * TODO
     * @param v
     * @return true if visitor successfully has been added
     */
    public boolean addVisitor(Visitor v) {
        if (visitors.contains(v)) {
            return false;
        } else {
            return visitors.add(v);
        }
    }

    /**
     * TODO
     * @param v
     * @return true if visitor successfully has been removed
     */
    public boolean removeVisitor(Visitor v) {
        if (!visitors.contains(v)) {
            return false;
        } else {
            return visitors.remove(v);
        }
    }
}
