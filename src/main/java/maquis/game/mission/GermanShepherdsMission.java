package maquis.game.mission;

import maquis.game.Game;
import maquis.game.Location;
import maquis.game.LocationType;
import maquis.game.Resource;
import maquis.view.PopupUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * The occupiers have dogs to help with patrols.  Use poison to eliminate them.
 * Deliver 1 Medicine and 1 Food to this location on three separate days.  Before this mission is completed, Milice
 * units may not be eliminated.
 */
public class GermanShepherdsMission extends Mission{
    private List<Integer> visits = new ArrayList<>();

    public GermanShepherdsMission(Game game){
        super(game, "German Shepherds", 1, "The occupiers have dogs to help with patrols.  Use poison to eliminate them.", "Deliver 1 Medicine and 1 Food to this location on three separate days.  Before this mission is completed, Milice units may not be eliminated.");
        addMissionRequirement("Deliver 1 medicine and 1 food to the Mission location");
        addMissionRequirement("Deliver 1 medicine and 1 food to the Mission location");
        addMissionRequirement("Deliver 1 medicine and 1 food to the Mission location");
    }

    @Override
    public void setup() {
        super.setup();
        game.setAllowMiliceElimination(false);
    }

    @Override
    public void restart() {
        super.restart();
        setup();
        visits.clear();
    }

    @Override
    public void visitLocation(LocationType locationType) {
        super.visitLocation(locationType);
        if (completed) return;
        Location missionLocation = game.getBoard().getLocationWithMission(this);
        if  (missionLocation.getType() == locationType &&
                game.hasResources(Resource.MEDICINE, 1) &&
                game.hasResources(Resource.FOOD, 1) &&
                !visits.contains(game.getTurn())){
            game.discardResources(Resource.MEDICINE, 1);
            game.discardResources(Resource.FOOD, 1);
            visits.add(game.getTurn());
            completed = visits.size() >= 3;
            game.setAllowMiliceElimination(completed);
            getMissionRequirements().get(visits.size() - 1).setCompleted(true);
            if (completed)
                PopupUtil.popupNotification(null, "Mission", getName() + " Completed");
        }
    }
}
