package maquis.game.mission;

import maquis.game.Game;
import maquis.game.Location;
import maquis.game.LocationType;
import maquis.game.Resource;
import maquis.view.PopupUtil;

/**
 * A German plane landed in the field southeast of town.  The pilot is in a hotel nearby.  A perfect time to strike!
 * You must have at least 5 morale to carry out this mission.  Deliver 1 Weapon and 1 Explosive to this location.
 * Before this mission is completed, the East Field and the Southeast Spare Room are unusable.
 */
public class BombForTheOfficerMission extends Mission{
    public BombForTheOfficerMission(Game game){
        super(game, "Bomb For The Officer", 2, "A German plane landed in the field southeast of town.  The pilot is in a hotel nearby.  A perfect time to strike!", "You must have at least 5 morale to carry out this mission.  Deliver 1 Weapon and 1 Explosive to this location. Before this mission is completed, the East Field and the Southeast Spare Room are unusable.");
        addMissionRequirement("Have 5+ Morale");
        addMissionRequirement("Deliver 1 weapon and 1 explosive to the Mission location");
        getMissionRequirements().get(0).setCompleted(true);
    }

    @Override
    public void setup() {
        super.setup();
        Location eastField = game.getBoard().getLocation(LocationType.FIELD_2);
        eastField.setBlocked(true);
        Location southEastSpareRoom= game.getBoard().getLocation(LocationType.SPARE_ROOM_3);
        southEastSpareRoom.setBlocked(true);
    }

    @Override
    public void restart() {
        super.restart();
        super.setup();
    }

    @Override
    public void visitLocation(LocationType locationType) {
        super.visitLocation(locationType);
        if (completed) return;
        Location missionLocation = game.getBoard().getLocationWithMission(this);
        getMissionRequirements().get(0).setCompleted(game.getMorale() >= 5);
        if  (missionLocation.getType() == locationType &&
                game.hasResources(Resource.WEAPONS, 1) &&
                game.hasResources(Resource.EXPLOSIVES, 1) &&
                game.getMorale() >= 5){
            game.discardResources(Resource.WEAPONS, 1);
            game.discardResources(Resource.EXPLOSIVES, 1);
            Location eastField = game.getBoard().getLocation(LocationType.FIELD_2);
            eastField.setBlocked(false);
            Location southEastSpareRoom= game.getBoard().getLocation(LocationType.SPARE_ROOM_3);
            southEastSpareRoom.setBlocked(false);
            getMissionRequirements().get(1).setCompleted(true);
            completed = true;
            PopupUtil.popupNotification(null, "Mission", getName() + " Completed");
        }
    }
}
