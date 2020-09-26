package maquis.game;

import java.util.ArrayList;
import java.util.List;

public class FieldLocation extends Location{
    private List<Resource> resources = new ArrayList<>();
    private int turnDropped;

    public FieldLocation(LocationType type){
        super(type);
    }

    public List<Resource> getResources() {
        return resources;
    }

    public int getTurnDropped() {
        return turnDropped;
    }

    public void setTurnDropped(int turnDropped) {
        this.turnDropped = turnDropped;
    }
}
