package maquis.game.mission;

import maquis.game.Game;
import maquis.game.LocationType;
import maquis.game.Resource;
import maquis.view.PopupUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The occupation has enjoyed unfettered access to the city for too long.  Slow them down!
 * To destroy a bridge, deliver 2 Explosives to the Black Market.  At the end of the day, place a Marker on a bridge
 * of your choice connected to The Black Market.  Agents may not pass destroyed bridges for the rest of the game.
 */
public class TakeOutTheBridgesMission extends Mission{
    private boolean pontDuNordBridgeDestroyed = false;
    private boolean poorDistrictBridgeDestroyed = false;

    private boolean armPontDuNordBridge = false;
    private boolean armPoorDistrictBridge = false;

    public TakeOutTheBridgesMission(Game game){
        super(game, "Take Out The Bridges", 2, "The occupation has enjoyed unfettered access to the city for too long.  Slow them down!", "To destroy a bridge, deliver 2 Explosives to the Black Market.  At the end of the day, place a Marker on a bridge of your choice connected to The Black Market.  Agents may not pass destroyed bridges for the rest of the game.");
        addMissionRequirement("Deliver 2 explosives to the Black Market (Pont du Nord)");
        addMissionRequirement("Deliver 2 explosives to the Black Market (Poor District Bridge)");
    }

    @Override
    public void restart() {
        super.restart();
        pontDuNordBridgeDestroyed = false;
        poorDistrictBridgeDestroyed = false;
        armPoorDistrictBridge = false;
        armPontDuNordBridge = false;
    }

    @Override
    public void visitLocation(LocationType locationType) {
        if (completed) return;
        super.visitLocation(locationType);

        if (locationType == LocationType.BLACK_MARKET && game.hasResources(Resource.EXPLOSIVES, 2)){
            List<String> choices = new ArrayList();
            if (!pontDuNordBridgeDestroyed)
                choices.add(LocationType.PONT_DU_NORD.getName());
            if (!poorDistrictBridgeDestroyed)
                choices.add(LocationType.POOR_DISTRICT.getName());

            int choice =
                    PopupUtil.popupQuestion(
                            null,
                            "Arm the Bomb!",
                            "Plant Explosives on which Bridge?",
                            choices.toArray(new String[0]));
            if (choice == JOptionPane.CLOSED_OPTION)
                return;
            LocationType chosen = LocationType.fromName(choices.get(choice));
            if (chosen == LocationType.PONT_DU_NORD) {
                armPontDuNordBridge = true;
                getMissionRequirements().get(0).setCompleted(true);
            }
            else {
                armPoorDistrictBridge = true;
                getMissionRequirements().get(1).setCompleted(true);
            }
        }
    }

    @Override
    public void turnTeardown() {
        if (completed) return;
        super.turnTeardown();
        if (armPontDuNordBridge){
            armPontDuNordBridge = false;
            pontDuNordBridgeDestroyed = true;
            game.getBoard().getRoad(LocationType.BLACK_MARKET, LocationType.PONT_DU_NORD).setBlocked(true);
            PopupUtil.popupNotification(null, "Bridge Destroyed!", LocationType.PONT_DU_NORD.getName() + " Destroyed!");
        }
        else if (armPoorDistrictBridge){
            armPoorDistrictBridge = false;
            poorDistrictBridgeDestroyed = true;
            game.getBoard().getRoad(LocationType.BLACK_MARKET, LocationType.POOR_DISTRICT).setBlocked(true);
            PopupUtil.popupNotification(null, "Bridge Destroyed!", LocationType.POOR_DISTRICT.getName() + " Destroyed!");
        }
        completed = poorDistrictBridgeDestroyed && pontDuNordBridgeDestroyed;
        if (completed)
            PopupUtil.popupNotification(null, "Mission", getName() + " Completed");
    }

    @Override
    public boolean connectToBoard() {
        return false;
    }
}
