package maquis.game.mission;

import maquis.game.Game;
import maquis.game.LocationType;
import maquis.game.Resource;
import maquis.view.PopupUtil;

/**
 * We've received intelligence that the occupiers are going to be transporting Panzers along the railway near your
 * town.  Plant bombs to destroy the train as it passes.
 * Deliver 3 explosives.
 * Complete only on turns 6-9
 */
public class DestroyTheTrainMission extends Mission{
    public DestroyTheTrainMission(Game game){
        super(game, "Destroy the Train", 2, "We've received intelligence that the occupiers are going to be transporting Panzers along the railway near your town.  Plant bombs to destroy the train as it passes.", "Deliver 3 explosives. Complete only on turns 6-9");
        addMissionRequirement("Deliver 3 explosives between turns 6-9");
    }

    @Override
    public void visitLocation(LocationType locationType) {
        super.visitLocation(locationType);
        if (completed) return;
        if  (game.getTurn() >= 6 &&
                game.getTurn() <= 9 &&
                game.getResources().stream().filter(r -> r == Resource.EXPLOSIVES).count() >= 3){
            game.getResources().remove(Resource.EXPLOSIVES);
            game.getResources().remove(Resource.EXPLOSIVES);
            game.getResources().remove(Resource.EXPLOSIVES);
            completed = true;
            getMissionRequirements().get(0).setCompleted(true);
            if (completed)
                PopupUtil.popupNotification(null, "Mission", getName() + " Completed");
        }
    }
}
