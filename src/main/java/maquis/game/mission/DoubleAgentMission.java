package maquis.game.mission;

import maquis.game.*;
import maquis.view.PopupUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * We must unearth the double agent known only as the "Dark Lady"...
 * Visit all locations on the west side of the river except the Fence and Spare Room.  Once completed, turn over the top
 * patrol card; Location #1 is the location of the "Dark Lady".  Visit that location again to complete the mission.
 * Remove one Agent permanently from the game.
 */
public class DoubleAgentMission extends Mission{
    private Map<LocationType, Boolean> visited = new HashMap<>();
    private LocationType darkLadyLocation;

    public DoubleAgentMission(Game game){
        super(game, "Double Agent", 1, "We must unearth the double agent known only as the \"Dark Lady\"...", "Visit all locations on the west side of the river except the Fence and Spare Room.  Once completed, turn over the top patrol card; Location #1 is the location of the \"Dark Lady\".  Visit that location again to complete the mission. Remove one Agent permanently from the game.");
        visited.put(LocationType.DOCTOR, false);
        visited.put(LocationType.RUE_BARADAT, false);
        visited.put(LocationType.RADIO_A, false);
        visited.put(LocationType.POOR_DISTRICT, false);
        visited.put(LocationType.PONT_DU_NORD, false);
        visited.put(LocationType.PONT_LEVEQUE, false);
        darkLadyLocation = null;

        addMissionRequirement("Visit Doctor");
        addMissionRequirement("Visit Rue Baradat");
        addMissionRequirement("Visit Radio A");
        addMissionRequirement("Visit Poor District");
        addMissionRequirement("Visit Pont du Nord");
        addMissionRequirement("Visit Pont Leveque");
        addMissionRequirement("Visit Dark Lady location");
    }

    @Override
    public void restart() {
        super.restart();
        visited.keySet().stream().forEach(k -> visited.put(k, false));
        darkLadyLocation = null;
    }

    @Override
    public void visitLocation(LocationType locationType) {
        super.visitLocation(locationType);
        if (completed)
            return;
        if (darkLadyLocation != null && darkLadyLocation == locationType){
            // Find an unrecruited agent
            Agent unrecruited = game.getUnrecruitedAgent();
            if (unrecruited != null){
                unrecruited.setRecruited(true);
                unrecruited.setArrested(true);
            }
            else { // No unrecruited agents, arrest this agent
                Agent agent = game.getBoard().getLocation(locationType).getAgents().remove(0);
                agent.setArrested(true);
            }
            completed = true;
            getMissionRequirements().get(6).setCompleted(true);
            PopupUtil.popupNotification(null, "Mission", getName() + " Completed");
        }
        else if (visited.containsKey(locationType)){
            visited.put(locationType, true);

            switch (locationType){
                case DOCTOR:
                    getMissionRequirements().get(0).setCompleted(true);
                    break;
                case RUE_BARADAT:
                    getMissionRequirements().get(1).setCompleted(true);
                    break;
                case RADIO_A:
                    getMissionRequirements().get(2).setCompleted(true);
                    break;
                case POOR_DISTRICT:
                    getMissionRequirements().get(3).setCompleted(true);
                    break;
                case PONT_DU_NORD:
                    getMissionRequirements().get(4).setCompleted(true);
                    break;
                case PONT_LEVEQUE:
                    getMissionRequirements().get(5).setCompleted(true);
                    break;
                default:
                    break;
            }

            if (visited.values().stream().anyMatch(b -> b == false))
                return; // Still visiting locations

            // All locations visited
            if (darkLadyLocation == null) {
                darkLadyLocation = game.getPatrolDeck().draw().getLocation1();
                PopupUtil.popupNotification(null, "Mission", "Dark Lady is at " + darkLadyLocation.getName());
                getMissionRequirements().get(6).setDescription("Visit " + darkLadyLocation.getName() + " (Dark Lady)");
            }
        }
    }
}
