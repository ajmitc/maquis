package maquis.game.mission;

import maquis.game.Game;
import maquis.game.Location;
import maquis.view.PopupUtil;

/**
 * Knowledge is power.  Work with other Resistance Fighters from other cities to monitor the Occupation.  Train a
 * cryptographer, then have them communicate with other Resistance operatives.
 * An Agent must be placed here by end of Day 6 and must remain here until the end of Day 10.
 */
public class CodedMessagesMission extends Mission{
    private boolean agentPlacedByDay6 = false;
    private boolean agentRemainedUntilDay10 = false;

    public CodedMessagesMission(Game game){
        super(game, "Coded Messages", 2, "Knowledge is power.  Work with other Resistance Fighters from other cities to monitor the Occupation.  Train a cryptographer, then have them communicate with other Resistance operatives.", "An Agent must be placed here by end of Day 6 and must remain here until the end of Day 10.");
        addMissionRequirement("Place Agent at this Mission location by end of turn 6");
        addMissionRequirement("Agent must remain at this Mission location until end of turn 10");
    }

    @Override
    public void restart() {
        super.restart();
        agentPlacedByDay6 = false;
        agentRemainedUntilDay10 = false;
    }

    @Override
    public void turnTeardown() {
        super.turnTeardown();
        if (game.getTurn() >= 6 && game.getTurn() <= 9) {
            Location missionLocation = game.getBoard().getLocationWithMission(this);
            if (game.getTurn() == 6){
                agentPlacedByDay6 = !missionLocation.getAgents().isEmpty();
                agentRemainedUntilDay10 = agentPlacedByDay6;
            }
            else if (game.getTurn() == 9 && agentRemainedUntilDay10){
                agentRemainedUntilDay10 = !missionLocation.getAgents().isEmpty();
                completed = agentPlacedByDay6 && agentRemainedUntilDay10;
                getMissionRequirements().get(0).setCompleted(completed);
                if (completed)
                    PopupUtil.popupNotification(null, "Mission", getName() + " Completed");
            }
            else if (agentRemainedUntilDay10) {
                agentRemainedUntilDay10 = !missionLocation.getAgents().isEmpty();
            }
            getMissionRequirements().get(0).setCompleted(agentPlacedByDay6);
        }
    }
}
