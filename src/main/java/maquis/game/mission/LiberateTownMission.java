package maquis.game.mission;

import maquis.game.Game;
import maquis.game.LocationType;
import maquis.game.Resource;
import maquis.view.PopupUtil;

/**
 * The Allies are pushing forward.  If we rise up at the right time, our town could
 * emerge unscathed. For that, we will need weapons and courage!
 * Possess at least 3 Weapons & 4 Morale at the end of the game
 */
public class LiberateTownMission extends Mission{
    public LiberateTownMission(Game game){
        super(game, "Liberate the Town", 2, "The Allies are pushing forward.  If we rise up at the right time, our town could emerge unscathed. For that, we will need weapons and courage!", "Possess at least 3 Weapons & 4 Morale at the end of the game");
        addMissionRequirement("Possess 3+ weapons");
        addMissionRequirement("Possess 4+ morale");
        getMissionRequirements().get(1).setCompleted(true);
    }

    @Override
    public void turnTeardown() {
        super.turnTeardown();
        if  (game.getTurn() == 15 && game.hasResources(Resource.WEAPONS, 3) && game.getMorale() >= 4){
            completed = true;
            PopupUtil.popupNotification(null, "Mission", getName() + " Completed");
        }
    }

    @Override
    public void visitLocation(LocationType locationType) {
        super.visitLocation(locationType);
        if (completed) return;
        getMissionRequirements().get(0).setCompleted(game.hasResources(Resource.WEAPONS, 3));
        getMissionRequirements().get(1).setCompleted(game.getMorale() >= 4);
    }
}
