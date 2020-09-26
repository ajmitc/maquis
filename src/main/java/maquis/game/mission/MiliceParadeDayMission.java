package maquis.game.mission;

import maquis.game.*;
import maquis.view.PopupUtil;

import java.util.Arrays;
import java.util.List;

/**
 * The Milice are holding parades around town - a brave volunteer must show our defiance.  They probably
 * won't make it back...
 * Deliver 1 Weapon to Rue Baradat on a Parade Day (Day 3, 6, 9, 12, and 14).  The Agent is Arrested.
 * Increase morale by 1.  Before this mission is completed, the road between Rue Baradat and Fence is blocked on Parade
 * Days.
 */
public class MiliceParadeDayMission extends Mission{
    private static final List<Integer> PARADE_DAYS = Arrays.asList(3, 6, 9, 12, 14);

    public MiliceParadeDayMission(Game game){
        super(game, "Milice Parade Day", 1, "The Milice are holding parades around town - a brave volunteer must show our defiance.  They probably won't make it back...", "Deliver 1 Weapon to Rue Baradat on a Parade Day (Day 3, 6, 9, 12, and 14).  The Agent is Arrested. Increase morale by 1.  Before this mission is completed, the road between Rue Baradat and Fence is blocked on Parade Days.");
        addMissionRequirement("Deliver 1 weapon to Rue Baradat on turn 3, 6, 9, 12, or 14");
    }

    @Override
    public void turnSetup() {
        super.turnSetup();
        Road road = game.getBoard().getRoad(LocationType.RUE_BARADAT, LocationType.FENCE);
        road.setBlocked(PARADE_DAYS.contains(game.getTurn()));
    }

    @Override
    public void turnTeardown() {
        super.turnTeardown();
        Road road = game.getBoard().getRoad(LocationType.RUE_BARADAT, LocationType.FENCE);
        road.setBlocked(false);
    }

    @Override
    public void visitLocation(LocationType locationType) {
        super.visitLocation(locationType);
        if (completed) return;
        if  (PARADE_DAYS.contains(game.getTurn()) && locationType == LocationType.RUE_BARADAT && game.hasResources(Resource.WEAPONS, 1)){
            game.discardResources(Resource.WEAPONS, 1);
            Location location = game.getBoard().getLocation(locationType);
            Agent agent = location.getAgents().remove(0);
            agent.setArrested(true);
            game.setMorale(game.getMorale() + 1);
            Road road = game.getBoard().getRoad(LocationType.RUE_BARADAT, LocationType.FENCE);
            road.setBlocked(false);
            completed = true;
            getMissionRequirements().get(0).setCompleted(true);
            PopupUtil.popupNotification(null, "Mission", "Agent arrested at Rue Baradat. -1 Weapon. +1 Morale.");
            PopupUtil.popupNotification(null, "Mission", getName() + " Completed");
        }
    }
}
