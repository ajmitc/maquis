package maquis.game.mission;

import maquis.game.Game;
import maquis.game.Location;
import maquis.game.LocationType;
import maquis.view.PopupUtil;

/**
 * The local commander has commandeered a fancy house north of town.   We need to make sure he knows
 * that he will never has us cowed.
 *
 * Place an Agent on Rue Baradat, Pont Leveque, and Pont du Nord to write anti-fascist graffiti.  Once all three
 * locations are tagged, place an Agent here to complete.
 */
public class OfficersMansionMission extends Mission{
    private boolean visitedPontLeveque;
    private boolean visitedPontDuNord;
    private boolean visitedRueBaradat;

    public OfficersMansionMission(Game game){
        super(game, "Officer's Mansion", 1, "The local commander has commandeered a fancy house north of town.   We need to make sure he knows that he will never has us cowed.", "Place an Agent on Rue Baradat, Pont Leveque, and Pont du Nord to write anti-fascist graffiti.  Once all three locations are tagged, place an Agent at the Mission Location to complete.");
        addMissionRequirement("Visit Rue Baradat");
        addMissionRequirement("Visit Pont Leveque");
        addMissionRequirement("Visit Pont du Nord");
        addMissionRequirement("Visit Mission location");
    }

    @Override
    public void restart() {
        super.restart();
        visitedPontDuNord  = false;
        visitedPontLeveque = false;
        visitedRueBaradat  = false;
    }

    @Override
    public void visitLocation(LocationType locationType) {
        super.visitLocation(locationType);
        if (completed) return;
        if (locationType == LocationType.RUE_BARADAT) {
            visitedRueBaradat = true;
            getMissionRequirements().get(0).setCompleted(true);
            return;
        }
        else if (locationType == LocationType.PONT_LEVEQUE) {
            visitedPontLeveque = true;
            getMissionRequirements().get(1).setCompleted(true);
            return;
        }
        else if (locationType == LocationType.PONT_DU_NORD) {
            visitedPontDuNord = true;
            getMissionRequirements().get(2).setCompleted(true);
            return;
        }
        else if (locationType == game.getBoard().getLocationWithMission(this).getType()) {
            completed = visitedPontDuNord && visitedPontLeveque && visitedRueBaradat;
            getMissionRequirements().get(3).setCompleted(completed);
            if (completed)
                PopupUtil.popupNotification(null, "Mission", getName() + " Completed");
        }
    }
}
