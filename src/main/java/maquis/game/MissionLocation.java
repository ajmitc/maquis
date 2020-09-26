package maquis.game;

import maquis.game.mission.Mission;

public class MissionLocation extends Location{
    private Mission mission;

    public MissionLocation(LocationType type){
        super(type);
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public boolean connectsToBoard() {
        return mission != null? mission.connectToBoard(): true;
    }
}
