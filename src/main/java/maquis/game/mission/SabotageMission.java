package maquis.game.mission;

import maquis.game.Game;
import maquis.game.Location;
import maquis.game.LocationType;
import maquis.game.Resource;
import maquis.view.PopupUtil;

/**
 * The occupiers run a munitions factory on the outskirts of town. Infiltrate and sabotage the operation by any means possible.
 * An agent must visit this mission twice, then bring 1 explosive on a third day
 */
public class SabotageMission extends Mission{
    private int visits = 0;

    public SabotageMission(Game game){
        super(game, "Sabotage", 1, "The occupiers run a munitions factory on the outskirts of town. Infiltrate and sabotage the operation by any means possible.", "An agent must visit this mission twice on different days, then bring 1 explosive on a third day");
        addMissionRequirement("Visit Mission location");
        addMissionRequirement("Visit Mission location");
        addMissionRequirement("Visit Mission location with 1 explosive");
    }

    @Override
    public void restart() {
        super.restart();
        visits = 0;
    }

    @Override
    public void visitLocation(LocationType locationType) {
        super.visitLocation(locationType);
        if (completed) return;
        Location location = game.getBoard().getLocationWithMission(this);
        if (locationType == location.getType()){
            if (visits < 2)
                getMissionRequirements().get(visits).setCompleted(true);
            visits += 1;
            if (visits >= 3 && game.hasResources(Resource.EXPLOSIVES, 1)) {
                game.getResources().remove(Resource.EXPLOSIVES);
                completed = true;
                getMissionRequirements().get(2).setCompleted(true);
                PopupUtil.popupNotification(null, "Mission", getName() + " Completed");
            }
        }
    }
}
