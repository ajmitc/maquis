package maquis.game.mission;

import maquis.game.Game;
import maquis.game.Location;
import maquis.view.PopupUtil;

import javax.swing.*;

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
    public boolean returnAgentToSafeHouse() {
        return game.getTurn() > 10;
    }

    @Override
    public void turnSetup() {
        super.turnSetup();
        Location missionLocation = game.getBoard().getLocationWithMission(this);
        if (game.getTurn() == 6 && missionLocation.getAgents().isEmpty())
            PopupUtil.popupNotification(null, "Coded Messages Mission", "WARNING: You must place an agent in the Coded Messages mission location by the end of this turn!");
    }

    @Override
    public void turnTeardown() {
        super.turnTeardown();
        if (game.getTurn() >= 6 && game.getTurn() <= 10) {
            Location missionLocation = game.getBoard().getLocationWithMission(this);
            if (game.getTurn() == 6){
                agentPlacedByDay6 = !missionLocation.getAgents().isEmpty();
                agentRemainedUntilDay10 = agentPlacedByDay6;
                if (!agentPlacedByDay6)
                    PopupUtil.popupNotification(null, "Mission", getName() + " Failed!");
            }
            else if (game.getTurn() == 10 && agentPlacedByDay6){
                if (agentRemainedUntilDay10) {
                    completed = true;
                    PopupUtil.popupNotification(null, "Mission", getName() + " Completed");
                }
                else
                    PopupUtil.popupNotification(null, "Mission", getName() + " Failed!");
            }
            else if (agentRemainedUntilDay10) {
                agentRemainedUntilDay10 = !missionLocation.getAgents().isEmpty();
            }
            getMissionRequirements().get(0).setCompleted(agentPlacedByDay6);
            getMissionRequirements().get(1).setCompleted(agentRemainedUntilDay10);
        }
    }
}
