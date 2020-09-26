package maquis.game.mission;

import maquis.game.Game;
import maquis.game.Location;
import maquis.game.LocationType;
import maquis.game.Road;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Mission {
    private String name;
    private int difficulty;
    private String description;
    private String completionRequirements;
    protected Game game;
    protected boolean completed = false;
    private List<MissionRequirement> missionRequirements = new ArrayList<>();

    public Mission(Game game, String name, int difficulty, String description, String completionRequirements){
        this.game = game;
        this.name = name;
        this.difficulty = difficulty;
        this.description = description;
        this.completionRequirements = completionRequirements;
    }

    /**
     * This method is called when a Mission is chosen in a Game.  It performs any Mission-specific setup before
     * the game starts.
     */
    public void setup(){
        Location missionLocation = game.getBoard().getLocationWithMission(this);
        missionLocation.setGuaranteedAction(true);

        if (!connectToBoard()){
            Road road = game.getBoard().getRoads().stream().filter(r -> r.hasLocationType(missionLocation.getType())).findFirst().get();
            road.setBlocked(true);
        }
    }

    public void restart(){
        missionRequirements.stream().forEach(mr -> mr.setCompleted(false));
    }

    /**
     * This method is called at the start of every turn to allow the mission to modify the game state before the turn
     * starts.
     */
    public void turnSetup(){}

    /**
     * This method is called at the end of every turn to allow the mission to modify the game state after the turn is
     * finished.
     */
    public void turnTeardown(){}

    public boolean isCompleted(){return completed;}

    public boolean connectToBoard(){return true;};

    public boolean returnAgentToSafeHouse(){return true;}

    /**
     * This method is called when an Agent visits a location.
     * This is useful for Missions like Graffiti, where the agents need to visit
     * certain locations to complete it.
     * Subclasses that need this feature should override this method.
     * @param locationType
     */
    public void visitLocation(LocationType locationType){}

    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getDescription() {
        return description;
    }

    public String getCompletionRequirements() {
        return completionRequirements;
    }

    public List<MissionRequirement> getMissionRequirements() {
        return missionRequirements;
    }

    public void addMissionRequirement(String description){
        missionRequirements.add(new MissionRequirement(description));
    }

    public String toString(){
        return name;
    }

    public static class MissionRequirement{
        private String description;
        private boolean completed;
        private JCheckBox checkBox;

        public MissionRequirement(String description){
            this.description = description;
            completed = false;
            checkBox = null;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
            if (this.checkBox != null) {
                this.checkBox.setText(this.description);
                this.checkBox.setText(this.description);
            }
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public void setCheckBox(JCheckBox checkBox) {
            this.checkBox = checkBox;
        }
    }
}
