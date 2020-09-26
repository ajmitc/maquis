package maquis.game.mission;

import maquis.game.*;
import maquis.view.PopupUtil;

/**
 * The best place to collect reconnaissance is often from the inside.  Insert a mole into the Milice.
 * Deliver 2 Intel to this location.  The Agent must remain here until another Agent delivers 1 Weapon and 1 Explosive.
 * While the first Agent is here, you may look at the top card of the Patrol Deck before the placement phase.
 */
public class InfiltrationMission extends Mission{
    private Agent firstAgent = null;

    public InfiltrationMission(Game game){
        super(game, "Infiltration", 1, "The best place to collect reconnaissance is often from the inside.  Insert a mole into the Milice.", "Deliver 2 Intel to this location.  The Agent must remain here until another Agent delivers 1 Weapon and 1 Explosive. While the first Agent is here, you may look at the top card of the Patrol Deck before the placement phase.");
        addMissionRequirement("Deliver 2 intel to the Mission location");
        addMissionRequirement("Agent must remain at the Mission location");
        addMissionRequirement("Another Agent must deliver 1 weapon and 1 explosive to the Mission location");
    }

    @Override
    public void restart() {
        super.restart();
        firstAgent = null;
    }

    @Override
    public boolean returnAgentToSafeHouse() {
        return completed;
    }

    @Override
    public void visitLocation(LocationType locationType) {
        super.visitLocation(locationType);
        if (completed) return;
        Location missionLocation = game.getBoard().getLocationWithMission(this);
        if (missionLocation.getType() == locationType){
            if (firstAgent == null && game.hasResources(Resource.INTEL, 2)){
                firstAgent = missionLocation.getAgents().get(0);
                game.discardResources(Resource.INTEL, 2);
                getMissionRequirements().get(0).setCompleted(true);
                getMissionRequirements().get(1).setCompleted(true);
            }
            else if (firstAgent != null && game.hasResources(Resource.WEAPONS, 1) && game.hasResources(Resource.EXPLOSIVES, 1) && missionLocation.getAgents().size() >= 2) {
                game.discardResources(Resource.WEAPONS, 1);
                game.discardResources(Resource.EXPLOSIVES, 1);
                completed = true;
                getMissionRequirements().get(2).setCompleted(true);
                PopupUtil.popupNotification(null, "Mission", getName() + " Completed");
            }
        }
    }
}
