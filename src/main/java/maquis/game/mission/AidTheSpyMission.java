package maquis.game.mission;

import maquis.game.*;
import maquis.view.PopupUtil;

/**
 * A British spy parachuted in a few days ago, and he needs your help to provide him with equipment and
 * supplies to help him carry out his mission.
 * Discard 2 weapons, then discard 1 money and 2 food
 */
public class AidTheSpyMission extends Mission{
    private boolean deliveredWeapons = false;
    private boolean deliveredSupplies = false;

    public AidTheSpyMission(Game game){
        super(game, "Aid the Spy", 2, "A British spy parachuted in a few days ago, and he needs your help to provide him with equipment and supplies to help him carry out his mission.", "Discard 2 weapons, then discard 1 money and 2 food");
        addMissionRequirement("Discard 2 weapons at the Mission location");
        addMissionRequirement("Discard 1 money and 2 food at the Mission location");
    }

    @Override
    public void restart(){
        super.restart();
        deliveredWeapons  = false;
        deliveredSupplies = false;
    }

    @Override
    public void visitLocation(LocationType locationType) {
        super.visitLocation(locationType);
        if (completed) return;
        MissionLocation missionLocation = (MissionLocation) game.getBoard().getLocationWithMission(this);
        if  (locationType == missionLocation.getType() && !deliveredWeapons && game.hasResources(Resource.WEAPONS, 2)){
            game.discardResources(Resource.WEAPONS, 2);
            deliveredWeapons = true;
            getMissionRequirements().get(0).setCompleted(true);
        }
        else if  (locationType == missionLocation.getType() && deliveredWeapons && !deliveredSupplies && game.hasResources(Resource.MONEY, 1) && game.hasResources(Resource.FOOD, 2)){
            game.discardResources(Resource.FOOD, 2);
            game.discardResources(Resource.MONEY, 1);
            deliveredSupplies = true;
            getMissionRequirements().get(1).setCompleted(true);
        }
        completed = deliveredWeapons && deliveredSupplies;
        if (completed)
            PopupUtil.popupNotification(null, "Mission", getName() + " Completed");
    }
}
