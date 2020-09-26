package maquis.game.mission;

import maquis.game.Game;
import maquis.game.LocationType;
import maquis.game.Resource;
import maquis.view.PopupUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Get the word out and counter the propaganda of the occupation
 * Discard 2 intel, then 2 intel, then 2 intel
 */
public class UndergroundNewspaperMission extends Mission{
    private List<Integer> intelDeliveries = new ArrayList<>();

    public UndergroundNewspaperMission(Game game){
        super(game, "Underground Newspaper", 1, "Get the word out and counter the propaganda of the occupation.", "Discard 2 intel, then 2 intel, then 2 intel");
        addMissionRequirement("Discard 2 intel");
        addMissionRequirement("Discard 2 intel");
        addMissionRequirement("Discard 2 intel");
    }

    @Override
    public void restart() {
        super.restart();
        intelDeliveries.clear();
    }

    @Override
    public void visitLocation(LocationType locationType) {
        super.visitLocation(locationType);
        if (completed) return;
        if  (!intelDeliveries.contains(game.getTurn()) && game.hasResources(Resource.INTEL, 2)){
            game.discardResources(Resource.INTEL, 2);
            intelDeliveries.add(game.getTurn());
            completed = intelDeliveries.size() >= 3;
            getMissionRequirements().get(intelDeliveries.size() - 1).setCompleted(true);
            if (completed)
                PopupUtil.popupNotification(null, "Mission", getName() + " Completed");
        }
    }
}
