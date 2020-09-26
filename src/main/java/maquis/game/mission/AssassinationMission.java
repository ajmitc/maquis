package maquis.game.mission;

import maquis.game.Game;
import maquis.game.Location;
import maquis.game.Road;
import maquis.view.PopupUtil;

/**
 * We need to eliminate the occupation leadership.
 * Eliminate all Milice Units.  This mission must be completed last.
 */
public class AssassinationMission extends Mission{

    public AssassinationMission(Game game){
        super(game, "Assassination", 2, "We need to eliminate the occupation leadership.", "Eliminate all Milice Units.  This mission must be completed last.");
        addMissionRequirement("Eliminate all Milice Units");
    }

    @Override
    public boolean isCompleted(){
        Mission otherMission = game.getMission1() == this? game.getMission2(): game.getMission1();
        boolean completed = otherMission.isCompleted() && game.getSoldiers() == 5;
        getMissionRequirements().get(0).setCompleted(completed);
        if (completed)
            PopupUtil.popupNotification(null, "Mission", getName() + " Completed");
        return completed;
    }

    @Override
    public boolean connectToBoard() {
        return false;
    }
}
