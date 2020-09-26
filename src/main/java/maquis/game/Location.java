package maquis.game;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private LocationType type;
    private LocationType spareRoomType;
    private List<Agent> agents = new ArrayList<>();
    private boolean milice;
    private boolean soldier;
    private boolean guaranteedAction;
    // Some missions block off locations until completed (ie. Bomb for the Officer)
    private boolean blocked;

    public Location(LocationType type){
        this.type = type;
        this.spareRoomType = null;
        milice = false;
        soldier = false;
        guaranteedAction = false;
        blocked = false;
    }

    public String getName() {
        return this.type.getName();
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public LocationType getSpareRoomType() {
        return spareRoomType;
    }

    public void setSpareRoomType(LocationType spareRoomType) {
        this.spareRoomType = spareRoomType;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents.clear();
        this.agents.addAll(agents);
    }

    public boolean hasMilice() {
        return milice;
    }

    public void setMilice(boolean milice) {
        this.milice = milice;
    }

    public boolean hasSoldier() {
        return soldier;
    }

    public void setSoldier(boolean soldier) {
        this.soldier = soldier;
    }

    public boolean isGuaranteedAction() {
        return guaranteedAction;
    }

    public void setGuaranteedAction(boolean guaranteedAction) {
        this.guaranteedAction = guaranteedAction;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String toString(){
        return type.getName();
    }
}
